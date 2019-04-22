package com.example.smartfridgeapp;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.amazonaws.mobile.client.AWSMobileClient;

public class AccountActivity extends AppCompatActivity {
    private TextView acname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        acname = findViewById(R.id.account_name_txt);
        acname.setText("User Name: " + PreferenceManager.getDefaultSharedPreferences(this).getString("display_name", "John Doe"));
        
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

