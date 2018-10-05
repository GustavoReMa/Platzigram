package com.example.usuario.platzigram.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.usuario.platzigram.R;
import com.example.usuario.platzigram.adapter.PictureAdapterRecyclerView;
import com.example.usuario.platzigram.model.Picture;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        showToolbar(getResources().getString(R.string.tab_search),false,view);

        RecyclerView picturesRecycler = (RecyclerView) view.findViewById(R.id.pictureSearchRecycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        picturesRecycler.setLayoutManager(gridLayoutManager);

        PictureAdapterRecyclerView pictureAdapterRecyclerView = new
                PictureAdapterRecyclerView(buidPictures(),R.layout.cardview_picture,getActivity());

        picturesRecycler.setAdapter(pictureAdapterRecyclerView);


        return view;
    }

    public ArrayList<Picture> buidPictures(){
        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("https://www.novalandtours.com/images/guide/guilin.jpg","Uriel Ramírez","4 días","3 Me gusta"));
        pictures.add(new Picture("http://i.imgur.com/DvpvklR.png","Juan Pablo","3 días","10 Me gusta"));
        pictures.add(new Picture("https://i.imgur.com/EkYkK4Z.jpg","Anahí Salgado","2 días","9 Me gusta"));
        pictures.add(new Picture("https://img.difoosion.com/wp-content/blogs.dir/28/files/2016/06/fondo5.jpg", "Gabriel Mederos", "4 días", "3 Me gusta"));
        pictures.add(new Picture("http://images.eldiario.es/canariasahora/sociedad/fondos-acentejojpg_EDIIMA20140403_0542_13.jpg", "Gabriel Caballero", "2 días", "5 Me gusta"));
        pictures.add(new Picture("http://coolmusic.jinradio.com/wp-content/uploads/sites/2/2014/03/CoolBack7.jpg", "Pablo Dina", "1 días", "4 Me gusta"));
        return pictures;
    }


    public void showToolbar(String title, boolean upButton,View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}
