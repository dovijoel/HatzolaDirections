package support.techwise.hatzolahdirections;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int LOADER_ID = 1;
    private static final int PERMISSION_REQUEST_CODE_READ_SMS = 1;

    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    // The adapter that binds our data to the ListView
    SMSRecycleViewAdaptor ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECEIVE_SMS))) {
                Toast.makeText(this, "SMS permissions needed!", Toast.LENGTH_LONG).show();
            } else {
                Log.d("DEBUG", "Trying to get permissions: " + Manifest.permission.READ_SMS + "...");
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE_READ_SMS);
            }
        } else {
            showSMSes();
        }
    }

    private void showSMSes() {
        //get handle on RecyclerView and create a layout manager for it
        RecyclerView rv = (RecyclerView) findViewById(R.id.smsRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        //create the adapter and attach it to the RecyclerView
        ra = new SMSRecycleViewAdaptor(this, null);
        rv.setAdapter(ra);

        mCallbacks = this;
        LoaderManager lm = getSupportLoaderManager();
        lm.initLoader(LOADER_ID, null, mCallbacks);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showSMSes();
                } else {
                    Toast.makeText(this, "SMS permission needed!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //set up columns to get
        String[] reqCols = new String[] { "_id", "address", "date", "body" };

        //set up URI to get SMSes
        Uri smsUri = Uri.parse("content://sms/inbox");
        Log.d("DEBUG", "on create loader");
        return new CursorLoader(MainActivity.this, smsUri, reqCols, null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //load is done so now put the cursor data into the adapter
        switch (loader.getId()) {
            case LOADER_ID:
                Log.d("DEBUG", "swapping cursor");
                ra.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ra.swapCursor(null);
    }
}
