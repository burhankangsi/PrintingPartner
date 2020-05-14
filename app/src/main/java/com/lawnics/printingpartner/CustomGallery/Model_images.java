package com.lawnics.printingpartner.CustomGallery;

import android.net.Uri;

import java.util.ArrayList;


public class Model_images {
    String str_folder;
    ArrayList<String> al_imagepath;
    ArrayList<Uri> al_imageuri;

    public String getStr_folder() {
        return str_folder;
    }

    public void setStr_folder(String str_folder) {
        this.str_folder = str_folder;
    }

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }

    public ArrayList<Uri> getAl_imageuri() {
        return al_imageuri;
    }

    public void setAl_imageuri(ArrayList<Uri> al_imageuri) {
        this.al_imageuri = al_imageuri;
    }
}
