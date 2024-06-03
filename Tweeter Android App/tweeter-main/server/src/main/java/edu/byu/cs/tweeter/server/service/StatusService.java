package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.base.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamodb.item.PreFeedBatch;

public class StatusService extends BaseService {
    public StatusService (DAOFactory factory) {
        super(factory);
    }

    private int getStatusesStartingIndex(String lastStatusPost, List<Status> allStatuses) {
        int statusesIndex = 0;

        if(lastStatusPost != null) {
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatusPost.equals(allStatuses.get(i).getPost())) {
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }

    public FeedResponse getFeed(FeedRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!statusDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        List<Status> allFeed = statusDAO.getPageOfFeed(request.getUserAlias(), request.getLimit(), request.getLastDatetime());
        for (int i = 0; i < allFeed.size(); i++) {
            User user = userDAO.getUser(allFeed.get(i).getUser().getAlias());
            allFeed.get(i).setUser(user);
        }

        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFeed.size() > 0) {
                int feedIndex = getStatusesStartingIndex(request.getLastDatetime(), allFeed);

                for(int limitCounter = 0; feedIndex < allFeed.size() && limitCounter < request.getLimit(); feedIndex++, limitCounter++) {
                    responseFeed.add(allFeed.get(feedIndex));
                }

                hasMorePages = (allFeed.size() == responseFeed.size());
            }
        }

        return new FeedResponse(responseFeed, hasMorePages);
    }

    public StoryResponse getStory(StoryRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!statusDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        List<Status> allStory = statusDAO.getPageOfStory(request.getUserAlias(), request.getLimit(), request.getLastDatetime());
        for (int i = 0; i < allStory.size(); i++) {
            User user = userDAO.getUser(allStory.get(i).getUser().getAlias());
            allStory.get(i).setUser(user);
        }

        List<Status> responseStory = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStory.size() > 0) {
                int storyIndex = getStatusesStartingIndex(request.getLastDatetime(), allStory);

                for(int limitCounter = 0; storyIndex < allStory.size() && limitCounter < request.getLimit(); storyIndex++, limitCounter++) {
                    responseStory.add(allStory.get(storyIndex));
                }

                hasMorePages = (allStory.size() == responseStory.size());
            }
        }

        return new StoryResponse(responseStory, hasMorePages);
    }

    public PostStatusResponse postStatus (PostStatusRequest request) {
        if(request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!statusDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        boolean success = statusDAO.insertStory(request.getStatus());

        insertSQSPostStatusQueue(request.getStatus());

        return new PostStatusResponse(success);
    }

    private void insertSQSPostStatusQueue(Status status) {
        String messageBody = new Gson().toJson(status);
        String queueUrl = "https://sqs.us-east-2.amazonaws.com/094955051536/SQSPostStatusQueue";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
    }

    public void postUpdateFeedMessages(SQSEvent event) {
        if (event.getRecords() == null) {
            System.out.println("Empty or invalid message was sent");
            return;
        }

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String body = msg.getBody();
            System.out.println("Body: " + body);

            // Parse data
            Status status = new Gson().fromJson(body, Status.class);

            // Get all the followers of the author
            List<String> followerAliases = followDAO.getFollowers(status.getUser().getAlias());

            // 3.  Send 2nd message to UpdateFeedQueue so it triggers UpdateFeeds (updates n user feeds at a time with the new status)

            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

            List<List<String>> followerAliasBatches = chopped(followerAliases, 25);
            for (List<String> followerAliasBatch: followerAliasBatches) {
                // Make object to hold request data
                PreFeedBatch preFeedBatch = new PreFeedBatch(status, followerAliasBatch);

                // Serialize data as JSON
                String msgBody = new Gson().toJson(preFeedBatch);

                SendMessageRequest outgoingMsg = new SendMessageRequest()
                        .withQueueUrl("https://sqs.us-east-2.amazonaws.com/094955051536/SQSUpdateFeedQueue")
                        .withMessageBody(msgBody);

                sqs.sendMessage(outgoingMsg);
                System.out.println("Sent msgBody:" + msgBody);
            }
        }
    }

    private static <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    public void updateFeeds(SQSEvent event) {
        if (event.getRecords() == null) {
            System.out.println("Empty or invalid message was sent");
            return;
        }

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String body = msg.getBody();
            System.out.println("Body: " + body);

            PreFeedBatch preFeedBatch = new Gson().fromJson(body, PreFeedBatch.class);

            // Send to DAO to batch create feed entries
            statusDAO.insertFeed(preFeedBatch);
        }
    }
}
