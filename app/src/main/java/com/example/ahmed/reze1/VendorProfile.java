package com.example.ahmed.reze1;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.reze1.app.AppConfig;
import com.example.ahmed.reze1.helper.vendor.AmenitiesFragment;
import com.example.ahmed.reze1.helper.vendor.DetailsFragment;
import com.example.ahmed.reze1.helper.vendor.ProductFragment;
import com.example.ahmed.reze1.helper.vendor.ReviewsFragment;

/**
 * Created by Mona Abdallh on 4/28/2018.
 */

public class VendorProfile extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    android.support.v4.view.PagerAdapter adapter;
    RelativeLayout createHeader;
    TextView vendorNameView;
    Button createPostButton;
    Button createProductButton;

    String vendorId;

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

        vendorNameView.setText(getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_NAME_SHARED, null));

        vendorId = getActivity().getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AppConfig.LOGGED_IN_USER_ID_SHARED, null);

        createHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        tabLayout.addTab(tabLayout.newTab().setText(R.string.amenities));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.review));

        adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position)
                {
                    case 0:
                        ProductFragment schedule = new ProductFragment();
                        return schedule;
                    case 1:
                        DetailsFragment details = new DetailsFragment();
                        return details;
                    case 2:
                        AmenitiesFragment amenities = new AmenitiesFragment();
                        return amenities;
                    case 3:
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1005 && data != null){
            Toast.makeText(getActivity(), "product posted", Toast.LENGTH_LONG).show();
        }
    }
}
