package com.shaftware;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shaftware.shaftquack.R;

public class AddChannelActivity extends AppCompatActivity {
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        edit = (EditText)findViewById(R.id.ChannelName);
    }

    public void addChannel(View view) {
        DatabaseReference channelsRef = FirebaseDatabase.getInstance().getReference().child("channels");
        DatabaseReference tempRef = channelsRef.push();
        tempRef.setValue(new Channel(edit.getText().toString()));
        Intent intent = new Intent(AddChannelActivity.this, ConversationActivity.class);
        intent.putExtra("ROOM_ID", tempRef.getKey());
        intent.putExtra("ROOM_NAME", edit.getText().toString());
        startActivity(intent);
    }
}
