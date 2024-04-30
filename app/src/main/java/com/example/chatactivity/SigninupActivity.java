package com.example.chatactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninupActivity extends AppCompatActivity {

    EditText userName,userEmail, userPassword;
    TextView signinBtn, signupBtn;
    String name,email,password;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinup);

        userName=findViewById(R.id.usernametext);
        userEmail=findViewById(R.id.emailtext);
        userPassword=findViewById(R.id.passwordtext);
        signinBtn=findViewById(R.id.login);
        signupBtn =findViewById(R.id.signup);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= userName.getText().toString().trim();
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    userName.setError("Lutfen isim giriniz");
                    userName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    userEmail.setError("Lutfen mail giriniz");
                    userEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    userPassword.setError("Lutfen sifrenizi giriniz");
                    userPassword.requestFocus();
                    return;
                }
                Signup();

            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(SigninupActivity.this, MainActivity.class));
            finish();
        }
    }

    private void Signup() {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(),password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        firebaseUser.updateProfile(userProfileChangeRequest);
                        UserModel userModel= new UserModel(FirebaseAuth.getInstance().getUid(),name,email,password);
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);
                        Intent intent = new Intent(SigninupActivity.this,MainActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SigninupActivity.this, "Kayt basarisiz", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}