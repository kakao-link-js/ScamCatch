package com.example.cjs60.scamcatch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cjs60.scamcatch.Fragment.PhoneCallFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { //무조건 이거부터 실행됨.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //메인 액티비티에 레이아웃을 걸어준다.
        PermissionCheck();

        PhoneCallFragment phoneCallFragment = new PhoneCallFragment(); //PhoneCallFragment로 넘어가기 위한 객체 생성

        //activity_main에 있는 main_frame에 phoneCallFragment를 씌운다
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, phoneCallFragment).commit();
    }

    //권한있는지 없는지 확인
    public void PermissionCheck(){
        int permission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},0);
        }
        else{
            //권한있음.
        }
    }
}