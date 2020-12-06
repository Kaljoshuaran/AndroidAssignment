package com.example.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;

    private String userID;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = mFirebaseUser.getUid();

        final TextView nameTextView = (TextView) findViewById(R.id.nameTV);
        final TextView emailTextView = (TextView) findViewById(R.id.emailTV);
        final TextView phoneTextView = (TextView) findViewById(R.id.phoneTV);
        final TextView greetingTextView = (TextView) findViewById(R.id.greetingTV);

        final TextView greetingDrawersTextView = (TextView) findViewById(R.id.greetingDrawers);

        mDatabaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    String profileName = userProfile.name;
                    String profileEmail = userProfile.email;
                    String profilePhone = userProfile.phone;

                    greetingDrawersTextView.setText("Welcome back, " + profileName + "!");

                    greetingTextView.setText("Greetings, " + profileName + "!");
                    nameTextView.setText(profileName);
                    emailTextView.setText(profileEmail);
                    phoneTextView.setText(profilePhone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();

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
        MainActivity.redirectActivity(this, DashboardActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void ClickProfile(View v) {
        recreate();
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
}