package v20170926.xulaAtmServerController;

public class Session {

    private int sessionId;
    private int userId = -1;

    public Session(){
        sessionId= -1;
        userId = -1;
    }

    public Session(int sessionId){
        this.sessionId = sessionId;
        userId = -1;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
