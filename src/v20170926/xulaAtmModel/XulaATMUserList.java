package v20170926.xulaAtmModel;

import java.security.SecureRandom;
import java.util.ArrayList;

public class XulaATMUserList {

    private SecureRandom random = new SecureRandom();

    private ArrayList<XulaATMUser> atmUsers;

    public XulaATMUserList(){

        atmUsers = new ArrayList<XulaATMUser>();

    }

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

    public Result changePassword(long userId, String currPass, String newPass){

        return null;

    }

    public Result isValidUserName(String userName){

        final int USERNAME_MIN_LEN = 6;
        final int USERNAME_MAX_LEN = 40;

        if(userName == null || userName.isEmpty()){

            String errMsg = "UserName cannot be empty!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        //Check length
        if (userName.length()<USERNAME_MIN_LEN || userName.length() > USERNAME_MAX_LEN){

            String errMsg = "UserName must be between and in length!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        //Check first char is a Letter
        char firstChar = userName.charAt(0);
        if(!Character.isLetter(firstChar)){

            String errMsg = "UserName must start with a letter!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        //Check for spaces
        if(userName.indexOf(' ') > -1){
            String errMsg = "UserName cannot contain spaces!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        return new Result(Result.SUCCESS_CODE);

    }

}
