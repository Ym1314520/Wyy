package com.example.wyy.fragment.search;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wyy.R;
import com.example.wyy.adapter.SearchAdapter;
import com.example.wyy.data.Song_inner;
import com.example.wyy.json.JsonUtil;

import org.json.JSONException;

import java.util.List;

public class SearchResult extends Fragment {
    String TAG="SECH";
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    JsonUtil jsonUtil;
    List<Song_inner> searchList;
    SearchAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonUtil=new JsonUtil();
        Bundle bundle=getArguments();
        if(bundle!=null){
            Log.d(TAG, "拿到bundle数据了哈");
            String data=bundle.getString("data");
            try {
                searchList=jsonUtil.jsonSearchList(data);
                adapter=new SearchAdapter(searchList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search_result, container, false);
        recyclerView=view.findViewById(R.id.search_list);
        manager=new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }

}
