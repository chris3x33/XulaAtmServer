package v20170926.xulaAtmModel;

import java.util.ArrayList;

public class XulaATMTransactionList {

    private ArrayList<XulaATMTransaction> atmTransactions;

    public XulaATMTransactionList(){

        atmTransactions = new ArrayList<XulaATMTransaction>();

    }

    public void recordTransaction(double amount, String type, String otherAccount, String dateTime){

        atmTransactions.add(new XulaATMTransaction(amount,type,otherAccount,dateTime));

    }

}
