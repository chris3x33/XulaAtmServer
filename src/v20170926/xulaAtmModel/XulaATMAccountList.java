package v20170926.xulaAtmModel;

import java.security.SecureRandom;
import java.util.ArrayList;

public class XulaATMAccountList {

    private SecureRandom random = new SecureRandom();
    private ArrayList<XulaATMAccount> atmAccounts;

    public XulaATMAccountList(String accountListFolder) {
    }

    public XulaATMAccountList() {
    }

    public double getAccountBalance(long accountID) {

        return -1;

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
