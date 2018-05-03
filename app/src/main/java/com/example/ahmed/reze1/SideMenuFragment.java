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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_side_menu, container, false);

        myTeamView = view.findViewById(R.id.myTeamView);
        myTeamView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myTeamView:
                Intent intent = new Intent(getActivity(), ViewTeamsActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
