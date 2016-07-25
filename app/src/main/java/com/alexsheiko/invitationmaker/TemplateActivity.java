package com.alexsheiko.invitationmaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.alexsheiko.invitationmaker.adapters.TemplateAdapter;

public class TemplateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        getSupportActionBar().setElevation(0);

        String category = getIntent().getStringExtra("category");
        getSupportActionBar().setTitle(category);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        TemplateAdapter adapter = new TemplateAdapter(this);
        gridView.setAdapter(adapter);

        for (int i = 1; i < 100; i++) {
            String imageName = category.toLowerCase() + "_template_" + i;
            int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (resId != 0) {
                adapter.add(resId);
            }
        }
    }
}