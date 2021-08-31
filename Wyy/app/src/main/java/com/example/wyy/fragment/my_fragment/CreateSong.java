package com.example.wyy.fragment.my_fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.wyy.R;
import com.example.wyy.adapter.CreateSongAdapter;
import com.example.wyy.data.Song_inner;
import com.example.wyy.data.UserCreateSong;

import java.util.ArrayList;
import java.util.List;

public class CreateSong extends Fragment {
    String TAG="Power";
    List<Song_inner> createSongList;
    CreateSongAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            createSongList = (ArrayList<Song_inner>) getArguments().getSerializable("songList");
            adapter = new CreateSongAdapter(createSongList);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create_song, container, false);
        recyclerView=view.findViewById(R.id.createSongs);
        manager=new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
