package com.lawnics.printingpartner.CustomGallery.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lawnics.printingpartner.CustomGallery.Model_images;
import com.lawnics.printingpartner.R;

import java.util.List;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.ViewHolder> {

    Context context;
    List<Model_images> model_imagesList;
    ImagePickActivity imagePickActivity;
    ImageListAdapter imageListAdapter;
    View redLayout;
    ViewGroup parent;

    public FolderListAdapter(Context context, List<Model_images> model_imagesList, ImageListAdapter imageListAdapter, View redLayout, ViewGroup parent) {
        this.context = context;
        this.model_imagesList = model_imagesList;
        this.imagePickActivity = (ImagePickActivity) context;
        this.imageListAdapter = imageListAdapter;
        this.redLayout = redLayout;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_folders, parent, false), imagePickActivity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(model_imagesList.get(position).getStr_folder());
        holder.count.setText(model_imagesList.get(position).getAl_imagepath().size() + "");
        Glide.with(context).load(model_imagesList.get(position).getAl_imagepath().get(0)).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickActivity.position[0] = position;
                imageListAdapter.setImages(model_imagesList.get(position).getAl_imageuri());
                imagePickActivity.toggleFloatButtons(false);
                imagePickActivity.bt_Image.setText(model_imagesList.get(position).getStr_folder());
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, count;
        CardView cardView;
        ImagePickActivity imagePickActivity;

        public ViewHolder(@NonNull View itemView, ImagePickActivity imagePickActivity) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_folder_imageView);
            name = itemView.findViewById(R.id.item_folderName);
            count = itemView.findViewById(R.id.item_folder_image_count);
            cardView = itemView.findViewById(R.id.item_folder_cardview);
            this.imagePickActivity = imagePickActivity;
        }
    }


}
