package v20170926.xulaAtmModel;

public class XulaATM {

    public double getAccountBalance(long accountID) {

        return -1;

    }

    public long[] getAccountIDs(long userId) {

        return new long[0];

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

    public NewUserCreationResult createNewUser(String username, String password) {

        return null;
    }

    public OpenAccountResult openNewAccount(long userId) {

        return null;
    }


}
