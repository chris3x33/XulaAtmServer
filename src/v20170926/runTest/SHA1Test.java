package v20170926.runTest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Test {

    public static void main(String[] args) {

    }

    private static String encrypt(String password) throws NoSuchAlgorithmException {

        //Set encrypt type
        final String encryptType = "SHA-1";

        //initialize encryption
        MessageDigest encryptor = MessageDigest.getInstance(encryptType);
        encryptor.reset();

        //get password bytes

        //encrypt password bytes

        //convert bytes to hex

        return null;
    }

}
