package com.example.ahmed.reze1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Mona Abdallh on 5/3/2018.
 */

public class SideMenuFragment extends Fragment implements View.OnClickListener {

    TextView myTeamView;
    TextView sideEventsView;
    TextView sideSavedPostsView;
    TextView sideStoreView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_side_menu, container, false);

        myTeamView = view.findViewById(R.id.myTeamView);
        sideEventsView = view.findViewById(R.id.sideEventsView);
        sideSavedPostsView = view.findViewById(R.id.sideSavedPostsView);
        sideStoreView = view.findViewById(R.id.sideStoreView);

        myTeamView.setOnClickListener(this);
        sideEventsView.setOnClickListener(this);
        sideSavedPostsView.setOnClickListener(this);
        sideStoreView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.myTeamView:
                intent = new Intent(getActivity(), ViewTeamsActivity.class);
                startActivity(intent);
                break;

            case R.id.sideEventsView:
                intent = new Intent(getActivity(), MyEventsActivity.class);
                startActivity(intent);
                break;

            case R.id.sideSavedPostsView:
                intent = new Intent(getActivity(), SavedPostsActivity.class);
                startActivity(intent);
                break;

            case R.id.sideStoreView:
                intent = new Intent(getActivity(), StoresListActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
