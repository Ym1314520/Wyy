package com.example.wyy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wyy.R;
import com.example.wyy.Refresh;
import com.example.wyy.UpPullOnScrollListener;
import com.example.wyy.adapter.SongAdapter;
import com.example.wyy.data.Song;
import com.example.wyy.data.Song_inner;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;
import com.example.wyy.main.MainActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongList extends AppCompatActivity implements View.OnClickListener{
    String TAG="Ym";
    static int position=10;
    InternetUtil internetUtil;
    HashMap<String,String> map;
    Handler handler;
    JsonUtil jsonUtil;
    ImageView img_song,back,collection;
    TextView name_songs;
    String id;
    RecyclerView songs;
    LinearLayoutManager manager;
    SongAdapter adapter;
    List<Song_inner> song_innerList;

    @Override
    protected void onStart() {
        init();
        //从上一个歌单界面拿到具体歌单id
        Intent intent=getIntent();
        if(intent!=null){
            id=intent.getStringExtra("id");
        }
        Log.d(TAG, "onStart:id"+id);

        //网络数据拿到后返回这里
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data=msg.obj.toString();
                switch (msg.what){
                    case 1:
                        try {
                            jsonUtil.jsonSongList(data);
                            HashMap<String,String> map=jsonUtil.getMap();
                            Glide.with(getApplicationContext()).load(map.get("coverImgUrl")).into(img_song);
                            name_songs.setText(map.get("name"));
                            //做歌单界面的recyclerview
                            song_innerList=jsonUtil.getSong_innerList();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter=new SongAdapter(song_innerList);
                        songs.setLayoutManager(manager);
                        songs.setAdapter(adapter);

                        break;
                    case 2:
                        try {
                            String code=jsonUtil.jsonCode(data);
                            if(code.equals("200")){
                                Log.d(TAG, "收藏成功啦！");
                                Toast.makeText(getBaseContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                                collection.setSelected(true);

                            }
                            else if(code.equals("301")){
                                Toast.makeText(getApplicationContext(),"需要登录",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getBaseContext(),"收藏失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }


                return false;
            }
        });

        //网络开始请求
        map=new HashMap<>();
        map.put("id",id);
        Log.d(TAG, "onStart,网络请求前拿到的id："+id);
        internetUtil.sendPostNetRequest("http://49.234.117.142:3000/playlist/detail",map,handler);
        //返回逻辑
        back.setOnClickListener(this);
        //收藏逻辑
        collection.setOnClickListener(this);

        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onStart:重新创建了一个哈");
        setContentView(R.layout.song_list);
    }

    //初始化所有数据
    private void init() {
        collection=findViewById(R.id.list_collection);
        songs=findViewById(R.id.songsList);//歌单recyclerview
        manager=new LinearLayoutManager(songs.getContext());
        jsonUtil=new JsonUtil();
        internetUtil=new InternetUtil();
        img_song=findViewById(R.id.img_song);
        name_songs=findViewById(R.id.name_songs);
        back=findViewById(R.id.back);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.list_collection:
                HashMap<String,String> map=new HashMap<>();
                map.put("t","1");
                map.put("id",id);
                internetUtil.sendPostNetRequest("http://49.234.117.142:3000/playlist/subscribe",map,handler);
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onStart:销毁了哈");
        super.onDestroy();
    }

}
