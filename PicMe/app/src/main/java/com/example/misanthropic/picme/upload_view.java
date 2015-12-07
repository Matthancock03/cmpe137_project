package com.example.misanthropic.picme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

public class upload_view extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    Context con = this;
    String imgDecodableString;
    String index;
    Firebase ref;
    String albumKey;
    AlbumsHolder holder;
    public Bitmap yourSelectedImage;

    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_view);
        unpackBundle();
        holder = AlbumsHolder.getInstance();
        ref = new Firebase("https://PicMe.firebaseio.com/albums/" + holder.email + "/" + albumKey);

        upload = (Button) findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Implement upload functionality
                //Firebase upload = ref.child("Email").child("-K4kgQJeLE5z18clVq82").child("album");
                ref.child("images").push().setValue(bitmapToBase64(yourSelectedImage));
                holder.albumMap.get(albumKey).images.add(bitmapToBase64(yourSelectedImage));
                Intent GotoCreateAlbum = new Intent(con, AlbumView.class);
                Bundle bundle = new Bundle();
                bundle.putString("USER_EMAIL", holder.email);
                //bundle.putString("USER_NAME", "");

                GotoCreateAlbum.putExtras(bundle);
                startActivity(GotoCreateAlbum);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri uri = data.getData();
                yourSelectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imgView = (ImageView) findViewById(R.id.imgView);
                // Set the Image in ImageView after decoding the String
                yourSelectedImage = MainActivity.getResizedBitmap(yourSelectedImage, 500);
                imgView.setImageBitmap(yourSelectedImage);

            }
            else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    //convert to base64
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    //convert from base64 to bitmap
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public void unpackBundle(){
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(!extras.isEmpty() && extras.containsKey("ALBUM_KEY")){
            albumKey = extras.getString("ALBUM_KEY");
            index = extras.getString("INDEX");
        }else{
            Intent GotoCreateAlbum = new Intent(this, CreateAlbum.class);
            Bundle bundle = new Bundle();
            bundle.putString("USER_EMAIL", holder.email);
            //bundle.putString("USER_NAME", "");

            GotoCreateAlbum.putExtras(bundle);
            startActivity(GotoCreateAlbum);
        }
    }

}
