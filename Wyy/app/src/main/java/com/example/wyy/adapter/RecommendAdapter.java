package com.example.wyy.adapter;

import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wyy.R;
import com.example.wyy.activity.SongList;
import com.example.wyy.data.Recommend;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.HolderView> {
    List<Recommend> recommendList;

    public RecommendAdapter(List<Recommend> temp_list){
        recommendList=temp_list;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend,parent,false);
        HolderView holder=new HolderView(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        final Recommend recommend=recommendList.get(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), SongList.class);
                intent.putExtra("id",recommend.getId());
                v.getContext().startActivity(intent);
            }
        });

        holder.title.setText(recommend.getTitle());
        Glide.with(holder.itemView).load(recommend.getImageUrl()).into(holder.imageUrl);
    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {
        ImageView imageUrl;
        TextView title;
        LinearLayout layout;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            imageUrl=itemView.findViewById(R.id.imageUrl);
            title=itemView.findViewById(R.id.title);
            layout=itemView.findViewById(R.id.lay_recommend);
        }
    }
}
