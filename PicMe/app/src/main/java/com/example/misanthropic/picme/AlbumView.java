package com.example.misanthropic.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class AlbumView extends FragmentActivity {

    String userId;
    String name;
    private Button createAlbum;
    private Button deleteAlbum;

    final Integer [] myThumbs = {
            R.mipmap.sample_2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);
        Firebase.setAndroidContext(this);
        Log.d("AlbumView", "In AlbumView");
        unpackBundle(); // Pulls data passed from other activities out of Bundles

        GridView gridview = (GridView) findViewById(R.id.gridview);
        createAlbum = (Button) findViewById(R.id.AddAlbum);
        deleteAlbum = (Button) findViewById(R.id.DeleteAlbum);

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
                //Toast.makeText(AlbumView.this, "" + position,
                //        Toast.LENGTH_SHORT).show();
                Intent gotoPhotos = new Intent(getApplicationContext(), PhotoView.class);
                startActivity(gotoPhotos);
            }
        });
    }

    public void gotoCreateAlbum(){
        Intent GotoCreateAlbum = new Intent(this, CreateAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putString("USER_ID", userId);
        bundle.putString("USER_NAME", name);

        GotoCreateAlbum.putExtras(bundle);
        startActivity(GotoCreateAlbum);
    }

    public void unpackBundle(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(!extras.isEmpty() && extras.containsKey("USER_ID")){
            userId = extras.getString("USER_ID");
            Log.d("Id Album View", userId);
            if(extras.containsKey("NAME")){
                name = extras.getString("NAME");
            }
        }
    }

}
