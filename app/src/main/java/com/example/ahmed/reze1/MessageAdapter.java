package com.example.ahmed.reze1;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;

    public MessageAdapter(List<Message> messages) {
        mMessages = messages;
      //  mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (Message.TYPE_MESSAGE) {
            case 0:
                layout = R.layout.layout_message;
                break;
            case 1:
                layout = R.layout.layout_amessage;
                break;
        }

            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setImage(message.getImage());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private EmojiTextView mMessageView;
        private TextView amMessageView;
        private TextView textView;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mMessageView = (EmojiTextView) itemView.findViewById(R.id.message);
            textView = (TextView) itemView.findViewById(R.id.textView);
            Toast.makeText(itemView.getContext(),Message.TYPE_MESSAGE+"",Toast.LENGTH_LONG).show();
            mMessageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (textView.getVisibility() == View.GONE){
                       textView.setVisibility(View.VISIBLE);
                   }else if (textView.getVisibility() == View.VISIBLE){
                        textView.setVisibility(View.GONE);
                    }
                }
            });

        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            if(null == message) return;
            textView.setText(formatter.format(date));
            mMessageView.setText(message);
           // amMessageView.setText(message);

        }

        public void setImage(Bitmap bmp){
            if(null == mImageView) return;
            if(null == bmp) return;
            mMessageView.setVisibility(View.GONE);
            mImageView.setImageBitmap(bmp);
        }
        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
