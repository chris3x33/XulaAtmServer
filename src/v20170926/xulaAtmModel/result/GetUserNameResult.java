package v20170926.xulaAtmModel.result;

public class GetUserNameResult extends Result{

    private String userName;

    public GetUserNameResult(int status, String message) {
        super(status, message);
    }

    public GetUserNameResult(Result result) {
        super(result.getStatus(),result.getMessage());

    }

    public GetUserNameResult(int status) {
        super(status);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
