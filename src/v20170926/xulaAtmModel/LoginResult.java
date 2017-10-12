package v20170926.xulaAtmModel;

public class LoginResult extends Result {
    private long userId = -1;

    public LoginResult(int status, String message) {
        super(status, message);
    }

    public LoginResult(Result result) {
        super(result.getStatus(),result.getMessage());

    }

    public LoginResult(int status, long userId) {
        super(status);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
