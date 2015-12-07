package com.example.misanthropic.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class PhotoView extends AppCompatActivity {

    AlbumsHolder holder = AlbumsHolder.getInstance();

    final public Integer [] Thumbs = {R.mipmap.sample_10,
            R.mipmap.androidmarshmallow, R.mipmap.nfllogo,
            R.mipmap.album_image, R.mipmap.earthimage,
            R.mipmap.brocolli, R.mipmap.seagulf,
            R.mipmap.bball, R.mipmap.sample_7,
            R.mipmap.earthimage, R.mipmap.sample_1,
            R.mipmap.sample_2, R.mipmap.earth,
            R.mipmap.nukacola, R.mipmap.androidmarshmallow
    };

    final public String [] PicTitle = {
            "sample_10", "androidmarshamallow", "nfllogo", "albumimage", "earthimage",
            "brocolli", "seagulf", "bball", "sample_7", "earthimage", "sample_1", "sample_2",
            "earth", "nukacola", "androidmarshmallow"
    };

    Button addphoto;
    Button deletephoto;
    public String albumKey;
    public String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);
        addphoto = (Button) findViewById(R.id.addphoto);
        deletephoto = (Button) findViewById(R.id.deletephoto);

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoUploadView = new Intent(getApplicationContext(), Upload_View.class);
                Bundle bundle = new Bundle();
                if (albumKey != null) {
                    bundle.putString("AlbumKey", albumKey);
                    bundle.putString("INDEX", index);
                }
                gotoUploadView.putExtras(bundle);
                startActivity(gotoUploadView);
            }
        });

        getIntent();

        GridView gridview = (GridView) findViewById(R.id.gridview);

        gridview.setAdapter(new ImageAdapter(this, holder.albumCovers, holder.albumNames));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gotoImageDetail = new Intent(getApplicationContext(), ImageDetail.class);
                gotoImageDetail.putExtra("id", position);
                startActivity(gotoImageDetail);
            }
        });
    }





}
