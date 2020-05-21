package com.lawnics.printingpartner.Model;

public class ManagementModel {

    public ManagementModel(String s) {
        this.paper_type = s;
    }

    public String getPaper_type() {
        return paper_type;
    }

    public void setPaper_type(String paper_type) {
        this.paper_type = paper_type;
    }

    public String getDoc_img() {
        return doc_img;
    }

    public void setDoc_img(String doc_img) {
        this.doc_img = doc_img;
    }

    String paper_type;

    public String getRight_arrow() {
        return right_arrow;
    }

    public void setRight_arrow(String right_arrow) {
        this.right_arrow = right_arrow;
    }

    String right_arrow;

    public ManagementModel(String paper_type, String doc_img, String arrow) {
        this.paper_type = paper_type;
        this.doc_img = doc_img;
        this.right_arrow = arrow;
    }

    String doc_img;
}
