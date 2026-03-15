package com.nomann.practice_all_inyears;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class webview_to_read extends AppCompatActivity {

    private WebView webView;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_to_read);

        webView = findViewById(R.id.webView);
        Button btnRead = findViewById(R.id.btnReadAndOpen);

        // ১. WebView সেটিংস (JavaScript এনাবল করা জরুরি)
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.thedailystar.net");



        // ৩. বাটনে ক্লিক করলে ওয়েবসাইট থেকে টেক্সট স্ক্যান করবে

    }


}
