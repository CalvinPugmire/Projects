package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.base.DAOFactory;
import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        System.out.println("7");
        DAOFactory factory = new DynamoDAOFactory();
        StatusService service = new StatusService(factory);
        service.postUpdateFeedMessages(event);
        return null;    // No return needed, the UpdateFeed queue will provide the data to the next lambda
    }
}
