package com.example.loginandsignupwithemailverification;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    public TextView registerTV;
    public EditText email, password;
    public Button login;
    private ProgressDialog mProgressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerTV = findViewById(R.id.toSignup);
        login = findViewById(R.id.loginBtn);
        mProgressDialog = new ProgressDialog(this);
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("Processing Your Request");
                mProgressDialog.setTitle("Please Wait...");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();
                checkLoginDetails();
                
            }
        });

    }

    private void checkLoginDetails() {
        String userEmail = email.getText().toString();
        String userPass = password.getText().toString();

        if(userEmail.isEmpty())
            email.setError("Required");
        if(userPass.isEmpty())
            password.setError("Required");

        RequestQueue queue = Volley.newRequestQueue(Login.this);

        String url =  "http://192.168.43.233:9080/api/v1/user/login";

        // Set Parameters
        HashMap<String, String> params = new HashMap<String, String >();
        params.put("email", userEmail);
        params.put("password", userPass);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Get values from the JSON response
                    String fullName = (String) response.get("fullname");
                    String email = (String) response.get("email");

                    Intent intent = new Intent(Login.this, ProfileActivity.class);
                    intent.putExtra("fullname", fullName);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    mProgressDialog.dismiss();
                    finish();
                } catch (JSONException e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });

        queue.add(jsonObjectRequest);

    }

    private boolean isValidEmail(CharSequence input) {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches();
    }
}