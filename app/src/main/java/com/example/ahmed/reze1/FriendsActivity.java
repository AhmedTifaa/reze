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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.api.user.UserResponse;
import com.example.ahmed.reze1.app.AppConfig;
import com.github.nkzawa.emitter.Emitter;

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
    RecyclerView friendsRecyclerView;
    UserRecyclerAdapter adapter;
    JSONArray data;


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
        SocketConnect.socket.emit("online");
        SocketConnect.socket.on("online", handleIncomingMessages);
        getUsers();

    }
    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            FriendsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    data = (JSONArray) args[0];
                    Toast.makeText(getBaseContext(),""+data.length(),Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private class FriendsViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView imageView;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.UserName);
            imageView = itemView.findViewById(R.id.onlineState);
        }


        public void bind(final UserResponse user) throws JSONException {
            username.setText(user.getName());
            for (int i = 0;i < data.length();i++){
                if (Integer.toString(user.getId()).equals(data.get(i))){

                    imageView.setVisibility(View.VISIBLE);
                }
                else{
                    imageView.setVisibility(View.GONE);
                }
            }
        }
    }

    private class UserRecyclerAdapter extends RecyclerView.Adapter<FriendsViewHolder>{

        @NonNull
        @Override
        public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(FriendsActivity.this).inflate(R.layout.friend_card,parent,false);
            return new FriendsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
            try {
                holder.bind(users.get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                        Log.i("response", "onResponse: " + response);
                        users = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                UserResponse userResponse = new UserResponse();
                                JSONObject object = jsonArray.getJSONObject(i);
                                userResponse.setName(object.getString("username"));
                                userResponse.setId(object.getInt("id"));
                                users.add(userResponse);
                                friendsRecyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
                                friendsRecyclerView.setAdapter(adapter);
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
