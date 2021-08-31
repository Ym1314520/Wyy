package com.example.wyy.adapter;

import android.util.Log;
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
import com.example.wyy.RecyclerView2;
import com.example.wyy.data.Song;

import java.util.List;

public class SongsAdapter extends RecyclerView2.Adapter<SongsAdapter.ViewHolder> {
    String TAG="MENG";
    List<List<Song>> songList;

    public SongsAdapter(List<List<Song>> temp_list){
        songList=temp_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "position:"+position);
        final List<Song> songs=songList.get(position);
        Log.d(TAG, String.valueOf(songList.size()));
            Song song=songs.get(0);
            holder.author.setText(song.getName());
            holder.mainTitle.setText(song.getMainTitle());
            holder.subTitle.setText(song.getSubTitle());
            Glide.with(holder.itemView).load(song.getImageUrl()).into(holder.pic);

            Song song2=songs.get(1);
            holder.author2.setText(song2.getName());
            holder.mainTitle2.setText(song2.getMainTitle());
            holder.subTitle2.setText(song2.getSubTitle());
            Glide.with(holder.itemView).load(song2.getImageUrl()).into(holder.pic2);

            Song song3=songs.get(2);
            holder.author3.setText(song3.getName());
            holder.mainTitle3.setText(song3.getMainTitle());
            holder.subTitle3.setText(song3.getSubTitle());
            Glide.with(holder.itemView).load(song3.getImageUrl()).into(holder.pic3);



    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic,pic2,pic3;
        TextView mainTitle,subTitle,author;
        TextView mainTitle2,subTitle2,author2;
        TextView mainTitle3,subTitle3,author3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.pic_song);
            mainTitle=itemView.findViewById(R.id.mainTitle);
            subTitle=itemView.findViewById(R.id.subTitle);
            author=itemView.findViewById(R.id.author);

            pic2=itemView.findViewById(R.id.pic_song2);
            mainTitle2=itemView.findViewById(R.id.mainTitle2);
            subTitle2=itemView.findViewById(R.id.subTitle2);
            author2=itemView.findViewById(R.id.author2);

            pic3=itemView.findViewById(R.id.pic_song3);
            mainTitle3=itemView.findViewById(R.id.mainTitle3);
            subTitle3=itemView.findViewById(R.id.subTitle3);
            author3=itemView.findViewById(R.id.author3);
        }
    }
}
