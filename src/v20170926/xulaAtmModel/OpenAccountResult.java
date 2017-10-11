package v20170926.xulaAtmModel;

public class OpenAccountResult extends Result {

    private long userId = -1;

    public OpenAccountResult(int status, String message) {
        super(status, message);
    }

    public OpenAccountResult(Result result) {
        super(result.getStatus(),result.getMessage());

    }

    public OpenAccountResult(int status, long userId) {
        super(status);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
