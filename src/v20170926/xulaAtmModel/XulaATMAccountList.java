package v20170926.xulaAtmModel;

import java.security.SecureRandom;
import java.util.ArrayList;

public class XulaATMAccountList {

    private SecureRandom random = new SecureRandom();
    private ArrayList<XulaATMAccount> atmAccounts;

    public XulaATMAccountList(String accountListFolder) {
    }

    public XulaATMAccountList() {
        atmAccounts = new ArrayList<XulaATMAccount>();
    }

    public double getAccountBalance(long accountID) {

        return -1;

    }

    public boolean accountExists(long accountID){
        for (XulaATMAccount atmAccount : atmAccounts){
            if (atmAccount.getAccountId() == accountID){
                return true;
            }
        }

        return false;
    }


    public WithdrawResult withdraw(long fromAccountId, double
            withdrawAmount) {

        return null;
    }

    public DepositResult deposit(long toAccountId, double depositAmount) {

        return null;
    }

    public TransferResult transfer(long fromAccountId, long toAccount, double transferAmount) {

        return null;
    }


}
