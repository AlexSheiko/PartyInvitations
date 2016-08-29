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
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.File;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Random;

import static com.alexsheiko.invitationmaker.R.menu.result;


public class ResultActivity extends AppCompatActivity
        implements RewardedVideoAdListener {

    private static final String AD_UNIT_ID = "ca-app-pub-3038649646029056/3650335528";

    private boolean mStartup = true;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(imageUri);

        TextView congratsLabel = (TextView) findViewById(R.id.congratsLabel);
        String[] congratsArray = getResources().getStringArray(R.array.congrats);
        int index = new Random().nextInt(congratsArray.length);
        congratsLabel.setText(congratsArray[index]);

        // Initialize the Mobile Ads SDK.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mStartup) {
            findViewById(R.id.finish_container).setVisibility(View.VISIBLE);
        }
        mStartup = false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            Toast.makeText(this, "Thank you, enjoy using the app!", Toast.LENGTH_SHORT).show();

            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(1.00))
                    .putCurrency(Currency.getInstance("USD"))
                    .putItemName("Donation 1 USD")
                    .putItemType("Donation")
                    .putItemId("donate1")
                    .putSuccess(resultCode == Activity.RESULT_OK));
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickFinish(View view) {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            navigateToMainScreen();
        }
    }

    private void navigateToMainScreen() {
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

            Answers.getInstance().logShare(new ShareEvent());
        } catch (Exception e) {
            Toast.makeText(this, "Failed to share image: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void onClickImage(View view) {
        onBackPressed();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "Thank you for supporting the app!", Toast.LENGTH_SHORT).show();
        navigateToMainScreen();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "Thank you for supporting the app!", Toast.LENGTH_SHORT).show();
        navigateToMainScreen();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
    }
}
