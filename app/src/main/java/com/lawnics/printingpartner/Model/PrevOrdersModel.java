package com.lawnics.printingpartner.Model;

public class PrevOrdersModel {

    String custName;
    String cust_image;
    String ord_no;
    String location;
    String no_pages;
    String no_docs;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public PrevOrdersModel(String custName) {
        this.custName = custName;
    }

    public PrevOrdersModel(String fileName, String cust_image, String ord_no, String loc, String no_pages, String no_docs
            , String price, String status, String time) {
        this.custName = fileName;
        this.cust_image = cust_image;
        this.ord_no = ord_no;
        this.location = loc;
        this.no_pages = no_pages;
        this.no_docs = no_docs;
        this.itemPrice = price;
        this.status = status;
        this.time = time;
    }

    public String getFileName() {
        return custName;
    }

    public void setFileName(String fileName) {
        this.custName = fileName;
    }

}
