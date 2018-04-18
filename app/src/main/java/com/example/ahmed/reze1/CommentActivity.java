package com.example.ahmed.reze1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.GUI.CustomEditText;
import com.example.ahmed.reze1.api.post.ApiResponse;
import com.example.ahmed.reze1.api.post.CommentResponse;
import com.example.ahmed.reze1.app.AppConfig;
import com.example.ahmed.reze1.helper.VolleyCustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String COMMENTS_EXTRA = "comment_activity.comments_extra";
    private static final String POST_ID_EXTRA = "comment_activity.post_id_extra";
    private static final String TIME_NOW_EXTRA = "comment_activity.time_now_extra";
    private static final String LIKES_EXTRA = "comment_activity.likes_extra";

    ImageView backView;
    ArrayList<CommentResponse> comments;
    int[] likes;
    RecyclerView commentsRecyclerView;
    RecyclerView.Adapter adapter;
    ImageView sendCommentView;
    TextView postLikesView;
    CustomEditText commentEditText;
    int postId;
    CommentResponse commentResponse;
    long now;
    String userId;

    public static Intent createIntent(ArrayList<CommentResponse> commentItems, int[] likeItems, int postId, long now, Context context){
        Intent intent = new Intent(context, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(COMMENTS_EXTRA, commentItems);
        bundle.putIntArray(LIKES_EXTRA, likeItems);
        bundle.putInt(POST_ID_EXTRA, postId);
        bundle.putLong(TIME_NOW_EXTRA, now);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        backView = findViewById(R.id.commentBackView);
        backView.setOnClickListener(this);

        postLikesView = findViewById(R.id.postLikesCommentView);

        userId = getSharedPreferences(AppConfig.SHARED_PREFERECE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "0");

        comments = (ArrayList<CommentResponse>) getIntent().getExtras().getSerializable(COMMENTS_EXTRA);

        likes = getIntent().getExtras().getIntArray(LIKES_EXTRA);

        if (likes != null && likes.length > 0){
            postLikesView.setVisibility(View.VISIBLE);
            postLikesView.setText(likes.length + "");
        } else {
            postLikesView.setVisibility(View.GONE);
        }


        postId = getIntent().getExtras().getInt(POST_ID_EXTRA);
        now = getIntent().getExtras().getLong(TIME_NOW_EXTRA);
        commentsRecyclerView = findViewById(R.id.commentRecView);
        adapter = new CommentRecyclerAdapter();

        commentsRecyclerView.setAdapter(adapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendCommentView = findViewById(R.id.sendCommentView);
        commentEditText = findViewById(R.id.commentEditText);

        //todo
        sendCommentView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentBackView:
                if (commentResponse != null){
                    Intent intent = new Intent();
                    intent.putExtra("comment", commentResponse);
                    intent.putExtra("post_id", postId);
                    setResult(RESULT_OK, intent);
                }
                onBackPressed();
                break;
            case R.id.sendCommentView:
                performComment();
                break;
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView commentTextView;
        TextView createdAtView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            commentTextView = itemView.findViewById(R.id.commentTextView);
            createdAtView = itemView.findViewById(R.id.commentCreatedAtView);
        }

        public void bind(CommentResponse comment){
            commentTextView.setText(comment.getCommentText());
            createdAtView.setText(comment.getCreatedAt());

            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH).parse(comment.getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long milliseconds = date.getTime();
            long millisecondsFromNow = milliseconds - now;
            createdAtView.setText(DateUtils.getRelativeTimeSpanString(milliseconds, now, milliseconds-now));
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

    private void performComment(){
        if (commentEditText.getText().toString().length() > 0){
            final String commentText = commentEditText.getText().toString();
            commentEditText.setText(null);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.18:80/reze/user_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("error")){
                                    Toast.makeText(CommentActivity.this, "Error submitting comment", Toast.LENGTH_SHORT).show();
                                } else {
                                    commentResponse = new CommentResponse();
                                    commentResponse.setCommenterId(jsonObject.getInt("commenterId"));
                                    commentResponse.setCommentId(jsonObject.getInt("commentId"));
                                    commentResponse.setCommentText(jsonObject.getString("commentText"));
                                    commentResponse.setReplies(null);
                                    commentResponse.setCreatedAt(jsonObject.getString("createdAt"));

                                    comments.add(commentResponse);
                                    adapter.notifyItemInserted(comments.size()-1);
                                    commentsRecyclerView.scrollToPosition(comments.size()-1);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley error", "onErrorResponse: " + error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();

                    map.put("method", "add_comment");
                    map.put("post_id", String.valueOf(postId));
                    map.put("comment", commentText);
                    map.put("userId", userId);

                    return map;
                }
            };

            Volley.newRequestQueue(CommentActivity.this).add(stringRequest);
        } else {
            Toast.makeText(this, "Empty post!", Toast.LENGTH_SHORT).show();
        }
    }
}
