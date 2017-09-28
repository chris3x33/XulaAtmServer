package v20170926.xulaAtmModel;

public class XulaATMUserList {

    private XulaATMUser[] atmUsers;

    public long[] getAccountIDs(long userId) {

        return new long[0];

    }

    public boolean userExists(long userId){
        for (XulaATMUser atmUser : atmUsers){
            if (atmUser.getUserId() == userId){
                return true;
            }
        }

        return false;
    }
}
