package com.alexsheiko.invitationmaker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.alexsheiko.invitationmaker.ads.AdProviderImage;
import com.alexsheiko.invitationmaker.base.BaseActivity;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;

import java.io.File;


public class ResultActivity extends BaseActivity {

    private boolean mStartup = true;
    private AdProviderImage mAdProvider;
    private FloatingActionButton mSendFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(imageUri);

        mAdProvider = new AdProviderImage();
        mAdProvider.prepare(this);

        mSendFAB = (FloatingActionButton) findViewById(R.id.sendButton);
        mSendFAB.setOnClickListener(view -> shareImage(imageUri));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mStartup) {
            findViewById(R.id.finish_container).setVisibility(View.VISIBLE);
            mSendFAB.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            mSendFAB.setImageResource(R.drawable.ic_send);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSendFAB.setElevation(0.0f);
            }
        }
        mStartup = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
            navigateToMainScreen();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void shareImage(Uri imageUri) {
        File fileSD = new File(imageUri.toString());
        Uri uri = FileProvider.getUriForFile(this, "com.alexsheiko.invitationmaker.fileprovider", fileSD);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

        Answers.getInstance().logShare(new ShareEvent());
    }

    public void onClickImage(View view) {
        onBackPressed();
    }
}
