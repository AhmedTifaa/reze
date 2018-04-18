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
import com.example.ahmed.reze1.api.post.CommentReplyResponse;
import com.example.ahmed.reze1.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReplayActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String REPLIES_EXTRA = "replay_activity.replies_extra";
    private static final String POST_ID_EXTRA = "replay_activity.post_id_extra";
    private static final String COMMENT_ID_EXTRA = "replay_activity.comment_id_extra";
    private static final String TIME_NOW_EXTRA = "replay_activity.time_now_extra";
    private static final String LIKES_EXTRA = "replay_activity.likes_extra";

    ArrayList<CommentReplyResponse> replies;
    ImageView backView;
    long now;
    ImageView sendReplayView;
    TextView commentLikesView;
    CustomEditText replayEditText;
    int postId;
    int commentId;
    String userId;
    CommentReplyResponse replayResponse;
    int[] likes;
    RecyclerView repliesRecyclerView;
    RecyclerView.Adapter adapter;

    public static Intent createIntent(ArrayList<CommentReplyResponse> replayItems, int[] likeItems, int postId, int comment_id, long now, Context context){
        Intent intent = new Intent(context, ReplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(REPLIES_EXTRA, replayItems);
        bundle.putInt(POST_ID_EXTRA, postId);
        bundle.putLong(TIME_NOW_EXTRA, now);
        bundle.putIntArray(LIKES_EXTRA, likeItems);
        bundle.putInt(COMMENT_ID_EXTRA, comment_id);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "0");

        backView = findViewById(R.id.replayBackView);
        backView.setOnClickListener(this);

        commentLikesView = findViewById(R.id.likesCommentView);


        replies = (ArrayList<CommentReplyResponse>) getIntent().getExtras().getSerializable(REPLIES_EXTRA);
        likes = getIntent().getExtras().getIntArray(LIKES_EXTRA);
        commentId = getIntent().getExtras().getInt(COMMENT_ID_EXTRA);

        if (likes != null && likes.length > 0){
            commentLikesView.setVisibility(View.VISIBLE);
            commentLikesView.setText(likes.length + "");
        } else {
            commentLikesView.setVisibility(View.GONE);
        }


        postId = getIntent().getExtras().getInt(POST_ID_EXTRA);
        now = getIntent().getExtras().getLong(TIME_NOW_EXTRA);
        repliesRecyclerView = findViewById(R.id.replayRecView);
        adapter = new ReplayRecyclerAdapter();
        repliesRecyclerView.setAdapter(adapter);
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendReplayView = findViewById(R.id.sendReplayView);
        replayEditText = findViewById(R.id.replayEditText);

        sendReplayView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.commentBackView:
                /*if (replayResponse != null){
                    Intent intent = new Intent();
                    intent.putExtra("comment", replayResponse);
                    intent.putExtra("post_id", postId);
                    setResult(RESULT_OK, intent);
                }*/
                onBackPressed();
                break;
            case R.id.sendReplayView:
                performReplay();
                break;
        }
    }

    private class ReplayViewHolder extends RecyclerView.ViewHolder{

        TextView replayTextView;
        TextView createdAtView;
        TextView replierView;

        public ReplayViewHolder(View itemView) {
            super(itemView);

            replayTextView = itemView.findViewById(R.id.commentTextView);
            createdAtView = itemView.findViewById(R.id.commentCreatedAtView);
            replierView = itemView.findViewById(R.id.commenterView);
        }

        public void bind(CommentReplyResponse replay){
            replayTextView.setText(replay.getReplayText());
            createdAtView.setText(replay.getCreatedAt());
            replierView.setText(replay.getUsername());

            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH).parse(replay.getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long milliseconds = date.getTime();
            long millisecondsFromNow = milliseconds - now;
            createdAtView.setText(DateUtils.getRelativeTimeSpanString(milliseconds, now, milliseconds-now));
        }
    }

    private class ReplayRecyclerAdapter extends RecyclerView.Adapter<ReplayViewHolder>{

        @NonNull
        @Override
        public ReplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ReplayActivity.this).inflate(R.layout.post_comment, parent, false);
            return new ReplayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReplayViewHolder holder, int position) {
            holder.bind(replies.get(position));
        }

        @Override
        public int getItemCount() {
            return replies.size();
        }
    }

    private void performReplay(){
        if (replayEditText.getText().toString().length() > 0){
            final String replayText = replayEditText.getText().toString();
            replayEditText.setText(null);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.18:80/reze/user_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("error")){
                                    Toast.makeText(ReplayActivity.this, "Error submitting replay", Toast.LENGTH_SHORT).show();
                                } else {
                                    replayResponse = new CommentReplyResponse();
                                    replayResponse.setReplayId(jsonObject.getInt("replayId"));
                                    replayResponse.setReplierId(jsonObject.getInt("replierId"));
                                    replayResponse.setReplayText(jsonObject.getString("replayText"));
                                    replayResponse.setCreatedAt(jsonObject.getString("createdAt"));
                                    replayResponse.setUsername(jsonObject.getString("username"));
                                    JSONArray jsonArray = jsonObject.getJSONArray("likes");
                                    if (jsonArray != null && jsonArray.length() > 0){
                                        int[] likes = new int[jsonArray.length()];
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            likes[i] = (int) jsonArray.get(0);
                                        }
                                    }
                                    replayResponse.setLikes(likes);

                                    replies.add(replayResponse);
                                    adapter.notifyItemInserted(replies.size()-1);
                                    repliesRecyclerView.scrollToPosition(replies.size()-1);
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

                    map.put("method", "add_replay");
                    map.put("post_id", String.valueOf(postId));
                    map.put("comment_id", String.valueOf(commentId));
                    map.put("replay", replayText);
                    map.put("userId", userId);

                    return map;
                }
            };

            Volley.newRequestQueue(ReplayActivity.this).add(stringRequest);
        } else {
            Toast.makeText(this, "Empty replay!", Toast.LENGTH_SHORT).show();
        }
    }
}
