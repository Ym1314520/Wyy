package com.example.wyy.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wyy.R;
import com.example.wyy.activity.MusicPlayer;
import com.example.wyy.data.Song_inner;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{
    List<Song_inner> song_innerList;


    public SongAdapter(List<Song_inner> temp_list){
        song_innerList=temp_list;
    }

    public void setSong_innerList(List<Song_inner> song_innerList) {
        this.song_innerList = song_innerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.songs,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Song_inner song=song_innerList.get(position);
        holder.number.setText(String.valueOf(position+1));
        holder.song_title.setText(song.getTitle());
        holder.song_sub.setText(song.getSubtitle());
        holder.author.setText(song.getAuthor());
        Random random=new Random();
        boolean b=random.nextBoolean();
        if(b){
            holder.ran.setImageResource(R.drawable.sq);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), MusicPlayer.class);
                intent.putExtra("position",position);
                intent.putExtra("songList",(Serializable) song_innerList);
                v.getContext().startActivity(intent);
            }
        });

        holder.love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.love.setSelected(true);

            }
        });
    }

    @Override
    public int getItemCount() {
        return song_innerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number,song_title,song_sub,author;
        ImageView love,vedio,ran;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.number);
            song_sub=itemView.findViewById(R.id.song_sub);
            song_title=itemView.findViewById(R.id.song_title);
            love=itemView.findViewById(R.id.love_song);
            vedio=itemView.findViewById(R.id.media2);
            author=itemView.findViewById(R.id.singer);
            layout=itemView.findViewById(R.id.lay3);
            ran=itemView.findViewById(R.id.ran);
        }
    }
}
