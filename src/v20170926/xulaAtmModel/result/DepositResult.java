package v20170926.xulaAtmModel.result;

public class DepositResult extends Result {

    private String depositMsg;

    public DepositResult() {
    }

    public DepositResult(int status) {
        super(status);
    }

    public DepositResult(int status, String message) {
        super(status, message);
    }

    public String getDepositMsg() {
        return depositMsg;
    }

    public void setDepositMsg(String depositMsg) {
        this.depositMsg = depositMsg;
    }

}
