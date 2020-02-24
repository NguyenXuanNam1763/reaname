package com.example.renamefile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renamefile.model.Images;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomViewHolder> {

    Context context;
    ArrayList<Images> arrayList;

    public HomeAdapter(Context context, ArrayList<Images> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
            Bitmap bitmap= BitmapFactory.decodeByteArray(images.getImage(),0,images.getImage().length);
            holder.img_item.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_item)
        ImageView img_item;

        public HomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
