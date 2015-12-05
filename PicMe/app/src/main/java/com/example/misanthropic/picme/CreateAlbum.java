package com.example.misanthropic.picme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.IOException;

public class CreateAlbum extends FragmentActivity {
    Firebase ref;
    Firebase albums;
    Firebase imageRef;
    String userId;
    String name;
    EditText albumName;
    Button createButton;

    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_album);
        Firebase.setAndroidContext(this);

        albums = new Firebase("https://PicMe.firebaseio.com/albums");
        ref = new Firebase("https://PicMe.firebaseio.com/");
        createButton = (Button) findViewById(R.id.create_album);
        Log.d("Create Album", "In onCreate");

        unpackBundle();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlbum();
            }
        });
        albumName = (EditText)findViewById(R.id.album_name);
    }

    public void unpackBundle(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(!extras.isEmpty() && extras.containsKey("USER_ID")){
            TextView text = (TextView)findViewById(R.id.displayHeader);
            userId = extras.getString("USER_ID");
            Log.d("CreateAlbum: ID ", userId);
            if(extras.containsKey("NAME")){
                name = extras.getString("NAME");
                text.setText(text.getText() + " " + name);
            }
        }
    }

    public void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void saveImage(String image){
        //Log.d("Image Converted", image);
        String album = albumName.getText().toString();
        Log.d("Album Name", album);
        ref = albums.child(userId).child("albums").push();
        ref.child("album_name").setValue(album);
        imageRef = ref.child("images").push();
        imageRef.setValue(image);
    }

    public void createAlbum(){

        Log.d("Create Album:album name", albumName.toString());
        getImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Create Album", "In onActivityResult");
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = MainActivity.getResizedBitmap(bitmap, 500);
                String image64 = MainActivity.bitmapToBase64(bitmap);
                saveImage(image64);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
