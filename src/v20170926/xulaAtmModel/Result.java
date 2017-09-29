package v20170926.xulaAtmModel;

public class Result {

    private int status;
    private String message;

    public Result() {
        this.status = 1;
        this.message = "";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
