package test.fan.com.dialogtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.graphics.PixelFormat;
import android.os.Build;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by fan on 2018/4/25.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private static String TAG = "NotificationReceiver";

    private static int DISPLAY_INFO_WIDTH  = 0;
    private static int DISPLAY_INFO_HEIGHT = 1;

    private static View view                     = null;
    private static FrameLayout interceptorLayout = null;
    private static Context       mContext = null;
    private static WindowManager wm       = null;

    private static int height = 0;
    private static int width  = 0;



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "received intent : [" + intent + "]");

        mContext = context.getApplicationContext();
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        interceptorLayout = new FrameLayout(mContext) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                // Only fire on the ACTION_DOWN event, or you'll get two events (one for DOWN, one for UP)
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    // Check if the HOME button is pressed
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                        Log.v(TAG, "KEYCODE_DPAD_UP Button Pressed");
                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        if (view != null) {
                            TextView textView = view.findViewById(R.id.show_win_type);
                            textView.setY(textView.getY()-10);
                        }
                        return true;
                    }
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                        Log.v(TAG, "KEYCODE_DPAD_DOWN Button Pressed");
                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        if (view != null) {
                            TextView textView = view.findViewById(R.id.show_win_type);
                            textView.setY(textView.getY()+10);
                        }
                        return true;
                    }
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                        Log.v(TAG, "KEYCODE_DPAD_LEFT Button Pressed");
                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        if (view != null) {
                            TextView textView = view.findViewById(R.id.show_win_type);
                            textView.setX(textView.getX()-10);
                        }
                        return true;
                    }
                    if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        Log.v(TAG, "KEYCODE_DPAD_RIGHT Button Pressed");
                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        if (view != null) {
                            TextView textView = view.findViewById(R.id.show_win_type);
                            textView.setX(textView.getX()+10);
                        }
                        return true;
                    }
                }
                // Otherwise don't intercept the event
                return super.dispatchKeyEvent(event);
            }
        };

        if (intent.getAction().equals(MyApplication.ACTION_FAN_SHOW_SYSTEM_ALERT)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createFloatingWindow(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, getDisplayInfo(DISPLAY_INFO_HEIGHT)/3, getDisplayInfo(DISPLAY_INFO_HEIGHT)/3);
            } else {
                createFloatingWindow(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, getDisplayInfo(DISPLAY_INFO_HEIGHT)/3, getDisplayInfo(DISPLAY_INFO_HEIGHT)/3);
            }
        }

        if (intent.getAction().equals(MyApplication.ACTION_FAN_SHOW_DISMISS_ALERT)) {
            dimissFloatingWindow ();
        }
    }

    private String convertWinTypeToStr (int winType) {
        String res = "undefined";

        switch (winType) {
            case WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                res = "TYPE_APPLICATION_OVERLAY";
                break;
            case WindowManager.LayoutParams.TYPE_SYSTEM_ALERT :
                res = "TYPE_SYSTEM_ALERT";
                break;
        }

        return res;
    }

    private void createFloatingWindow (int winType, int width, int height) {

        if (view != null) {
            wm.removeView(view);
            view = null;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                winType,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;
        params.setTitle("System Message");
        params.width = width;
        params.height = height;

        view = View.inflate(mContext, R.layout.dialog_show, interceptorLayout);
        wm.addView(view, params);

        /* Show window type on show_win_type */
        TextView textViewWinType = view.findViewById(R.id.show_win_type);
        textViewWinType.setText(convertWinTypeToStr(winType));
        textViewWinType.setTextColor(0xffe4e465);

        Button dismissButton = view.findViewById(R.id.button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimissFloatingWindow();
            }
        });
    }

    private void dimissFloatingWindow () {
        if (view != null) {
            wm.removeView(view);
            view = null;
        }
    }

    private int getDisplayInfo (int type) {
        Log.d(TAG, "Display height : " + height + ", width : " + width);

        if (type == DISPLAY_INFO_WIDTH) {
            return width;
        } else if (type == DISPLAY_INFO_HEIGHT) {
            return height;
        }

        return 0;
    }
}
