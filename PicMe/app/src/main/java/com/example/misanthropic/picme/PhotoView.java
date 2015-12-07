package com.example.misanthropic.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class PhotoView extends AppCompatActivity {

    AlbumsHolder holder = AlbumsHolder.getInstance();

    Button addphoto;
    Button deletephoto;
    public String albumKey;
    public int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);
        unpackBundle();
        addphoto = (Button) findViewById(R.id.addphoto);
        deletephoto = (Button) findViewById(R.id.deletephoto);

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoUploadView = new Intent(getApplicationContext(), upload_view.class);
                Bundle bundle = new Bundle();
                if (albumKey != null) {
                    bundle.putString("ALBUM_KEY", albumKey);
                    Log.d("Album Key", albumKey);
                    bundle.putInt("INDEX", index);
                }
                gotoUploadView.putExtras(bundle);
                startActivity(gotoUploadView);
            }
        });

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, holder.albumMap.get(albumKey).images, holder.albumNames, 0));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gotoImageDetail = new Intent(getApplicationContext(), ImageDetail.class);
                    Bundle bundle = new Bundle();
                    AlbumsHolder.tempImage = holder.albumMap.get(albumKey).images.get(position);
                    gotoImageDetail.putExtras(bundle);
                Log.d("Gridview Listener", "In Listener");
                startActivity(gotoImageDetail);
            }
        });
    }


    public void unpackBundle(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Log.d("Key PhotoView", "Init");

        if(extras!= null){
            albumKey = extras.getString("ALBUM_KEY");
            Log.d("Key PhotoView", albumKey);
            if(extras.containsKey("INDEX")){
                index = extras.getInt("INDEX");
            }
        }
    }


}
