package com.example.ahmed.reze1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.reze1.api.post.CommentResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String COMMENTS_EXTRA = "comment_activity.comments_extra";

    ImageView backView;
    ArrayList<CommentResponse> comments;
    RecyclerView commentsRecyclerView;
    RecyclerView.Adapter adapter;

    public static Intent createIntent(ArrayList<CommentResponse> items, Context context){
        Intent intent = new Intent(context, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(COMMENTS_EXTRA, items);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        backView = findViewById(R.id.commentBackView);
        backView.setOnClickListener(this);

        comments = (ArrayList<CommentResponse>) getIntent().getExtras().getSerializable(COMMENTS_EXTRA);
        commentsRecyclerView = findViewById(R.id.commentRecView);
        adapter = new CommentRecyclerAdapter();
        commentsRecyclerView.setAdapter(adapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentBackView:
                onBackPressed();
                break;
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView commentTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            commentTextView = itemView.findViewById(R.id.commentTextView);
        }

        public void bind(CommentResponse comment){
            commentTextView.setText(comment.getCommentText());
        }
    }

    private class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentViewHolder>{

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CommentActivity.this).inflate(R.layout.post_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
            holder.bind(comments.get(position));
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
    }
}
