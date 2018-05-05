package com.example.ahmed.reze1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ahmed on 2/28/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    String type;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs, String type)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
        this.type = type;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                Home home = new Home();
                return home;
            case 1:
                Notification notification = new Notification();
                return  notification;
            case 2:
                Requests requests = new Requests();
                return  requests;
            case 3:
                if (type.equals("user")) {
                    Profile profile = new Profile();
                    return profile;
                } else if (type.equals("vendor")){
                    VendorProfile vendorProfile = new VendorProfile();
                    return vendorProfile;
                }
            case 4:
                SideMenuFragment sideMenu = new SideMenuFragment();
                return  sideMenu;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}