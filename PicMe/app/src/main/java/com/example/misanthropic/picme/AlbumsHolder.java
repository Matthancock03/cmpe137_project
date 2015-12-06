package com.example.misanthropic.picme;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Misanthropic on 12/6/15.
 */
public class AlbumsHolder {

    HashMap<String, Album> albumMap = new HashMap<>();
    public static ArrayList<Bitmap> albumCovers = new ArrayList<>();
    public static ArrayList<String> albumNames = new ArrayList<>();

    private static final AlbumsHolder holder = new AlbumsHolder();

    public static AlbumsHolder getInstance() {return holder;}
}
