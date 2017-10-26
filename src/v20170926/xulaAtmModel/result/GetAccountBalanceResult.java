package v20170926.xulaAtmModel.result;

public class GetAccountBalanceResult extends Result{

    private double accountBalance;

    public GetAccountBalanceResult(int status, double accountBalance) {
        super(status);
        this.accountBalance = accountBalance;
    }

    public GetAccountBalanceResult(int status, String message) {
        super(status, message);
        this.accountBalance = -1;
    }

    public double getAccountBalance() {
        return accountBalance;
    }
}
