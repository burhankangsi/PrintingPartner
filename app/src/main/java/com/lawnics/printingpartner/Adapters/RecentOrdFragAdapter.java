package com.lawnics.printingpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.DetailsActivity;
import com.lawnics.printingpartner.ManagementActivity;
import com.lawnics.printingpartner.Model.RecentOrdModel;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentOrdFragAdapter extends RecyclerView.Adapter <RecentOrdFragAdapter.ViewHolder> {

    private Context mContext;
    private List<RecentOrdModel> itemList;
    public RecentOrdFragAdapter(Context mContext, List<RecentOrdModel> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_recent_orders, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RecentOrdModel single_bid_item = itemList.get(position);
        holder.cust_name.setText(single_bid_item.getFileName());
        holder.order_no.setText(single_bid_item.getOrd_no());
        holder.location.setText(single_bid_item.getLocation());
        holder.doc_no.setText(single_bid_item.getNo_docs());
        holder.pages.setText(single_bid_item.getNo_pages());
        holder.price.setText(single_bid_item.getItemPrice());
        holder.time.setText(single_bid_item.getTime());

        Picasso.get().load(single_bid_item.getCust_image()).into(holder.cust_img);

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, DetailsActivity.class);
//                mContext.startActivity(intent);

            }
        });

        holder.decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, DetailsActivity.class);
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cust_name, order_no, location, doc_no, pages, time, price, details;
        public CircleImageView cust_img;
        public AppCompatButton accept_btn, decline_btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cust_name = itemView.findViewById(R.id.tv_cust_name_cv_rec_ord);
            order_no = itemView.findViewById(R.id.tv_order_no_cv_rec_ord_val);
            location = itemView.findViewById(R.id.tv_location_cv_rec_ord);
            doc_no = itemView.findViewById(R.id.tv_docs_cv_rec_ord);
            pages = itemView.findViewById(R.id.tv_pages_cv_rec_ord);
            time = itemView.findViewById(R.id.tv_time_cv_rec_ord);
            price = itemView.findViewById(R.id.tv_rec_cv_rec_ord_price);
            details = itemView.findViewById(R.id.tv_details_cv_rec_ord);
            cust_img = itemView.findViewById(R.id.circular_img_view_cv_recent_orders);

            accept_btn = itemView.findViewById(R.id.btn_cv_rec_orders_btn1);
            decline_btn = itemView.findViewById(R.id.btn_cv_rec_orders_btn2);

        }
    }
}
