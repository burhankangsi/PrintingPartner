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

import com.lawnics.printingpartner.Model.PaperQual_wh_Model;
import com.lawnics.printingpartner.PaperSizeActivity_white;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Paper_quality_wh_adapter extends RecyclerView.Adapter<Paper_quality_wh_adapter.ViewHolder> {

    private Context mContext;
    private List<PaperQual_wh_Model> itemList;

    public Paper_quality_wh_adapter(Context mContext, List<PaperQual_wh_Model> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Paper_quality_wh_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_paper_qual_white, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Paper_quality_wh_adapter.ViewHolder holder, int position) {
        PaperQual_wh_Model single_bid_item = itemList.get(position);
        holder.paper_type.setText(single_bid_item.getGsm());
        holder.description.setText(single_bid_item.getDescription());

        holder.prod_img.setImageResource(R.drawable.ic_white_page_icon);
        Picasso.get().load(single_bid_item.getIv_right_arrow()).into(holder.menu_iv_paper_qual_wh);

        holder.menu_iv_paper_qual_wh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PaperSizeActivity_white.class);
                intent.putExtra("papertype",single_bid_item.getPaper_type());
                intent.putExtra("gsm",single_bid_item.getGsm());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.paper_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PaperSizeActivity_white.class);
                intent.putExtra("papertype",single_bid_item.getPaper_type());
                intent.putExtra("gsm",single_bid_item.getGsm());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, PaperSizeActivity_white.class);
//                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView paper_type, description;
        public ImageView prod_img, menu_iv_paper_qual_wh;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            paper_type = itemView.findViewById(R.id.txt_paper_qual_cv_a4);
            description = itemView.findViewById(R.id.txt_paper_qual_cv_descriptions_wh);
            prod_img = itemView.findViewById(R.id.iv_cv_paper_qual_wh);
            menu_iv_paper_qual_wh = itemView.findViewById(R.id.iv_arrow_cv_paper_qual_wh);


        }
    }
}
