package com.example.loginandsignupwithemailverification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SignUp extends AppCompatActivity {
    public TextView loginTV;
    public Button registerBTN;
    public EditText fullName, userName, uEmail, pWord, confirmPword;
    public String verificationCode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.fullname);
        userName = findViewById(R.id.username);
        uEmail = findViewById(R.id.email);
        pWord = findViewById(R.id.password);
        confirmPword = findViewById(R.id.confirmPassword);
        registerBTN = findViewById(R.id.registerBtn);




        loginTV = findViewById(R.id.toLogin);
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDetails();
                generateVerificationCode();
                Toast.makeText(SignUp.this, verificationCode, Toast.LENGTH_LONG).show();
            }
        });


    }

    private void storeDetails() {
        String fullname = fullName.getText().toString();
        String username = userName.getText().toString();
        String email = uEmail.getText().toString();
        String password = pWord.getText().toString();
        String confirmPass = confirmPword.getText().toString();

        if(fullname.equals("") && username.equals("") && email.equals("") && password.equals("") && confirmPass.equals("")){
            Toast.makeText(SignUp.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
        }
        else{

        }

    }

    public void sendEmail(String stringReceiverEmail, String verificationCode){
        try{
        String stringSenderEmail = BuildConfig.SENDER_EMAIL;
        String stringPassSenderEmail = BuildConfig.EMAIL_PASS;

        String stringHost = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        javax.mail.Session session = Session.getInstance(properties, new Authenticator(){

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return  new PasswordAuthentication(stringSenderEmail, stringPassSenderEmail);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

        String emailContent = String.format("Hello Brian, \n\nYour verification code is %s \n\n Regards, Brian", verificationCode);
        mimeMessage.setSubject("Subject: Verification Code");
        mimeMessage.setText(emailContent);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        thread.start();

        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    public void generateVerificationCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        verificationCode = String.valueOf(code);
        String userEmail = uEmail.getText().toString();
        sendEmail(userEmail, verificationCode);
    }
}