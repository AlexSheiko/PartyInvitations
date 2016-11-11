package com.alexsheiko.invitationmaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class GridAdapter extends ArrayAdapter<Integer> {

    public GridAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.item_template, parent, false);
        }
        int resId = getItem(position);
        Glide.with(getContext())
                .load(resId).fitCenter().centerCrop()
                .listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        String templateName = getContext().getResources().getResourceEntryName(resId);
                        if (templateName.contains("paid")) {
                            view.findViewById(R.id.priceTag).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.priceTag).setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(((ImageView) view.findViewById(R.id.imageView)));

        return view;
    }

}
