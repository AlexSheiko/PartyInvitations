package com.alexsheiko.invitationmaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


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
            view = inflater.inflate(R.layout.grid_item_template, parent, false);
        }
        int resId = getItem(position);
        Glide.with(getContext()).load(resId).fitCenter().centerCrop().into(((ImageView) view));
        view.setOnClickListener(view1 -> {
            try {
                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
                File file = convertBitmapToFile(bitmap);
                Uri imageUri = Uri.fromFile(file);
                ((GridActivity) getContext()).openImageEditor(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    private File convertBitmapToFile(Bitmap bitmap) throws IOException {
        // Create a file to write bitmap data
        File file = new File(getContext().getCacheDir(), "image.png");

        // Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bytes = bos.toByteArray();

        // Write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();
        return file;
    }

}
