package com.lawnics.printingpartner.Model;

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
