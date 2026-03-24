package com.example.homework9;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework9.adapter.ImageAdapter;
import com.example.homework9.data.ImageRepository;
import com.example.homework9.model.ImageItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<ImageItem> imageItems = ImageRepository.getImages();

        TextView subtitleTextView = findViewById(R.id.subtitleTextView);
        RecyclerView imagesRecyclerView = findViewById(R.id.imagesRecyclerView);

        subtitleTextView.setText(getString(R.string.images_count, imageItems.size()));

        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imagesRecyclerView.setHasFixedSize(true);
        imagesRecyclerView.setAdapter(new ImageAdapter(imageItems));
        imagesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
