package com.lawnics.printingpartner.Model;

public class PaperSize_wh_Model {

    String paperType;

    public PaperSize_wh_Model(String paperType, String descriptions, String doc_img, String isChecked) {
        this.paperType = paperType;
        this.descriptions = descriptions;
        this.doc_img = doc_img;
        this.isChecked = isChecked;
    }

    String descriptions;

    public PaperSize_wh_Model(String s) {
        this.paperType = s;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getDoc_img() {
        return doc_img;
    }

    public void setDoc_img(String doc_img) {
        this.doc_img = doc_img;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    String doc_img;
    String isChecked;

}
