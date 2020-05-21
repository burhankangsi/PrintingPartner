package com.lawnics.printingpartner.Adapters;

//public class Papersize_color_Adapter {
//}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.Model.PaperSize_col_Model;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Papersize_color_Adapter extends RecyclerView.Adapter<Papersize_color_Adapter.ViewHolder> {

    private Context mContext;
    private List<PaperSize_col_Model> itemList;

    public Papersize_color_Adapter(Context mContext, List<PaperSize_col_Model> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_paper_size_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PaperSize_col_Model single_bid_item = itemList.get(position);
        holder.paper_type.setText(single_bid_item.getPaperType());
        holder.description.setText(single_bid_item.getDescriptions());

        Picasso.get().load(single_bid_item.getDoc_img()).into(holder.prod_img);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView paper_type, description;
        public ImageView prod_img;
        public CheckBox chk_paper_size_wh;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            paper_type = itemView.findViewById(R.id.txt_paper_size_cv_a4);
            description = itemView.findViewById(R.id.txt_paper_size_cv_descriptions_wh);
            prod_img = itemView.findViewById(R.id.iv_cv_paper_size_wh);
            chk_paper_size_wh = itemView.findViewById(R.id.chk_box_cv_paper_size_wh);


        }
    }
}
