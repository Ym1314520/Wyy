package com.example.wyy.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.wyy.R;
import com.example.wyy.activity.Search;
import com.example.wyy.fragment.MyPage;
import com.example.wyy.fragment.PeoplePage;
import com.example.wyy.fragment.PlayerPage;
import com.example.wyy.fragment.SearchPage;
import com.example.wyy.internetUtil.InternetUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String TAG="YYY";
    private BottomNavigationView bottom;
    private ViewPager2 vp2;
    private ArrayList<Fragment> fragments;
    private Toolbar toolbar;
    Fragment myPage;
    InternetUtil internetUtil;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.searchIt){//搜索栏跳转
            Intent intent=new Intent(this, Search.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        internetUtil=new InternetUtil();
        bottom=findViewById(R.id.bottom);
        vp2=findViewById(R.id.vp2);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragments=new ArrayList<>();
        fragments.add(new SearchPage());
        myPage=new MyPage();
        fragments.add(myPage);
        fragments.add(new PlayerPage());
        fragments.add(new PeoplePage());



        vp2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        vp2.setOffscreenPageLimit(1);
        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottom.getMenu().getItem(position).setChecked(true);
            }
        });

        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                vp2.setCurrentItem(item.getOrder());
                return true;
            }
        });

        //接收登陆后拿到的data
        Intent intent=getIntent();
        if(intent.getStringExtra("sinal")!=null) {
                vp2.setCurrentItem(1);
                //用bundle传消息给fragment
                Bundle bundle=new Bundle();
                bundle.putString("change",intent.getStringExtra("sinal"));
                myPage.setArguments(bundle);
        }


    }

}
