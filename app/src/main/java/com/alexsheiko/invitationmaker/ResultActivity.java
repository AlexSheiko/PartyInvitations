package com.alexsheiko.invitationmaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.internal.cds.util.IabHelper;

import java.io.File;

public class ResultActivity extends AppCompatActivity {

    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(imageUri);

        mHelper = new IabHelper(this, getString(R.string.base64EncodedPublicKey));
        mHelper.startSetup(result -> {
            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                Log.d("TAG", "Problem setting up In-app Billing: " + result);
            } else {
                // Hooray, IAB is fully set up!
                Log.d("TAG", "Hooray, IAB is fully set up!");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
            shareImage(imageUri);
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    private void shareImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(imageUri.toString())));
            String path = MediaStore.Images.Media.insertImage(
                    getContentResolver(), bitmap, "title",
                    "description");
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
        } catch (Exception e) {
            Toast.makeText(this, "Failed to share image: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        findViewById(R.id.finish_button).setVisibility(View.VISIBLE);
    }

    public void onClickFinish(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
