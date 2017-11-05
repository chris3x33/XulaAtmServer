package v20170926.xulaAtmModel;

import v20170926.xulaAtmModel.result.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class XulaATMUserList {

    private SecureRandom random = new SecureRandom();

    private ArrayList<XulaATMUser> atmUsers;
    private ArrayList<Long> unsavedUserIds;

    public XulaATMUserList(){

        atmUsers = new ArrayList<XulaATMUser>();
        unsavedUserIds = new ArrayList<Long>();

    }

    public XulaATMUserList(String userListFolder) throws FileNotFoundException {
        atmUsers = readUsersFrom(userListFolder);
        unsavedUserIds = new ArrayList<Long>();
    }

    private ArrayList<XulaATMUser> readUsersFrom(String userListFolderPath) throws FileNotFoundException {

        File userListFolder = new File(userListFolderPath);
        if(!userListFolder.isDirectory()){
            return new ArrayList<XulaATMUser>();
        }

        File[] userFiles = userListFolder.listFiles();
        if(userFiles == null){
            return new ArrayList<XulaATMUser>();
        }

        ArrayList<XulaATMUser> atmUsers = new ArrayList<XulaATMUser>();

        for (File userFile :userFiles){
            atmUsers.add(new XulaATMUser(userFile));
        }

        return atmUsers;
    }

    public XulaATMUser getATMUser(String userName){

        for (XulaATMUser atmUser : atmUsers){
            if (atmUser.getUserName().compareTo(userName) == 0){
                return atmUser;
            }
        }

        return null;
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

            String errMsg = String.format(
                    "UserName must be between %d and %d in length!!",
                    USERNAME_MIN_LEN,
                    USERNAME_MAX_LEN
            );

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

    public Result isUsablePassword(String password){

        if(password == null || password.isEmpty()){

            String errMsg = "Password cannot be empty!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        //Password needs to be at least 8 characters long
        //Can not be more than 40 characters long
        final int MIN_LEN = 6;
        final int MAX_LEN = 40;

        if (password.length()<MIN_LEN || password.length()>MAX_LEN){

            String errMsg = String.format(
                    "Password must be between %d and %d in length!!",
                    MIN_LEN,
                    MAX_LEN
            );

            return new Result(Result.ERROR_CODE, errMsg);
        }

        //Must have a lower case character
        //Must have an upper case character
        //Must have a number
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;

        for(int i=0;i < password.length();i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            }
        }

        if(!hasNumber){

            String errMsg = "Password must contain a number!!";

            return new Result(Result.ERROR_CODE, errMsg);
        }

        if(!hasLowerCase){

            String errMsg = "Password must contain a Lower Case Letter!!";

            return new Result(Result.ERROR_CODE, errMsg);

        }

        if(!hasUpperCase){

            String errMsg = "Password must contain a Upper Case Letter!!";

            return new Result(Result.ERROR_CODE, errMsg);

        }

        return new Result(Result.SUCCESS_CODE);
    }

    public long getUnusedUserId() {

        long unusedUserId;
        do {
            unusedUserId = Math.abs(random.nextLong());

        } while (userExists(unusedUserId));

        return unusedUserId;
    }

    public boolean createNewUser(String userName, String password, long userId) {

        XulaATMUser atmUser = new XulaATMUser(userName, password, userId);
        boolean isAdded = atmUsers.add(atmUser);

        if (isAdded){
            unsavedUserIds.add(atmUser.getUserId());
        }

        return isAdded;
    }

    public XulaATMUser getATMUser(long userId) {
        for (XulaATMUser atmUser : atmUsers){
            if (atmUser.getUserId()==userId){
                return atmUser;
            }
        }

        return null;
    }

    public void writeTo(String userListFolderPath) throws IOException {
        for (XulaATMUser atmUser : atmUsers){
            atmUser.writeTo(userListFolderPath);
        }
    }

    public void writeUnsavedUsersTo(String userListFolderPath) throws IOException {

        for (long atmUserId : unsavedUserIds){

            if (!userExists(atmUserId)){
                continue;
            }

            XulaATMUser atmUser = getATMUser(atmUserId);

            synchronized (atmUser) {
                atmUser.writeTo(userListFolderPath);
            }

        }

    }
}
