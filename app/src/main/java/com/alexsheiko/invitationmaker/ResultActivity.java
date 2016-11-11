package com.alexsheiko.invitationmaker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexsheiko.invitationmaker.ads.AdProviderImage;
import com.alexsheiko.invitationmaker.base.BaseActivity;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.ShareEvent;

import java.io.File;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Random;

import static com.alexsheiko.invitationmaker.R.menu.result;


public class ResultActivity extends BaseActivity {

    private boolean mStartup = true;
    private AdProviderImage mAdProvider;

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

        mAdProvider = new AdProviderImage();
        mAdProvider.prepare(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mStartup) {
            findViewById(R.id.finish_container).setVisibility(View.VISIBLE);

            // TODO: Move ad to after pressing Finish
            mAdProvider.show();
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
            navigateToMainScreen();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void shareImage(Uri imageUri) {
        File file = new File(imageUri.toString());
        Uri uri = FileProvider.getUriForFile(this, "com.alexsheiko.invitationmaker.fileprovider", file);

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
