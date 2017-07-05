package support.techwise.hatzolahdirections;

import android.database.Cursor;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for an SMS object with a utility method to get an SMS from a cursor
 * Created by dovi on 2017/07/03.
 */

public class smsItem {
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String get_Id() {
        return _Id;
    }

    public void set_Id(String _Id) {
        this._Id = _Id;
    }

    private String _Id;
    private String address;
    private String date;
    private String body;

    public static smsItem fromCursor(Cursor cursor) {
        smsItem sms = new smsItem();
        sms.set_Id(cursor.getString(0));
        sms.setAddress(cursor.getString(1));
        sms.setDate(cursor.getString(2));
        sms.setBody(cursor.getString(3));

        return sms;
    }

    /**
     * Checks to see if SMS is a hatzolah call
     * @return true if call, false if not call
     */
    public boolean isHatzolahType() {
        Pattern pattern = Pattern.compile("^.*\\n.*\\n.*\\nNat:.*\\nC:.*\\nB:.*\\n.*\\nCnr:.*");
        Matcher matcher = pattern.matcher(body);
        return matcher.matches();
    }

    public String getCallAddress() {
        String address = "";
        if (isHatzolahType()) {
            String[] strings = new String[8];
            StringTokenizer st = new StringTokenizer(body, "\n");
            int count = 0;
            while (st.hasMoreTokens()) {
                strings[count] = st.nextToken();
                count++;
            }
            address = strings[6];
        } else {
            address = "NOT A VALID CALL SMS";
        }
        return address;
    }
}
