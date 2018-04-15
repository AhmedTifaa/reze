package com.example.ahmed.reze1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tifaa on 15/04/18.
 */


public class contactAddapter extends ArrayAdapter<buildFriend> {
    Context context;

    int resource;
    public contactAddapter(@NonNull Context context, int resource, @NonNull ArrayList<buildFriend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_friend,parent,false);
        TextView textView = (TextView)convertView.findViewById(R.id.sugName);
        buildFriend buildFriend = getItem(position);
        textView.setText(buildFriend.getName());

        return super.getView(position, convertView, parent);
    }
}
