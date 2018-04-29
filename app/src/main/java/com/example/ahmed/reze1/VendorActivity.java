package com.example.ahmed.reze1;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.*;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.ahmed.reze1.helper.vendor.AmenitiesFragment;
import com.example.ahmed.reze1.helper.vendor.DetailsFragment;
import com.example.ahmed.reze1.helper.vendor.ReviewsFragment;
import com.example.ahmed.reze1.helper.vendor.ProductFragment;

public class VendorActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;
    RelativeLayout createProductHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.vendorTabLayout);
        viewPager = findViewById(R.id.vendorViewPager);
        /*createProductHeader = findViewById(R.id.createHeader);

        createProductHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateProductFragment fragment = CreateProductFragment.createFragment(0);
                fragment.show(getFragmentManager(), null);
            }
        });*/

        tabLayout.addTab(tabLayout.newTab().setText(R.string.product));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.details));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.amenities));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.review));

        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
    }
}
