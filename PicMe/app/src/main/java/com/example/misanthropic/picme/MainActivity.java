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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//public class MainActivity extends AppCompatActivity implements CreateAlbumFragment.OnFragmentInteractionListener{
public class MainActivity extends FragmentActivity{

    Firebase ref;
    Firebase albums;
    AlbumsHolder holder  = AlbumsHolder.getInstance();

    HashMap<String, Album> albumMap = new HashMap<>();
    HashMap<String, Bitmap> albumKeysAndImages = new HashMap<>();
    ArrayList<Bitmap> albumCovers = new ArrayList<>();
    ArrayList<String> albumNames = new ArrayList<>();
    String email;
    String name;
    private Button viewalbum;
    private Button sharealbum;
    private Button friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        albums = new Firebase("https://PicMe.firebaseio.com/albums");
        ref = new Firebase("https://PicMe.firebaseio.com/");
        unpackBundle(); // Pulls data passed from other activities out of Bundles
        this.albumMap = holder.albumMap;
        this.albumKeysAndImages = holder.albumKeysAndCovers;
        this.albumCovers = holder.albumCovers;
        this.albumNames  = holder.albumNames;

        viewalbum = (Button) findViewById(R.id.view_album);

        viewalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartViewAlbumActivity();
            }
        });

        sharealbum = (Button) findViewById(R.id.share_album);

        sharealbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartShareAlbumActivity();
            }
        });

        friends = (Button) findViewById(R.id.friends);

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartShareAlbumActivity();
            }
        });


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

    public void StartShareAlbumActivity(){
        Intent startShareAlbum = new Intent(this, ShareAlbum.class);
        Bundle bundle = new Bundle();
        if(email != null && name != null) {
            bundle.putString("USER_EMAIL", email);
            bundle.putString("USER_NAME", name);
        }
        startShareAlbum.putExtras(bundle);
        startActivity(startShareAlbum);
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

            if(holder.albumCovers.isEmpty()) {
                holder.email = email;
                populateAlbums();
            }
            if(extras.containsKey("NAME")){
                name = extras.getString("NAME");
                text.setText(text.getText() + " " + name);
            }
        }
    }

    public void populateAlbums(){
        Query query = albums.child(email).child("albums").orderByKey();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("AlbumView Firebase Key", snapshot.getKey());
                String albumKey = snapshot.getKey();
                Album albm = new Album();
                DataSnapshot albumNme = snapshot.child("album_name");
                DataSnapshot imageArray = snapshot.child("images");

                Iterable<DataSnapshot> iterable = imageArray.getChildren();
                Iterator<DataSnapshot> it = iterable.iterator();
                int count = 0;

                while(it.hasNext()){

                    DataSnapshot img = (DataSnapshot)it.next();
                    Bitmap temp = MainActivity.base64ToBitmap(img.getValue().toString());
                    //Log.d(img.getKey(), img.getValue().toString());
                    albm.images.put(Integer.parseInt(img.getKey()), img.getValue().toString());
                    albm.albumKey = albumKey;
                    if(count == 0){
                        albumKeysAndImages.put(albumKey, temp);
                        albumCovers.add(temp);
                        albumNames.add(albumNme.getValue().toString());
                        Log.d("AlbumView albumName: ", albumNme.getValue().toString());
                    }
                    count++;
                }
                albumMap.put(albumKey, albm);
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
