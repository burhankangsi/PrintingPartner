package com.lawnics.printingpartner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.DetailsActivity;
import com.lawnics.printingpartner.Model.DetailActivityModel;
import com.lawnics.printingpartner.Model.RecentOrdModel;
import com.lawnics.printingpartner.OrderActivity;
import com.lawnics.printingpartner.PaperProduct;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailsActivityAdapter extends RecyclerView.Adapter<DetailsActivityAdapter.ViewHolder> {

    private Context mContext;
    private List<DetailActivityModel> itemList;
    public DetailsActivityAdapter(Context mContext, List<DetailActivityModel> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public DetailsActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_details_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsActivityAdapter.ViewHolder holder, int position) {

        DetailActivityModel single_bid_item = itemList.get(position);
        holder.doc_name.setText(single_bid_item.getDocument_name());
//        holder.order_no.setText(single_bid_item.getOrd_no());
        holder.no_of_docs.setText(single_bid_item.getNo_of_docs());
        holder.pages.setText(single_bid_item.getNo_of_pages());
        holder.paper_size.setText(single_bid_item.getPaper_size());
        holder.paper_color.setText(single_bid_item.getPaper_color());
        holder.time.setText(single_bid_item.getTime());

        holder.paper_type.setText(single_bid_item.getPaper_type());
        holder.gsm.setText(single_bid_item.getGSM());
        holder.credits.setText(single_bid_item.getCredits());
        holder.orientation.setText(single_bid_item.getOrientation());

        Picasso.get().load(single_bid_item.getDoc_image()).into(holder.prod_img);

        holder.prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, OrderActivity.class);
                mContext.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView doc_name, order_no, paper_color, paper_type, pages, time, credits, gsm
                , orientation, paper_size, no_of_docs;
        public ImageView prod_img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            doc_name = itemView.findViewById(R.id.tv_docs_name_cv_details);
            order_no = itemView.findViewById(R.id.tv_order_no_cv_rec_ord);
            paper_size = itemView.findViewById(R.id.tv_page_size_cv_details);
            paper_color = itemView.findViewById(R.id.tv_page_color_cv_details);

            no_of_docs = itemView.findViewById(R.id.tv_docs_cv_details);
            pages = itemView.findViewById(R.id.tv_pages_cv_details);
            time = itemView.findViewById(R.id.tv_time_cv_details);
            credits = itemView.findViewById(R.id.tv_details_cv_credits);
            paper_type = itemView.findViewById(R.id.tv_page_type_cv_details);
            orientation = itemView.findViewById(R.id.tv_orientation_cv_details);
            gsm = itemView.findViewById(R.id.tv_gsm_val_cv_details);
            prod_img = itemView.findViewById(R.id.img_view_cv_details_activity);

        }
    }
}
