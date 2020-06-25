package com.lawnics.printingpartner.Model;

import com.lawnics.printingpartner.RecentOrdersFrag;

public class RecentOrdModel {

    //Whatever the attributes in the item of recent order item comes here
    String custName;
    String cust_image;
    String ord_no;
    String location;
    String no_pages;
    String no_docs;
    String file_path;
    String filename;
    String date_recent;
    String gsm;
    String buyerId;
    String copies;
    String orientation;
    String paper_side;
    String paper_color;
    String print_color;
    String paper_size;

    public String getPaper_size() {
        return paper_size;
    }

    public void setPaper_size(String paper_size) {
        this.paper_size = paper_size;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getPaper_side() {
        return paper_side;
    }

    public void setPaper_side(String paper_side) {
        this.paper_side = paper_side;
    }

    public String getPaper_color() {
        return paper_color;
    }

    public void setPaper_color(String paper_color) {
        this.paper_color = paper_color;
    }

    public String getPrint_color() {
        return print_color;
    }

    public void setPrint_color(String print_color) {
        this.print_color = print_color;
    }

    public String getCopies() {
        return copies;
    }

    public void setCopies(String copies) {
        this.copies = copies;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public RecentOrdModel(){}
    public String getDate_recent() {
        return date_recent;
    }

    public void setDate_recent(String date_recent) {
        this.date_recent = date_recent;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStatus_time() {
        return status_time;
    }

    public void setStatus_time(String status_time) {
        this.status_time = status_time;
    }

    String status_time;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RecentOrdModel(String custName) {
        this.custName = custName;
    }

    public String getCust_image() {
        return cust_image;
    }

    public void setCust_image(String cust_image) {
        this.cust_image = cust_image;
    }

    public String getOrd_no() {
        return ord_no;
    }

    public void setOrd_no(String ord_no) {
        this.ord_no = ord_no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNo_pages() {
        return no_pages;
    }

    public void setNo_pages(String no_pages) {
        this.no_pages = no_pages;
    }

    public String getNo_docs() {
        return no_docs;
    }

    public void setNo_docs(String no_docs) {
        this.no_docs = no_docs;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String itemPrice;
    String status;
    String time;



    public RecentOrdModel(String fileName, String cust_image, String ord_no, String loc, String no_pages, String no_docs
    , String price, String status, String time, String s_time) {
        this.custName = fileName;
        this.cust_image = cust_image;
        this.ord_no = ord_no;
        this.location = loc;
        this.no_pages = no_pages;
        this.no_docs = no_docs;
        this.itemPrice = price;
        this.status = status;
        this.time = time;
        this.status_time = s_time;
    }

    public String getFileName() {
        return custName;
    }

    public void setFileName(String fileName) {
        this.custName = fileName;
    }

}
