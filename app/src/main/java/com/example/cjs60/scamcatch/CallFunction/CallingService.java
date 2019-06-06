package com.example.cjs60.scamcatch.CallFunction;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.cjs60.scamcatch.Firebase.ConnectFirebase;
import com.example.cjs60.scamcatch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallingService extends Service {

    public static final String EXTRA_CALL_NUMBER = "call_number";
    protected View rootView;
    public static boolean itIsOpen = false;
    public String TAG = "CallingService";
    public ConnectFirebase connectFirebase;


    @BindView(R.id.tv_call_number)
    TextView tv_call_number;
    TextView suspiNum;
    TextView lastReport;

    String call_number;

    WindowManager.LayoutParams params;
    public static int LAYOUT_FLAG =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    private WindowManager windowManager;



    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG+"//onBind ","Start ");
        // Not used
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG+"//onCreate","start onCreate");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        connectFirebase = new ConnectFirebase();

        int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%

        params = new WindowManager.LayoutParams(
                width,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                PixelFormat.TRANSLUCENT);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.called_layout, null);
        tv_call_number = (TextView)rootView.findViewById(R.id.tv_call_number);
        suspiNum = (TextView)rootView.findViewById(R.id.susPiNum);
        lastReport = (TextView)rootView.findViewById(R.id.lastCall);

        rootView.findViewById(R.id.btn_close).setOnClickListener(onClickListener);
        ButterKnife.bind(this, rootView);
        setDraggable();
        Log.d(TAG,"finish onCreate");
    }



    private void setDraggable() {
        Log.d(TAG+"//setDraggable","start");
        rootView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        if (rootView != null)
                            windowManager.updateViewLayout(rootView, params);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG+"//setDraggable","start onStartCommand " +"Jaesumnia");

        setExtra(intent);
        windowManager.addView(rootView, params);

        if (!TextUtils.isEmpty(call_number)) {
            tv_call_number.setText(call_number);
            connectFirebase.GetReportCountAndShow(call_number,suspiNum);
            connectFirebase.GetReportDate(call_number,lastReport);
        }
        Log.d(TAG,"end onStartCommand call_number : " +call_number+"Jaesumnia");
        return START_REDELIVER_INTENT;
    }

    //intent 에 넣은 번호를 받아온다.
    private void setExtra(Intent intent) {
        Log.d(TAG,"start setExtra");

        if (intent == null) {
            removePopup();
            return;
        }
        call_number = intent.getStringExtra(EXTRA_CALL_NUMBER);
        Log.d(TAG,"end setExtra call_number : " + call_number);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        removePopup();
    }

    Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_close:
                    removePopup();
                    break;
            }
        }
    };


    public void removePopup() {
        Log.d(TAG,"removePopup");
        if (rootView != null && windowManager != null) windowManager.removeView(rootView);
    }
}
