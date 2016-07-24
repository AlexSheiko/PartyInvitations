package com.alexsheiko.invitationmaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.alexsheiko.invitationmaker.R;
import com.bumptech.glide.Glide;


public class TemplateAdapter extends ArrayAdapter<Integer> {

    public TemplateAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.list_item_template, parent, false);
        }
        int resId = getItem(position);
        Glide.with(getContext()).load(resId).fitCenter().centerCrop().into(((ImageView) view));
        return view;
    }
}
