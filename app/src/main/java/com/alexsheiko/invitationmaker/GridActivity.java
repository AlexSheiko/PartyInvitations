package com.alexsheiko.invitationmaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.alexsheiko.invitationmaker.ads.AdCloseListener;
import com.alexsheiko.invitationmaker.ads.AdProviderVideo;
import com.alexsheiko.invitationmaker.base.BaseActivity;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridActivity extends BaseActivity implements AdCloseListener {

    public static final int REQUEST_CREATE = 101;
    private static final int REQUEST_SHARE = 237;
    private AdProviderVideo mAdProvider;
    private int mShowingAdForId;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        String category = getIntent().getStringExtra("category");
        getSupportActionBar().setTitle(category);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridAdapter adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            int resId = adapter.getItem(position);
            String imageName = getResources().getResourceEntryName(resId);
            boolean isImagePaid = imageName.contains("paid");
            if (!isImagePaid) {
                openEditor(resId);
            } else {
                // Show video ad
                mAdProvider.onClickShow();
                mShowingAdForId = resId;
            }

            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentType("Template")
                    .putContentId(imageName));
        });

        List<Integer> templates = new ArrayList<>();
        for (int i = 1; i < 300; i++) {
            String imageName = category.toLowerCase() + "_template_" + i;
            imageName = imageName.replace(" ", "_");
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                templates.add(resId);
            } else {
                String imageNamePaid = category.toLowerCase() + "_template_" + i + "_paid";
                imageNamePaid = imageNamePaid.replace(" ", "_");
                int resIdPaid = getResources().getIdentifier(imageNamePaid, "drawable", getPackageName());
                if (resIdPaid != 0) {
                    templates.add(resIdPaid);
                }
            }
        }
        Collections.shuffle(templates);
        adapter.addAll(templates);

        mAdProvider = new AdProviderVideo();
        mAdProvider.prepare(this, this);
    }

    private void openEditor(int resId) {
        new AsyncTask<Integer, Void, Uri>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showSnackBar();
            }

            @Override
            protected Uri doInBackground(Integer... integers) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                try {
                    File file = convertBitmapToFile(bitmap);
                    return Uri.fromFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Uri imageUri) {
                super.onPostExecute(imageUri);
                openImageEditor(imageUri);
                dismissSnackbar();
            }
        }.execute(resId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREATE) {
            if (resultCode == RESULT_OK) {
                Uri editedImageUri = data.getData();
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("imageUri", editedImageUri.toString());
                startActivityForResult(intent, REQUEST_SHARE);
                overridePendingTransition(0, 0);
            }
        } else if (requestCode == REQUEST_SHARE) {
            Uri imageUri = Uri.parse(data.getStringExtra("imageUri"));
            openImageEditor(imageUri);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openImageEditor(Uri imageUri) {
        ToolLoaderFactory.Tools[] tools = {
                ToolLoaderFactory.Tools.TEXT,
                ToolLoaderFactory.Tools.DRAW,
                ToolLoaderFactory.Tools.CROP,
                ToolLoaderFactory.Tools.ENHANCE,
        };
        Intent imageEditorIntent = new AdobeImageIntent.Builder(this)
                .setData(imageUri)
                .withSharedElementTransition(true)
                .withToolList(tools)
                .build();

        /* Start the Image Editor */
        startActivityForResult(imageEditorIntent, GridActivity.REQUEST_CREATE);
    }

    private File convertBitmapToFile(Bitmap bitmap) throws IOException {
        //Todo: Show loading snackbar and parse image in background
        // Create a file to write bitmap data
        File file = new File(getCacheDir(), "image.png");

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

    @Override
    public void onAdClosed() {
        mAdProvider.loadVideo();
        openEditor(mShowingAdForId);
    }

    private void showSnackBar() {
        View parentLayout = findViewById(android.R.id.content);
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(parentLayout, "Loading image...", Snackbar.LENGTH_INDEFINITE);
        }
        mSnackbar.show();
    }

    private void dismissSnackbar() {
        if (mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }
}