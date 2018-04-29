package com.armstring.firebasefirstapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmailLogin);
        etPassword = (EditText) findViewById(R.id.etPasswordLogin);

        mAuth = FirebaseAuth.getInstance();


    }

    public void btnLogin(View view) {

        String emailString = etEmail.getText().toString();
        String passwordString = etPassword.getText().toString();

        if(!emailString.equals("") && !passwordString.equals("")) {
            mAuth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,
                                        "login successful", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(LoginActivity.this,
                                        "unexpected error happened", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(LoginActivity.this,
                    "please enter email and password", Toast.LENGTH_SHORT).show();
        }

    }
}
