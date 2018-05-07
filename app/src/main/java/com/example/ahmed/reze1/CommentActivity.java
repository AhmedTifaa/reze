package com.example.ahmed.reze1;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.GUI.CustomEditText;
import com.example.ahmed.reze1.api.news_feed.EventResponse;
import com.example.ahmed.reze1.api.news_feed.NewsFeedItem;
import com.example.ahmed.reze1.api.news_feed.VendorPostsResponse;
import com.example.ahmed.reze1.api.post.ApiCommentResponse;
import com.example.ahmed.reze1.api.post.ApiResponse;
import com.example.ahmed.reze1.api.post.CommentReplyResponse;
import com.example.ahmed.reze1.api.post.CommentResponse;
import com.example.ahmed.reze1.api.post.PostResponse;
import com.example.ahmed.reze1.api.product.ProductResponse;
import com.example.ahmed.reze1.app.AppConfig;
import com.example.ahmed.reze1.helper.VolleyCustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REPLAY_REQUEST_CODE = 1006;

    private static final String COMMENTS_EXTRA = "comment_activity.comments_extra";
    private static final String POST_ID_EXTRA = "comment_activity.post_id_extra";
    private static final String TIME_NOW_EXTRA = "comment_activity.time_now_extra";
    private static final String LIKES_EXTRA = "comment_activity.likes_extra";
    private static final String POST_OWNER_EXTRA = "comment_activity.post_owner_extra";

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
    int ownerId;
    ProgressBar commentProgressView;
    int addedComments = 0;


    public static Intent createIntent(int[] likeItems, int postId, long now, int postOwnerId, Context context){
        Intent intent = new Intent(context, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putIntArray(LIKES_EXTRA, likeItems);
        bundle.putInt(POST_ID_EXTRA, postId);
        bundle.putLong(TIME_NOW_EXTRA, now);
        bundle.putInt(POST_OWNER_EXTRA, postOwnerId);
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
        commentProgressView = findViewById(R.id.commentProgressView);
        commentProgressView.getIndeterminateDrawable().setColorFilter(getResources()
                .getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        commentProgressView.setVisibility(View.VISIBLE);


        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "0");

        ownerId = getIntent().getExtras().getInt(POST_OWNER_EXTRA, 0);

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

        fetchComments();
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
                    //intent.putExtra("comment", commentResponse);
                    intent.putExtra("added_size", addedComments);
                    intent.putExtra("post_id", postId);
                    setResult(RESULT_OK, intent);
                }
                onBackPressed();
                break;
            case R.id.sendCommentView:
                if (!commentEditText.getText().toString().contentEquals("")) {
                    CommentResponse response = new CommentResponse();
                    response.setCommentText(commentEditText.getText().toString());
                    response.setPending(true);
                    comments.add(response);
                    adapter.notifyItemInserted(comments.size()-1);
                    commentsRecyclerView.scrollToPosition(comments.size()-1);
                    performComment();
                }
                break;
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView commentTextView;
        TextView createdAtView;
        TextView commenterView;
        TextView commentReplayView;
        TextView commentLikeView;
        TextView postingView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            commentTextView = itemView.findViewById(R.id.commentTextView);
            createdAtView = itemView.findViewById(R.id.commentCreatedAtView);
            commenterView = itemView.findViewById(R.id.commenterView);
            commentReplayView = itemView.findViewById(R.id.commentReplayView);
            commentLikeView = itemView.findViewById(R.id.commentLikeView);
            postingView = itemView.findViewById(R.id.postingView);
        }

        public void bind(final CommentResponse comment, boolean pending){
            if (pending){
                postingView.setVisibility(View.VISIBLE);
            } else {
                postingView.setVisibility(View.GONE);

                commentTextView.setText(comment.getCommentText());
                createdAtView.setText(comment.getCreatedAt());
                commenterView.setText(comment.getCommenterName());
                String replay = getResources().getString(R.string.replay);

                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH).parse(comment.getCreatedAt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long milliseconds = date.getTime();
                long millisecondsFromNow = milliseconds - now;
                createdAtView.setText(DateUtils.getRelativeTimeSpanString(milliseconds, now, milliseconds - now));
                if (comment.getReplaySize() > 0) {
                    commentReplayView.setText(comment.getReplaySize() + " " + replay);
                }

                if (comment.getLikes() != null && comment.getLikes().length > 0) {
                    String like = getResources().getString(R.string.like);
                    commentLikeView.setText(comment.getLikes().length + " " + like);
                }

                commentReplayView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<CommentReplyResponse> replies = new ArrayList<>();
                        int[] likes = new int[0];
                        if (comment.getReplies() != null) {
                            replies = new ArrayList<>(Arrays.asList(comment.getReplies()));
                        }

                        if (comment.getLikes() != null) {
                            likes = comment.getLikes();
                        }


                        Intent intent = ReplayActivity.createIntent(
                                replies,
                                likes,
                                postId,
                                comment.getCommentId(),
                                now,
                                CommentActivity.this
                        );

                        startActivityForResult(intent, REPLAY_REQUEST_CODE);
                        //startActivity(intent);
                    }
                });
            }
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
            holder.bind(comments.get(position), comments.get(position).isPending());
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php",
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
                                    commentResponse.setCommenterName(jsonObject.getString("commenterName"));
                                    commentResponse.setPending(false);
                                    comments.set(comments.size()-1, commentResponse);
                                    //comments.add(commentResponse);
                                    adapter.notifyItemChanged(comments.size()-1);
                                    commentsRecyclerView.scrollToPosition(comments.size()-1);
                                    addedComments ++;
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
                    map.put("owner_id",  String.valueOf(ownerId));
                    map.put("userId", userId);

                    return map;
                }
            };

            Volley.newRequestQueue(CommentActivity.this).add(stringRequest);
        } else {
            Toast.makeText(this, "Empty comment!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REPLAY_REQUEST_CODE){
            if (data != null){
                CommentReplyResponse returnReplay = (CommentReplyResponse) data.getSerializableExtra("replay");
                int commentId = data.getIntExtra("comment_id", 0);

                for (int i = 0; i < comments.size(); i++) {
                    if (comments.get(i).getCommentId() == commentId) {
                        int c_size = 0;
                        if (comments.get(i).getReplies() != null)
                            c_size = comments.get(i).getReplies().length ;
                        CommentReplyResponse[] c_resArray = new CommentReplyResponse[c_size + 1];
                        if (comments.get(i).getReplies() != null) {
                            for (int j = 0; j < comments.get(i).getReplies().length; j++) {
                                c_resArray[j] = comments.get(i).getReplies()[j];
                            }
                        }

                        c_resArray[c_size] = returnReplay;
                        comments.get(i).setReplies(c_resArray);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

            }
        }
    }

    private void fetchComments(){
        VolleyCustomRequest stringRequest = new VolleyCustomRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php",
                ApiCommentResponse.class,
                new Response.Listener<ApiCommentResponse>() {
                    @Override
                    public void onResponse(ApiCommentResponse response) {
                        if (!response.isError()){
                            if (response.getComments() != null) {
                                comments = new ArrayList<>(Arrays.asList(response.getComments()));
                                for (CommentResponse commentResponse:comments) {
                                    commentResponse.setPending(false);
                                }
                                commentProgressView.setVisibility(View.GONE);
                                commentsRecyclerView.setAdapter(adapter);
                                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                            }
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
                map.put("method", "get_comments");
                map.put("post_id", String.valueOf(postId));
                map.put("cursor", "0");

                return map;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
