package com.example.ahmed.reze1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.api.post.CommentResponse;
import com.example.ahmed.reze1.api.user.ApiResponse;
import com.example.ahmed.reze1.api.user.UserResponse;
import com.example.ahmed.reze1.app.AppConfig;
import com.example.ahmed.reze1.helper.VolleyCustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amr on 4/19/2018.
 */

public class FriendsActivity extends AppCompatActivity {
    private static final String USER_ID = "USER_ID";
    private static final String User_NAME = "User_NAME";
    ArrayList<UserResponse> users;
    int user_id;
    String user_name;
    RecyclerView friendsRecyclerView;
    UserRecyclerAdapter adapter;


    public static Intent createIntent(int userId,String userName ,Context context){
        Intent intent = new Intent(context, CommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(USER_ID, userId);
        bundle.putString(User_NAME,userName);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //getIntent().getExtras().getInt(USER_ID);
        //getIntent().getExtras().getString(User_NAME);
        friendsRecyclerView = findViewById(R.id.friends_list);
        adapter=new UserRecyclerAdapter();
        getUsers();

    }
        private class CommentViewHolder extends RecyclerView.ViewHolder{

            TextView username;

            public CommentViewHolder(View itemView) {
                super(itemView);
                username=itemView.findViewById(R.id.UserName);

            }

            public void bind(final UserResponse user){
                username.setText(user.getName());
            }
        }

        private class UserRecyclerAdapter extends RecyclerView.Adapter<CommentViewHolder>{

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

             View view= LayoutInflater.from(FriendsActivity.this).inflate(R.layout.friend_card,parent,false);
                return new CommentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
                holder.bind(users.get(position));
            }

            @Override
            public int getItemCount() {
                return users.size();
            }
        }

    private void getUsers(){
        StringRequest stringRequest = new  StringRequest(Request.Method.POST, "https://rezetopia.com/app/friendlist.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("volley response", "onResponse: " + response);
                        users = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                UserResponse userResponse = new UserResponse();
                                JSONObject object = new JSONObject((Map) jsonArray.getJSONObject(i));
                                userResponse.setName(object.getString("username"));
                                userResponse.setId(object.getInt("id"));
                                users.add(userResponse);
                            }
                        }
                        catch (JSONException e) {
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

                String userId = getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "0");
                map.put("id", userId);

                return map;

            }
        };

        Volley.newRequestQueue(FriendsActivity.this).add(stringRequest);
    }
}
