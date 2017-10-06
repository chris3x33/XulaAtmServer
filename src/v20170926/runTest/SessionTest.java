package v20170926.runTest;

import v20170926.xulaAtmServerController.SessionList;

public class SessionTest {

    private static SessionList sessionList = new SessionList();

    public static void main(String[] args) {

        int sessionId1 = sessionList.newSession();
        int sessionId2 = sessionList.newSession();

        System.out.println("sessionId1 = "+sessionId1);
        System.out.println("sessionId2 = "+sessionId2);

    }

}
