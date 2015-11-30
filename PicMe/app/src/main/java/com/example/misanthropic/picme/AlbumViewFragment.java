package com.example.misanthropic.picme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;

import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.BaseAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumViewFragment extends Fragment {

    Firebase ref;
    Firebase user;
    MainActivity main = (MainActivity)getActivity();
    String email = main.getUserEmail();

    private int PICK_IMAGE_REQUEST = 1;

    private OnFragmentInteractionListener mListener;

    public static AlbumViewFragment newInstance(String param1, String param2) {
        AlbumViewFragment fragment = new AlbumViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());
        ref = new Firebase("https://PicMe.firebaseio.com/users");
        user = ref.child("users").child("email");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album_view, container, false);

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        Button addphoto = (Button) view.findViewById(R.id.addphoto);
        Button deletephoto = (Button) view.findViewById(R.id.deletephoto);

        // TODO: get images from Firebase

        //gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(HelloGridView.this, "" + position,
                //      Toast.LENGTH_SHORT).show();
            }
        });

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement add image
            }
        });

        deletephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;

    }

    public void uploadImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
