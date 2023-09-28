package com.example.passguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPassword extends AppCompatActivity {
    EditText forgotEmail;
    Button btnForgotPass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog pd;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    TextView alreadyHaveAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotEmail=findViewById(R.id.forgotEmail);
        btnForgotPass=findViewById(R.id.btnForgotPass);
        pd=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        alreadyHaveAccount=findViewById(R.id.alreadyHaveAccount);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=forgotEmail.getText().toString();
                if(!email.matches(emailPattern)){
                    forgotEmail.requestFocus();
                    forgotEmail.setError("Invalid Email");
                } else{
                    resetPassword(email);
                }
            }
        });
    }
    private void resetPassword(String email) {
        pd.setTitle("Processing...");
        pd.setMessage("Please wait for few seconds");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(ForgotPassword.this, "Reset Link Successfully Sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                        } else {
                            pd.dismiss();
                            Toast.makeText(ForgotPassword.this, "an Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}