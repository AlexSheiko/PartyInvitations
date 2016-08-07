package com.alexsheiko.invitationmaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static com.alexsheiko.invitationmaker.R.menu.result;


public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(imageUri);
    }

    @Override
    protected void onStop() {
        super.onStop();
        findViewById(R.id.finish_button).setVisibility(View.VISIBLE);
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
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        Intent intent = new Intent();
        intent.putExtra("imageUri", imageUri.toString());
        setResult(Activity.RESULT_CANCELED, intent);
        super.onBackPressed();
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
