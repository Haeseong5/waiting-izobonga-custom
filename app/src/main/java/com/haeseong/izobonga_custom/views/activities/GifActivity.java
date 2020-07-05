package com.haeseong.izobonga_custom.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haeseong.izobonga_custom.R;

public class GifActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        imageView = findViewById(R.id.image_view);
        Glide.with(this).load(R.raw.background_gif).into(imageView);


    }
}
