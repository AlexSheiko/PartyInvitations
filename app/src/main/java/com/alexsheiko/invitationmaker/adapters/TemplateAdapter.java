package com.alexsheiko.invitationmaker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
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
        view.setOnClickListener(view1 -> {
            Uri imageUri = Uri.parse("android.resource://"
                    + getContext().getPackageName()
                    + "/drawable/"
                    + getContext().getResources().getResourceEntryName(resId)
            );

            Intent imageEditorIntent = new AdobeImageIntent.Builder(getContext())
                    .setData(imageUri)
                    .build();

            /* Start the Image Editor with request code 1 */
            ((Activity) getContext()).startActivityForResult(imageEditorIntent, 1);
        });
        return view;
    }
}
