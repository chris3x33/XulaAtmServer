package v20170926.runTest;

import v20170926.xulaAtmServerController.SessionList;

public class SessionTest {

    private static SessionList sessionList = new SessionList();

    public static void main(String[] args) {

        long sessionId1 = sessionList.newSession();
        long sessionId2 = sessionList.newSession();

        System.out.println("sessionId1 = "+sessionId1);
        System.out.println("sessionId2 = "+sessionId2);

        System.out.println("SessionList Size = "+sessionList.size());

        System.out.println("Exists sessionId2 = "+sessionList.sessionExists(sessionId2));
        System.out.println("delete sessionId2 = "+sessionList.deleteSession(sessionId2));
        System.out.println("Exists sessionId2 = "+sessionList.sessionExists(sessionId2));

        System.out.println("SessionList Size = "+sessionList.size());


    }

}
