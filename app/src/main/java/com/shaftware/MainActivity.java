package com.shaftware;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;

import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.hdodenhof.circleimageview.CircleImageView; //Obviously non-shady import ^.^
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide; //Image handling library

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.shaftware.shaftquack.R;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.shaftware.shaftquack.R;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "MainActivity";
    private final String ANONYMOUS = "anonymous";
    private static final String MESSAGES_CHILD = "messages";

    private String mUsername;
    private String mPhotoURL;

    //Google and Firebase Resourses
    private SharedPreferences mSharedPreferences;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mMessageRecyclerView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference; //For pushing and pulling messages
    private FirebaseRecyclerAdapter<MessagePacket, MessageView> mFirebaseAdapter; //Bridge for sync
    private GoogleApiClient mGoogleApiClient;

    //Check if a user is currently logged in. If not,
    //transfer control to login screen.
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername = ANONYMOUS;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
        else {
            Log.d(TAG, "Got user" + mUsername);
            mUsername = mFirebaseUser.getDisplayName();
            mPhotoURL = mFirebaseUser.getPhotoUrl().toString();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //Setup database synchronization
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<MessagePacket, MessageView>(
                MessagePacket.class, R.layout.item_message, MessageView.class,
                mFirebaseDatabaseReference.child(MESSAGES_CHILD)
        ) {
            @Override
            protected void populateViewHolder(MessageView viewHolder, MessagePacket model, int position) {
                Log.d(TAG, "Populating view holder");
                viewHolder.messageTextView.setText(model.getText());
                viewHolder.nameTextView.setText(model.getName());

                if (model.getPhotoURL() == null) {
                    viewHolder.profileImageView.setImageDrawable
                            (ContextCompat.getDrawable(MainActivity.this,
                            R.drawable.ic_account_circle_black_36dp));
                }
                else {
                    Glide.with(MainActivity.this).load(model.getPhotoURL())
                            .into(viewHolder.profileImageView);
                }

                if (model.getTimestamp() == null) {
                    viewHolder.timestamp.setText("Timestamp not found");
                }
                else {
                    viewHolder.timestamp.setText(model.getTimestamp());
                }

            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager
                        .findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1
                        || (positionStart >= (messageCount - 1)
                        && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

    }

    //Action listener for logout button. Log the user out of the system
    //and return to the sign in page (user may perform no further actions
    //until signed in again.
    public void handleLogout(View v) {
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mUsername = ANONYMOUS;
        startActivity(new Intent(this, SignInActivity.class));
        return;
    }

    public void handleSend(View v) {
        EditText messageBox = (EditText) findViewById(R.id.messageBox);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        MessagePacket message = new MessagePacket(
                messageBox.getText().toString(),
                mUsername, mPhotoURL, date);

        mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(message);
        messageBox.setText("");

    }

    //Handle a failed connection to Google servers
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error", Toast.LENGTH_SHORT).show();
    }

}
