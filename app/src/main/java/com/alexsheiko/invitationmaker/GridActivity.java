package com.alexsheiko.invitationmaker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory;

public class GridActivity extends AppCompatActivity {

    public static final int REQUEST_CREATE = 101;
    private static final int REQUEST_SHARE = 237;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        String category = getIntent().getStringExtra("category");
        getSupportActionBar().setTitle(category);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridAdapter adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);

        for (int i = 1; i < 100; i++) {
            String imageName = category.toLowerCase() + "_template_" + i;
            imageName = imageName.replace(" ", "_");
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                adapter.add(resId);
            }
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
        }
    }

    public void openImageEditor(Uri imageUri) {
        ToolLoaderFactory.Tools[] tools = {
                ToolLoaderFactory.Tools.BLUR,
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
}