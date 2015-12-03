package com.example.misanthropic.picme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jianganson72 on 12/3/15.
 */
public class PhotoViewFragment extends Fragment {
    public PhotoViewFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_view, container, false);
    }


}
