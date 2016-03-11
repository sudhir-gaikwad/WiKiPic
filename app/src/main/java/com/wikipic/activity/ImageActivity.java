package com.wikipic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wikipic.R;
import com.wikipic.util.Constants;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ImageView imageView = (ImageView) findViewById(R.id.full_image);
        String imageUrl = getIntent().getStringExtra(Constants.INTENT_EXTRA_IMAGE_URL);
        String imageTitle = getIntent().getStringExtra(Constants.INTENT_EXTRA_IMAGE_TITLE);

        getSupportActionBar().setTitle(imageTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.image_placeholder)
                .animate(R.anim.zoom_anim)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
