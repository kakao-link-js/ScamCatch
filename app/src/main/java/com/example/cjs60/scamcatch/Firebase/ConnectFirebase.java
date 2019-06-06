package com.example.cjs60.scamcatch.Firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectFirebase {
    private DatabaseReference m_Database;
    String TAG = "ConnectFirebase";
    long mNow;
    Date mDate;
    int count;
    double accuracy;
    String m_Date;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public ConnectFirebase(){
        m_Database = FirebaseDatabase.getInstance().getReference();
    }
    public int GetReportCount (String phoneNum){
        //The uID it's unique id generated by firebase database
        m_Database.child("report_list").child(phoneNum).child("count").addListenerForSingleValueEvent(
                new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null) {
                            Log.d(TAG,"GetReportCount : dataSnapShot is Null " +"JAESUMNIA");
                            count = 0;
                        }
                        else {
                            String value = dataSnapshot.getValue().toString();
                            Log.d(TAG, "Get " + phoneNum + " this count " + value +"JAESUMNIA");
                            count = Integer.parseInt(value);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"onCanecelld at GetReport Count");
                        count = 0;
                        // Getting Post failed, log a message
                    }
                });
        return count;
    }

    public void GetReportCountAndShow (String phoneNum, TextView susPiNum){
        //The uID it's unique id generated by firebase database
        m_Database.child("report_list").child(phoneNum).child("count").addListenerForSingleValueEvent(
                new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null) {
                            Log.d(TAG,"GetReportCountAndShow : dataSnapShot is Null"+"Jaesumnia");
                            count = 0;
                        }
                        else {
                            String value = dataSnapshot.getValue().toString();
                            Log.d(TAG, "Get " + phoneNum + " this count " + value+"Jaesumnia");
                            count = Integer.parseInt(value);
                        }
                        susPiNum.setText(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"onCanecelld at GetReport Count");
                        count = 0;
                        susPiNum.setText(count);
                        // Getting Post failed, log a message
                    }
                });
    }

    public void GetReportDate(String phoneNum, TextView lastReport){
        //The uID it's unique id generated by firebase database
        m_Database.child("report_list").child(phoneNum).child("LastCall").addListenerForSingleValueEvent(
                new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null)
                            m_Date = "No Phone Report";
                        else {
                            String value = dataSnapshot.getValue().toString();
                            Log.d(TAG,"Get "+phoneNum+" LastReportDate "+value);
                            m_Date = value;
                        }
                        lastReport.setText("LastReport : "+m_Date);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"onCanecelld at GetReport Date");
                        m_Date = "No Phone Report";
                        lastReport.setText("LastReport : "+m_Date);
                        // Getting Post failed, log a message
                    }
                });
    }

    public double GetAccuracy (String phoneNum){
        //The uID it's unique id generated by firebase database
        m_Database.child("id").child("id"+phoneNum).child("accuracy").addListenerForSingleValueEvent(
                new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null)
                            accuracy = 0;
                        else {
                            String value = dataSnapshot.getValue().toString();
                            Log.d(TAG, "Get " + phoneNum + " this count " + value);
                            accuracy = Double.parseDouble(value) * 100;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"onCanecelld at GetReport Count");
                        accuracy = 0;
                        // Getting Post failed, log a message
                    }
                });
        return accuracy;
    }

    public void SendReport(String phoneNum,int count){
        //Log.d("firebasetest :",String.valueOf(count));
        m_Database.child("report_list").child(phoneNum).child("count").setValue(count+1);
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        m_Database.child("report_list").child(phoneNum).child("LastCall").setValue(mFormat.format(mDate));
    }
}
