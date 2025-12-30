package com.example.orientataionquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    Context context;
    ArrayList<imgsModel>productListView;

    ItemsAdapter(Context context, ArrayList<imgsModel>productListView){
        this.context=context;
        this.productListView=productListView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view=LayoutInflater.from(context).inflate(R.layout.universityimgs,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
imgsModel model=productListView.get(position);
holder.ProductsImg.setImageResource(model.getImg());
    }

    @Override
    public  int getItemCount() {
        return productListView.size();
    }

    public void updateImages(ArrayList<imgsModel> newProductListView){
        productListView.clear();
        productListView.addAll(newProductListView);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ProductsImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductsImg=itemView.findViewById(R.id.universityImgs);
        }
    }


}
