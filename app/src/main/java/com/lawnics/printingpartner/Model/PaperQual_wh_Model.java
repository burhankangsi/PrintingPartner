package com.lawnics.printingpartner.Model;

public class PaperQual_wh_Model {
    String gsm;
    String paper_type;

    public String getPaper_type() {
        return paper_type;
    }

    public void setPaper_type(String paper_type) {
        this.paper_type = paper_type;
    }

    public PaperQual_wh_Model(){}
    public PaperQual_wh_Model(String s) {
        this.gsm = s;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDoc_img() {
        return doc_img;
    }

    public void setDoc_img(String doc_img) {
        this.doc_img = doc_img;
    }

    String description;

    public String getIv_right_arrow() {
        return iv_right_arrow;
    }

    public void setIv_right_arrow(String iv_right_arrow) {
        this.iv_right_arrow = iv_right_arrow;
    }

    String iv_right_arrow;

    public PaperQual_wh_Model(String gsm, String description, String doc_img, String arrow) {
        this.gsm = gsm;
        this.description = description;
        this.doc_img = doc_img;
        this.iv_right_arrow = arrow;
    }

    String doc_img;
}
