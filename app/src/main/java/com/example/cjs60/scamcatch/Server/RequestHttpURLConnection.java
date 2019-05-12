package com.example.cjs60.scamcatch.Server;


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHttpURLConnection {

    private String TAG = "ConnectServer";
    public String request(String _url, JSONObject data) {
        Log.d(TAG,"start request");
        String result = null;
        BufferedReader reader = null;

        try {
            URL urlObject = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();

            Log.d(TAG, "start try");
            Log.e("url",_url.toString());
            Log.e("connection",conn.toString());

            conn.setReadTimeout(150000); //15초동안 서버로부터 반응없으면 에러
            conn.setConnectTimeout(15000); // 접속하는 커넥션 타임 15초동안 접속안되면 접속 안되는 것으로 간주

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Accept", "text/html");
            conn.setRequestProperty("Content-Type", "application/json"); //서버로 보내는 패킷이 어떤타입인지 선언

            conn.setDoInput(true);
            conn.setDoOutput(true);
            Log.d(TAG,"connect Open");

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(data.toString().getBytes());

            InputStream stream = conn.getInputStream();
            Log.d(TAG,"get stream");

            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line ="";

            while ((line = reader.readLine()) != null)
                buffer.append(line);

            if(reader != null) {
                Log.d(TAG,"get reader");
                reader.close(); //버퍼를 닫아줌
            }

            result = buffer.toString();

            if(result == null){
                Log.d(TAG,"result null");
            } else{
                Log.d(TAG,"result : " + result.toString());
            }

            if(conn != null) {
                Log.d(TAG,"disconnect");
                conn.disconnect();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error : "+e.toString());
        }

        return null;
    }
}