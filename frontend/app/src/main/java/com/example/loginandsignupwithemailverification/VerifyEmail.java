package com.example.loginandsignupwithemailverification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerifyEmail extends AppCompatActivity {

    public EditText code1, code2, code3, code4, code5, code6;
    public Button verifyBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        Intent intent = getIntent();
        String verificationCode = intent.getStringExtra("verification_Code");

        code1 = findViewById(R.id.codeA);
        code2 = findViewById(R.id.codeB);
        code3 = findViewById(R.id.codeC);
        code4 = findViewById(R.id.codeD);
        code5 = findViewById(R.id.codeE);
        code6 = findViewById(R.id.codeF);

        verifyBtn = findViewById(R.id.verify);

        setupEditTextListener(code1, code2);
        setupEditTextListener(code2, code3);
        setupEditTextListener(code3, code4);
        setupEditTextListener(code4, code5);
        setupEditTextListener(code5, code6);
        setupEditTextListener(code6, null);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = code1.getText().toString();
                String b = code2.getText().toString();
                String c = code3.getText().toString();
                String d = code4.getText().toString();
                String e = code5.getText().toString();
                String f = code6.getText().toString();

                String code = a + b + c + d + e + f;

                if(!verificationCode.equals(code)){
                    Toast.makeText(VerifyEmail.this, "Verification Code is Incorrect, Please try again", Toast.LENGTH_SHORT).show();
                    code1.setText("");
                    code2.setText("");
                    code3.setText("");
                    code4.setText("");
                    code5.setText("");
                    code6.setText("");
                } else{
                    Toast.makeText(VerifyEmail.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerifyEmail.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void setupEditTextListener(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && nextEditText != null) {
                    nextEditText.requestFocus();  // Move focus to next EditText
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        });
    }
}