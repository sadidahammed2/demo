package com.nomann.practice_all_inyears;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {

    TextView mTextField;
    EditText edittext;
    Button start,button5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button5=findViewById(R.id.button5);

        edittext=findViewById(R.id.edittext);
        mTextField=findViewById(R.id.mTextField);
        start= findViewById(R.id.start);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this,webview_to_read.class);
                startActivity(intent);
            }
        });



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              String e =  edittext.getText().toString();

              long b = Long.parseLong(e)*60000;

                new CountDownTimer(b, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        Intent intent = new Intent(MainActivity3.this,MainActivity.class);
                        startActivity(intent);
                    }
                }.start();





            }
        });



    }











}