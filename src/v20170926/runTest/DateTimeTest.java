package v20170926.runTest;

import static v20170926.utils.DateTime.getCurrentDateTime;

public class DateTimeTest {
    public static void main(String[] args) {

        String currentDateTime = getCurrentDateTime("yyyy_dd_MM_HH_mm_ss");

        System.out.println("currentDateTime: "+currentDateTime);

    }
}
