package com.lawnics.printingpartner.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.Feature;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lawnics.printingpartner.DetailsActivity;
import com.lawnics.printingpartner.ManagementActivity;
import com.lawnics.printingpartner.Model.RecentOrdModel;
import com.lawnics.printingpartner.PaperProduct;
import com.lawnics.printingpartner.Print_Paper;
import com.lawnics.printingpartner.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.camera.core.CameraX.getContext;

public class RecentOrdFragAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<RecentOrdModel> itemList;
    ProgressDialog dialog;
    public RecentOrdFragAdapter(Context mContext, List<RecentOrdModel> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
        this.dialog = new ProgressDialog(this.mContext);
    }

    @Override
    public int getItemViewType(int position) {
        RecentOrdModel temp = itemList.get(position);
        if(temp.getStatus().equals("Confirmed"))
            return 1;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType==1) {
            view = layoutInflater.inflate(R.layout.item_card_rec_orders_accepted, parent, false);
            return new ViewHolder2(view);
        }else{
            view = layoutInflater.inflate(R.layout.item_card_recent_orders,parent,false);
            return new ViewHolder1(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //viewHolder1.recent_day.setText(re.getDate_recent());
        //viewHolder1.recent_time.setText(re.getTime());
        //viewHolder1.date_card.setVisibility(View.VISIBLE);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        sortDate();
        RecentOrdModel single_bid_item = itemList.get(position);

        switch(holder.getItemViewType()){
            case 0:
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                if (position != 0) {
                    processDate(viewHolder1.recent_day, viewHolder1.recent_time, viewHolder1.date_card, single_bid_item.getTime(), single_bid_item.getDate_recent()
                            , this.itemList.get(position - 1).getDate_recent(), this.itemList.get(position - 1).getTime()
                            , false);
                } else {
                    processDate(viewHolder1.recent_day, viewHolder1.recent_time, viewHolder1.date_card, single_bid_item.getTime(), single_bid_item.getDate_recent()
                            , null, null
                            , true);
                }
                viewHolder1.cust_name.setText(single_bid_item.getCustName());
                viewHolder1.order_no.setText(single_bid_item.getOrd_no().split(" ")[1]);
                viewHolder1.location.setText(single_bid_item.getLocation());
                viewHolder1.doc_no.setText(single_bid_item.getNo_docs()+" docs, ");
                viewHolder1.pages.setText(single_bid_item.getNo_pages()+" pages");
                viewHolder1.price.setText(single_bid_item.getItemPrice());
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                String[] timeS = single_bid_item.getTime().split("_");
                Date _24HourDt = null;
                try {
                    _24HourDt = _24HourSDF.parse(timeS[0] + ":" + timeS[1]);
                    viewHolder1.time.setText(_12HourSDF.format(_24HourDt));
                    viewHolder1.st_time.setText(_12HourSDF.format(_24HourDt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(single_bid_item.getCust_image()).into(((ViewHolder1) holder).cust_img);

                viewHolder1.details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        mContext.startActivity(intent);
                    }
                });

                viewHolder1.accept_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child(single_bid_item.getBuyerId().trim()).child("orders").child(single_bid_item.getDate_recent().trim()).child(single_bid_item.getTime().trim()).child("status").setValue("Confirmed");
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child("Printing_partner").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Orders").child(single_bid_item.getBuyerId()).child(single_bid_item.getDate_recent()).child(single_bid_item.getTime())
                                .child(single_bid_item.getOrd_no()).child("status").setValue("Confirmed");
                        String root = mContext.getExternalFilesDir(null).toString();
                        File temp = new File(root + "/" + single_bid_item.getOrd_no());
                        if (temp.exists()) {
                            ArrayList<String> bitmapPaths = new ArrayList<>();
                            File[] files = temp.listFiles();
                            Arrays.sort(files, new Comparator<File>() {
                                @Override
                                public int compare(File o1, File o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
                            for (int i = 0; i < files.length; i++) {

                                File imageFile = new File(temp, files[i].getName());
                                if (!files[i].getName().equals(single_bid_item.getOrd_no() + ".pdf"))
                                    bitmapPaths.add(imageFile.getAbsolutePath());
                            }
                            Intent intent = new Intent(mContext, Print_Paper.class);
                            intent.putStringArrayListExtra("images", (ArrayList<String>) bitmapPaths);
                            intent.putExtra("Copies", single_bid_item.getCopies());
                            intent.putExtra("Paper Size", single_bid_item.getPaper_size());
                            intent.putExtra("Orientation", single_bid_item.getOrientation());
                            intent.putExtra("Print Side", single_bid_item.getPaper_side());
                            intent.putExtra("Print Color", single_bid_item.getPrint_color());
                            intent.putExtra("Page Color", single_bid_item.getPaper_color());
                            intent.putExtra("Customer Name", single_bid_item.getCustName());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ((Activity) mContext).startActivity(intent);
                        } else {
                            new RetrievePDFStream(single_bid_item.getCustName(), single_bid_item.getCopies(), single_bid_item.getPaper_size()
                                    , single_bid_item.getOrientation(), single_bid_item.getPaper_side()
                                    , single_bid_item.getPrint_color(), single_bid_item.getPaper_color(), single_bid_item.getOrd_no()).execute(single_bid_item.getFile_path());
                            //new RetrievePDFStream(single_bid_item.getOrd_no()).execute(single_bid_item.getFile_path());
                        }
                        //Intent intent = new Intent(mContext, RecentOrdersFragCompletedAdapter.class);
                        //mContext.startActivity(intent);
                        // pos = position;

                    }
                });
                viewHolder1.decline_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child(single_bid_item.getBuyerId().trim()).child("orders").child(single_bid_item.getDate_recent().trim()).child(single_bid_item.getTime().trim()).child("status").setValue("Rejected");
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child("Printing_partner").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Orders").child(single_bid_item.getBuyerId()).child(single_bid_item.getDate_recent()).child(single_bid_item.getTime())
                                .child(single_bid_item.getOrd_no()).child("status").setValue("Rejected");
                        itemList.remove(position);

                    }
                });
                break;
            case 1:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                if (position != 0) {
                    processDate(viewHolder2.recent_day, viewHolder2.recent_time, viewHolder2.date_card, single_bid_item.getTime(), single_bid_item.getDate_recent()
                            , this.itemList.get(position - 1).getDate_recent(), this.itemList.get(position - 1).getTime()
                            , false);
                } else {
                    processDate(viewHolder2.recent_day, viewHolder2.recent_time, viewHolder2.date_card, single_bid_item.getTime(), single_bid_item.getDate_recent()
                            , null, null
                            , true);
                }
                viewHolder2.cust_name.setText(single_bid_item.getCustName());
                viewHolder2.order_no.setText(single_bid_item.getOrd_no().split(" ")[1]);
                viewHolder2.location.setText(single_bid_item.getLocation());
                viewHolder2.doc_no.setText(single_bid_item.getNo_docs()+" docs, ");
                viewHolder2.pages.setText(single_bid_item.getNo_pages()+" pages");
                viewHolder2.price.setText(single_bid_item.getItemPrice());
                SimpleDateFormat _24HourSDF1 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF1 = new SimpleDateFormat("hh:mm a");
                String[] timeS1 = single_bid_item.getTime().split("_");
                Date _24HourDt1 = null;
                try {
                    _24HourDt1 = _24HourSDF1.parse(timeS1[0] + ":" + timeS1[1]);
                    viewHolder2.time.setText(_12HourSDF1.format(_24HourDt1));
                    viewHolder2.st_time.setText(_12HourSDF1.format(_24HourDt1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(single_bid_item.getCust_image()).into(((ViewHolder2) holder).cust_img);
                viewHolder2.complete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog= new Dialog(mContext);
                        dialog.setCancelable(false);

                        dialog.setContentView(R.layout.confirm_dialog);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                        dialog.show();
                        dialog.findViewById(R.id.confirm_yes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Printing_partner").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Orders").child(single_bid_item.getBuyerId()).child(single_bid_item.getDate_recent())
                                        .child(single_bid_item.getTime()).child(single_bid_item.getOrd_no()).child("status").setValue("Completed");
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                db.child("Printing_partner").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Orders").child(single_bid_item.getBuyerId()).child(single_bid_item.getDate_recent()).child(single_bid_item.getTime())
                                        .child(single_bid_item.getOrd_no()).child("status").setValue("Completed");
                                itemList.remove(position);
                                dialog.dismiss();
                            }
                        });
                        dialog.findViewById(R.id.confirm_no).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                break;
            default:
                break;
        }
        /*ViewHolder1 viewHolder1 = (ViewHolder1) holder;
        RecentOrdModel single_bid_item = itemList.get(position);
        viewHolder1.cust_name.setText(single_bid_item.getFileName());
        viewHolder1.order_no.setText(single_bid_item.getOrd_no());
        viewHolder1.location.setText(single_bid_item.getLocation());
        viewHolder1.doc_no.setText(single_bid_item.getNo_docs());
        viewHolder1.pages.setText(single_bid_item.getNo_pages());
        viewHolder1.price.setText(single_bid_item.getItemPrice());
        viewHolder1.time.setText(single_bid_item.getTime());

        Picasso.get().load(single_bid_item.getCust_image()).into(viewHolder1.cust_img);

        viewHolder1.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        });

        viewHolder1.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new RetrievePDFStream(single_bid_item.getFilename()).execute(single_bid_item.getFile_path());

                //Intent intent = new Intent(mContext, RecentOrdersFragCompletedAdapter.class);
                //mContext.startActivity(intent);


            }
        });

        viewHolder1.decline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, DetailsActivity.class);
//                mContext.startActivity(intent);
            }
        });*/
    }
    public void whenCompleted(List<RecentOrdModel> itemList){
        this.itemList = itemList;
        notifyDataSetChanged();
    }
    public void sortDate(){
        HashMap<Integer,Date> dateHashMap = new HashMap<>();
        LinkedHashMap<Integer,Date> linkedHashMap = new LinkedHashMap<>();
        ArrayList<Map.Entry<Integer,Date>> arr = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for(int i = 0;i<itemList.size();i++){
            String[] dateS = itemList.get(i).getDate_recent().split("_",3);
            String[] timeS = itemList.get(i).getTime().split("_");
            if(Integer.parseInt(dateS[1])<10){
                dateS[1] = "0"+dateS[1];
            }
            if(Integer.parseInt(dateS[2])<10){
                dateS[2] = "0"+dateS[2];
            }
            try {
                Date date = dateFormat.parse(dateS[2]+"/"+dateS[1]+"/"+dateS[0]+" "+timeS[0]+":"+timeS[1]);
                dateHashMap.put(i,date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for(Map.Entry<Integer,Date> e : dateHashMap.entrySet()){
            arr.add(e);
        }
        Comparator<Map.Entry<Integer,Date>> comparator = new Comparator<Map.Entry<Integer, Date>>() {
            @Override
            public int compare(Map.Entry<Integer, Date> o1, Map.Entry<Integer, Date> o2) {
                Date d1 = o1.getValue();
                Date d2 = o2.getValue();
                return d1.compareTo(d2);
            }
        };
        Collections.sort(arr,comparator);
        for(Map.Entry<Integer,Date> e:arr){
            linkedHashMap.put(e.getKey(),e.getValue());
        }
        ArrayList<RecentOrdModel> temp = new ArrayList<>();
        for(int i =0;i<itemList.size();i++){
            temp.add(itemList.get(i));
        }
        itemList.clear();
        for(Map.Entry<Integer,Date> sorted : linkedHashMap.entrySet()){
            itemList.add(temp.get(sorted.getKey()));
        }
    }
    public void processDate(@NonNull TextView date, TextView time, CardView cardView, String timeStr, String dateStr, String date_cmp, String time_cmp,@NonNull Boolean isItemFirst){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        if(isItemFirst){
            Date date1 = null;
            try {
                String[] dateS = dateStr.split("_",3);
                if(Integer.parseInt(dateS[1])<10){
                    dateS[1] = "0"+dateS[1];
                }
                if(Integer.parseInt(dateS[2])<10){
                    dateS[2] = "0"+dateS[2];
                }
                date1 = dateFormat.parse(dateS[2]+"/"+dateS[1]+"/"+dateS[0]);
                if(DateUtils.isToday(date1.getTime())) {
                    date.setText("Today");
                }
                else if(DateUtils.isToday(date1.getTime() + DateUtils.DAY_IN_MILLIS)) {
                    date.setText("Yesterday");
                }
                else{
                    date.setText(dateS[2]+"-"+dateS[1]+"-"+dateS[0]);
                }
                String[] timeS = timeStr.split("_");
                Date _24HourDt = _24HourSDF.parse(timeS[0]+":"+timeS[1]);
                if(Integer.parseInt(_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[0])<=30){
                    time.setText(_12HourSDF.format(_24HourDt).split(":")[0]+":00"+" to "+
                            _12HourSDF.format(_24HourDt).split(":")[0]+":30 "+_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[1]);
                }else{
                    time.setText(_12HourSDF.format(_24HourDt).split(":")[0]+":30"+" to "+
                            _12HourSDF.format(_24HourDt).split(":")[0]+":59 "+_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[1]);
                }
                cardView.setVisibility(View.VISIBLE);
            } catch (ParseException e) {
                e.printStackTrace();
                cardView.setVisibility(View.GONE);
            }
        }else{
            if(!dateStr.equals(date_cmp)){
                Date date1 = null;
                try {
                    String[] dateS = dateStr.split("_",3);
                    if(Integer.parseInt(dateS[1])<10){
                        dateS[1] = "0"+dateS[1];
                    }
                    if(Integer.parseInt(dateS[2])<10){
                        dateS[2] = "0"+dateS[2];
                    }
                    date1 = dateFormat.parse(dateS[2]+"/"+dateS[1]+"/"+dateS[0]);
                    if(DateUtils.isToday(date1.getTime())) {
                        date.setText("Today");
                    }
                    else if(DateUtils.isToday(date1.getTime() + DateUtils.DAY_IN_MILLIS)) {
                        date.setText("Yesterday");
                    }
                    else{
                        date.setText(dateS[2]+"-"+dateS[1]+"-"+dateS[0]);
                    }
                    String[] timeS = timeStr.split("_");
                    Date _24HourDt = _24HourSDF.parse(timeS[0]+":"+timeS[1]);
                    if(Integer.parseInt(_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[0])<=30){
                        time.setText(_12HourSDF.format(_24HourDt).split(":")[0]+":00"+" to "+
                                _12HourSDF.format(_24HourDt).split(":")[0]+":30 "+_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[1]);
                    }else{
                        time.setText(_12HourSDF.format(_24HourDt).split(":")[0]+":31"+" to "+
                                _12HourSDF.format(_24HourDt).split(":")[0]+":59 "+_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[1]);
                    }
                    cardView.setVisibility(View.VISIBLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                    cardView.setVisibility(View.GONE);
                }
            }else {
                Date time1 = null,time2 = null,date1 = null;
                String[] timeS = timeStr.split("_");
                String[] timeS_Cmp = time_cmp.split("_");
                try {
                    Date _24HourDt = _24HourSDF.parse(timeS[0]+":"+timeS[1]);
                    Date _24HourDt_cmp = _24HourSDF.parse(timeS_Cmp[0]+":"+timeS_Cmp[1]);
                    time1 = _12HourSDF.parse(_12HourSDF.format(_24HourDt));
                    time2 = _12HourSDF.parse(_12HourSDF.format(_24HourDt_cmp));
                    long difference = time2.getTime() - time1.getTime();
                    int days = (int) (difference / (1000*60*60*24));
                    int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    int min = (int) (((difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60))+ hours*60);
                    if(Math.abs(min)>30){
                        String[] dateS = dateStr.split("_",3);
                        if(Integer.parseInt(dateS[1])<10){
                            dateS[1] = "0"+dateS[1];
                        }
                        if(Integer.parseInt(dateS[2])<10){
                            dateS[2] = "0"+dateS[2];
                        }
                        date1 = dateFormat.parse(dateS[2]+"/"+dateS[1]+"/"+dateS[0]);
                        if(DateUtils.isToday(date1.getTime())) {
                            date.setText("Today");
                        }
                        else if(DateUtils.isToday(date1.getTime() + DateUtils.DAY_IN_MILLIS)) {
                            date.setText("Yesterday");
                        }
                        else{
                            date.setText(dateS[2]+"-"+dateS[1]+"-"+dateS[0]);
                        }
                        if(Integer.parseInt(_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[0])<=30){
                            time.setText(_12HourSDF.format(_24HourDt).split(":")[0]+":00"+" to "+
                                    _12HourSDF.format(_24HourDt).split(":")[0]+":30 "+_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[1]);
                        }else{
                            time.setText(_12HourSDF.format(_24HourDt).split(":")[0]+":31"+" to "+
                                    _12HourSDF.format(_24HourDt).split(":")[0]+":59 "+_12HourSDF.format(_24HourDt).split(":")[1].split(" ")[1]);
                        }
                        cardView.setVisibility(View.VISIBLE);
                    }else{
                        cardView.setVisibility(View.GONE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    cardView.setVisibility(View.GONE);
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView cust_name, order_no, location, doc_no, pages, time, price, details,st_time;
        public CircleImageView cust_img;
        public AppCompatButton accept_btn, decline_btn;
        public CardView date_card;
        public TextView recent_time,recent_day;
        public ViewHolder1(@NonNull View itemView) {
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
            date_card = itemView.findViewById(R.id.date_recent_card);
            accept_btn = itemView.findViewById(R.id.btn_cv_rec_orders_btn1);
            decline_btn = itemView.findViewById(R.id.btn_cv_rec_orders_btn2);
            recent_time = itemView.findViewById(R.id.recent_card_time);
            recent_day = itemView.findViewById(R.id.recent_card_day);
            st_time = itemView.findViewById(R.id.tv_status_time_cv_recent_ord);
        }

    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView cust_name, order_no, location, doc_no, pages, time, price, details,st_time;
        public CircleImageView cust_img;
        public AppCompatButton complete_btn, decline_btn;
        public CardView date_card;
        public TextView recent_time,recent_day;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            cust_name = itemView.findViewById(R.id.tv_cust_name_cv_rec_ord_accepted);
            order_no = itemView.findViewById(R.id.tv_order_no_cv_rec_ord_val_accepted);
            location = itemView.findViewById(R.id.tv_location_cv_rec_ord_accepted);
            doc_no = itemView.findViewById(R.id.tv_docs_cv_rec_ord_accepted);
            pages = itemView.findViewById(R.id.tv_pages_cv_rec_ord_accepted);
            time = itemView.findViewById(R.id.tv_time_cv_rec_ord_accepted);
            price = itemView.findViewById(R.id.tv_rec_cv_rec_ord_price_accepted);
            details = itemView.findViewById(R.id.tv_details_cv_rec_ord_accepted);
            cust_img = itemView.findViewById(R.id.circular_img_view_cv_recent_orders_accepted);
            complete_btn = itemView.findViewById(R.id.btn_cv_rec_orders_complete);
            decline_btn = itemView.findViewById(R.id.btn_cv_rec_orders_btn2);
            st_time = itemView.findViewById(R.id.tv_status_time_cv_recent_ord_accepted);
            recent_time = itemView.findViewById(R.id.recent_card_time_accepted);
            recent_day = itemView.findViewById(R.id.recent_card_day_accepted);
            date_card = itemView.findViewById(R.id.date_recent_card_accepted);
        }
    }


    class RetrievePDFStream extends AsyncTask<String, Void, List<String>>
    {
        String filename;
        String copies;String paper_size;String orientation;String print_side;String print_color;String page_color;String cust_name;
        /*public RetrievePDFStream(String filename)
        {
            this.filename = filename;
        }*/
        public RetrievePDFStream(String cust_name,String copies,String paper_size,String orientation,String print_side,String print_color,String page_color,String filename){
            this.copies = copies;
            this.orientation = orientation;
            this.page_color = page_color;
            this.paper_size = paper_size;
            this.print_color = print_color;
            this.print_side = print_side;
            this.cust_name = cust_name;
            this.filename = filename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading...");
            dialog.show();
        }

        @Override
        protected List<String> doInBackground(String... strings) {

            InputStream inputStream = null;
            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String root = mContext.getExternalFilesDir(null).toString();
                File temp = new File(root + "/"+filename);
                if(!temp.exists()){
                    temp.mkdirs();
                }
                File outputFile = new File(temp,filename+".pdf");
                FileOutputStream fos = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                inputStream.close();
                return RenderBitmapsFromPDF(mContext, Uri.fromFile(outputFile),filename,this.cust_name,this.copies
                ,this.paper_size,this.orientation,this.print_side,this.print_color,this.page_color,dialog);
            }
            catch (IOException e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
        }
    }


    public List<String> RenderBitmapsFromPDF(Context context,Uri path,String filename,String cust_name,String copies,String paper_size,String orientation,String print_side,String print_color,String page_color,Dialog dialog){
        List<String> bmpPaths = new ArrayList<>();
        try {
            System.out.println(getPath(context,path));
            File file1 = new File(getPath(context,path));
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file1,ParcelFileDescriptor.MODE_READ_WRITE));

            final int pageCount = renderer.getPageCount();
            int x =1;
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);
                Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                        Bitmap.Config.ARGB_8888);
                page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                String root = mContext.getExternalFilesDir(null).toString();
                File temp = new File(root + "/"+filename);
                if(!temp.exists()){
                    temp.mkdirs();
                }
                File file = new File(temp,String.valueOf(x)+"_rendered_"+System.currentTimeMillis()+".jpg");
                x++;
                try{

                    OutputStream stream;

                    stream = new FileOutputStream(file);

                    mBitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

                    stream.flush();

                    stream.close();

                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                Uri mImageUri = Uri.parse(file.getAbsolutePath());

                bmpPaths.add(file.getAbsolutePath());
                page.close();
            }
            Intent intent = new Intent(mContext, Print_Paper.class);
            intent.putStringArrayListExtra("images", (ArrayList<String>) bmpPaths);
            intent.putExtra("Copies", copies);
            intent.putExtra("Paper Size",paper_size);
            intent.putExtra("Orientation",orientation);
            intent.putExtra("Print Side",print_side);
            intent.putExtra("Print Color",print_color);
            intent.putExtra("Page Color",page_color);
            intent.putExtra("Customer Name",cust_name);
            ((Activity)mContext).startActivity(intent);
            renderer.close();
            return bmpPaths;
        } catch (IOException e) {
            return null;
        }
    }

    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
