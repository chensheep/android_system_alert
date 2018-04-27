package test.fan.com.dialogtest;

import android.app.Application;
import android.util.Log;

/**
 * Created by fan on 2018/4/27.
 */

public class MyApplication extends Application {

    public static String TAG = "MyApplication";

    public static String ACTION_FAN_SHOW_SYSTEM_ALERT  = "com.fan.sysalert.show";
    public static String ACTION_FAN_SHOW_DISMISS_ALERT = "com.fan.sysalert.dismiss";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication oncreate");
    }

}
