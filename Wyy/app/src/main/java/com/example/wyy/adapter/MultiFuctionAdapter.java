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

import java.util.ArrayList;
import java.util.List;

public class MultiFuctionAdapter extends RecyclerView.Adapter<MultiFuctionAdapter.ViewHoler> {
    List<Function> functionList;

    public MultiFuctionAdapter(List<Function> tempList){
        functionList=tempList;
    }

    @NonNull
    @Override
    public MultiFuctionAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.multi,parent,false);
        ViewHoler holer=new ViewHoler(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull MultiFuctionAdapter.ViewHoler holder, int position) {
        final Function function=functionList.get(position);
        holder.funcPic.setImageResource(function.getPic());
        holder.funcName.setText(function.getName());
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        ImageView funcPic;
        TextView funcName;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            funcPic=itemView.findViewById(R.id.functionPic);
            funcName=itemView.findViewById(R.id.functionName);
        }
    }
}
