package com.example.loginandsignupwithemailverification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    public TextView loginTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginTV = findViewById(R.id.toLogin);
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}