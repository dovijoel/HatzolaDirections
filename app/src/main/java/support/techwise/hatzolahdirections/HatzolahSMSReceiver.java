package support.techwise.hatzolahdirections;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Code from http://androidexample.com/Incomming_SMS_Broadcast_Receiver_-_Android_Example/index.php?view=article_discription&aid=62
 * Created by dovi on 2017/07/04.
 */

public class HatzolahSMSReceiver extends BroadcastReceiver {
    final SmsManager smsManager = SmsManager.getDefault();
    smsItem sms;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                    }
                    else {
                        currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    }
                    sms = new smsItem();
                    sms.setAddress(currentMessage.getDisplayOriginatingAddress());
                    sms.setBody(currentMessage.getDisplayMessageBody());
                    sms.setDate(String.valueOf(currentMessage.getTimestampMillis()));
                    if (sms.isHatzolahType()) {
                        showNotification(context);
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private void showNotification(Context c) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(c)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.hatzolah)
                        .setContentTitle("Hatzolah Call")
                        .setContentText("Navigate to " + sms.getCallAddress());
        // Creates an explicit intent for an Activity in your app
        Intent mapIntent = DirectionsLib.createMapIntent(sms);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
        // Adds the back stack for the Intent (but not the Intent itself)
        //stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(mapIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        int mId = 1000;
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
