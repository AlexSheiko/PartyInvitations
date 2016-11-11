package com.alexsheiko.invitationmaker;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;
import com.alexsheiko.invitationmaker.ads.AdProviderVideo;
import com.alexsheiko.invitationmaker.base.BaseActivity;
import com.android.vending.billing.IInAppBillingService;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.PurchaseEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridActivity extends BaseActivity {

    public static final int REQUEST_CREATE = 101;
    private static final int REQUEST_SHARE = 237;
    private static final int REQUEST_PURCHASE = 333;
    IInAppBillingService mService;
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    private AdProviderVideo mAdProvider;
    private List<String> mOwnedPaidImages = new ArrayList<>();
    private int mShowingAdForId;

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
            if (!isImagePaid || mOwnedPaidImages.contains(imageName)) {
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

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        mAdProvider = new AdProviderVideo();
        mAdProvider.prepare(this);
    }

    private void openEditor(int resId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            File file = convertBitmapToFile(bitmap);
            Uri imageUri = Uri.fromFile(file);
            openImageEditor(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mShowingAdForId != 0) {
            openEditor(mShowingAdForId);
            mShowingAdForId = 0;
        }
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
        } else if (requestCode == REQUEST_PURCHASE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Thank you, enjoy using the template!", Toast.LENGTH_SHORT).show();
                // TODO: Save unlocked image
            }
            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putSuccess(resultCode == RESULT_OK));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
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
}