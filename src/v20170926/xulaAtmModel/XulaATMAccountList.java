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

    public long getUnusedAccountId() {

        long unusedUserId;
        do {
            unusedUserId = Math.abs(random.nextLong());

        } while (accountExists(unusedUserId));

        return unusedUserId;
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

    public XulaATMAccount getAccount(long accountId){

        for (XulaATMAccount atmAccount : atmAccounts){
            if (atmAccount.getAccountId() == accountId){
                return atmAccount;
            }
        }

        return null;

    }

    public boolean createNewAccount(long accountId, int accountType, double balance) {

        return atmAccounts.add(new XulaATMAccount(accountId,accountType,balance));

    }

}
