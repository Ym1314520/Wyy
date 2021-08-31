package com.example.wyy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyy.R;
import com.example.wyy.fragment.search.SearchResult;
import com.example.wyy.internetUtil.InternetUtil;

import java.util.HashMap;

public class Search extends AppCompatActivity implements View.OnClickListener{
    ImageView search_back;
    EditText search_text;
    InternetUtil internetUtil;
    HashMap<String,String> search_map;
    Handler handler;
    SearchResult searchResult;
    FragmentTransaction fragmentTransaction;
    FragmentManager manager;
    int FLAG=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        manager=getSupportFragmentManager();

        init();

        //处理返回搜索数据
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                String data=msg.obj.toString();
                Bundle bundle=new Bundle();
                bundle.putString("data",data);
                searchResult=new SearchResult();
                searchResult.setArguments(bundle);
                fragmentTransaction=manager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_fragment, searchResult);
                fragmentTransaction.commit();
                return false;
            }
        });



        search_back.setOnClickListener(this);
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search_map=new HashMap<>();
                internetUtil=new InternetUtil();
                search_map.put("keywords",search_text.getText().toString());
                internetUtil.sendPostNetRequest("http://49.234.117.142:3000/cloudsearch",search_map,handler);

                return false;
            }
        });
    }

    private void init() {
        search_back=findViewById(R.id.search_back);
        search_text=findViewById(R.id.search_text);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
