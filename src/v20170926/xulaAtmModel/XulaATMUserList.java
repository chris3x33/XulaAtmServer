package v20170926.xulaAtmModel;

public class XulaATMUserList {

    private XulaATMUser[] atmUsers;

    public XulaATMUser getATMUser(String userName){

        for (XulaATMUser atmUser : atmUsers){
            if (atmUser.getUserName().compareTo(userName) == 0){
                return atmUser;
            }
        }

        return null;
    }

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

    public boolean userExists(String userName){
        for (XulaATMUser atmUser : atmUsers){
            if (atmUser.getUserName().compareTo(userName) == 0){
                return true;
            }
        }

        return false;
    }
}
