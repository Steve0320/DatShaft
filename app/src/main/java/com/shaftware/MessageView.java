package com.shaftware;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.shaftware.shaftquack.R; //Very important, don't forget

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Steven Bertolucci on 10/31/2016.
 * This class defines the layout with which MessagePackets are
 * displayed. This class constitutes one half of the
 * FirebaseRecyclerAdapter adapter. The MessagePacket class
 * constitutes the other half. These classes should therefore
 * be called together as such:
 * FirebaseRecyclerAdapter<MessagePacket, MessageView>(...)
 */

public class MessageView extends RecyclerView.ViewHolder {

    public CircleImageView profileImageView;
    public TextView nameTextView;
    public TextView messageTextView;
    public TextView timestamp;

    public MessageView(View v) {

        super(v);   //Superclass constructor, registers v as itemView
        profileImageView = (CircleImageView) itemView.findViewById(R.id.profileView);
        nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
        timestamp = (TextView) itemView.findViewById(R.id.timestampTextView);

    }

}
