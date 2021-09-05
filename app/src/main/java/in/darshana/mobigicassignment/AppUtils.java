package in.darshana.mobigicassignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppUtils {
    public static String getSysDate() {
        DateFormat _dateFormat;
        String _todaysDate = "";

        _dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Calendar cal = Calendar.getInstance();
        _todaysDate = _dateFormat.format(cal.getTime());

//        //System.out.println("timeStamp dateFormat-->"+_todaysDate);

        return _todaysDate + "+00:00";
    }
}
