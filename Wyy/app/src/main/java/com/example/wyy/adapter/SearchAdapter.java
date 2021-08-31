package com.example.wyy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wyy.R;
import com.example.wyy.activity.MusicPlayer;
import com.example.wyy.activity.Search;
import com.example.wyy.data.Song_inner;

import java.io.Serializable;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    List<Song_inner> searchList;

    public SearchAdapter(List<Song_inner> temp_list){
        searchList=temp_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder= new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter,parent,false));
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Song_inner search=searchList.get(position);
        holder.title.setText(search.getTitle());
        holder.subTitle.setText(search.getSubtitle());
        holder.author.setText(search.getAuthor());
        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), MusicPlayer.class);
                intent.putExtra("songList",(Serializable) searchList);
                intent.putExtra("position",position);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,subTitle,author;
        LinearLayout lay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.search_title);
            subTitle=itemView.findViewById(R.id.search_subTitle);
            author=itemView.findViewById(R.id.search_author);
            lay=itemView.findViewById(R.id.search_lay);
        }
    }
}
