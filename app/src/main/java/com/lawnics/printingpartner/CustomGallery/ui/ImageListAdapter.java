package com.lawnics.printingpartner.CustomGallery.ui;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lawnics.printingpartner.CustomGallery.listener.ImageListClickListener;
import com.lawnics.printingpartner.R;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    List<Integer> selects = new ArrayList<>();
    Context context;
    ImagePickActivity imagePickActivity;
    private List<Uri> images = new ArrayList<>();
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();
    private int maxCount;
    private ImageListClickListener listClickListener;

    protected ImageListAdapter(int maxCount, ImageListClickListener listClickListener, Context context) {
        this.maxCount = maxCount;
        this.listClickListener = listClickListener;
        this.context = context;
        this.imagePickActivity = (ImagePickActivity) context;
    }

    public void setImages(List<Uri> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        try {
            Glide.with(holder.itemView).load(images.get(position)).into(holder.ivImage);
            if (selectedPositions.get(position)) holder.llCheck.setVisibility(View.VISIBLE);
            else holder.llCheck.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(v -> {
                imagePickActivity.toggleFloatButtons(false);
                if (selectedPositions.get(position)) {
                    selectedPositions.put(position, false);
                    int pos = selects.indexOf(position);
                    selects.remove(pos);
                    holder.llCheck.setVisibility(View.GONE);
                    listClickListener.onImageClicked(getSelectedItemCount());
                } else {
                    if (maxCount == 1) {
                        int index = selectedPositions.indexOfValue(true);
                        if (index != -1) {
                            int originalPosition = selectedPositions.keyAt(index);
                            selectedPositions.put(originalPosition, false);
                            int pos = selects.indexOf(position);
                            selects.remove(pos);
                            notifyItemChanged(originalPosition);
                        }
                    } else if (maxCount == getSelectedItemCount()) return;

                    selectedPositions.put(position, true);
                    selects.add(position);
                    listClickListener.onImageClicked(getSelectedItemCount());
                    holder.llCheck.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public List<Uri> getSelectedUris() {
        List<Uri> selected = new ArrayList<>();
        /*for (int i = 0; i < selectedPositions.size(); i++) {
            if (selectedPositions.valueAt(i))
                selected.add(images.get(selectedPositions.keyAt(i)));
        }*/
        for (int i = 0; i < selects.size(); i++) {
            selected.add(images.get(selects.get(i)));
        }
        return selected;
    }

    private int getSelectedItemCount() {
        int count = 0;
        for (int i = 0; i < selectedPositions.size(); i++) {
            if (selectedPositions.valueAt(i)) count++;
        }
        return count;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private LinearLayout llCheck;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            llCheck = itemView.findViewById(R.id.ll_check);
        }
    }

}
