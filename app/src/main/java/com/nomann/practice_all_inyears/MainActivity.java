package com.nomann.practice_all_inyears;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // ভেরিয়েবল ডিক্লেয়ারেশন
    EditText editText;
    Button button,button2 ,button3,buttonn,button4;
    RecyclerView recyclerView;
    ArrayList<String> allNotes = new ArrayList<>(); // ডাটা রাখার লিস্ট
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ভিউ কানেক্ট করা
        button4= findViewById(R.id.button4);

        button3 = findViewById(R.id.button3);
        editText = findViewById(R.id.edit_text_id);
        button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recycler_view);

        button2 = findViewById(R.id.button2);

        // RecyclerView সেটআপ
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customAdapter = new CustomAdapter(allNotes); // অ্যাডাপ্টারে লিস্টটি পাঠিয়ে দেওয়া
        recyclerView.setAdapter(customAdapter);



        buttonn= findViewById(R.id.buttonn);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent n = new Intent(MainActivity.this, trading.class);
                startActivity(n);
            }
        });
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mess = new Intent(MainActivity.this,tradingpractice.class);
                startActivity(mess);
            }
        });

        // বাটন ক্লিক লজিক

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,dataadd.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,plear.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userText = editText.getText().toString();

                if (!userText.isEmpty()) {
                    allNotes.add(userText); // ১. লিস্টে ডাটা যোগ করা
                    customAdapter.notifyDataSetChanged(); // ২. অ্যাডাপ্টারকে জানানো যে নতুন ডাটা আসছে
                    editText.setText(""); // ৩. ইনপুট বক্স খালি করা
                }
            }
        });
    }

    // --- Custom Adapter Class ---
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private ArrayList<String> localDataSet;

        // কনস্ট্রাক্টর: এখান দিয়েই MainActivity থেকে ডাটা অ্যাডাপ্টারে আসে
        public CustomAdapter(ArrayList<String> dataSet) {
            this.localDataSet = dataSet;
        }

        // ViewHolder ক্লাস: এটি প্রতিটি রো (Row) এর ডিজাইন ধরে রাখে
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;

            public ViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.textView);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // text_row_item এক্সএমএল ফাইলটিকে এখানে কানেক্ট করা হয়
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.text_row_item, viewGroup, false);
            return new ViewHolder(view);
        }
        int position = 10;

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            // আপনার কাঙ্ক্ষিত অংশ: লিস্ট থেকে ডাটা নিয়ে টেক্সটভিউতে সেট করা
            String note = localDataSet.get(position);
            viewHolder.getTextView().setText(note);
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }
    }
}
