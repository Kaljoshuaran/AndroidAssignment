package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout mDrawerLayout;

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    private String userID;

    private Button detailsFirst, detailsSecond, detailsThird, detailsFourth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

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
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();

            }

        });

        detailsFirst = findViewById(R.id.details1);
        detailsFirst.setOnClickListener(this);

        detailsSecond = findViewById(R.id.details2);
        detailsSecond.setOnClickListener(this);

        detailsThird = findViewById(R.id.details3);
        detailsThird.setOnClickListener(this);

        detailsFourth = findViewById(R.id.details4);
        detailsFourth.setOnClickListener(this);
    }

    public void ClickMenu(View v) {
        openDrawer(mDrawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View v) {
        closeDrawer(mDrawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View v) {
        recreate();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickDashboard(View v) {
        redirectActivity(this,DashboardActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickProfile(View v) {
        redirectActivity(this,ProfileActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickLogOut(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public static void redirectActivity(Activity mActivity, Class mClass) {
        Intent intent = new Intent(mActivity, mClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        closeDrawer(mDrawerLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.details1:
                startActivity(new Intent(MainActivity.this, DetailFirst.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.details2:
                startActivity(new Intent(MainActivity.this, DetailSecond.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.details3:
                startActivity(new Intent(MainActivity.this, DetailThird.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.details4:
                startActivity(new Intent(MainActivity.this, DetailFourth.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
        }
    }
}