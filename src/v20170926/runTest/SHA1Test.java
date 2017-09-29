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
        byte[] passwordBytes = password.getBytes();

        //encrypt password bytes
        byte[] encryptedBytes = encryptor.digest(passwordBytes);

        //convert bytes to hex
        return toHexString(encryptedBytes);
    }

    private static String toHexString(byte[] bytes){

        StringBuilder hexStringBuilder = new StringBuilder();

        for (byte b : bytes){
            hexStringBuilder.append(
                    String.format("%02x", b)
            );
        }

        return hexStringBuilder.toString();

    }

}
