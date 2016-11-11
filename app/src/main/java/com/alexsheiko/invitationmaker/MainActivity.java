package com.alexsheiko.invitationmaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexsheiko.invitationmaker.base.BaseActivity;

public class MainActivity extends BaseActivity {

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
            }
        }
        startActivity(intent);
    }
}
