package com.example.cjs60.scamcatch.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Dialog;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.example.cjs60.scamcatch.Firebase.ConnectFirebase;
import com.example.cjs60.scamcatch.MainActivity;
import com.example.cjs60.scamcatch.PopUpActivity;
import com.example.cjs60.scamcatch.R;

@SuppressLint("ValidFragment")
public class SettingFragment extends Fragment {
    View view;
    MainActivity mainActivity;
    SharedPreferences sharedPreferences;
    Switch aSwitch;

    ConnectFirebase connectFirebase;

    @SuppressLint("ValidFragment")
    public SettingFragment(MainActivity mActivity){ //PhoneCallFragment 생성자 (생성자란 이 클래스가 만들어질때 무조건 실행됨)
        mainActivity = mActivity; //메인엑티비티를 가져와서 메인엑티비티의 메소드를 사용할 수 있게 한다.
    }


    @Override //프래그먼트 생성시에 한번 실행되는 메소드
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override //프래그먼트 생성시에 뷰(화면)를 구성하는 메소드
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_frgment,null); //view를 불러온다.

        connectFirebase = new ConnectFirebase();

        view.findViewById(R.id.uisimBtn).setOnClickListener(onClickListener);
        view.findViewById(R.id.helpBtn).setOnClickListener(onClickListener);
        aSwitch = (Switch)view.findViewById(R.id.switchBtn);
        sharedPreferences = getContext().getSharedPreferences("sFile",0);
        SetSwitchBtn();
        return view;
    }

    Button.OnClickListener onClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.uisimBtn:
                    mOnPopupClick(view);
                    break;
                case R.id.helpBtn:
                    int count = connectFirebase.GetReportCount("01051306027");
                    connectFirebase.SendReport("01051306027",count);
                    Toast.makeText(getContext(),connectFirebase.GetReportDate("01051306027"),Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public void SetSwitchBtn(){
        aSwitch.setChecked(sharedPreferences.getBoolean("alarm",true));
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("alarm",isChecked).commit();
            }
        });
    }


    //버튼
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출

        Intent intent = new Intent(getActivity(), PopUpActivity.class);
        intent.putExtra("percent",sharedPreferences.getString("percent","80"));
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            String result = data.getStringExtra("result");
            Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();

            //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("percent",result); // key, value를 이용하여 저장하는 형태
            editor.commit();
        }
    }

}
