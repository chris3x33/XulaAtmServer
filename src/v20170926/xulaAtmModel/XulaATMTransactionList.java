package v20170926.xulaAtmModel;

import java.util.ArrayList;

public class XulaATMTransactionList {

    private ArrayList<XulaATMTransaction> atmTransactions;

    public XulaATMTransactionList(String transactionListFolder) {
        atmTransactions = new ArrayList<XulaATMTransaction>();
    }

    public void recordTransaction(double amount, int type, String otherAccount, double prevAmount, String dateTime){

        atmTransactions.add(new XulaATMTransaction(amount,type,otherAccount, prevAmount, dateTime));

    }

}
