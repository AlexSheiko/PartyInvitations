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
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexsheiko.invitationmaker.ads.AdProviderImage;
import com.alexsheiko.invitationmaker.base.BaseActivity;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;

import java.io.File;
import java.util.Random;


public class ResultActivity extends BaseActivity {

    private boolean mStartup = true;
    private AdProviderImage mAdProvider;
    private FloatingActionButton mSendFAB;
    private Uri mShareUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(imageUri);

        mAdProvider = new AdProviderImage();
        mAdProvider.prepare(this, mRewardListener);

        mSendFAB = (FloatingActionButton) findViewById(R.id.sendButton);
        mSendFAB.setOnClickListener(view -> shareImage(imageUri));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mStartup) {
            showFinishButton();
        }
        mStartup = false;
    }

    private void showFinishButton() {
        findViewById(R.id.finish_container).setVisibility(View.VISIBLE);
        mSendFAB.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        mSendFAB.setImageResource(R.drawable.ic_send);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSendFAB.setElevation(0.0f);
            CardView containerCardView = (CardView) findViewById(R.id.containerCardView);
            containerCardView.setElevation(0.0f);
        }
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
        recycleImage();
        showCongrats();
        navigateToMainScreen();
    }

    private void recycleImage() {
        new File(mShareUri.toString()).delete();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showCongrats() {
        String[] congratsArray = getResources().getStringArray(R.array.congrats);
        int index = new Random().nextInt(congratsArray.length);
        Toast.makeText(this, congratsArray[index], Toast.LENGTH_LONG).show();
    }

    private void shareImage(Uri imageUri) {
        File fileSD = new File(imageUri.toString());
        mShareUri = FileProvider.getUriForFile(this, "com.alexsheiko.invitationmaker.fileprovider", fileSD);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, mShareUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

        Answers.getInstance().logShare(new ShareEvent());
        // TODO: Remove file from cache
    }

    public void onClickImage(View view) {
        onBackPressed();
    }
}
