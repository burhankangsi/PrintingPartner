package com.lawnics.printingpartner.Model;

public class DetailActivityModel {

    public DetailActivityModel(String doc_name) {
        this.document_name = doc_name;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getDoc_image() {
        return doc_image;
    }

    public void setDoc_image(String doc_image) {
        this.doc_image = doc_image;
    }

    public String getOrd_no() {
        return ord_no;
    }

    public void setOrd_no(String ord_no) {
        this.ord_no = ord_no;
    }


    public String getPaper_color() {
        return paper_color;
    }

    public void setPaper_color(String paper_color) {
        this.paper_color = paper_color;
    }

    public String getPaper_type() {
        return paper_type;
    }

    public void setPaper_type(String paper_type) {
        this.paper_type = paper_type;
    }

    public String getNo_of_pages() {
        return no_of_pages;
    }

    public void setNo_of_pages(String no_of_pages) {
        this.no_of_pages = no_of_pages;
    }

    public String getGSM() {
        return GSM;
    }

    public void setGSM(String GSM) {
        this.GSM = GSM;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getPaper_size() {
        return paper_size;
    }

    public void setPaper_size(String paper_size) {
        this.paper_size = paper_size;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String document_name;
    String doc_image;

    public String getNo_of_docs() {
        return no_of_docs;
    }

    public void setNo_of_docs(String no_of_docs) {
        this.no_of_docs = no_of_docs;
    }

    String no_of_docs;

    public DetailActivityModel(String document_name, String doc_image, String ord_no, String fileName, String filePath, String paper_color, String paper_type, String no_of_docs
                               ,String no_of_pages, String GSM, String orientation, String paper_size, String credits, String time) {
        this.document_name = document_name;
        this.doc_image = doc_image;
        this.ord_no = ord_no;
        this.fileName = fileName;
        this.filePath = filePath;
        this.no_of_docs = no_of_docs;
        this.paper_color = paper_color;
        this.paper_type = paper_type;
        this.no_of_pages = no_of_pages;
        this.GSM = GSM;
        this.orientation = orientation;
        this.paper_size = paper_size;
        this.credits = credits;
        this.time = time;
    }

    String ord_no;
    String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    String filePath;
    String paper_color;
    String paper_type;
    String no_of_pages;
    String GSM;
    String orientation;
    String paper_size;
    String credits;
    String time;

}
