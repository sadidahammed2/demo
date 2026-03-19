package com.nomann.practice_all_inyears;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class plear extends AppCompatActivity {

    private WebView courseWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // সিকিউরিটির জন্য স্ক্রিনশট বন্ধ রাখা (ঐচ্ছিক)

        setContentView(R.layout.activity_plear);

        // UI ইনসেট হ্যান্ডেল করা (আপনার দেওয়া ডিফল্ট কোড)


        // WebView সেটআপ
        courseWebView = findViewById(R.id.courseWebView);
        setupWebView();

        String videoId = "NDs1NUWwWUY";
        loadPlyrPlayer(videoId);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = courseWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // জাভাস্ক্রিপ্ট অন করা
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        courseWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        courseWebView.setBackgroundColor(Color.BLACK);
        courseWebView.setWebChromeClient(new WebChromeClient());
    }

    private void loadPlyrPlayer(String videoId) {
        // Plyr HTML টেম্পলেট
        String html = "<!DOCTYPE html><html><head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no'>" +
                "<link rel='stylesheet' href='https://cdn.plyr.io/3.7.8/plyr.css' />" +
                "<style>" +
                "  html, body { margin: 0; padding: 0; width: 100%; height: 100%; background: #000; overflow: hidden; }" +
                "  .plyr { width: 100% !important; height: 100% !important; --plyr-color-main: #4F46E5; }" +
                "</style>" +
                "</head><body>" +
                "  <div id='player' data-plyr-provider='youtube' data-plyr-embed-id='" + videoId + "'></div>" +
                "  <script src='https://cdn.plyr.io/3.7.8/plyr.polyfilled.js'></script>" +
                "  <script>" +
                "    const player = new Plyr('#player', {" +
                "      controls: ['play-large', 'rewind', 'play', 'fast-forward', 'progress', 'current-time', 'duration', 'settings', 'pip', 'fullscreen']," +
                "      seekTime: 10," +
                "      youtube: { noCookie: true, rel: 0, showinfo: 0, modestbranding: 1 }" +
                "    });" +
                "  </script></body></html>";

        // ইউটিউব ভিডিও লোড করা
        courseWebView.loadDataWithBaseURL("https://www.youtube-nocookie.com", html, "text/html", "utf-8", null);
    }
}