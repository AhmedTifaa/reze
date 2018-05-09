package app.reze.ahmed.reze1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.*;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import app.reze.ahmed.reze1.api.vendor.VendorResponse;
import app.reze.ahmed.reze1.helper.VolleyCustomRequest;
import app.reze.ahmed.reze1.helper.vendor.DetailsFragment;
import app.reze.ahmed.reze1.helper.vendor.ReviewsFragment;
import app.reze.ahmed.reze1.helper.vendor.ProductFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class VendorActivity extends AppCompatActivity {

    private static final String VENDOR_ID_EXTRA = "vendor_id";
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;
    ImageView vendorPpView;
    ImageView coverPpView;
    TextView vendorNameView;
    TextView vendorAddressView;

    String vendorId;
    RequestQueue requestQueue;
    VendorResponse vendor;

    public static Intent createIntent(String id , Context context){
        Intent intent = new Intent(context, VendorActivity.class);
        intent.putExtra(VENDOR_ID_EXTRA, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        vendorId = getIntent().getStringExtra(VENDOR_ID_EXTRA);
        Log.i("vendor_id", "onCreate: " + vendorId);
        performGetVendor();

        tabLayout = findViewById(R.id.vendorTabLayout);
        viewPager = findViewById(R.id.vendorViewPager);
        vendorPpView = findViewById(R.id.vendorPpView);
        coverPpView = findViewById(R.id.coverPpView);
        vendorNameView = findViewById(R.id.vendorNameView);
        vendorAddressView = findViewById(R.id.vendorAddressView);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.product));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.details));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.review));

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position)
                {
                    case 0:
                        Log.i("create_vendor_id", "onCreate: " + vendorId);
                        ProductFragment product = ProductFragment.createFragment(vendorId);
                        return product;
                    case 1:
                        DetailsFragment details = new DetailsFragment();
                        return details;
                    case 2:
                        ReviewsFragment reviews = new ReviewsFragment();
                        return reviews;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }
        };

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setAdapter(adapter);
    }

    private void performGetVendor1(){
        VolleyCustomRequest stringRequest = new VolleyCustomRequest(Request.Method.POST, "https://rezetopia.com/app/reze/vendor_operation.php",
                VendorResponse.class,
                new Response.Listener<VendorResponse>() {
                    @Override
                    public void onResponse(VendorResponse response) {
                        if (response != null){
                            Log.i("onResponseVendor", "onResponse: " + response.getName());
                            vendor = response;
                            updateUi();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponseVendor", "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("method", "get_vendor");
                map.put("vendor_id", vendorId);

                return map;
            }
        };


        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void updateUi(){
        vendorAddressView.setText(vendor.getAddress());
        if (vendor.getImageUrl() != null) {
            Picasso.with(this).load(vendor.getImageUrl()).into(vendorPpView);
        }
    }

    private void performGetVendor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://rezetopia.com/app/reze/vendor_operation.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            Log.i("onResponseVendor", "onResponse: " + response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponseVendor", "onErrorResponse: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("method", "get_vendor");
                map.put("vendor_id", vendorId);

                return map;
            }
        };


        Volley.newRequestQueue(this).add(stringRequest);
    }

}
