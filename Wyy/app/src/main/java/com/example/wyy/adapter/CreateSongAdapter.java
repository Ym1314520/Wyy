package com.example.wyy.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wyy.R;
import com.example.wyy.activity.SongList;
import com.example.wyy.data.Song_inner;
import com.example.wyy.data.UserCreateSong;
import com.example.wyy.fragment.my_fragment.CreateSong;

import java.io.Serializable;
import java.util.List;

public class CreateSongAdapter extends RecyclerView.Adapter<CreateSongAdapter.ViewHolder> {
    List<Song_inner> userCreateSongList;
    public CreateSongAdapter(List<Song_inner> temp_list){
        userCreateSongList=temp_list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder=new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.create_song,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Song_inner createSong=userCreateSongList.get(position);
        holder.name_create.setText(createSong.getTitle());
        holder.count_create.setText(createSong.getTrackCount()+"首");
        Glide.with(holder.itemView).load(createSong.getCoverImgUrl()).into(holder.pic_create);
        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), SongList.class);
                intent.putExtra("id",createSong.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userCreateSongList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_create,count_create;
        ImageView pic_create;
        LinearLayout lay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic_create=itemView.findViewById(R.id.pic_create);
            name_create=itemView.findViewById(R.id.name_create);
            count_create=itemView.findViewById(R.id.count_create);
            lay=itemView.findViewById(R.id.lay2);
        }
    }
}
