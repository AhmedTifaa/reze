package com.example.ahmed.reze1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.GUI.CustomButton;
import com.example.ahmed.reze1.api.news_feed.NewsFeedItem;
import com.example.ahmed.reze1.api.post.ApiResponse;
import com.example.ahmed.reze1.api.post.CommentResponse;
import com.example.ahmed.reze1.api.post.PostResponse;
import com.example.ahmed.reze1.api.product.ProductResponse;
import com.example.ahmed.reze1.app.AppConfig;
import com.example.ahmed.reze1.helper.ListPopupWindowAdapter;
import com.example.ahmed.reze1.helper.MenuCustomItem;
import com.example.ahmed.reze1.helper.VolleyCustomRequest;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCallback} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int COMMENT_ACTIVITY_RESULT = 1001;
    private static final int CREATE_POST_RESULT = 1002;
    private static final int VIEW_HEADER = 1;
    private static final int VIEW_POST = 2;
    private static final int VIEW_PRODUCT = 3;
    private WebView webview;
    String distfile = "";
    private PostResponse[] posts;
    private ProductResponse[] products;
    private ArrayList<NewsFeedItem> newsFeedItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private EditText commentEditText;
    int nextCursor = 0;
    int adapterPos;
    RequestQueue requestQueue;
    long now;
    String userId;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCallback mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.homePostsRecyclerView);
        commentEditText = view.findViewById(R.id.commentEditText);

        userId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, "0");


        requestQueue = Volley.newRequestQueue(getActivity());
        fetchNewsFeed();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCallback) {
            mListener = (OnCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnCallback {
        void onProfile();
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout createPostLayout;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            createPostLayout = itemView.findViewById(R.id.createPostLayout);

            createPostLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                    startActivityForResult(intent, CREATE_POST_RESULT);
                }
            });
        }
    }

    private class PostViewHolder extends RecyclerView.ViewHolder{

        TextView postTextView;
        Button likeButton;
        Button commentButton;
        TextView dateView;
        TextView usernameView;
        ImageView ppView;
        ImageView postSideMenu;
        ImageView hiddenMenuPositionView;
        public PostViewHolder(final View itemView) {
            super(itemView);

            postTextView = itemView.findViewById(R.id.postTextView);
            likeButton = itemView.findViewById(R.id.postLikeButton);
            commentButton = itemView.findViewById(R.id.postCommentButton);
            dateView = itemView.findViewById(R.id.postDateView);
            usernameView = itemView.findViewById(R.id.postUserName);
            ppView = itemView.findViewById(R.id.ppView);
            postSideMenu = itemView.findViewById(R.id.postSideMenu);
            hiddenMenuPositionView = itemView.findViewById(R.id.hiddenMenuPositionView);
        }

        public void bind(final NewsFeedItem item, final int pos) {
            String postText = null;
            if (item.getOwnerName() != null){
                usernameView.setText(item.getOwnerName());
            }
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH).parse(item.getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long milliseconds = date.getTime();
            long millisecondsFromNow = milliseconds - now;
            dateView.setText(DateUtils.getRelativeDateTimeString(getActivity(), milliseconds, millisecondsFromNow, DateUtils.DAY_IN_MILLIS, 0));

            try {
                postText = URLEncoder.encode(item.getPostText(), "ISO-8859-1");
                postText = URLDecoder.decode(postText, "UTF-8");
                postTextView.setText(postText);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (item.getLikes() != null && item.getLikes().length > 0){
                //likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_holo_green,  0, 0, 0);
                likeButton.setText(item.getLikes().length + " Like");


                Log.e("loggedInUserId", userId);
                for (int id : item.getLikes()) {
                    Log.e("likesUserId", String.valueOf(id));
                    if (String.valueOf(id).contentEquals(String.valueOf(userId))){
                        likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_holo_green,  0, 0, 0);
                        break;
                    }
                }
            }

            if (item.getPostComments() != null && item.getPostComments().length > 0){
                //commentButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_holo_green,  0, 0, 0);
                commentButton.setText(item.getPostComments().length + " Comment");
            }

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<CommentResponse> comments = new ArrayList<>(Arrays.asList(item.getPostComments()));
                    Intent intent = CommentActivity.createIntent(comments, item.getLikes(), item.getId(), now, item.getOwnerId(),
                            getActivity());

                    adapterPos = pos;
                    startActivityForResult(intent, COMMENT_ACTIVITY_RESULT);

                    //startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                }
            });

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < item.getLikes().length; i++) {
                        if (item.getLikes()[i] == Integer.parseInt(userId)){
                            reverseLike(item, pos);
                            return;
                        }
                    }

                    performLike(item, pos);
                }
            });

            usernameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getOwnerId() == Integer.parseInt(userId)){
                        mListener.onProfile();
                    } else if (item.getType() == NewsFeedItem.POST_TYPE){
                        startOtherProfile(pos);
                    }
                }
            });

            ppView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getOwnerId() == Integer.parseInt(userId)){
                        mListener.onProfile();
                    } else if (item.getType() == NewsFeedItem.POST_TYPE){
                        startOtherProfile(pos);
                    }
                }
            });

            postSideMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (String.valueOf(item.getOwnerId()).contentEquals(String.valueOf(userId))) {
                        showPostPopupWindow(postSideMenu, true, item.getId(), item.getOwnerId());
                    } else {
                        showPostPopupWindow(postSideMenu, false, item.getId(), item.getOwnerId());
                    }
                }
            });
        }

        private void startOtherProfile(int position){
            Intent intent = OtherProfileActivity.createIntent(String.valueOf(newsFeedItems.get(position).getOwnerId()), newsFeedItems.get(position).getOwnerName(), getActivity());
            startActivity(intent);
        }

        private void performLike(final NewsFeedItem item, final int pos){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")){
                                    likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star_holo_green,  0, 0, 0);
                                    likeButton.setText((item.getLikes().length + 1) + " Like");
                                    int[] likes = new int[item.getLikes().length + 1];
                                        for (int i = 0; i < posts[pos].getLikes().length; i++) {
                                        likes[i] = posts[pos].getLikes()[i];
                                    }

                                    likes[posts[pos].getLikes().length] = Integer.parseInt(userId);
                                    posts[pos].setLikes(likes);
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

                    map.put("method", "post_like");
                    map.put("userId", userId);
                    map.put("owner_id", String.valueOf(item.getOwnerId()));
                    map.put("post_id", String.valueOf(item.getId()));
                    map.put("add_like", String.valueOf(true));

                    return map;
                }
            };

            requestQueue.add(stringRequest);
        }

        private void reverseLike(final NewsFeedItem item, final int pos){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.18:80/reze/user_post.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("volley response", "onResponse: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")){

                                    likeButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_star,  0, 0, 0);

                                    if (item.getLikes().length > 1){
                                        likeButton.setText((item.getLikes().length - 1) + " Like");
                                    } else {
                                        likeButton.setText("Like");
                                    }



                                    ArrayList<Integer> likesList = new ArrayList<>();

                                    for (int id : item.getLikes()) {
                                        if (id != Integer.parseInt(userId)){
                                            likesList.add(id);
                                        }
                                    }

                                    int[] likes = new int[likesList.size()];

                                    for(int i = 0; i < likesList.size(); i++) {
                                        likes[i] = likesList.get(i);
                                    }

                                    posts[pos].setLikes(likes);
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

                    map.put("method", "post_like");
                    map.put("userId", userId);
                    map.put("owner_id", String.valueOf(item.getOwnerId()));
                    map.put("post_id", String.valueOf(item.getId()));
                    map.put("remove_like", String.valueOf(true));

                    return map;
                }
            };

            requestQueue.add(stringRequest);
        }
    }

    private class VendorProductHolder extends RecyclerView.ViewHolder{

        TextView postUserName;
        TextView postDateView;
        ImageView postSideMenu;
        TextView productTitleView;
        TextView productDetailView;
        TextView priceView;
        TextView avilView;
        CustomButton productBuyNow;
        ImageView ppView;

        public VendorProductHolder(View itemView) {
            super(itemView);

            postUserName = itemView.findViewById(R.id.postUserName);
            postDateView = itemView.findViewById(R.id.postDateView);
            postSideMenu = itemView.findViewById(R.id.postSideMenu);
            productTitleView = itemView.findViewById(R.id.productTitleView);
            productDetailView = itemView.findViewById(R.id.productDetailView);
            priceView = itemView.findViewById(R.id.priceView);
            avilView = itemView.findViewById(R.id.avilView);
            productBuyNow = itemView.findViewById(R.id.productBuyNow);
            ppView = itemView.findViewById(R.id.ppView);
        }

        public void bind(final NewsFeedItem item){
            productTitleView.setText(item.getProductTitle());
            productDetailView.setText(item.getProductDescription());
            priceView.setText(String.valueOf(item.getProductPrice()));
            postUserName.setText(item.getOwnerName());
            if (item.getProductSoldAmount() < item.getProductAmount()){
                avilView.setText(R.string.available);
                productBuyNow.setText(R.string.buy);
            } else {
                avilView.setText(R.string.unavailable);
                productBuyNow.setText(R.string.sold_out);
                productBuyNow.setEnabled(false);
            }

            ppView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getType() == NewsFeedItem.PRODUCT_TYPE){
                        if (item.getOwnerId() > 0) {
                            Intent intent = VendorActivity.createIntent(String.valueOf(item.getOwnerId()), getActivity());
                            startActivity(intent);
                        }
                    }
                }
            });

            postUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getType() == NewsFeedItem.PRODUCT_TYPE){
                        if (item.getOwnerId() > 0) {
                            Intent intent = VendorActivity.createIntent(String.valueOf(item.getOwnerId()), getActivity());
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    private class PostRecyclerAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_HEADER){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_post_header, parent, false);
                return new HeaderViewHolder(view);
            } else if (viewType == VIEW_POST){
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.post_card, parent, false);
                return new PostViewHolder(view);
            } else {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_product_card, parent, false);
                return new VendorProductHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof PostViewHolder) {
                PostViewHolder pHolder = (PostViewHolder) holder;
                pHolder.bind(newsFeedItems.get(position-1), position-1);
            } else if (holder instanceof VendorProductHolder){
                VendorProductHolder productHolder = (VendorProductHolder) holder;
                productHolder.bind(newsFeedItems.get(position-1));
            }
        }

        @Override
        public int getItemCount() {
            return newsFeedItems.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)){
                return VIEW_HEADER;
            } else if (newsFeedItems.get(position-1).getType() == NewsFeedItem.PRODUCT_TYPE){
                return VIEW_PRODUCT;
            }

            return VIEW_POST;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }

    private void fetchNewsFeed(){
        VolleyCustomRequest stringRequest = new VolleyCustomRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php", ApiResponse.class,
                new Response.Listener<ApiResponse>() {
                    @Override
                    public void onResponse(ApiResponse response) {
                        if (response.getPosts() != null){
                            Log.i("volley response", "onResponse: " + response.getPosts()[0].getCreatedAt());
                            posts = response.getPosts();
                            products = response.getProducts();
                            newsFeedItems = new ArrayList<>();
                            if (posts != null) {
                                for (PostResponse postResponse : posts) {
                                    NewsFeedItem item = new NewsFeedItem();
                                    item.setId(postResponse.getPostId());
                                    item.setCreatedAt(postResponse.getCreatedAt());
                                    item.setLikes(postResponse.getLikes());
                                    item.setOwnerId(Integer.parseInt(postResponse.getUserId()));
                                    item.setOwnerName(postResponse.getUsername());
                                    item.setPostAttachment(postResponse.getAttachment());
                                    item.setPostComments(postResponse.getComments());
                                    item.setPostText(postResponse.getText());
                                    item.setType(NewsFeedItem.POST_TYPE);
                                    newsFeedItems.add(item);
                                }
                            }

                            if (products != null){
                                for (ProductResponse productResponse : products) {
                                    NewsFeedItem item = new NewsFeedItem();
                                    item.setProductAmount(productResponse.getAmount());
                                    item.setProductDescription(productResponse.getDescription());
                                    item.setProductImageUrl(productResponse.getImageUrl());
                                    item.setProductPrice(productResponse.getPrice());
                                    item.setProductSale(productResponse.getSale());
                                    item.setProductSoldAmount(productResponse.getSoldAmount());
                                    item.setProductTitle(productResponse.getTitle());
                                    item.setId(productResponse.getId());
                                    item.setOwnerId(productResponse.getVendorId());
                                    item.setOwnerName(productResponse.getName());
                                    item.setType(NewsFeedItem.PRODUCT_TYPE);
                                    newsFeedItems.add(item);
                                }
                            }

                            nextCursor = response.getNextCursor();
                            now = response.getNow();
                            Collections.shuffle(newsFeedItems);
                            updateUi();
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
                map.put("method", "get_news_feed");
                map.put("userId", userId);
                map.put("cursor", "0");

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void updateUi(){
        if (adapter == null){
            adapter = new PostRecyclerAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_ACTIVITY_RESULT){
            if (data != null){
                CommentResponse commentResponse = (CommentResponse) data.getSerializableExtra("comment");
                int postId = data.getIntExtra("post_id", 0);
                for (int i = 0; i < posts.length; i++) {
                    if (posts[i].getPostId() == postId){
                        int c_size = posts[i].getComments().length;
                        CommentResponse[] c_resArray = new CommentResponse[c_size+1];
                        for (int j = 0; j <  posts[i].getComments().length; j++) {
                            c_resArray[j] = posts[i].getComments()[j];
                        }

                        c_resArray[c_size] = commentResponse;
                        posts[i].setComments(c_resArray);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
            Toast.makeText(getActivity(), "result", Toast.LENGTH_SHORT).show();
        } else if (requestCode == CREATE_POST_RESULT){
            if (data != null){
                PostResponse postResponse = (PostResponse) data.getSerializableExtra("post");
                PostResponse[] p_array = new PostResponse[posts.length + 1];

                p_array[0] = postResponse;

                for (int i = 0; i < posts.length; i++) {
                    p_array[i+1] = posts[i];
                }

                posts = p_array;
                adapter.notifyItemInserted(0);
            }
        }
    }

    private void removePost(final int postId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("remove_post", response);
                        ArrayList<PostResponse> newPosts = new ArrayList<>(Arrays.asList(posts));
                        for (int i = 0; i < newPosts.size(); i++) {
                            if (newPosts.get(i).getPostId() == postId){
                                newPosts.remove(i);
                                posts = newPosts.toArray(new PostResponse[newPosts.size()]);
                                adapter.notifyDataSetChanged();
                                break;
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

                map.put("method", "remove_post");
                map.put("user_id", userId);
                map.put("post_id", String.valueOf(postId));

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void savePost(final int postId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("save_post", response);
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

                map.put("method", "save_post");
                map.put("user_id", userId);
                map.put("post_id", String.valueOf(postId));

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void reportPost(final int postId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("report_post", response);
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

                map.put("method", "report_post");
                map.put("user_id", userId);
                map.put("post_id", String.valueOf(postId));

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void showPostPopupWindow(View anchor, final boolean owner, final int postId, final int postOwnerId) {
        final ListPopupWindow popupWindow = new ListPopupWindow(getActivity());

        List<MenuCustomItem> itemList = new ArrayList<>();
        if (owner){
            itemList.add(new MenuCustomItem(getActivity().getResources().getString(R.string.edit), R.drawable.ic_edit));
            itemList.add(new MenuCustomItem(getActivity().getResources().getString(R.string.save_post), R.drawable.ic_save));
            itemList.add(new MenuCustomItem(getActivity().getResources().getString(R.string.remove), R.drawable.ic_remove));
        } else {
            itemList.add(new MenuCustomItem(getActivity().getResources().getString(R.string.save_post), R.drawable.ic_save));
            itemList.add(new MenuCustomItem(getActivity().getResources().getString(R.string.report_post), R.drawable.ic_report));
        }


        ListAdapter adapter = new ListPopupWindowAdapter(getActivity(), itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(400);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (String.valueOf(postOwnerId).contentEquals(String.valueOf(userId))){
                    if (i == 0){
                        //todo edit post
                    } else if (i == 1){
                        savePost(postId);
                    } else if (i == 2){
                        removePost(postId);
                    }
                } else {
                    if (i == 0){
                        savePost(postId);
                    } else if (i == 1){
                        reportPost(postId);
                    }
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
    }
}
