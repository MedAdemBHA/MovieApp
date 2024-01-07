package com.example.movieapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class Login extends AppCompatActivity implements View.OnClickListener{
    Button btn_registre;
    private FirebaseAuth mAuth;
    EditText email,password;
    Button btn1;
    String TAG="Android Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_registre= findViewById(R.id.Btn_register);
        btn_registre.setOnClickListener(this);

        mAuth =FirebaseAuth.getInstance() ;
        email=findViewById(R.id.Email_log);
        password=findViewById(R.id.PW_log);
        btn1= findViewById(R.id.Btn_login);
        btn1.setOnClickListener(this);
    }
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
            Log.d(TAG,"Alredy logged in");
            Log.d(TAG,currentUser.getEmail());
            Log.d(TAG,currentUser.getUid());

        }
    } public void Login() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(Login.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("NotConstructor")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "signInWithEmailAndPassword:success");
                                Intent myIntent3 = new Intent(Login.this, Home.class);
                                startActivity(myIntent3);


                            } else {

                                Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });}

    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Btn_register) {
            Intent myIntent = new Intent(Login.this, Registre.class);
            startActivity(myIntent);
        }
        if (view.getId() == R.id.Btn_login) {
            Login();
            }

    }
}