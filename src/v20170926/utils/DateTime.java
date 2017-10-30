package v20170926.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

    private static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy";

    public static String getCurrentDate() {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        return dateFormat.format(new Date());

    }

}
