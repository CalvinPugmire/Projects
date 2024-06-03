package edu.byu.cs.tweeter.client.presenter.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.FolUnFolObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GotFollowingObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GotFollowersObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowingObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.service.observer.StatusObserver;
import edu.byu.cs.tweeter.client.model.service.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainPresenter.View> {
    public interface View extends BaseView {
        void followingButton();
        void followButton();
        void updateFollowButton(Boolean removed);
        void enableFollowButton();
        void updateFollowingCount(int count);
        void updateFollowerCount(int count);
        void updateFollowCounts();
        void logoutSuccessful();
    }

    private final FollowService followService;
    private StatusService statusService;
    private final UserService userService;

    public MainPresenter(View view) {
        super(view);
        followService = new FollowService();
        userService = new UserService();
    }

    /**/
    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }
    /**/

    public void onStatusPosted(String post) {
        view.displayMessage("Posting Status...");
        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(newStatus, new StatObserver());
        } catch (Exception ex) {
            view.displayError("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new IsFollowObserver());
    }

    public void follow(User selectedUser) {
        view.displayMessage("Adding " + selectedUser.getName() + "...");
        followService.follow(selectedUser, new FolUnObserver());
    }

    public void unfollow(User selectedUser) {
        view.displayMessage("Removing " + selectedUser.getName() + "...");
        followService.unfollow(selectedUser, new FolUnObserver());
    }

    public void updateFollowCounts (User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(selectedUser, new GetFollowersObserver(), new GetFollowingObserver());
    }

    public void logout () {
        userService.logout(new OutObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm:ss aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public class StatObserver extends BasePresenter<MainPresenter.View>.ServObserver implements StatusObserver {
        @Override
        public void handlePostStatus() {
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public String getFailMessage() {
            return "Failed to post status: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to post status because of exception: ";
        }
    }

    public class GetFollowingObserver extends BasePresenter<MainPresenter.View>.ServObserver implements GotFollowingObserver {
        @Override
        public void handleGotFollowing(int count) {
            view.updateFollowingCount(count);
        }

        @Override
        public String getFailMessage() {
            return "Failed to get following count: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to get following count because of exception: ";
        }
    }

    public class GetFollowersObserver extends BasePresenter<MainPresenter.View>.ServObserver implements GotFollowersObserver {
        @Override
        public void handleGotFollowers(int count) {
            view.updateFollowerCount(count);
        }

        @Override
        public String getFailMessage() {
            return "Failed to get follower count: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to get follower count because of exception: ";
        }
    }

    public class IsFollowObserver extends BasePresenter<MainPresenter.View>.ServObserver implements IsFollowingObserver {
        @Override
        public void handleIsFollowing(boolean following) {
            if (following) {
                view.followingButton();
            } else {
                view.followButton();
            }
        }

        @Override
        public String getFailMessage() {
            return "Failed to get follow status: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to get follow status because of exception: ";
        }
    }

    public class FolUnObserver extends BasePresenter<MainPresenter.View>.ServObserver implements FolUnFolObserver {
        @Override
        public void handleFolUnFol(boolean setfollow) {
            view.updateFollowCounts();
            view.updateFollowButton(setfollow);
        }

        @Override
        public void handleFinishFolUnFol() {
            view.enableFollowButton();
        }

        @Override
        public String getFailMessage() {
            return "Failed to (un)follow user: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to (un)follow user because of exception: ";
        }
    }

    public class OutObserver extends BasePresenter<MainPresenter.View>.ServObserver implements LogoutObserver {
        @Override
        public void handleLogout() {
            view.logoutSuccessful();
        }

        @Override
        public String getFailMessage() {
            return "Failed to logout: ";
        }

        @Override
        public String getExMessage() {
            return "Failed to logout because of exception: ";
        }
    }
}
