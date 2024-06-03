package edu.byu.cs.tweeter.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.base.DAOFactory;
import edu.byu.cs.tweeter.server.dao.base.FollowDAO;
import edu.byu.cs.tweeter.server.dao.base.UserDAO;
import edu.byu.cs.tweeter.server.dao.dynamodb.DynamoDAOFactory;

public class Filler {
    // How many follower users to add
    // We recommend you test this with a smaller number first, to make sure it works for you
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@lego";

    public static void main(String[] args) {
        DAOFactory factory = new DynamoDAOFactory();

        // Get instance of DAOs by way of the Abstract Factory Pattern
        UserDAO userDAO = factory.getUserDAO();
        FollowDAO followDAO = factory.getFollowDAO();

        List<User> users = new ArrayList<>();
        List<String> followers = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {
            String firstName = "Guy " + i;
            String lastName = "Person " + i;
            String alias = "@guy" + i;
            String imgURL = "https://cpcs340bucket.s3.us-east-2.amazonaws.com/%40user";

            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User(firstName, lastName, alias, imgURL);
            users.add(user);

            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }
        String hashedPassword = hashPassword("password");

        // Call the DAOs for the database logic
        /*if (users.size() > 0) {
            userDAO.addUserBatch(users, hashedPassword);
        }
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
            userDAO.updateUserCounts(FOLLOW_TARGET, NUM_USERS, 0);
        }*/
    }

    private static String hashPassword(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH";
    }
}