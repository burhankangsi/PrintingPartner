package com.lawnics.printingpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.Model.ManagementModel;
import com.lawnics.printingpartner.Model.PaperQual_col_Model;
import com.lawnics.printingpartner.PaperQualityActivity_color;
import com.lawnics.printingpartner.PaperQualityActivity_white;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Management_Adapter extends RecyclerView.Adapter<Management_Adapter.ViewHolder> {

    private Context mContext;
    private List<ManagementModel> itemList;

    public Management_Adapter (Context mContext, List<ManagementModel> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public Management_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Management_Adapter.ViewHolder holder, int position) {

        ManagementModel single_bid_item = itemList.get(position);
        holder.paper_type.setText(single_bid_item.getPaper_type());
      //  holder.description.setText(single_bid_item.getDescription());

        Picasso.get().load(single_bid_item.getDoc_img()).into(holder.prod_img);
        Picasso.get().load(single_bid_item.getRight_arrow()).into(holder.menu_iv_manage);

        holder.menu_iv_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PaperQualityActivity_white.class);
                mContext.startActivity(intent);
            }
        });

        holder.paper_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PaperQualityActivity_color.class);
                mContext.startActivity(intent);
            }
        });

        holder.prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PaperQualityActivity_white.class);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView paper_type;
        public ImageView prod_img, menu_iv_manage;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            paper_type = itemView.findViewById(R.id.txt_paper_type_manage);
           // description = itemView.findViewById(R.id.txt_paper_qual_cv_descriptions_col);
            prod_img = itemView.findViewById(R.id.iv_cv_prod_img_manage);
            menu_iv_manage = itemView.findViewById(R.id.iv_arrow_cv_manage);


        }
    }
}
