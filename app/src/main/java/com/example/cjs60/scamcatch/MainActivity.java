package com.example.cjs60.scamcatch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cjs60.scamcatch.Fragment.Address.AddressFragment;
import com.example.cjs60.scamcatch.Fragment.ContactListFragment;
import com.example.cjs60.scamcatch.Fragment.PhoneCallFragment;
import com.example.cjs60.scamcatch.Fragment.RecentCallListFragment;
import com.example.cjs60.scamcatch.Fragment.SettingFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    PhoneCallFragment phoneCallFragment;
    RecentCallListFragment recentCallListFragment;
    SettingFragment settingFragment;
    AddressFragment contactListFragment;

    private ArrayList<String> nameList;
    private ArrayList<String> numberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //무조건 이거부터 실행됨.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //메인 액티비티에 레이아웃을 걸어준다.
        PermissionCheck(); //권한을 받는다.
        SetLayout(); //레이아웃에 이벤트를 다 걸어준다.

        phoneCallFragment = new PhoneCallFragment(this); //PhoneCallFragment로 넘어가기 위한 객체 생성
        recentCallListFragment = new RecentCallListFragment(); //객체 생성
        settingFragment = new SettingFragment(); //객체 생성
        contactListFragment = new AddressFragment(); //객체 생성

        getAddressBooks();

        //activity_main에 있는 main_frame에 phoneCallFragment를 씌운다
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, phoneCallFragment).commit();
    }

    public void ChangeCallFragment(){

    }

    //권한있는지 없는지 확인
    public void PermissionCheck(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},0); //전화거는 퍼미션
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CALL_LOG},0); //기록받아오는 퍼미션
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},0); //오디오기록 퍼미션
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},0); //오디오기록 퍼미션
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

    public ArrayList getNames(){
        return this.nameList;
    }

    public void getAddressBooks ()
    {
        //주소록 가져오는 부분
        nameList = new ArrayList();
        numberList = new ArrayList();

        Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null,null,null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc");

        while(c.moveToNext()){
            //연락처 id 값
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)); // 아이디 가져온다.
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)); //이름을 가져온다.
            nameList.add(name);

            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null,null);

            if(phoneCursor.moveToNext()){
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d("Test number",number);
                numberList.add(number);
            }
            phoneCursor.close();
        }
        c.close();

    }
    public ArrayList getNumbers(){
        return this.numberList;
    }
}