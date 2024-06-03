package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.base.DAOFactory;

public class FollowService extends BaseService {
    public FollowService (DAOFactory factory) {
        super(factory);
    }

    private int getFollowsStartingIndex(String lastFollowAlias, List<User> allFollows) {
        int followsIndex = 0;

        if(lastFollowAlias != null) {
            for (int i = 0; i < allFollows.size(); i++) {
                if(lastFollowAlias.equals(allFollows.get(i).getAlias())) {
                    followsIndex = i + 1;
                    break;
                }
            }
        }

        return followsIndex;
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!followDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        List<String> allFolloweeAliases = followDAO.getPageOfFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        List<User> allFollowees = new ArrayList<>();
        for (int i = 0; i < allFolloweeAliases.size(); i++) {
            allFollowees.add(userDAO.getUser(allFolloweeAliases.get(i)));
        }

        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees.size() > 0) {
                int followeesIndex = getFollowsStartingIndex(request.getLastFolloweeAlias(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < userDAO.getFollowingCount(request.getFollowerAlias());
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!followDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        List<String> allFollowerAliases = followDAO.getPageOfFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
        List<User> allFollowers = new ArrayList<>();
        for (int i = 0; i < allFollowerAliases.size(); i++) {
            allFollowers.add(userDAO.getUser(allFollowerAliases.get(i)));
        }

        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers.size() > 0) {
                int followersIndex = getFollowsStartingIndex(request.getLastFollowerAlias(), allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < userDAO.getFollowersCount(request.getFolloweeAlias());
            }
        }

        return new FollowersResponse(responseFollowers, hasMorePages);
    }

    public FollowersCountResponse getFollowersCount (FollowersCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!followDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        int followersCount = userDAO.getFollowersCount(request.getTargetUser().getAlias());
        return new FollowersCountResponse(followersCount);
    }

    public FollowingCountResponse getFollowingCount (FollowingCountRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!followDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        int followingCount = userDAO.getFollowingCount(request.getTargetUser().getAlias());
        return new FollowingCountResponse(followingCount);
    }

    public FollowResponse follow (FollowRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        String followerAlias = followDAO.getUser(request.getAuthToken());

        boolean success = followDAO.insertFollow(followerAlias, request.getFolloweeAlias());

        userDAO.updateUserCounts(followerAlias, 0, 1);
        userDAO.updateUserCounts(request.getFolloweeAlias(), 1, 0);

        return new FollowResponse(success);
    }

    public UnfollowResponse unfollow (UnfollowRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        String followerAlias = followDAO.getUser(request.getAuthToken());

        boolean success = followDAO.deleteFollow(followerAlias, request.getFolloweeAlias());

        userDAO.updateUserCounts(followerAlias, 0, -1);
        userDAO.updateUserCounts(request.getFolloweeAlias(), -1, 0);

        return new UnfollowResponse(success);
    }

    public IsFollowerResponse isFollower (IsFollowerRequest request) {
        if(request.getFollower() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }

        if (!followDAO.valAuthToken(request.getAuthToken())) {
            throw new RuntimeException("[Bad Request] Invalid authtoken");
        }

        boolean isFollower = followDAO.getFollow(request.getFollower(), request.getFollowee());
        return new IsFollowerResponse(isFollower);
    }
}
