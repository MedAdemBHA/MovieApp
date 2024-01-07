package com.example.movieapplication;


import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;;
import com.google.android.gms.tasks.Task;

public class Registre extends AppCompatActivity implements View.OnClickListener  {
    ImageButton btn_back;
    EditText emailR,passwordR;
    Button btnR;
    private FirebaseAuth mAuth;
    String TAG="Android Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);
        btn_back= findViewById(R.id.Btn_back);
        btn_back.setOnClickListener(this);
        mAuth =FirebaseAuth.getInstance() ;
        emailR=findViewById(R.id.Email_reg);
        passwordR=findViewById(R.id.PW_reg);
        btnR= findViewById(R.id.Btn_Reg);
        btnR.setOnClickListener(this);

    }
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent i = new Intent(Registre.this, Home.class);
            startActivity(i);
            Log.d(TAG,"Alredy logged in");
            Log.d(TAG,currentUser.getEmail());
            Log.d(TAG,currentUser.getUid());

        }

    }
    public void signUp(){
        String emailText = emailR.getText().toString();
        String passwordText = passwordR.getText().toString();
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(Registre.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(Registre.this, "You created a new account", Toast.LENGTH_SHORT).show();
                                Intent myIntent2 = new Intent(Registre.this, Home.class);
                                startActivity(myIntent2);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Registre.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Btn_back) {
            Intent myIntent = new Intent(Registre.this, Login.class);
            startActivity(myIntent);
        }
        if (view.getId() == R.id.Btn_Reg) {
            signUp();
        }

    }
}