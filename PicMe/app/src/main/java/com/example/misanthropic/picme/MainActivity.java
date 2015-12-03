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

import java.io.ByteArrayOutputStream;

//public class MainActivity extends AppCompatActivity implements CreateAlbumFragment.OnFragmentInteractionListener{
public class MainActivity extends FragmentActivity{

    String userEmail;
    private Button viewalbum;
    private Button sharealbum;
    private Button createalbum;
    private Button friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBundle(); // Pulls data passed from other activities out of Bundles


        viewalbum = (Button) findViewById(R.id.view_album);

        viewalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartViewAlbumActivity();
            }
        });

    }

    public void StartCreateAlbumActivity(){
        Intent GotoCreateAlbum = new Intent(this, CreateAlbum.class);
        startActivity(GotoCreateAlbum);
    }

    public void StartViewAlbumActivity(){
        Intent startViewAlbum = new Intent(this, AlbumView.class);
        startActivity(startViewAlbum);
    }

    // Use this to convert image before saving to Firebase
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    //Use this to convert retrieved image to bitmap for displaying
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    public String getUserEmail(){
        return userEmail;
    }

    public void loadBundle(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(!extras.isEmpty() && extras.containsKey("USER_EMAIL")){
            TextView text = (TextView)findViewById(R.id.displayHeader);
            userEmail = extras.getString("USER_EMAIL");
            Log.d("Email in Main" , userEmail);
            text.setText(text.getText() + " " + userEmail);
        }
    }

    /*
    @Override
    public void onFragmentInteraction(Uri uri) {

    }*/

    /*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/

}
