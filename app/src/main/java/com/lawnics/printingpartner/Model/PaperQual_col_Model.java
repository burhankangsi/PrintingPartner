package com.lawnics.printingpartner.Model;

public class PaperQual_col_Model {
    String gsm;

    public PaperQual_col_Model(String s) {
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

    public String getRight_arrow() {
        return right_arrow;
    }

    public void setRight_arrow(String right_arrow) {
        this.right_arrow = right_arrow;
    }

    String right_arrow;

    public PaperQual_col_Model(String gsm, String description, String doc_img, String arrow) {
        this.gsm = gsm;
        this.description = description;
        this.doc_img = doc_img;
        this.right_arrow = arrow;
    }

    String doc_img;

}
