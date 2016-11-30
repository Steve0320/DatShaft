package com.shaftware;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaftware.shaftquack.R;

import java.util.ArrayList;
import java.util.Map;

public class ChannelsActivity extends AppCompatActivity {
    private final String TAG = "ChannelsActivity";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ArrayList<String> roomIds;
    private ArrayList<String> roomNames;
    private LinearLayout linearLayout;
    //final MediaPlayer mp = MediaPlayer.create(this, R.raw.quack);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        linearLayout = (LinearLayout) findViewById(R.id.channelLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("channels");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "" + dataSnapshot.getChildrenCount());
                Map<String, Object> rooms = (Map<String, Object>) dataSnapshot.getValue();

                roomIds = new ArrayList<String>();
                roomNames = new ArrayList<String>();
                for (Map.Entry<String, Object> room : rooms.entrySet()){
                    roomIds.add(room.getKey());
                    Map<String, String> temp = (Map<String, String>) room.getValue();
                    roomNames.add(temp.get("name"));
                }


                Button button;
                float d = getBaseContext().getResources().getDisplayMetrics().density;
                for(int i = 0; i < roomNames.size(); i++) {
                    button = (Button)getLayoutInflater().inflate(R.layout.shaft_button_template, null);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(300*d), (int)(50*d)); // width and height
                    lp.setMargins(0,(int)(14*d),0,(int)(14*d)); // space each item
                    button.setLayoutParams(lp);
                    button.setText(roomNames.get(i));
                    button.setTag(roomIds.get(i));
                    button.setOnClickListener(getOnClickListener(button));
                    linearLayout.addView(button);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        linearLayout.removeAllViews();
    }


    private View.OnClickListener getOnClickListener(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
               // mp.start();
                Intent intent = new Intent(ChannelsActivity.this, ConversationActivity.class);
                intent.putExtra("ROOM_ID", (String) button.getTag());
                intent.putExtra("ROOM_NAME", button.getText());
                startActivity(intent);
            }
        };
    }
}
