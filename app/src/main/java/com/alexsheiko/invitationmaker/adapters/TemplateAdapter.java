package com.alexsheiko.invitationmaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.alexsheiko.invitationmaker.R;
import com.alexsheiko.invitationmaker.TemplateActivity;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


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
            openImageEditor(resId);
        });
        return view;
    }

    private void openImageEditor(int resId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
            File file = convertBitmapToFile(bitmap);
            Uri imageUri = Uri.fromFile(file);

            ToolLoaderFactory.Tools[] tools = {
                    ToolLoaderFactory.Tools.CROP,
                    ToolLoaderFactory.Tools.MEME,
                    ToolLoaderFactory.Tools.TEXT,
                    ToolLoaderFactory.Tools.DRAW,
                    ToolLoaderFactory.Tools.FRAMES,
                    ToolLoaderFactory.Tools.ENHANCE,
            };
            Intent imageEditorIntent = new AdobeImageIntent.Builder(getContext())
                    .setData(imageUri)
                    .withToolList(tools)
                    .build();

            /* Start the Image Editor with request code 1 */
            ((TemplateActivity) getContext()).startActivityForResult(imageEditorIntent, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
