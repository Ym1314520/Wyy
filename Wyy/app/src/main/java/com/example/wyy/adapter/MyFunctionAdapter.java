package com.example.wyy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wyy.R;
import com.example.wyy.data.Function;

import java.util.List;

public class MyFunctionAdapter extends RecyclerView.Adapter<MyFunctionAdapter.ViewHolder> {
    List<Function> functionList;
    public MyFunctionAdapter(List<Function> temp_list){
        functionList=temp_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_function,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Function function=functionList.get(position);
        holder.myPic.setImageResource(function.getPic());
        holder.myText.setText(function.getName());
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myPic;
        TextView myText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myPic=itemView.findViewById(R.id.myPic);
            myText=itemView.findViewById(R.id.myText);
        }
    }
}
