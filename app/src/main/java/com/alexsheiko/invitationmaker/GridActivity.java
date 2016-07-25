package com.alexsheiko.invitationmaker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

public class GridActivity extends AppCompatActivity {

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
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                adapter.add(resId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri editedImageUri = data.getData();
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("imageUri", editedImageUri.toString());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
    }
}