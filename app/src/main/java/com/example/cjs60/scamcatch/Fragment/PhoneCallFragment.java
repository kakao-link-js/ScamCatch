package com.example.cjs60.scamcatch.Fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cjs60.scamcatch.MainActivity;
import com.example.cjs60.scamcatch.R;


public class PhoneCallFragment extends Fragment {
    View view; //
    private Button one,two,three,four,five,six,seven,eight,nine,zero,call,del; //레이아웃에 있는 버튼을 받기위한 변수 선언.
    private TextView callText; //레이아웃에 있는 번호적는 Text를 받기위한 변수 선언.
    private String Enum;

    @Override //프래그먼트 생성시에 한번 실행되는 메소드
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override //프래그먼트 생성시에 뷰(화면)를 구성하는 메소드
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.phone_call_layout,null); //view를 불러온다.
        SetLayout(view);

        return view;
    }

    //레이아웃의 객체들과 변수들을 연결하는 메소드. 항상 제일 먼저 선언되어야 한다.
        private void SetLayout(View view){
        one = (Button)view.findViewById(R.id.onebtn);
        one.setOnClickListener(onClickListener);
        two = (Button)view.findViewById(R.id.twobtn);
        two.setOnClickListener(onClickListener);
        three = (Button)view.findViewById(R.id.thirdbtn);
        three.setOnClickListener(onClickListener);
        four = (Button)view.findViewById(R.id.fourbtn);
        four.setOnClickListener(onClickListener);
        five = (Button)view.findViewById(R.id.fivebtn);
        five.setOnClickListener(onClickListener);
        six = (Button)view.findViewById(R.id.sixbtn);
        six.setOnClickListener(onClickListener);
        seven = (Button)view.findViewById(R.id.sevenbtn);
        seven.setOnClickListener(onClickListener);
        eight = (Button)view.findViewById(R.id.eigthbtn);
        eight.setOnClickListener(onClickListener);
        nine = (Button)view.findViewById(R.id.ninebtn);
        nine.setOnClickListener(onClickListener);
        zero = (Button)view.findViewById(R.id.zerobtn);
        zero.setOnClickListener(onClickListener);
        call = (Button)view.findViewById(R.id.callbtn);
        call.setOnClickListener(onClickListener);
        del = (Button)view.findViewById(R.id.delbtn);
        del.setOnClickListener(onClickListener);
        callText = (TextView)view.findViewById(R.id.callEditText);
    }
    //버튼 클릭 이벤트를 담은 메소드.
    Button.OnClickListener onClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.onebtn:
                    callText.setText(callText.getText() + "1"); //기존것 + 숫자
                    break;
                case R.id.twobtn:
                    callText.setText(callText.getText() + "2"); //기존것 + 숫자
                    break;
                case R.id.thirdbtn:
                    callText.setText(callText.getText() + "3"); //기존것 + 숫자
                    break;
                case R.id.fourbtn:
                    callText.setText(callText.getText() + "4"); //기존것 + 숫자
                    break;
                case R.id.fivebtn:
                    callText.setText(callText.getText() + "5"); //기존것 + 숫자
                    break;
                case R.id.sixbtn:
                    callText.setText(callText.getText() + "6"); //기존것 + 숫자
                    break;
                case R.id.sevenbtn:
                    callText.setText(callText.getText() + "7"); //기존것 + 숫자
                    break;
                case R.id.eigthbtn:
                    callText.setText(callText.getText() + "8"); //기존것 + 숫자
                    break;
                case R.id.ninebtn:
                    callText.setText(callText.getText() + "9"); //기존것 + 숫자
                    break;
                case R.id.zerobtn:
                    callText.setText(callText.getText() + "0"); //기존것 + 숫자
                    break;
                case R.id.delbtn:
                    String text = callText.getText().toString(); //이미 적힌 텍스트를 받는다.
                    if(text.length()>0) //텍스트의 길이가 0 보다 작으면 지우지 않는다.
                        callText.setText(text.substring(0, text.length() - 1)); //전체길이에서 1만 뺀다.
                    break;
                case R.id.callbtn:
                    Enum = callText.getText().toString();
                    String tel = "tel:" + Enum;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(tel));
                    startActivity(intent);
                    break;
            }
        }
    };


}