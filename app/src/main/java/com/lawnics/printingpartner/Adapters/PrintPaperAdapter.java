package com.lawnics.printingpartner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.lawnics.printingpartner.Model.PaperProductModel;
import com.lawnics.printingpartner.Model.Print_Paper_Model;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PrintPaperAdapter extends RecyclerView.Adapter<PrintPaperAdapter.ViewHolder> {

    private Context mContext;
    private List<Print_Paper_Model> itemList;

    public PrintPaperAdapter(Context mContext, List<Print_Paper_Model> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public PrintPaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_print_paper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Print_Paper_Model single_bid_item = itemList.get(position);
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
            prod_img = itemView.findViewById(R.id.img_cv_print_paper);

        }
    }
}
