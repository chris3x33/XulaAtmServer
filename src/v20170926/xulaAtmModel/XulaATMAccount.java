package v20170926.xulaAtmModel;

public class XulaATMAccount {

    private long accountId;
    private double balance;
    private int accountType;
    private XulaATMTransactionList atmTransactionList;

    public XulaATMAccount(long accountId, int accountType, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.accountType = accountType;
    }


    public long getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountType() {
        return accountType;
    }


}
