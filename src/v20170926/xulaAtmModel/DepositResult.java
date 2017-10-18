package v20170926.xulaAtmModel;

public class DepositResult extends Result {
    public DepositResult() {
    }

    public DepositResult(int status) {
        super(status);
    }

    public DepositResult(int status, String message) {
        super(status, message);
    }
}
