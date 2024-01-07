package com.example.movieapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MymovieListe extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationliste;
    ListView myListview;
    List<Myliste> myMovieList;
    DatabaseReference MylisteDbRef;
    private FirebaseAuth mAuth;
    TextView btn_logout;
    ImageButton btn_settings;

    private EditText updateNote;
    private Calendar calendar;
    TextView MovieName;

    private EditText updateTime;
    private EditText updateDate;
    Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymovie_liste);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_settings = findViewById(R.id.Btn_settings);
        btn_settings.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        MovieName=findViewById(R.id.movieName);

        calendar = Calendar.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        myListview = findViewById(R.id.myListView);
        myMovieList = new ArrayList<>();

        MylisteDbRef = FirebaseDatabase.getInstance("https://movieapplication-60015-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child(currentUser.getUid());

        //track the data base changes
        MylisteDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myMovieList.clear();
                // Iterate through the children of the dataSnapshot
                for (DataSnapshot MylisteDatasnap : dataSnapshot.getChildren()) {
                    Myliste myliste = MylisteDatasnap.getValue(Myliste.class);
                    myMovieList.add(myliste);
                }

                // Check if the list is empty and handle it accordingly
                if (myMovieList.isEmpty()) {
                    Toast.makeText(MymovieListe.this, "No movies found", Toast.LENGTH_SHORT).show();
                } else {
                    ListAdapter adapter = new ListAdapter(MymovieListe.this, myMovieList);
                    myListview.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error appropriately (log or display a message)
                Toast.makeText(MymovieListe.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        myListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Myliste myliste = myMovieList.get(position);

                showUpdateDialog(myliste.getId(), myliste.getMovieName());
                return false;
            }
        });

        bottomNavigationliste = findViewById(R.id.bottomNavigationListe);
        MenuItem listeItem = bottomNavigationliste.getMenu().findItem(R.id.liste);
        listeItem.setChecked(true);

        bottomNavigationliste.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    Intent myIntent = new Intent(MymovieListe.this, Home.class);
                    startActivity(myIntent);
                    return true;
                } else if (item.getItemId() == R.id.liste) {
                    // Handle navigation to the same screen if needed
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MymovieListe.this, MainActivity.class);
        startActivity(intent);
        Log.d("logout", "logout: success");
        finish();
    }

    private void updateData(String id, String MovieName, String note, String time, String date) {
        // Update the data in Firebase
        Myliste updatedMyliste = new Myliste(id, MovieName, note, time, date);

        MylisteDbRef.child(id).setValue(updatedMyliste);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_logout) {
            logout();
        }
        if (view.getId() == R.id.Btn_settings) {
            Intent myIntent = new Intent(MymovieListe.this, Profile.class);
            startActivity(myIntent);
        }
    }

    private void showUpdateDialog(final String id, String name) {
        final AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.update_dialog, null);

        mDialog.setView(mDialogView);
        updateNote = mDialogView.findViewById(R.id.update_text);
        updateTime = mDialogView.findViewById(R.id.update_Time);
        updateDate = mDialogView.findViewById(R.id.update_Date);
        btnUpdate = mDialogView.findViewById(R.id.btnUpdate);
        btnDelete = mDialogView.findViewById(R.id.btnDelete);

        mDialog.setTitle("Updating record with ID: " + id);

        final AlertDialog alertDialog = mDialog.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updatedNoteText = updateNote.getText().toString();
                String updatedTimeText = updateTime.getText().toString();
                String updatedDateText = updateDate.getText().toString();
                String MovieNamee = name;



                updateData(id,MovieNamee,updatedNoteText,updatedTimeText,updatedDateText);
                Toast.makeText(MymovieListe.this, "Record Updated", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord(id);

                alertDialog.dismiss();
            }
        });

    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void deleteRecord(String id){

        DatabaseReference recordRef = MylisteDbRef.child(id);

        recordRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showToast("Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Error deleting record");
            }
        });
    }
    public void showTimePickerDialogDialoge(View v) {
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
                        updateTime.setText(sdf.format(calendar.getTime()));

                    }
                },
                hour,
                minute,
                false
        );

        timePickerDialog.show();
    }

    public void showDatePickerDialogDialoge(View v) {
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
                        updateDate.setText(sdf.format(calendar.getTime()));
                    }
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }
}