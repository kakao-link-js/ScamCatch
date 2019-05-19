package com.example.cjs60.scamcatch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cjs60.scamcatch.Server.NetworkTask;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECORD_AUDIO;

public class CallActivity extends AppCompatActivity {


    public TextView timer;
    public TextView phoneNum;
    public TextView stt;
    public String phone;
    public long myBaseTime;
    public SpeechToText speechToText;
    public NetworkTask networkTask;
    public String TAG = "CallActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);

        SetLayout();
        SetPhoneNum();

        myBaseTime = SystemClock.elapsedRealtime();
        myTimer.sendEmptyMessage(0);
        int requestCode = 5; // unique code for the permission request
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);
        speechToText = new SpeechToText(this);
        speechToText.execute();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(networkTask != null && networkTask.getStatus() == AsyncTask.Status.RUNNING)
            networkTask.cancel(true);
        if(speechToText.getStatus() == AsyncTask.Status.RUNNING)
            speechToText.cancel(true);
    }

    public void SetLayout(){
        findViewById(R.id.endCallBtn).setOnClickListener(onClickListener);
        timer = (TextView)findViewById(R.id.timer);
        phoneNum = (TextView)findViewById(R.id.phoneNum);
        stt = (TextView)findViewById(R.id.speechToText);
    }

    //버튼 클릭 이벤트를 담은 메소드.
    Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.endCallBtn:
                    if(networkTask != null && networkTask.getStatus() == AsyncTask.Status.RUNNING)
                        networkTask.cancel(true);
                    if(speechToText.getStatus() == AsyncTask.Status.RUNNING)
                        speechToText.cancel(true);
                    finish();
                    break;
            }
        }
    };

    public void SetPhoneNum(){
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        phoneNum.setText(phone.substring(0,3)+'-'+phone.substring(3,7)+'-'+phone.substring(7,11));
    }

    public void ChangeSTTText(String addData){
        stt.setText(stt.getText() + addData);
        sendToServer();
    }


    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            timer.setText(getTimeOut());

            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            myTimer.sendEmptyMessage(0);
        }
    };

    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut(){
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d", outTime/1000 / 60, (outTime/1000)%60);
        return easy_outTime;
    }

    //서버에 요청할 데이터
    private JSONObject FormatingData(String text, String phoneNumber){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("text", text);
            jsonObject.accumulate("current_id", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,phoneNumber + " : "+ text);
        return jsonObject;
    }

    private void sendToServer() {
        String url = "http://52.247.220.74/accuracy";

        JSONObject jsonObject = FormatingData((String) stt.getText(), (String) phoneNum.getText());
        networkTask = new NetworkTask(getApplicationContext(), url, jsonObject);
        try {
            String result = networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            Toast.makeText(this,"result : "+ result,Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}