package com.example.cjs60.scamcatch;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.util.concurrent.Future;

public class SpeechToText extends AsyncTask {

    private static String speechSubscriptionKey = "920e15067413402fbbde3f577f92a796";
    private static String serviceRegion = "koreacentral";
    private String TAG = "STT";
    public String txt;
    public TextView stt;

    public SpeechToText(TextView stt2){
        stt = stt2;
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        while(true) {
            try {
                SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
                assert (config != null);
                Log.d(TAG,"1");

                SpeechRecognizer reco = new SpeechRecognizer(config);
                assert (reco != null);
                Log.d(TAG,"2");

                Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
                assert (task != null);
                Log.d(TAG,"3");

                SpeechRecognitionResult result = task.get();
                assert (result != null);
                Log.d(TAG,"4");

                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    txt = result.toString();
                } else {
                    txt = result.toString();
                }
                Log.d(TAG,txt);

                txt = txt.substring(txt.indexOf('<')+1,txt.indexOf('>'));
                stt.setText(stt.getText() + txt);
                reco.close();
            } catch (Exception ex) {
                Log.d(TAG,"unexpected " + ex.getMessage());
                assert (false);
            }
            if(isCancelled())
                break;
        }
        Log.d(TAG,"end SpeechToText");
        return null;
    }
}
