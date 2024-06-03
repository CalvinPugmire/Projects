package org.example;

public class Main {
    public static void main(String[] args) {
        FollowDAO dao = new FollowDAO();

        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@ClintEastwood", "Clint Eastwood");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@a", "a");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@b", "b");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@c", "c");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@d", "d");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@e", "e");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@f", "f");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@g", "g");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@h", "h");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@i", "i");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@j", "j");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@k", "k");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@l", "l");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@m", "m");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@n", "n");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@o", "o");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@p", "p");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@q", "q");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@r", "r");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@s", "s");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@t", "t");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@u", "u");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@v", "v");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@w", "w");
        dao.insertFollow("@FredFlintstone", "Fred Flintstone", "@x", "x");

        dao.insertFollow("@ClintEastwood", "Clint Eastwood", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@a", "a", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@b", "b", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@c", "c", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@d", "d", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@e", "e", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@f", "f", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@g", "g", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@h", "h", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@i", "i", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@j", "j", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@k", "k", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@l", "l", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@m", "m", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@n", "n", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@o", "o", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@p", "p", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@q", "q", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@r", "r", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@s", "s", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@t", "t", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@u", "u", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@v", "v", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@w", "w", "@FredFlintstone", "Fred Flintstone");
        dao.insertFollow("@x", "x", "@FredFlintstone", "Fred Flintstone");

        Follow follow1 = dao.getFollow("@FredFlintstone", "@ClintEastwood");
        System.out.println(follow1.getFollower_name() + " " + follow1.getFollowee_name());
        System.out.println();

        dao.updateFollow("@FredFlintstone", "@ClintEastwood", "Fred Flinty", "Clint Easty");
        Follow follow2 = dao.getFollow("@FredFlintstone", "@ClintEastwood");
        System.out.println(follow2.getFollower_name() + " " + follow2.getFollowee_name());
        System.out.println();

        dao.deleteFollow("@FredFlintstone", "@ClintEastwood");
        Follow follow3 = dao.getFollow("@FredFlintstone", "@ClintEastwood");
        System.out.println(follow3.getFollower_name() + " " + follow3.getFollowee_name());
        System.out.println();

        int pageSize = 5;

        String lastUserAlias = null;

        DataPage<Follow> followers = dao.getPageOfFollowers("@FredFlintstone", pageSize, lastUserAlias);
        for (int i=0; i<5; i++) {
            System.out.println(followers.getValues().get(i).getFollowee_name()+" follows "+followers.getValues().get(i).getFollower_name());
        }
        lastUserAlias = followers.getValues().get(followers.getValues().size()-1).getFollowee_alias();
        System.out.println();

        followers = dao.getPageOfFollowers("@FredFlintstone", pageSize, lastUserAlias);
        for (int i=0; i<5; i++) {
            System.out.println(followers.getValues().get(i).getFollowee_name()+" follows "+followers.getValues().get(i).getFollower_name());
        }
        lastUserAlias = followers.getValues().get(followers.getValues().size()-1).getFollowee_alias();
        System.out.println();

        lastUserAlias = null;

        DataPage<Follow> followees = dao.getPageOfFollowees("@FredFlintstone", pageSize, lastUserAlias);
        for (int i=0; i<5; i++) {
            System.out.println(followees.getValues().get(i).getFollowee_name()+" follows "+followees.getValues().get(i).getFollower_name());
        }
        lastUserAlias = followees.getValues().get(followees.getValues().size()-1).getFollower_alias();
        System.out.println();

        followees = dao.getPageOfFollowees("@FredFlintstone", pageSize, lastUserAlias);
        for (int i=0; i<5; i++) {
            System.out.println(followees.getValues().get(i).getFollowee_name()+" follows "+followees.getValues().get(i).getFollower_name());
        }
        lastUserAlias = followees.getValues().get(followees.getValues().size()-1).getFollower_alias();
        System.out.println();
    }
}