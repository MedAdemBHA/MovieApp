package com.example.movieapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class detailsmovie extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference MylisteDbRef;
    private FirebaseAuth mAuth;
    Button Btn_add;

    private Calendar calendar;
    private EditText editTextNote;
    private EditText editTextTime;
    private EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsmovie);

        Btn_add = findViewById(R.id.Btn_add);
        Btn_add.setOnClickListener(this);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate = findViewById(R.id.editTextDate);
        calendar = Calendar.getInstance();
        editTextNote = findViewById(R.id.edit_text);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        String iduser = currentUser.getUid();
        MylisteDbRef = FirebaseDatabase.getInstance("https://movieapplication-60015-default-rtdb.europe-west1.firebasedatabase.app").getReference().child(iduser);


        String Name = getIntent().getStringExtra("Name");
        String showId = getIntent().getStringExtra("show_id");
        String showLanguage = getIntent().getStringExtra("show_language");
        String showPremiered = getIntent().getStringExtra("show_premiered");
        String showSummaryHtml = getIntent().getStringExtra("show_summary");
        String showSummaryText = Html.fromHtml(showSummaryHtml).toString();
        String showImageUrl = getIntent().getStringExtra("show_img_url");


        TextView idTextView = findViewById(R.id.showIdTextView);
        TextView languageTextView = findViewById(R.id.showLanguageTextView);
        TextView premieredTextView = findViewById(R.id.showPremieredTextView);
        TextView summaryTextView = findViewById(R.id.showSummaryTextView);

        ImageView imageViewShow = findViewById(R.id.imageViewShow);

        // Set the text of each TextView with the retrieved values
        idTextView.setText("MovieName: " + Name);
        languageTextView.setText("Language: " + showLanguage);
        premieredTextView.setText("Premiered: " + showPremiered);
        summaryTextView.setText("Summary: " + showSummaryText);
        // Load the image using Picasso
        Picasso.get().load(showImageUrl).into(imageViewShow);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Btn_add) {
            addToMyList();
        }
    }

    private void addToMyList() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            final String name = getIntent().getStringExtra("Name");
            final String date = editTextDate.getText().toString();
            final String note = editTextNote.getText().toString();
            final String time = editTextTime.getText().toString();
            final String id =   MylisteDbRef.push().getKey();


            MylisteDbRef.orderByChild("movieName").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(detailsmovie.this, "Movie with the same name already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Myliste myliste = new Myliste(id, name, note, date, time);
                        assert id != null;
                        MylisteDbRef.child(id).setValue(myliste)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Show Toast message
                                        Toast.makeText(detailsmovie.this, "Value added to Firebase", Toast.LENGTH_SHORT).show();

                                        // Reset input values
                                        editTextDate.setText("");
                                        editTextNote.setText("");
                                        editTextTime.setText("");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("TAG", "Error adding value to Firebase", e);
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("TAG", "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            Log.d("TAG", "Current user is null");
        }
    }





    public void showTimePickerDialog(View v) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        editTextTime.setText(sdf.format(calendar.getTime()));
                    }
                },
                hour,
                minute,
                false
        );

        timePickerDialog.show();
    }

    public void showDatePickerDialog(View v) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                        editTextDate.setText(sdf.format(calendar.getTime()));
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

}
