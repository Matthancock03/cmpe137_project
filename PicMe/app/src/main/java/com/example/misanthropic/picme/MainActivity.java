package com.example.misanthropic.picme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

//public class MainActivity extends AppCompatActivity implements CreateAlbumFragment.OnFragmentInteractionListener{
public class MainActivity extends FragmentActivity{

    String email;
    String name;
    private Button viewalbum;
    private Button sharealbum;
    private Button friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unpackBundle(); // Pulls data passed from other activities out of Bundles
        Firebase.setAndroidContext(this);


        viewalbum = (Button) findViewById(R.id.view_album);

        viewalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartViewAlbumActivity();
            }
        });
        /*
        sharealbum = (Button) findViewById(R.id.share_album);

        sharealbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartViewAlbumActivity();
            }
        });

        friends = (Button) findViewById(R.id.friends);

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartViewAlbumActivity();
            }
        });
        */

    }

    public void StartCreateAlbumActivity(){
        Intent GotoCreateAlbum = new Intent(this, CreateAlbum.class);
        Bundle bundle = new Bundle();

        if(email != null && name != null) {
            bundle.putString("USER_EMAIL", email);
            bundle.putString("USER_NAME", name);
        }
        GotoCreateAlbum.putExtras(bundle);
        startActivity(GotoCreateAlbum);
    }

    public void StartViewAlbumActivity(){
        Intent startViewAlbum = new Intent(this, AlbumView.class);
        Bundle bundle = new Bundle();
        if(email != null && name != null) {
            bundle.putString("USER_EMAIL", email);
            bundle.putString("USER_NAME", name);
        }
        startViewAlbum.putExtras(bundle);
        startActivity(startViewAlbum);
    }

    // Use this to convert image before saving to Firebase
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    //Use this to convert retrieved image to bitmap for displaying
    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getEmail(){
        return email;
    }

    public void unpackBundle(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(!extras.isEmpty() && extras.containsKey("USER_EMAIL")){
            TextView text = (TextView)findViewById(R.id.displayHeader);
            email = extras.getString("USER_EMAIL");
            Log.d("Email in Main", email);
            if(extras.containsKey("NAME")){
                name = extras.getString("NAME");
                text.setText(text.getText() + " " + name);
            }
        }
    }
}
