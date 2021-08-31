package com.example.wyy.fragment;

import android.content.Intent;
import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wyy.R;
import com.example.wyy.activity.SongList;
import com.example.wyy.adapter.MyFunctionAdapter;
import com.example.wyy.data.Function;
import com.example.wyy.fragment.my_fragment.CollectionSong;
import com.example.wyy.fragment.my_fragment.CreateSong;
import com.example.wyy.internetUtil.InternetUtil;
import com.example.wyy.json.JsonUtil;
import com.example.wyy.login_register.login_ui.LoginUi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.Serializable;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPage extends Fragment implements View.OnClickListener {
    String TAG = "HEHE";
    private RecyclerView multiRecycler;
    private StaggeredGridLayoutManager manager;
    private MyFunctionAdapter myAdapter;
    private List<Function> functionList;
    private TextView level;
    private LinearLayout love_lay;
    //嵌套Fragment
    private ArrayList<Fragment> fragments;
    private ViewPager2 vp2;
    private BottomNavigationView myBottom;

    //登录控件
    private TextView login;
    private JsonUtil jsonUtil;
    private InternetUtil internetUtil;

    //登陆后更新用户数据
    private Handler handler_get;
    private Handler handler_post;
    private ImageView lovePic;
    private TextView count;
    private CreateSong createSong;
    private CollectionSong collectionSong;
    private HashMap<String, String> map;
    private HashMap<String, String> map2;
    String uid;
    private FragmentStateAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //一些美化的布局初始化
        initData();

        //将要嵌套的两个fragment实例
        createSong = new CreateSong();
        collectionSong = new CollectionSong();

        //嵌套fragment进去
        fragments = new ArrayList<>();
//        fragments.add(new CollectionSong());
//        fragments.add(new CreateSong());

        //回调handler处理的等级信息
        handler_get = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data = msg.obj.toString();
                try {
                    level.setText("Lv." + jsonUtil.jsonLevel(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


        handler_post = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data = msg.obj.toString();
                try {
                    //我的喜欢歌曲界面处理
                    HashMap<String, String> map = jsonUtil.jsonCoverPic(data);
                    Glide.with(MyPage.this).load(map.get("picUrl")).into(lovePic);
                    count.setText(map.get("count") + "首");
                    //点击跳转我喜欢的歌单
                    love_lay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(v.getContext(), SongList.class);
                            intent.putExtra("id",map.get("id"));
                            startActivity(intent);
                        }
                    });

                    //这是更新两个嵌套的fragment
                    fragments.add(collectionSong);
                    fragments.add(createSong);
                    adapter.notifyDataSetChanged();

                    //我的收藏和创建歌单
                    Bundle bundle = new Bundle();
                    bundle.putString("uid",uid);
                    bundle.putSerializable("collection", (Serializable) jsonUtil.getCollectionSongList());
                    collectionSong.setArguments(bundle);
                    bundle.putSerializable("songList", (Serializable) jsonUtil.getCreateSongList());
                    createSong.setArguments(bundle);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });



    }

    private void initData() {
        jsonUtil = new JsonUtil();
        functionList = new ArrayList<>();
        internetUtil = new InternetUtil();
        functionList.add(new Function(R.drawable.know, "消息/通知"));
        functionList.add(new Function(R.drawable.money, "我的钱包"));
        functionList.add(new Function(R.drawable.set, "设置"));
        functionList.add(new Function(R.drawable.collect, "收藏"));
        functionList.add(new Function(R.drawable.load, "本地/下载"));
        functionList.add(new Function(R.drawable.buy, "已购"));
        functionList.add(new Function(R.drawable.fun, "趣味游戏"));
        functionList.add(new Function(R.drawable.more, "更多"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        //一些控件的初始化
        multiRecycler = view.findViewById(R.id.multi2);
        love_lay=view.findViewById(R.id.love_lay);
        login = view.findViewById(R.id.logins);
        level = view.findViewById(R.id.level);
        lovePic = view.findViewById(R.id.lovePic);
        count = view.findViewById(R.id.count);
        manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        multiRecycler.setLayoutManager(manager);
        myAdapter = new MyFunctionAdapter(functionList);
        multiRecycler.setAdapter(myAdapter);


        //嵌套fragment的初始化
        vp2 = view.findViewById(R.id.my_vp2);
        myBottom = view.findViewById(R.id.my_bottom);
        adapter=new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        };

        vp2.setAdapter(adapter);

        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                myBottom.getMenu().getItem(position).setChecked(true);
            }
        });

        myBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                vp2.setCurrentItem(item.getOrder());
                vp2.setOffscreenPageLimit(1);
                return true;
            }
        });

        //接收bundle传入的data值，表示登陆后
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("change");
            try {
                //这是网络请求设置等级信息
                map = jsonUtil.jsonUser(data);
                login.setText(map.get("nickname"));
                new InternetUtil().sendGetNetRequest("http://49.234.117.142:3000/user/level", handler_get);


                map2 = new HashMap<>();
                map2.put("uid", map.get("userId"));
                uid=map.get("userId");
                new InternetUtil().sendPostNetRequest("http://49.234.117.142:3000/user/playlist", map2, handler_post);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //登录逻辑
        if (login.getText().toString().equals("去登录")) {
            LinearLayout linearLayout = view.findViewById(R.id.layout);
            linearLayout.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout:
                Intent intent = new Intent(v.getContext(), LoginUi.class);
                startActivity(intent);
                break;
        }
    }

}
