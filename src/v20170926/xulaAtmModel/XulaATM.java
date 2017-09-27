package v20170926.xulaAtmModel;

public class XulaATM {

    private XulaATMAccountList atmAccountList;
    private XulaATMUserList atmUserList;

    public double getAccountBalance(long accountID) {

        return atmAccountList.getAccountBalance(accountID);

    }

    public long[] getAccountIDs(long userId) {

        return new long[0];

    }

    public WithdrawResult withdraw(long fromAccountId, double
            withdrawAmount) {

        return atmAccountList.withdraw(fromAccountId, withdrawAmount);

    }

    public DepositResult deposit(long toAccountId, double depositAmount) {

        return atmAccountList.deposit(toAccountId,depositAmount);
    }

    public TransferResult transfer(long fromAccountId, long toAccount, double transferAmount) {

        return atmAccountList.transfer(fromAccountId, toAccount, transferAmount);
    }

    public NewUserCreationResult createNewUser(String username, String password) {

        return null;
    }

    public OpenAccountResult openNewAccount(long userId) {

        return null;
    }


}
