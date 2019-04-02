package com.example.cjs60.scamcatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.cjs60.scamcatch.Fragment.PhoneCallFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //메인 액티비티에 레이아웃을 걸어준다.

        PhoneCallFragment phoneCallFragment = new PhoneCallFragment(); //PhoneCallFragment로 넘어가기 위한 객체 생성

        //activity_main에 있는 main_frame에 phoneCallFragment를 씌운다
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, phoneCallFragment).commit();

    }
}