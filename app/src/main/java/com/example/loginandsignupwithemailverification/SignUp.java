package com.example.loginandsignupwithemailverification;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class SignUp extends AppCompatActivity {
    public TextView loginTV;
    public Button registerBTN;
    public EditText fullName, userName, uEmail, pWord, confirmPword;
    public String verificationCode;
    public FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference;
    private ProgressDialog mProgressDialog;
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
        firebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");


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
                mProgressDialog.setMessage("Processing Your Request");
                mProgressDialog.setTitle("Please Wait...");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();
                //storeDetailsinFirebase();
                storeDetailsinMySQL();
            }
        });


    }

    private void storeDetailsinMySQL() {
        String fullname = fullName.getText().toString();
        String username = userName.getText().toString();
        String email = uEmail.getText().toString();
        String password = pWord.getText().toString();
        String confirmPass = confirmPword.getText().toString();

        if (fullname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            if (fullname.isEmpty()) {
                fullName.setError("Required");
                mProgressDialog.dismiss();}
            if (username.isEmpty()) {
                userName.setError("Required");
                mProgressDialog.dismiss();}
            if (email.isEmpty()){
                uEmail.setError("Required");
                mProgressDialog.dismiss();}
            if (password.isEmpty()){
                pWord.setError("Required");
                mProgressDialog.dismiss();}
            if (confirmPass.isEmpty()){
                confirmPword.setError("Required");
                mProgressDialog.dismiss();}
            Toast.makeText(SignUp.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        } else if (!password.equals(confirmPass)){
            confirmPword.setError("Passwords Do Not Match");
            mProgressDialog.dismiss();
        } else{
            generateVerificationCode();
            RequestQueue queue = Volley.newRequestQueue(SignUp.this);

            String url =  "http://192.168.43.233:9080/api/v1/register";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equalsIgnoreCase("success")){
                        Toast.makeText(SignUp.this, "Registration Succesful", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(SignUp.this, VerifyEmail.class);
                        intent.putExtra("verification_Code", verificationCode);
                        startActivity(intent);
                        finish();
                    } else if (response.equalsIgnoreCase("failed")) {
                        Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    } else {
                        Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                        Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    System.out.println(error.getMessage());
                    Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("fullname", fullname);
                    params.put("username", username);
                    params.put("email", email);
                    params.put("password", password);

                    return params;
                }
            };
            queue.add(stringRequest);

        }
    }

    private void storeDetailsinFirebase() {
        String fullname = fullName.getText().toString();
        String username = userName.getText().toString();
        String email = uEmail.getText().toString();
        String password = pWord.getText().toString();
        String confirmPass = confirmPword.getText().toString();

        if (fullname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            if (fullname.isEmpty()) 
                fullName.setError("Required");
            if (username.isEmpty()) 
                userName.setError("Required");
            if (email.isEmpty()) 
                uEmail.setError("Required");
            if (password.isEmpty()) 
                pWord.setError("Required");
            if (confirmPass.isEmpty()) 
                confirmPword.setError("Required");
            Toast.makeText(SignUp.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        } else if (!password.equals(confirmPword)){
            confirmPword.setError("Passwords Do Not Match");
        } else{
            generateVerificationCode();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressDialog.dismiss();
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if(user != null){
                                    String userId = user.getUid();
                                    User userDetails = new User(fullname, username, email);
                                    databaseReference.child(userId).setValue(userDetails);
                                    Toast.makeText(SignUp.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                }  else {
                                    Toast.makeText(SignUp.this, "Failed to store user details.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(SignUp.this, "Account creation failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

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
        mimeMessage.setSubject("Verification Code");
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