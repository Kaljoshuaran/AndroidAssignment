package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText regName, regEmail, regPass, regPass2, regPhone;
    private ProgressBar mProgressBar2;

    private Button registerBtn, LogBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        regName = findViewById(R.id.editName);
        regEmail = findViewById(R.id.editEmailAdd);
        regPass = findViewById(R.id.editRegPass);
        regPass2 = findViewById(R.id.editRegPass2);
        regPhone = findViewById(R.id.editTextPhone);

        mProgressBar2 = findViewById(R.id.progressBar2);

        LogBackBtn = findViewById(R.id.backToLoginBtn);
        LogBackBtn.setOnClickListener(this);

        registerBtn = findViewById(R.id.regBtn);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
            case R.id.regBtn:
                regBtn();
                break;
        }

    }

    private void regBtn() {
        String name = regName.getText().toString().trim();
        String email = regEmail.getText().toString().trim();
        String pass = regPass.getText().toString().trim();
        String cpass = regPass2.getText().toString().trim();
        String phone = regPhone.getText().toString().trim();

        if (name.isEmpty()) {
            regName.setError("Name is required!");
            regName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            regEmail.setError("Email is required!");
            regEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regEmail.setError("Please provide a valid email!");
            regEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            regPass.setError("Password is required!");
            regPass.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            regPass.setError("Password must be more than 6 characters!");
            regPass.requestFocus();
            return;
        }

        if (cpass.isEmpty() || !cpass.equals(pass)) {
            regPass2.setError("Password does not match!");
            regPass2.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            regPhone.setError("Phone number is required!");
            regPhone.requestFocus();
            return;
        }

        mProgressBar2.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    User user = new User(name, email, phone);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                mProgressBar2.setVisibility(View.GONE);

                            }else {
                                Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                                mProgressBar2.setVisibility(View.GONE);
                            }
                        }
                    });

                }else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                    mProgressBar2.setVisibility(View.GONE);
                }
            }
        });

    }
}