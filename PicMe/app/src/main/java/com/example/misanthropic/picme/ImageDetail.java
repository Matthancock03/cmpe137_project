package com.example.misanthropic.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import com.firebase.client.Firebase;

public class ImageDetail extends PhotoView {
    Firebase image;
    Firebase album;
    ImageView iv;
    ListView comments_holder;
    ArrayList<String> comments;
    EditText comment_field;
    ArrayAdapter adapter;

    String[] testValues = new String[] { "Android", "iPhone", "WindowsMobile",
            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
            "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
            "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
            "Android", "iPhone", "WindowsMobile" };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Intent i = getIntent();

        int position = (Integer) i.getExtras().getInt("id");
        iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(ImageAdapter.Images[position]);

        //Set up Firebase
        Firebase.setAndroidContext(this);
        image = new Firebase("https://PicMe.firebaseio.com/albums/images/id");

        // Get Comments ListView and set adapter
        comments_holder = (ListView)findViewById(R.id.comment_list);
        adapter = new ArrayAdapter<String>(this,R.layout.fragment_image_detail , testValues); // Used to dynamically update view on data change.
        comments_holder.setAdapter(adapter);
        // Get Comments from Firebase

    }

    public void addComment(){
        comment_field = (EditText)findViewById(R.id.comment_input);
        String com = comment_field.toString();
        comments.add(com);
        TextView txt = new TextView(this);
        txt.setText(com);
        comments_holder.addView(txt);
    }

    public void retrieveComments(){

    }

}
