package com.example.misanthropic.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class AlbumView extends FragmentActivity {

    private Button createAlbum;
    private Button deleteAlbum;

    final Integer [] myThumbs = {
            R.mipmap.sample_2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);

        getIntent();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        createAlbum = (Button) findViewById(R.id.AddAlbum);
        deleteAlbum = (Button) findViewById(R.id.DeleteAlbum);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCreateAlbum();
            }
        });

        gridview.setAdapter(new ImageAdapter(this, myThumbs));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(AlbumView.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                Intent gotoPhotos = new Intent(getApplicationContext(), PhotoView.class);
                startActivity(gotoPhotos);
            }
        });
    }

    public void gotoCreateAlbum(){
        Intent GotoCreateAlbum = new Intent(this, CreateAlbum.class);
        startActivity(GotoCreateAlbum);
    }

}
