package com.shaftware;

import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide; //Image handling library

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.shaftware.shaftquack.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.shaftware.shaftquack.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConversationActivity extends AppCompatActivity {

    private final String TAG = "ConversationActivity";
    private String MESSAGES_CHILD = "messages/";
    private String roomName;

    // Google and Firebase Resources
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mMessageRecyclerView;
    private DatabaseReference mFirebaseDatabaseReference; //For pushing and pulling messages
    private FirebaseRecyclerAdapter<MessagePacket, MessageView> mFirebaseAdapter; //Bridge for sync

    // User info
    private String mUsername;
    private String mPhotoURL;

    // Displays the conversation from the firebase server
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        MESSAGES_CHILD = MESSAGES_CHILD + getIntent().getStringExtra("ROOM_ID");
        roomName = getIntent().getStringExtra("ROOM_NAME");

        //Setup database synchronization
        setTitle(roomName);
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
                            (ContextCompat.getDrawable(ConversationActivity.this,
                                    R.drawable.ic_account_circle_black_36dp));
                }
                else {
                    Glide.with(ConversationActivity.this).load(model.getPhotoURL())
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

    public void handleSend(View v) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsername = mFirebaseUser.getDisplayName();
        mPhotoURL = mFirebaseUser.getPhotoUrl().toString();
        EditText messageBox = (EditText) findViewById(R.id.messageBox);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        MessagePacket message = new MessagePacket(
                messageBox.getText().toString(),
                mUsername, mPhotoURL, date);

        mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(message);
        messageBox.setText("");
    }
}
