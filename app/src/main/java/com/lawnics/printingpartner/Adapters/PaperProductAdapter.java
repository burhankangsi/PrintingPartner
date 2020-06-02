package com.lawnics.printingpartner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.PaperProductModel;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaperProductAdapter extends RecyclerView.Adapter<PaperProductAdapter.ViewHolder> {

    private Context mContext;
    private List<PaperProductModel> itemList;
    public PaperProductAdapter(Context mContext, List<PaperProductModel> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public PaperProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_paper_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperProductAdapter.ViewHolder holder, int position) {
        PaperProductModel single_bid_item = itemList.get(position);
        Picasso.get().load(single_bid_item.getImages()).into(holder.prod_img);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView prod_img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prod_img = itemView.findViewById(R.id.img_cv_paper_prod);

        }
    }
}
