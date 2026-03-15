package com.nomann.practice_all_inyears;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private WebView webView;
    private TextToSpeech tts;
    private ExtendedFloatingActionButton btnRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        webView = findViewById(R.id.webView);
        btnRead = findViewById(R.id.btnRead);

        // ১. WebView সেটিংস
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // জাভাস্ক্রিপ্ট ইন্টারফেস সেট করা
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.prothomalo.com/");

        // ২. TextToSpeech সেটিংস
        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                // বাংলা ভাষা সেট করা
                int result = tts.setLanguage(new Locale("bn", "BD"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "বাংলা সাপোর্ট করছে না!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ৩. বাটনে ক্লিক করলে ওয়েব থেকে টেক্সট নিয়ে পড়া শুরু করবে
        btnRead.setOnClickListener(v -> {
            Toast.makeText(this, "পড়া শুরু হচ্ছে...", Toast.LENGTH_SHORT).show();
            // জাভাস্ক্রিপ্ট দিয়ে পুরো পেজের টেক্সট কালেক্ট করা
            webView.loadUrl("javascript:window.HTMLOUT.processHTML(document.body.innerText);");
        });
    }

    // জাভাস্ক্রিপ্ট থেকে ডাটা রিসিভ করার জন্য ইন্টারফেস
    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String fullText) {
            if (fullText != null && !fullText.isEmpty()) {
                // বড় টেক্সটকে ছোট ছোট ভাগে ভাগ করে পড়া
                String[] chunks = fullText.split("(?<=\\.)|(?<=\\n)");

                boolean isFirst = true;
                for (String chunk : chunks) {
                    if (chunk.trim().isEmpty()) continue;

                    if (isFirst) {
                        // প্রথম অংশটি সাথে সাথে পড়া শুরু করবে
                        tts.speak(chunk, TextToSpeech.QUEUE_FLUSH, null, null);
                        isFirst = false;
                    } else {
                        // পরের অংশগুলো কিউতে থাকবে
                        tts.speak(chunk, TextToSpeech.QUEUE_ADD, null, null);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        // অ্যাপ বন্ধ হলে সাউন্ড বন্ধ করা
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}