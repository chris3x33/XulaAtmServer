package v20170926.xulaAtmModel.result;

public class CreateNewUserResult extends Result {

    private long userId = -1;

    public CreateNewUserResult(int status, String message) {
        super(status, message);
    }

    public CreateNewUserResult(Result result) {
        super(result.getStatus(),result.getMessage());

    }

    public CreateNewUserResult(int status, long userId) {
        super(status);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

}
