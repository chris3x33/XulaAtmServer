package v20170926.xulaAtmServerController;

import java.security.SecureRandom;
import java.util.ArrayList;

public class SessionList {

    private ArrayList<Session> sessions = new ArrayList<Session>();
    private SecureRandom random = new SecureRandom();

    public boolean sessionExists(long sessionId){

        for (Session session : sessions){

            if(session.getSessionId() == sessionId){
                return true;
            }

        }

        return false;

    }

    public Session getSession(long sessionId) {

        for (Session session : sessions){

            if(session.getSessionId() == sessionId){
                return session;
            }

        }

        return null;

    }

    public boolean deleteSession(long sessionId) {

        if(!sessionExists(sessionId)){
            return true;
        }

        for (int i = 0; i <sessions.size() ; i++) {

            Session session = sessions.get(i);

            if(session.getSessionId() == sessionId){

                sessions.remove(i);
                break;
            }
        }

        return !sessionExists(sessionId);

    }

    public long newSession(){

        long sessionId = Math.abs(random.nextLong());

        while (sessionExists(sessionId)){
            sessionId = random.nextInt();
        }

        sessions.add(new Session(sessionId));

        return sessionId;

    }

    public int size(){
        return sessions.size();
    }


}
