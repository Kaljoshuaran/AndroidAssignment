package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splashscreen.model.Articles;
import com.example.splashscreen.model.News;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    private String userID;

    DrawerLayout mDrawerLayout;

    RecyclerView mRecyclerView;
    final String API_KEY = "14fc614ece8f4bdc88e5a94223b0745d";
    DashboardAdapter mAdapter;
    List<Articles> mArticles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String country = getCountry();
        retrieveJson(country,API_KEY);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = mFirebaseUser.getUid();

        final TextView greetingDrawersTextView = (TextView) findViewById(R.id.greetingDrawers);

        mDatabaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    String profileName = userProfile.name;

                    greetingDrawersTextView.setText("Welcome back, " + profileName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();

            }

        });

    }

    public void ClickMenu(View v) {
        MainActivity.openDrawer(mDrawerLayout);
    }

    public void ClickLogo(View v) {
        MainActivity.closeDrawer(mDrawerLayout);
    }

    public void ClickHome(View v) {
        MainActivity.redirectActivity(this, MainActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickDashboard(View v) {
        recreate();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickProfile(View v) {
        MainActivity.redirectActivity(this, ProfileActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickLogOut(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(mDrawerLayout);
    }

    public void retrieveJson(String country, String apiKey) {

        Call<News> call = ApiClient.getInstance().getApi().getNews(country, apiKey);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    mArticles.clear();
                    mArticles = response.body().getArticles();
                    mAdapter = new DashboardAdapter(DashboardActivity.this, mArticles);
                    mRecyclerView.setAdapter(mAdapter);

                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}