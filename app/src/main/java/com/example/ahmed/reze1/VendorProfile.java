package com.example.ahmed.reze1;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.*;
import android.support.v7.widget.ListPopupWindow;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.ahmed.reze1.api.vendor.VendorResponse;
import com.example.ahmed.reze1.app.AppConfig;
import com.example.ahmed.reze1.helper.ListPopupWindowAdapter;
import com.example.ahmed.reze1.helper.MenuCustomItem;
import com.example.ahmed.reze1.helper.VolleyCustomRequest;
import com.example.ahmed.reze1.helper.vendor.AmenitiesFragment;
import com.example.ahmed.reze1.helper.vendor.DetailsFragment;
import com.example.ahmed.reze1.helper.vendor.ProductFragment;
import com.example.ahmed.reze1.helper.vendor.ReviewsFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mona Abdallh on 4/28/2018.
 */

public class VendorProfile extends Fragment {

    private static final int IMAGE_REQUEST_CODE = 103;
    TabLayout tabLayout;
    ViewPager viewPager;
    android.support.v4.view.PagerAdapter adapter;
    RelativeLayout createHeader;
    TextView vendorNameView;
    Button createPostButton;
    Button createProductButton;
    ImageView vendorPpView;
    ImageView coverPpView;
    TextView vendorAddressView;


    String vendorId;
    RequestQueue requestQueue;
    VendorResponse vendor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_vendor, container, false);

        tabLayout = v.findViewById(R.id.vendorTabLayout);
        viewPager = v.findViewById(R.id.vendorViewPager);
        createHeader = v.findViewById(R.id.createVendorHeader);
        vendorNameView = v.findViewById(R.id.vendorNameView);
        createPostButton = v.findViewById(R.id.createPostButton);
        createProductButton = v.findViewById(R.id.createProductButton);
        vendorPpView = v.findViewById(R.id.vendorPpView);
        coverPpView = v.findViewById(R.id.coverPpView);
        vendorAddressView = v.findViewById(R.id.vendorAddressView);

        vendorNameView.setText(getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_NAME_SHARED, null));

        vendorId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        requestQueue = Volley.newRequestQueue(getActivity());
        getVendor();

        createHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        vendorPpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPPPopupWindow(vendorPpView);
            }
        });


        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        createProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateProductFragment fragment = CreateProductFragment.createFragment(vendorId);
                fragment.show(getActivity().getFragmentManager(), null);
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText(R.string.product));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.details));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.review));

        adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position)
                {
                    case 0:
                        ProductFragment product = new ProductFragment();
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

        return v;
    }

    private void getVendor(){
        VolleyCustomRequest stringRequest = new VolleyCustomRequest(Request.Method.POST, "https://rezetopia.com/app/reze/vendor_operation.php",
                VendorResponse.class,
                new Response.Listener<VendorResponse>() {
                    @Override
                    public void onResponse(VendorResponse response) {
                        if (response != null){
                            Log.i("vendor", "onResponse: " + response.getName());
                            vendor = response;
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

                map.put("method", "get_vendor");
                map.put("vendor_id", vendorId);

                return map;
            }
        };


        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1005 && data != null){
            Toast.makeText(getActivity(), "product posted", Toast.LENGTH_LONG).show();
        }

        if (requestCode == IMAGE_REQUEST_CODE && data!= null){
            vendor.setImageUrl(data.getStringExtra("url"));
            Log.i("image_url", "onActivityResult: " + vendor.getImageUrl());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUi(){
        vendorAddressView.setText(vendor.getAddress());
        if (vendor.getImageUrl() != null) {
            Picasso.with(getActivity()).load(vendor.getImageUrl()).into(vendorPpView);
        }
    }

    private void showPPPopupWindow(View anchor) {
        final ListPopupWindow popupWindow = new ListPopupWindow(getActivity());

        List<MenuCustomItem> itemList = new ArrayList<>();
        itemList.add(new MenuCustomItem(getActivity().getResources().getString(R.string.edit), R.drawable.ic_edit));
        itemList.add(new MenuCustomItem(getActivity().getResources().getString(R.string.upload), R.drawable.ic_upload));


        ListAdapter adapter = new ListPopupWindowAdapter(getActivity(), itemList);
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(adapter);
        popupWindow.setWidth(400);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        break;
                    case 1:
                        Intent intent = ImageActivity.createIntent(null, getActivity());
                        startActivityForResult(intent, IMAGE_REQUEST_CODE);
                        break;
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
    }
}
