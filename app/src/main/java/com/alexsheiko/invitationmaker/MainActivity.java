package com.alexsheiko.invitationmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressWarnings("unused")
    public void onClickCategory(View view) {
        Intent intent = new Intent(this, TemplateActivity.class);
        for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
            View child = ((ViewGroup) view).getChildAt(i);
            if (child instanceof TextView) {
                intent.putExtra("category", ((TextView) child).getText().toString());
            }
        }
        startActivity(intent);
    }
}
