package com.example.movieapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Profile extends AppCompatActivity implements View.OnClickListener  {
    ImageButton btn_backHome;
    String TAG = "Android";
    TextView id;
    TextView profiluser;
    EditText name,email;
    Button update;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btn_backHome= findViewById(R.id.Btn_BackHome);
        btn_backHome.setOnClickListener(this);
        update = findViewById(R.id.Btn_updateUser);
        update.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        id = findViewById(R.id.ID);
        email = findViewById(R.id.editEmail);
        name = findViewById(R.id.editName);
        profiluser=findViewById(R.id.Profiluser);
    }
    public void onStart() {
        super.onStart();
        //check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent I = getIntent();
        String subName = I.getStringExtra("username");
        if (currentUser != null ){
            FirebaseUser user = mAuth.getCurrentUser();

            id.setText(user.getUid());
            email.setText(user.getEmail());
            name.setText(user.getDisplayName());
            profiluser.setText(user.getDisplayName());
        }
    }
    public void Update() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name.getText().toString())

                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Btn_BackHome) {
            Intent myIntent = new Intent(Profile.this, Home.class);
            startActivity(myIntent);
        }
        if (view.getId() == R.id.Btn_updateUser) {
            Update();
        }

    }
}