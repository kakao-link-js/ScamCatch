package com.example.cjs60.scamcatch.CallFunction;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.cjs60.scamcatch.CallFunction.CallingService;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {


    public static final String TAG = "IncomingCallTAG";
    // 전화번호
    private static String phoneNumber = "";
    // incoming 수신 플래그
    private static boolean callState; //통화를 받으면 true 로 바껴서 브로드캐스트를 또 안만듬
    SharedPreferences sharedPreferences;
    Bundle bundle;



    @Override
    public void onReceive(final Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("sFile",0);
        Log.d(TAG,"get sharedPreferences");
        if(!sharedPreferences.getBoolean("alarm",false)) { //내가 알람설정을 껐을때
            Log.d(TAG,"alarm sharedPreFernces false");
            return;
        }
        Log.d(TAG,"Pass AlarmSetting and Phone Num check :" + intent.getStringExtra("incoming_number"));
        if(intent.getStringExtra("incoming_number") == null){ // 첫전화면 true 두번째부턴 false 밖으로 나감
            Log.d(TAG,"incomingFlag true");
            callState = false;
            return;
        }else if(callState){
            Log.d(TAG,"incomingFlag true but callState true");
            return;
        }
        Log.d(TAG,"incomingFlag true and callState false");

        //여기까지

        // TODO Auto-generated method stub
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            // TODO 전화를 걸었을 떄이다. (안드로이드 낮은 버전에서는 여기로 들어온다)
            this.phoneNumber = bundle.getString("incoming_number");
            Log.d(TAG,"getPhoneNumber");
        }
        // 폰 상태 체크
        this.phoneNumber = intent.getStringExtra("incoming_number");
        processPhoneState(intent, context);
    }

    private void processPhoneState(Intent intent, Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        switch (tm.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                // TODO 전화를 왔을때이다. (벨이 울릴때)
                Log.d("CallingService", "벨이 울리고 있다. " + phoneNumber);
                if(!CallingService.itIsOpen)
                    OpenService(context);
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

    public void OpenService(Context context){
        Log.d(TAG,"StartOpenService");
        final String phone_number = phoneNumber;
        CallingService.itIsOpen = true;
        Intent serviceIntent = new Intent(context, CallingService.class);
        serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
        Log.d(TAG,"phone_number : "+phone_number);
        context.startForegroundService(serviceIntent);
    }

}
