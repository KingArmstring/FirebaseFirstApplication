package com.armstring.firebasefirstapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference database;

    TextView txtUserName;
    TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUserName = (TextView)findViewById(R.id.txtUserName);
        txtEmail = (TextView)findViewById(R.id.txtEmail);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users")
                .child(mAuth.getCurrentUser().getUid());

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txtUserName.setText(user.getUserName());
                txtEmail.setText(user.getEmailAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
