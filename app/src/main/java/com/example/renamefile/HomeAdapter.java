package com.example.renamefile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.renamefile.model.Images;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomViewHolder> {

    Context context;
    ArrayList<Images> arrayList;
    CallBack callBack;

    public HomeAdapter(Context context, ArrayList<Images> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.callBack =(CallBack) context;
    }

    @NonNull
    @Override
    public HomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.item_rcv,parent,false);
        return new HomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomViewHolder holder, int position) {
        Images images=arrayList.get(position);
        if(images!=null){
            Glide.with(context).load(images.getNewUri()+"/"+images.getNewName()).into(holder.img_item);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_item)
        ImageView img_item;
        @OnLongClick(R.id.img_item)
        public void onLogClick(View view){
            callBack.callback(arrayList.get(getPosition()).getId(),getPosition());
        }

        public HomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface CallBack{
        void callback(int id, int position);
    }
}
