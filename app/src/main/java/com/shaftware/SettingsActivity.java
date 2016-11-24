package com.shaftware;

import android.accounts.Account;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaftware.shaftquack.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private DatabaseReference acctRef;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        final String UID = curUser.getUid();
        acctRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(UID);

        ValueEventListener acctInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //DataSnapshot s = dataSnapshot.getChildren().iterator().next();

                if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                    DataSnapshot s = dataSnapshot.getChildren().iterator().next();
                    AccountPacket a = s.getValue(AccountPacket.class);
                    ((TextView) findViewById(R.id.text_username)).setText(a.getHandle());
                    ((TextView) findViewById(R.id.text_language)).setText(a.getLanguage());
                }
                else {
                    Log.w(TAG, "No such account found");
                    Log.d(TAG, "UID = " + UID);
                    AccountPacket newAcct = new AccountPacket(curUser.getDisplayName(), "English");
                    acctRef.push().setValue(newAcct);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "failed to get acct info");
            }
        };

        //acctRef.addListenerForSingleValueEvent(acctInfoListener);
        acctRef.addValueEventListener(acctInfoListener);

    }
}

