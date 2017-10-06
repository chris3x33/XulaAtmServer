package v20170926.xulaAtmServerController;

import java.security.SecureRandom;
import java.util.ArrayList;

public class SessionList {

    private ArrayList<Session> sessions = new ArrayList<Session>();
    private SecureRandom random = new SecureRandom();

    public boolean sessionExists(int sessionId){

        for (Session session : sessions){

            if(session.getSessionId() == sessionId){
                return true;
            }

        }

        return false;

    }

    public Session getSession(int sessionId) {

        for (Session session : sessions){

            if(session.getSessionId() == sessionId){
                return session;
            }

        }

        return null;

    }




}
