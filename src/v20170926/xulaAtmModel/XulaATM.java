package v20170926.xulaAtmModel;



public class XulaATM {

    private XulaATMAccountList atmAccountList;
    private XulaATMUserList atmUserList;
    private final String WELCOME_MSG = "Welcome to XULA ATM";

    public String getWelcomeMsg() {
        return WELCOME_MSG;
    }

    public Result isValidUserNsme(){
        return null;
    }

    public double getAccountBalance(long accountId) {

        return atmAccountList.getAccountBalance(accountId);

    }

    public long[] getAccountIDs(long userId) {

        return atmUserList.getAccountIDs(userId);

    }

    public boolean userExists(long userId) {

        return atmUserList.userExists(userId);

    }

    public boolean userExists(String userName) {
        return atmUserList.userExists(userName);
    }

    public WithdrawResult withdraw(long fromAccountId, double
            withdrawAmount) {

        return atmAccountList.withdraw(fromAccountId, withdrawAmount);

    }

    public DepositResult deposit(long toAccountId, double depositAmount) {

        return atmAccountList.deposit(toAccountId, depositAmount);
    }

    public TransferResult transfer(long fromAccountId, long toAccountId, double transferAmount) {

        return atmAccountList.transfer(fromAccountId, toAccountId, transferAmount);
    }

    public NewUserCreationResult createNewUser(String username, String password) {

        return null;
    }

    public OpenAccountResult openNewAccount(long userId) {

        return null;
    }

    public LoginResult login(String userName, String password){

        //Check if User Exists
        if(!userExists(userName)){

        }

        //Get user
        XulaATMUser atmUser = atmUserList.getATMUser(userName);

        //Check password
        ValidatePasswordResult validatePasswordResult = atmUser.validatePassword(password);

        //Setup LoginResult

        return null;
    }

    public Result changePassword(long userId, String currPass, String newPass){

        return atmUserList.changePassword(userId, currPass, newPass);

    }

    public Result isValidUserName(String userName){

        return atmUserList.isValidUserName(userName);

    }

}
