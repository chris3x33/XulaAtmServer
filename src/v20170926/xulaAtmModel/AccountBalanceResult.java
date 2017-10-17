package v20170926.xulaAtmModel;

public class AccountBalanceResult extends Result{

    private double accountBalance;

    public AccountBalanceResult(int status, double accountBalance) {
        super(status);
        this.accountBalance = accountBalance;
    }

    public AccountBalanceResult(int status, String message) {
        super(status, message);
        this.accountBalance = -1;
    }

    public double getAccountBalance() {
        return accountBalance;
    }
}
