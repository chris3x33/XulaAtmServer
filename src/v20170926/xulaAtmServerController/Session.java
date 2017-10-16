package v20170926.xulaAtmServerController;

public class Session {

    public static final int VALID_SESSION_CODE = 1;
    public static final int INVALID_SESSION_CODE = -1;
    public static final int EXPIRED_SESSION_CODE = -2;

    private long sessionId;
    private long userId = -1;
    private int expire;

    public Session(){
        sessionId= -1;
        userId = -1;
    }

    public Session(long sessionId){
        this.sessionId = sessionId;
        userId = -1;
    }

    public int getExpire() {
        return expire;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
