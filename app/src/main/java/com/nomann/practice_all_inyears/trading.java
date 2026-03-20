package com.nomann.practice_all_inyears;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class trading extends AppCompatActivity {

    private CandleView candleView;
    private Handler handler = new Handler();
    private Random random = new Random();

    // ট্রেডিং লজিক ভেরিয়েবল
    private float currentPrice = 400f; // বর্তমান দাম
    private float openPrice = 400f;    // ক্যান্ডেল শুরুর দাম
    private int tickCount = 0;         // কতবার নড়াচড়া করল

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trading);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        candleView = findViewById(R.id.candleView);
        startMarketSimulation();
    }

    private void startMarketSimulation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // ১. লাইভ প্রাইস পরিবর্তন করা (কাঁপানো)
                float move = (random.nextFloat() * 40) - 20;
                currentPrice += move;

                // সীমানা নির্ধারণ
                if (currentPrice < 100) currentPrice = 100;
                if (currentPrice > 800) currentPrice = 800;

                // ২. ক্যান্ডেল ভিউ আপডেট করা
                candleView.updateLiveCandle(openPrice, currentPrice);

                // ৩. নির্দিষ্ট সময় পর পর নতুন ক্যান্ডেল তৈরি (যেমন প্রতি ২০ টপকানি পর)
                tickCount++;
                if (tickCount >= 15) {
                    candleView.pushToHistory(openPrice, currentPrice);
                    openPrice = currentPrice; // নতুন ক্যান্ডেল আগেরটার ক্লোজ থেকে শুরু হবে
                    tickCount = 0;
                }

                candleView.invalidate();
                handler.postDelayed(this, 100); // প্রতি ০.১ সেকেন্ডে নড়বে
            }
        }, 100);
    }

    // কাস্টম ভিউ ক্লাস
    public static class CandleView extends View {
        private List<CandleModel> history = new ArrayList<>();
        private float liveOpen, liveClose;
        private Paint paintGreen, paintRed, paintLine;

        public CandleView(Context context, AttributeSet attrs) {
            super(context, attrs);
            paintGreen = new Paint();
            paintGreen.setColor(Color.parseColor("#00E676"));
            paintRed = new Paint();
            paintRed.setColor(Color.parseColor("#FF1744"));
            paintLine = new Paint();
            paintLine.setColor(Color.WHITE);
            paintLine.setStrokeWidth(2f);
            paintLine.setAlpha(80);
        }

        public void updateLiveCandle(float open, float close) {
            this.liveOpen = open;
            this.liveClose = close;
        }

        public void pushToHistory(float open, float close) {
            history.add(new CandleModel(open, close));
            if (history.size() > 20) history.remove(0); // মেমোরি ক্লিয়ার
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float spacing = 60f;
            float startX = getWidth() - 100f; // ডান পাশ থেকে শুরু
            float baseY = getHeight() / 1.5f;

            // ১. লাইভ ক্যান্ডেল ড্র করা (একদম ডানে)
            drawCandle(canvas, startX, baseY, liveOpen, liveClose);

            // ২. ইতিহাস বা পুরনো ক্যান্ডেলগুলো ড্র করা (বামে সরে যাবে)
            float historyX = startX - spacing;
            for (int i = history.size() - 1; i >= 0; i--) {
                CandleModel data = history.get(i);
                drawCandle(canvas, historyX, baseY, data.open, data.close);
                historyX -= spacing;
            }
        }

        private void drawCandle(Canvas canvas, float x, float baseY, float open, float close) {
            Paint p = (close >= open) ? paintGreen : paintRed;
            float top = baseY - Math.max(open, close);
            float bottom = baseY - Math.min(open, close);

            // ক্যান্ডেল বডি (একটু চওড়া)
            canvas.drawRect(x - 20, top, x + 20, bottom, p);
            // ক্যান্ডেল সুতা (Wick)
            canvas.drawLine(x, top - 20, x, bottom + 20, p);
        }
    }

    // ডাটা মডেল
    static class CandleModel {
        float open, close;
        CandleModel(float o, float c) { this.open = o; this.close = c; }
    }
}