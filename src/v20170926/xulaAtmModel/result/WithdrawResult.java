package v20170926.xulaAtmModel.result;

public class WithdrawResult extends Result {

    private String withdrawMsg;

    public WithdrawResult() {
    }

    public WithdrawResult(int status) {
        super(status);
    }

    public WithdrawResult(int status, String message) {
        super(status, message);
    }

    public String getWithdrawMsg() {
        return withdrawMsg;
    }

    public void setWithdrawMsg(String withdrawMsg) {
        this.withdrawMsg = withdrawMsg;
    }

}
