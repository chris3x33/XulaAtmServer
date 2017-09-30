package v20170926.sha1Utilits;

public class SHA1Utilits {

    private String toHexString(byte[] bytes){

        StringBuilder hexStringBuilder = new StringBuilder();

        for (byte b : bytes){
            hexStringBuilder.append(
                    String.format("%02x", b)
            );
        }

        return hexStringBuilder.toString().toUpperCase();

    }
}
