package com.example.cjs60.scamcatch;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.cjs60.scamcatch.Fragment.ContactListFragment;
import com.example.cjs60.scamcatch.Fragment.PhoneCallFragment;
import com.example.cjs60.scamcatch.Fragment.RecentCallListFragment;
import com.example.cjs60.scamcatch.Fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    PhoneCallFragment phoneCallFragment;
    RecentCallListFragment recentCallListFragment;
    SettingFragment settingFragment;
    ContactListFragment contactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //무조건 이거부터 실행됨.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //메인 액티비티에 레이아웃을 걸어준다.
        PermissionCheck(); //권한을 받는다.
        SetLayout(); //레이아웃에 이벤트를 다 걸어준다.

        phoneCallFragment = new PhoneCallFragment(this); //PhoneCallFragment로 넘어가기 위한 객체 생성
        recentCallListFragment = new RecentCallListFragment(); //객체 생성
        settingFragment = new SettingFragment(); //객체 생성
        contactListFragment = new ContactListFragment(); //객체 생성

        //activity_main에 있는 main_frame에 phoneCallFragment를 씌운다
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, phoneCallFragment).commit();
    }

    public void ChangeCallActivity(String callNumber){
        Intent intent = new Intent(getApplicationContext(),CallActivity.class);
        intent.putExtra("phone",callNumber);
        startActivity(intent);
    }

    //권한있는지 없는지 확인
    public void PermissionCheck(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                )
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE,//전화거는 퍼미션
                    Manifest.permission.READ_CALL_LOG, //기록받아오는 퍼미션
                    Manifest.permission.RECORD_AUDIO, //오디오기록 퍼미션
                    Manifest.permission.READ_PHONE_STATE //전화 상태를 읽을 수 있는 퍼미션
            },0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // SDK26 이상 부터 다른위에 그리기 권한 가져와야함
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                checkOverlayPermission();
            }
        }
    }

    //다른앱위에 그리는 권한은 매우 위험한 선택이라 이렇게 메소드로 환경설정으로 보냄
    private void checkOverlayPermission() {
        try {
            Uri uri = Uri.parse("package:" + getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
            startActivityForResult(intent, 5469);
        } catch (Exception e) {
            //toast(e.toString());
        }
    }


    //레이아웃들의 객체와 코드의 변수들을 연결
    public void SetLayout(){
        findViewById(R.id.keyPadbtn).setOnClickListener(onClickListener);//이벤트 연결
        findViewById(R.id.recentbtn).setOnClickListener(onClickListener);
        findViewById(R.id.contactbtn).setOnClickListener(onClickListener);
        findViewById(R.id.settingbtn).setOnClickListener(onClickListener);
    }
    //각 버튼들에 대한 리스너 걸기 (이벤트 걸기)
    Button.OnClickListener onClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.keyPadbtn: //keyPadbtn이 눌린경우
                    replaceFragment(phoneCallFragment);
                    break;
                case R.id.recentbtn: //recentbtn이 눌린경우
                    replaceFragment(recentCallListFragment);
                    break;
                case R.id.contactbtn: //contactbtn이 눌린경우
                    replaceFragment(contactListFragment);
                    break;
                case R.id.settingbtn: //settingbtn이 눌린경우
                    replaceFragment(settingFragment);
                    break;
            }
        }
    };

    //프래그먼트 전환 메소드 (애니메이션이 들어간다)
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}