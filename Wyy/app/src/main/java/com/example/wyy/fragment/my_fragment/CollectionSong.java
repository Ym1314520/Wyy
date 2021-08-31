package com.example.wyy.fragment.my_fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wyy.R;
import com.example.wyy.RecyclerView2;
import com.example.wyy.Refresh;
import com.example.wyy.UpPullOnScrollListener;
import com.example.wyy.adapter.CreateSongAdapter;
import com.example.wyy.data.Song_inner;
import com.example.wyy.data.UserCreateSong;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionSong extends Fragment implements UpPullOnScrollListener {
    String TAG = "HEHE";
    List<Song_inner> userCreateSongList;
    RecyclerView2 recyclerView;
    LinearLayoutManager manager;
    CreateSongAdapter adapter;
    String uid;
    Handler handler;
    JsonUtil jsonUtil;
    View view;
    List<Song_inner> collectionSongList;
    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!= null) {
            uid=bundle.getString("uid");
            Log.d(TAG, "UID"+uid);
            userCreateSongList = (ArrayList<Song_inner>) bundle.get("collection");
            adapter = new CreateSongAdapter(userCreateSongList);

        }
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data=msg.obj.toString();
                Log.d(TAG, data);
                jsonUtil=new JsonUtil();
                try {
                    Song_inner song=jsonUtil.jsonIt(data);
                    userCreateSongList.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(userCreateSongList.size()-2);
                Toast.makeText(getContext(),"更新成功",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreate:进入了view，准备设置数据，设置界面");
        view = inflater.inflate(R.layout.fragment_collection_song, container, false);
        recyclerView = view.findViewById(R.id.collection);
        manager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.addOnScrollListener(new Refresh(this));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onCreate:销毁了哈");
    }

    @Override
    public void onLoadMore() {
        HashMap<String,String> map=new HashMap<>();
        map.put("uid",uid);
        new InternetUtil().sendPostNetRequest("http://49.234.117.142:3000/user/playlist", map,handler);
    }

    @Override
    public void onRefresh() {

    }
}
