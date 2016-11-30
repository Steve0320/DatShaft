package com.shaftware;

import android.accounts.Account;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText uname;
    EditText lang;
    Button setEdit;
    Button commitEdit;

    boolean editState;

    private static final String TAG = "SettingsActivity";
    private DatabaseReference acctRef;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        uname = (EditText) findViewById(R.id.text_username);
        uname.setInputType(InputType.TYPE_NULL);
        setEdit = (Button) findViewById(R.id.buttonSettings);
        commitEdit = (Button) findViewById(R.id.buttonSettingsEdit);

        lang = (EditText) findViewById(R.id.text_language);
        lang.setInputType(InputType.TYPE_NULL);

        final FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        final String UID = curUser.getUid();
        acctRef = FirebaseDatabase.getInstance().getReference().child("accounts").child(UID);

        ValueEventListener acctInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //DataSnapshot s = dataSnapshot.getChildren().iterator().next();

                if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                    //DataSnapshot s = dataSnapshot.getChildren().iterator().next();
                    //AccountPacket a = s.getValue(AccountPacket.class);
                    AccountPacket a = dataSnapshot.getValue(AccountPacket.class);
                    ((TextView) findViewById(R.id.text_username)).setText(a.getHandle());
                    ((TextView) findViewById(R.id.text_language)).setText(a.getLanguage());
                }
                else {
                    Log.w(TAG, "No such account found");
                    Log.d(TAG, "UID = " + UID);
                    AccountPacket newAcct = new AccountPacket(curUser.getDisplayName(), "English");
                    //acctRef.push().setValue(newAcct);
                    acctRef.setValue(newAcct);
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

    public void onModifySettingsClick(View v) {

        uname.setInputType(InputType.TYPE_CLASS_TEXT);
        lang.setInputType(InputType.TYPE_CLASS_TEXT);
        setEdit.setEnabled(false);
        commitEdit.setEnabled(true);
        setEdit.setVisibility(View.INVISIBLE);
        commitEdit.setVisibility(View.VISIBLE);

    }

    public void onCommitSettingsClick(View v) {

        //Send information
        AccountPacket newInfo = new AccountPacket(uname.getText().toString(), lang.getText().toString());
        acctRef.setValue(newInfo);

        uname.setInputType(InputType.TYPE_NULL);
        lang.setInputType(InputType.TYPE_NULL);
        commitEdit.setEnabled(false);
        setEdit.setEnabled(true);
        setEdit.setVisibility(View.VISIBLE);
        commitEdit.setVisibility(View.INVISIBLE);

    }
}

