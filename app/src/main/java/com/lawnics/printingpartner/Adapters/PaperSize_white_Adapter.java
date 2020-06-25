package com.lawnics.printingpartner.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lawnics.printingpartner.Interfaces.OnItemCheck;
import com.lawnics.printingpartner.Model.PaperSize_wh_Model;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PaperSize_white_Adapter extends RecyclerView.Adapter<PaperSize_white_Adapter.ViewHolder> {

    private Context mContext;
    private List<PaperSize_wh_Model> itemList;
    public ArrayList<String> pos;
    PaperSize_white_Adapter adapter;
    OnItemCheck callback;
    Boolean selectAll=false;
    Boolean selectAllUncheck = false;
    public PaperSize_white_Adapter(Context mContext, List<PaperSize_wh_Model> itemList, OnItemCheck checkListener)
    {
        this.mContext = mContext;
        this.itemList = itemList;
        this.pos = new ArrayList<>();
        this.callback = checkListener;
    }
    public PaperSize_white_Adapter(boolean selectAll,boolean selectAllUncheck){
        this.selectAll = selectAll;
        this.selectAllUncheck = selectAllUncheck;
    }
    public PaperSize_white_Adapter(PaperSize_white_Adapter adapter){
        this.adapter = adapter;
        adapter.pos = new ArrayList<>();
    }
    @NonNull
    @Override
    public PaperSize_white_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_paper_size_white, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperSize_white_Adapter.ViewHolder holder, int position) {

        PaperSize_wh_Model single_bid_item = itemList.get(position);
        holder.paper_type.setText(single_bid_item.getPaperType().split(":",2)[0]);
        if(single_bid_item.getPaperType().split(":",2)[1].equals("unavailable")){
            holder.chk_paper_size_wh.setChecked(false);
        }else{
            pos.add(String.valueOf(position));
            holder.chk_paper_size_wh.setChecked(true);
        }
        holder.description.setText(single_bid_item.getDescriptions());
        //holder.prod_img.setImageResource(R.drawable.ic_a4_paper_icon);
        holder.chk_paper_size_wh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                try{
                    if(buttonView.isChecked()){
                        if(pos.contains(String.valueOf(position))){
                            callback.removeUnCheck(String.valueOf(position));
                            callback.addCheck(String.valueOf(position));
                        }else{
                            callback.removeUnCheck(String.valueOf(position));
                            pos.add(String.valueOf(position));
                            callback.addCheck(String.valueOf(position));
                        }
                    }
                }catch (Exception e){}
                if(!buttonView.isChecked()){
                    if(pos.contains(String.valueOf(position))){
                        pos.remove(String.valueOf(position));
                        callback.removeCheck(String.valueOf(position));
                    }
                    callback.addUncheck(String.valueOf(position));
                }
            }
        });
        if(selectAll){
            holder.chk_paper_size_wh.setChecked(true);
        }else if(selectAllUncheck){
            holder.chk_paper_size_wh.setChecked(false);
        }
    }
    /*public ArrayList<String> checkListener(){
        return adapter.pos;
    }*/
    public void selectAllv(){
        selectAll = true;
        selectAllUncheck = false;
        notifyDataSetChanged();
    }
    public void setSelectAllUncheck(){
        selectAll = false;
        selectAllUncheck = true;
        notifyDataSetChanged();
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
