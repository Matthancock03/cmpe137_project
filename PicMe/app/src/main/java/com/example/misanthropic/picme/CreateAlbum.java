package com.example.misanthropic.picme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.IOException;

public class CreateAlbum extends FragmentActivity {
    Album albumObject;
    Firebase ref;
    Firebase albums;
    Firebase imageRef;
    String email;
    String name;
    EditText albumName;
    Button createButton;
    Button upload;
    Bitmap yourSelectedImage;
    String image64;

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
        AlbumsHolder holder = AlbumsHolder.getInstance();
        email = holder.email;
        unpackBundle();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlbum();
            }
        });
        albumName = (EditText)findViewById(R.id.album_name);

        upload = (Button) findViewById(R.id.select_image);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }

    public void unpackBundle(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
    }

    public void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void setImage(){
        ImageView imgView = (ImageView) findViewById(R.id.album_create_image);
        // Set the Image in ImageView after decoding the String
        imgView.setImageBitmap(yourSelectedImage);
    }

    public void createAlbum(){
        //Log.d("Image Converted", image);
        if(image64 != null) {
            String album = albumName.getText().toString();
            albumObject = new Album();
            albumObject.album_name = album;
            albumObject.images.put(albumObject.images.size() + 1, image64);
            ref = albums.child(email).child("albums").push();
            ref.setValue(albumObject);


            Intent startViewAlbum = new Intent(this, AlbumView.class);
            Bundle bundle = new Bundle();
                bundle.putString("USER_EMAIL", email);
                bundle.putString("USER_NAME", name);

            startViewAlbum.putExtras(bundle);
            startActivity(startViewAlbum);
        }else{
            Toast.makeText(this, "Image not selected.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Create Album", "In onActivityResult");
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                yourSelectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                yourSelectedImage = MainActivity.getResizedBitmap(yourSelectedImage, 500);
                image64 = MainActivity.bitmapToBase64(yourSelectedImage);
                setImage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
