package com.example.movieapplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements View.OnClickListener {
    String TAG = "Android Test";
    TextView btn_logout;
    ImageButton btn_settings;
    ArrayList<recycleview_list> recycleview_list;
    RecyclerView recyclerView;
    recyclerview_adapter recyclerview_adapter;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_settings = findViewById(R.id.Btn_settings);
        btn_settings.setOnClickListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationHome);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recycleview_list = new ArrayList<>();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {

                    Toast.makeText(Home.this, "Home selected", Toast.LENGTH_SHORT).show();

                    return true;
                } else if (item.getItemId() == R.id.liste) {

                    Intent myIntent = new Intent(Home.this, MymovieListe.class);
                    startActivity(myIntent);
                    return true;
                }


                return false;
            }
        });

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://api.tvmaze.com/shows", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject info = response.getJSONObject(i);

                                recycleview_list.add(new recycleview_list(
                                        info.getString("name"),
                                        info.getString("language"),
                                        info.getString("premiered"),
                                        info.getString("summary"),
                                        info.getJSONObject("image").getString("medium"),
                                        info.getString("id"
                                        )
                                ));
                            }
                            if (response.length() > 0) {
                                recyclerview_adapter = new recyclerview_adapter(recycleview_list, Home.this);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                                recyclerView.setAdapter(recyclerview_adapter);

                            } else {
                                Toast.makeText(getApplicationContext(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Home.this, MainActivity.class);
        startActivity(intent);
        Log.d(TAG, "logout: success");
        finish();
    }







    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_logout) {
            logout();
        }
        if (view.getId() == R.id.Btn_settings) {
            Intent myIntent = new Intent(Home.this, Profile.class);
            startActivity(myIntent);
        }

    }
}
