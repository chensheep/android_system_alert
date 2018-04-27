package test.fan.com.dialogtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by fan on 2018/4/25.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!Settings.canDrawOverlays(getApplicationContext())) {
            requestAlertWindowPermission();
        }

        Button showButton    = findViewById(R.id.button_show);
        Button dismissButton = findViewById(R.id.button_dismiss);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                intent.setAction(MyApplication.ACTION_FAN_SHOW_SYSTEM_ALERT);
                sendBroadcast(intent);
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
                intent.setAction(MyApplication.ACTION_FAN_SHOW_DISMISS_ALERT);
                sendBroadcast(intent);
            }
        });
    }


    @Override
    protected void onPause () {
        super.onPause();
        Log.d("fan", "Into pause state.");
    }

    private static final int REQUEST_CODE = 1;
    private  void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Log.i("fan", "onActivityResult granted");
            }
        }
    }

}
