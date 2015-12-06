package com.example.misanthropic.picme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AlbumView extends FragmentActivity {
    ArrayList<Album> albumArray = new ArrayList<>();
    ArrayList<Bitmap> albumCovers = new ArrayList<>();
    ArrayList<String> albumNames = new ArrayList<>();
    String email;
    String name;
    Firebase ref;
    Firebase albums;
    private Button createAlbum;
    private Button deleteAlbum;
    GridView gridview;

    final Integer [] myThumbs = {
            R.mipmap.sample_10
    };

    final String [] Title = {
        "Sample"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);

        Firebase.setAndroidContext(this);
        albums = new Firebase("https://PicMe.firebaseio.com/albums");
        ref = new Firebase("https://PicMe.firebaseio.com/");
        unpackBundle(); // Pulls data passed from other activities out of Bundles

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, albumCovers, albumNames));


        createAlbum = (Button) findViewById(R.id.AddAlbum);
        deleteAlbum = (Button) findViewById(R.id.DeleteAlbum);

        createAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCreateAlbum();
            }
        });



        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent gotoPhotos = new Intent(getApplicationContext(), PhotoView.class);
                startActivity(gotoPhotos);
            }
        });

    }

    public void gotoCreateAlbum(){
        Intent GotoCreateAlbum = new Intent(this, CreateAlbum.class);
        Bundle bundle = new Bundle();
        bundle.putString("USER_EMAIL", email);
        bundle.putString("USER_NAME", name);

        GotoCreateAlbum.putExtras(bundle);
        startActivity(GotoCreateAlbum);
    }

    public void unpackBundle(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!= null && !extras.isEmpty() && extras.containsKey("USER_EMAIL")){
            email = extras.getString("USER_EMAIL");
            populateAlbums();
            Log.d("Id Album View", email);
            if(extras.containsKey("NAME")){
                name = extras.getString("NAME");
            }
        }
    }

    public void populateAlbums(){
        Query query = albums.child(email).child("albums").orderByKey();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("AlbumView Firebase Key", snapshot.getKey());
                Album albm = new Album();
                DataSnapshot albumNme = snapshot.child("album_name");
                DataSnapshot imageArray = snapshot.child("images");

                Iterable<DataSnapshot> iterable = imageArray.getChildren();
                Iterator<DataSnapshot> it = iterable.iterator();
                int count = 0;

                while(it.hasNext()){

                    DataSnapshot img = (DataSnapshot)it.next();
                    //Log.d(img.getKey(), img.getValue().toString());
                    albm.images.put(Integer.parseInt(img.getKey()), img.getValue().toString());
                    if(count == 0){
                        albumCovers.add(MainActivity.base64ToBitmap(img.getValue().toString()));
                        albumNames.add(albumNme.getValue().toString());
                        Log.d("AlbumView albumName: ", albumNme.getValue().toString());
                    }
                    count++;
                }

            }

            public void onChildRemoved(DataSnapshot snapshot){
                Log.d("AlbumView Firebase Key", snapshot.getKey());
            }

            public void onChildChanged(DataSnapshot snapshot, String str){
                Log.d("AlbumView Firebase Key", snapshot.getKey());
            }
            public void onChildMoved(DataSnapshot snapshot, String str){
                Log.d("AlbumView Firebase Key", snapshot.getKey());
            }

            public void onCancelled(FirebaseError error){
                Log.d("Firebase Error", error.toString());
            }
        });
    }

}
