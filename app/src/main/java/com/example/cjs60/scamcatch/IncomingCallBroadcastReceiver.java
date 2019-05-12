package com.example.cjs60.scamcatch;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {


    public static final String TAG = "PHONE STATE";
    // 전화번호
    private static String phoneNumber = "";
    // incoming 수신 플래그
    private static boolean incomingFlag = true; //첫번째 브로드케스트에서는 값이 안와서 한번 다시 켜기위한 변수
    private static boolean callState; //통화를 받으면 true 로 바껴서 브로드캐스트를 또 안만듬
    public CallingService callingService;
    //
    Bundle bundle;
    @Override
    public void onReceive(final Context context, Intent intent) {
        bundle = intent.getExtras();
        if(incomingFlag){
            callState = false;
            incomingFlag = false;
            return;
        }else if(callState){
            return;
        }

        //여기까지

        // TODO Auto-generated method stub
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            // TODO 전화를 걸었을 떄이다. (안드로이드 낮은 버전에서는 여기로 들어온다)
            this.phoneNumber = bundle.getString("incoming_number");
        }
        // 폰 상태 체크
        processPhoneState(intent, context);
    }

    private void processPhoneState(Intent intent, Context context){
        String intentNum ;
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        switch (tm.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                // TODO 전화를 왔을때이다. (벨이 울릴때)
                this.phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);;
                intentNum = intent.getStringExtra("incoming_number");
                Log.d("CallingService", "벨이 울리고 있다. " + phoneNumber + " // intent : "+ intentNum);
                if(!CallingService.itIsOpen)
                    OpenService(intent,context);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                callState = true;
                Log.d("CallingService","전화를 받았다.");
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                callState = false;
                Log.d("CallingService","전화를 끊었다.");
                break;
        }
    }

    public void OpenService(Intent intent,Context context){
        final String phone_number = phoneNumber;
        CallingService.itIsOpen = true;
        Intent serviceIntent = new Intent(context, CallingService.class);
        serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
        Log.d("CallingService","phone_number : "+phone_number + "  phoneNumber : "+ phoneNumber);
        context.startForegroundService(serviceIntent);
    }

}
