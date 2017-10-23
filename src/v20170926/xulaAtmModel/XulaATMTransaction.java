package v20170926.xulaAtmModel;

public class XulaATMTransaction {
    private double amount;
    private int type;// W , D
    private String otherAccount;
    private String dateTime;
    private double prevAmount;

    public XulaATMTransaction(double amount, int type, String otherAccount, double prevAmount, String dateTime) {
        this.amount = amount;
        this.type = type;
        this.otherAccount = otherAccount;
        this.prevAmount = prevAmount;
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getPrevAmount() {
        return prevAmount;
    }
}
