package support.techwise.hatzolahdirections;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by dovi on 2016/12/21.
 */
public class DirectionsLib {
    /**
     * Checks if app has permission to read SMSes, and requests permission if not
     * @param a activity called from
     */
    public static void checkPermissions(Activity a) {
        int permission_response = 0;
        if(ContextCompat.checkSelfPermission(a, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(a, "SMS permission needed!", Toast.LENGTH_SHORT).show();;
            } else {
                Log.d("DEBUG", "Trying to get permissions: " + Manifest.permission.READ_SMS + "...");
                ActivityCompat.requestPermissions(a, new String[] {Manifest.permission.READ_SMS}, permission_response);
            }
        }
    }

    public static Intent createMapIntent(smsItem sms) {
        String address = "";
        try {
            address = URLEncoder.encode(sms.getCallAddress(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        Uri mapUri = Uri.parse("geo:0,0?q=" + address);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(mapUri);
        return intent;
    }

    /*public static Intent createEditIntent(smsItem sms) {
        String address = sms.getCallAddress();
    }*/

    public static void startMaps(smsItem sms, Context context) {
        Intent intent = createMapIntent(sms);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
