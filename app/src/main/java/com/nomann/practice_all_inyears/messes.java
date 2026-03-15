package com.nomann.practice_all_inyears;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class messes extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private static final String SENT_ACTION = "SMS_SENT";

    private EditText etNumber, etMessage;
    private Button btnSim1, btnSim2;
    private BroadcastReceiver smsSentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messes);

        // UI Initialization
        etNumber = findViewById(R.id.etNumber);
        etMessage = findViewById(R.id.etMessage);
        btnSim1 = findViewById(R.id.btnSim1);
        btnSim2 = findViewById(R.id.btnSim2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SIM 1 বাটন ক্লিক
        btnSim1.setOnClickListener(v -> checkPermissionAndSend(0));

        // SIM 2 বাটন ক্লিক
        btnSim2.setOnClickListener(v -> checkPermissionAndSend(1));
    }

    private void checkPermissionAndSend(int simIndex) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            sendSMS(simIndex);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},
                    SMS_PERMISSION_CODE);
        }
    }

    private void sendSMS(int simIndex) {
        String phoneNumber = etNumber.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (phoneNumber.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "নম্বর এবং মেসেজ লিখুন", Toast.LENGTH_SHORT).show();
            return;
        }

        // PendingIntent তৈরি (Android 12+ এর জন্য FLAG_IMMUTABLE জরুরি)
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT_ACTION),
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);

        // ব্রডকাস্ট রিসিভার সেটআপ (Android 14 সিকিউরিটি ফিক্স)
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "মেসেজ সফলভাবে পাঠানো হয়েছে", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getBaseContext(), "মেসেজ পাঠাতে ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show();
                        break;
                }
                // রিসিভারটি আন-রেজিস্টার করা যাতে মেমোরি লিক না হয়
                unregisterReceiver(this);
                smsSentReceiver = null;
            }
        };

        // Android 14+ এ RECEIVER_NOT_EXPORTED ফ্ল্যাগ ব্যবহার করা বাধ্যতামূলক
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(smsSentReceiver, new IntentFilter(SENT_ACTION), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(smsSentReceiver, new IntentFilter(SENT_ACTION));
        }

        try {
            SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                List<SubscriptionInfo> subList = subManager.getActiveSubscriptionInfoList();

                if (subList != null && subList.size() > simIndex) {
                    int subId = subList.get(simIndex).getSubscriptionId();

                    SmsManager smsManager;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        smsManager = this.getSystemService(SmsManager.class).createForSubscriptionId(subId);
                    } else {
                        smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);
                    }

                    // পপ-আপ ছাড়াই সরাসরি মেসেজ পাঠাবে
                    smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null);
                } else {
                    Toast.makeText(this, "নির্বাচিত সিম পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // অ্যাপ বন্ধ হওয়ার সময় রিসিভার রেজিস্টার থাকলে সেটি রিলিজ করে দেওয়া
        if (smsSentReceiver != null) {
            try {
                unregisterReceiver(smsSentReceiver);
            } catch (Exception e) {
                // Already unregistered
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "পারমিশন পাওয়া গেছে!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "পারমিশন প্রয়োজন!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}