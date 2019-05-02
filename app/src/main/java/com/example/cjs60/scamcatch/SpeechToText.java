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

    private static String speechSubscriptionKey = "2d47191c07c64a6aa14624bde86e8950";
    private static String serviceRegion = "westus";
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

                SpeechRecognizer reco = new SpeechRecognizer(config);
                assert (reco != null);

                Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
                assert (task != null);

                SpeechRecognitionResult result = task.get();
                assert (result != null);

                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    txt = result.toString();
                } else {
                    txt = result.toString();
                }
                txt = txt.substring(txt.indexOf('<')+1,txt.indexOf('>'));
                stt.setText(stt.getText() + txt);
                reco.close();
            } catch (Exception ex) {
                Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
                assert (false);
            }
        }
    }
}
