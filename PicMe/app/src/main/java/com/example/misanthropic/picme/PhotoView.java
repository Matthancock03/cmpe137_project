package com.example.misanthropic.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class PhotoView extends AppCompatActivity {

    Integer [] Thumbs = {R.mipmap.sample_2, R.mipmap.sample_3,
            R.mipmap.sample_4, R.mipmap.sample_5,
            R.mipmap.sample_6, R.mipmap.sample_7,
            R.mipmap.sample_0, R.mipmap.sample_1,
            R.mipmap.sample_2, R.mipmap.sample_3,
            R.mipmap.sample_4, R.mipmap.sample_5,
            R.mipmap.sample_6, R.mipmap.sample_7,
            R.mipmap.sample_0, R.mipmap.sample_1,
            R.mipmap.sample_2, R.mipmap.sample_3,
            R.mipmap.sample_4, R.mipmap.sample_5,
            R.mipmap.sample_6, R.mipmap.sample_7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        getIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gridview);

        gridview.setAdapter(new ImageAdapter(this, Thumbs));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PhotoView.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                Intent gotoImageDetail = new Intent(getApplicationContext(), ImageDetail.class);
                gotoImageDetail.putExtra("id", position);
                startActivity(gotoImageDetail);
            }
        });
    }

}
