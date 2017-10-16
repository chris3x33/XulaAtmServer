package v20170926.xulaAtmModel;

public class XulaATMTransaction {
    private double amount;
    private String type;// W , D
    private String otherAccount;
    private String DateTime;

    public XulaATMTransaction(double amount, String type, String otherAccount, String dateTime) {
        this.amount = amount;
        this.type = type;
        this.otherAccount = otherAccount;
        DateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getOtherAccount() {
        return otherAccount;
    }

    public String getDateTime() {
        return DateTime;
    }
}
