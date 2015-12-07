package com.example.misanthropic.picme;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jianganson72 on 11/29/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    AlbumsHolder holder = AlbumsHolder.getInstance();
    public static Integer [] Images;
    public ArrayList<Bitmap> images = holder.albumCovers;
    public ArrayList<String> albumNames = holder.albumNames;
    public static String [] Titles;

    public ImageAdapter(Context c, Integer [] Image, String [] Title) {
        mContext = c;
        Images = Image;
        Titles = Title;
    }

    public ImageAdapter(Context c, ArrayList<Bitmap> images, ArrayList<String> albumNames) {
        mContext = c;
        this.images = images;
        this.albumNames = albumNames;
    }

    public ImageAdapter(Context c, ArrayList<String> images, ArrayList<String> albumNames, int dummy) {
        mContext = c;
        this.images = convertImages(images);
        this.albumNames = albumNames;
    }


    public int getCount() {
        return (Titles == null)?images.size(): Titles.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        grid = inflater.inflate(R.layout.fragment_albumtitle, null);
        TextView textview = (TextView) grid.findViewById(R.id.grid_item_label);
        ImageView imageview = (ImageView) grid.findViewById(R.id.grid_item_image);
        try{
        textview.setText(albumNames.get(position));
        }catch(Exception e)
        {
            textview.setText(String.valueOf(position));
        }
        imageview.setImageBitmap(images.get(position));


        return grid;
    }

    public ArrayList<Bitmap> convertImages(ArrayList<String> strings){
        ArrayList<Bitmap> imgs = new ArrayList<>();
        for(String x: strings){
            imgs.add(MainActivity.base64ToBitmap(x));
        }

        return imgs;
    }
}
