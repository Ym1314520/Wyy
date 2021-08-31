package com.example.wyy.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wyy.R;
import com.example.wyy.RecyclerView2;
import com.example.wyy.adapter.MultiFuctionAdapter;
import com.example.wyy.adapter.RecommendAdapter;
import com.example.wyy.adapter.SongsAdapter;
import com.example.wyy.adapter.ViewPager2Adapter;
import com.example.wyy.data.Banner;
import com.example.wyy.data.Function;
import com.example.wyy.data.Recommend;
import com.example.wyy.data.Song;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class SearchPage extends Fragment {
    String TAG = "SEARCHPAGE";
    //banner横幅
    private InternetUtil requestInternet;
    private ViewPager2 vp2;
    private ViewPager2Adapter vp2Adapter;
    private List<Banner> bannerList;
    private JsonUtil jsonUtil;

    //multi-function多功能
    private RecyclerView2 multiRecycler;
    private LinearLayoutManager multiManager;
    private List<Function> functionList;
    private MultiFuctionAdapter multiAdapter;

    //recommend推荐歌单
    private RecyclerView2 recomRecycler;
    private LinearLayoutManager recomManager;
    private List<Recommend> recommendList;
    private RecommendAdapter recommendAdapter;

    //songs新歌
    private RecyclerView2 songRecycler;
    private LinearLayoutManager songManager;
    private List<List<Song>> songList;
    private SongsAdapter songsAdapter;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //banner横幅加载
        requestInternet = new InternetUtil();
        jsonUtil = new JsonUtil();

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data = msg.obj.toString();
                Log.d(TAG, "mypageData:"+data);
                try {
                    jsonUtil.jsonBanner(data);
                    jsonUtil.jsonRecommend(data);
                    jsonUtil.jsonSongs(data);
                    bannerList = jsonUtil.getBannerList();
                    Log.d(TAG, "bannerList:"+bannerList.size());
                    recommendList = jsonUtil.getRecommendList();
                    songList = jsonUtil.getSongList();
                    Log.d(TAG, "这是已经返回处理完的数据啦");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                vp2Adapter = new ViewPager2Adapter(bannerList);
                vp2.setAdapter(vp2Adapter);
                recomManager = new LinearLayoutManager(recomRecycler.getContext());
                recomManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recomRecycler.setLayoutManager(recomManager);
                recommendAdapter = new RecommendAdapter(recommendList);
                recomRecycler.setAdapter(recommendAdapter);
                Log.d(TAG, "设置完recomRecycler");
                songManager=new LinearLayoutManager(songRecycler.getContext());
                songManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                songRecycler.setLayoutManager(songManager);
                songsAdapter=new SongsAdapter(songList);
                songRecycler.setAdapter(songsAdapter);
                Log.d(TAG, "设置完songRecycler了");

                return false;
            }
        });

        //banner+recommend
        Log.d(TAG,"我马上开始banner的网络请求啦");
        requestInternet.sendGetNetRequest("http://49.234.117.142:3000/homepage/block/page", handler);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_page, container, false);
        //banner加载
        vp2 = view.findViewById(R.id.innerVp2);
        //multi多功能加载
        multiRecycler = view.findViewById(R.id.multi);
        //recommend的加载
        recomRecycler = view.findViewById(R.id.recommend_recycler);
        //song新歌
        songRecycler=view.findViewById(R.id.songs);

        //multi-function多功能加载
        initMultiFunctionData();
        multiManager = new LinearLayoutManager(multiRecycler.getContext());
        multiManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        multiRecycler.setLayoutManager(multiManager);
        multiAdapter = new MultiFuctionAdapter(functionList);
        multiRecycler.setAdapter(multiAdapter);
        return view;
    }


    private void initMultiFunctionData() {
        functionList = new ArrayList<>();
        Function f1 = new Function(R.drawable.everyday, "每日推荐");
        Function f2 = new Function(R.drawable.fm, "私人FM");
        Function f3 = new Function(R.drawable.songs, "歌单");
        Function f4 = new Function(R.drawable.room, "歌房");
        Function f5 = new Function(R.drawable.figure, "数字专辑");
        Function f6 = new Function(R.drawable.game, "游戏专区");
        Function f7 = new Function(R.drawable.consider, "专注冥想");
        functionList.add(f1);
        functionList.add(f2);
        functionList.add(f3);
        functionList.add(f4);
        functionList.add(f5);
        functionList.add(f6);
        functionList.add(f7);
    }
}
