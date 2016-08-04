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
import com.adobe.creativesdk.aviary.internal.cds.util.IabResult;
import com.adobe.creativesdk.aviary.internal.cds.util.Purchase;

import java.io.File;

import static com.alexsheiko.invitationmaker.R.menu.result;

public class ResultActivity extends AppCompatActivity
        implements IabHelper.OnIabPurchaseFinishedListener {

    private static final int REQUEST_BUY_STICKERS = 263;
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
    protected void onStop() {
        super.onStop();
        findViewById(R.id.finish_button).setVisibility(View.VISIBLE);
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
        inflater.inflate(result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send) {
            Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
            shareImage(imageUri);
            return true;
        } else if (id == R.id.action_stickers) {
            mHelper.launchPurchaseFlow(this, "stickers", REQUEST_BUY_STICKERS, this);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        if (result.isFailure()) {
            Toast.makeText(this, "Failed to purchase stickers: " + result.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (purchase.getSku().equals("stickers")) {
            // give user access to stickers content and update the UI
            Toast.makeText(this, "Stickers successfully unlocked!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickFinish(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
}
