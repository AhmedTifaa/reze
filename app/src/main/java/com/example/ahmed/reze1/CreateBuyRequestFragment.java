package com.example.ahmed.reze1;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.reze1.GUI.CustomButton;
import com.example.ahmed.reze1.GUI.CustomEditText;
import com.example.ahmed.reze1.api.news_feed.EventResponse;
import com.example.ahmed.reze1.api.news_feed.GroupPostResponse;
import com.example.ahmed.reze1.api.news_feed.NewsFeedItem;
import com.example.ahmed.reze1.api.news_feed.VendorPostsResponse;
import com.example.ahmed.reze1.api.post.ApiResponse;
import com.example.ahmed.reze1.api.post.PostResponse;
import com.example.ahmed.reze1.api.product.ProductResponse;
import com.example.ahmed.reze1.helper.VolleyCustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mona Abdallh on 5/8/2018.
 */

public class CreateBuyRequestFragment extends DialogFragment {

    private static final String OWNER_ID_EXTRA = "owner_id";
    private static final String PRODUCT_ID_EXTRA = "product_id";
    private static final String STORE_ID_EXTRA = "store_id";

    int ownerId, productId, storeId;

    CustomEditText amountView;
    CustomEditText mobileNumberView;
    CustomButton createRequestButton;

    public static CreateBuyRequestFragment createFragment(int owner, int product, int store){
        CreateBuyRequestFragment fragment = new CreateBuyRequestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(OWNER_ID_EXTRA, owner);
        bundle.putInt(PRODUCT_ID_EXTRA, product);
        bundle.putInt(STORE_ID_EXTRA, store);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_buy_request, container, false);

        Bundle bundle = getArguments();
        ownerId = bundle.getInt(OWNER_ID_EXTRA);
        productId = bundle.getInt(PRODUCT_ID_EXTRA);
        storeId = bundle.getInt(STORE_ID_EXTRA);


        amountView = view.findViewById(R.id.amountView);
        mobileNumberView = view.findViewById(R.id.mobileNumberView);
        createRequestButton = view.findViewById(R.id.createRequestButton);

        createRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()){
                    performBuyRequest();
                }
            }
        });

        return view;
    }

    private boolean isValid(){
        if (!amountView.getText().toString().contentEquals("") && !mobileNumberView.getText().toString().contentEquals("")){
            performBuyRequest();
            return true;
        }

        return false;
    }

    private void performBuyRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/reze/user_post.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                Toast.makeText(getActivity(), R.string.create, Toast.LENGTH_LONG).show();
                                dismiss();
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
                map.put("method", "buy_request");
                map.put("user_id", "");
                map.put("owner_id", String.valueOf(ownerId));
                map.put("amount", String.valueOf(""));
                map.put("product_id", String.valueOf(productId));
                map.put("store_id", String.valueOf(storeId));
                map.put("user_phone_number", String.valueOf(""));

                return map;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
