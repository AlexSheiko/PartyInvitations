package com.alexsheiko.invitationmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressWarnings("unused")
    public void onClickCategory(View view) {
        Intent intent = new Intent(this, GridActivity.class);
        for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
            View child = ((ViewGroup) view).getChildAt(i);
            if (child instanceof TextView) {
                String category = ((TextView) child).getText().toString()
                        .replaceAll(" ", "").replace("\n", " ");
                intent.putExtra("category", category);

                Answers.getInstance().logContentView(new ContentViewEvent()
                        .putContentType("Category")
                        .putContentId(category));
            }
        }
        startActivity(intent);
    }
}
