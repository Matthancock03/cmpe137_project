package com.example.misanthropic.picme;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Misanthropic on 12/5/15.
 */
public class Album {
    public String album;
    public String albumKey;
    public String album_name;
    public ArrayList<String> comments = new ArrayList<String>();
    public ArrayList<String> images = new ArrayList<>();

    public Album(){

    }

    public String getAlbum(){
        return album_name;
    }

    public String get_Album(){
        return album;
    }

    public String getAlbumKey(){return albumKey;}

    public ArrayList<String> getImages(){
        return images;
    }
}
