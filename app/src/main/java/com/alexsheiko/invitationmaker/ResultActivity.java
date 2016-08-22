package com.alexsheiko.invitationmaker;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
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
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.io.File;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Random;

import static com.alexsheiko.invitationmaker.R.menu.result;


public class ResultActivity extends BillingActivity
        implements MoPubInterstitial.InterstitialAdListener {

    private MoPubInterstitial interstitial;

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
    }

    @Override
    protected void onPause() {
        super.onStop();
        findViewById(R.id.finish_container).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (interstitial != null) {
            interstitial.destroy();
        }
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

    public void onClickDonate(View view) {
        if (mService == null) return;
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                    "donate1", "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                    1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0));
        } catch (RemoteException | IntentSender.SendIntentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onClickFinish(View view) {
        interstitial = new MoPubInterstitial(this, "c2c50ad94d474a20a919ca7d33638a0b");
        interstitial.setInterstitialAdListener(this);
        interstitial.load();
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

    // InterstitialAdListener methods
    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        // This sample automatically shows the ad as soon as it's loaded, but
        // you can move this show call to a time more appropriate for your app.
        if (interstitial.isReady()) {
            interstitial.show();
        }
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        Log.d("MoPub", "Interstitial load failed: " + errorCode);
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        navigateToMainScreen();
        Toast.makeText(this, "Thank you!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        navigateToMainScreen();
        Toast.makeText(this, "Thank you!", Toast.LENGTH_SHORT).show();
    }
}
