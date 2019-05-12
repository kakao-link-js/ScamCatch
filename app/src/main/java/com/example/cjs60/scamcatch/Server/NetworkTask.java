package com.example.cjs60.scamcatch.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    //해당 화면 context 변수
    private Context context;

    //서버 요청할 주소, 데이터
    private String url;
    private JSONObject data;
    private String TAG = "ConnectServer";

    //NetworkTask 생성자
    public NetworkTask(Context context, String url, JSONObject data){
        Log.d(TAG,"make NetworkTask");
        this.context = context;
        this.url = url;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG,"onPreExecute");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.d(TAG,"do InBackground");
        String result = null;

        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.request(url, data);
        Log.d(TAG, "get result :" + result);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        //서버에 요청한 값이 null 일 때
        if(result == null){
            Toast.makeText(context,"인터넷 연결을 확인하세요", Toast.LENGTH_LONG).show();
            return;
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


}
