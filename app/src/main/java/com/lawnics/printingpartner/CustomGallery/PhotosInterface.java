package com.lawnics.printingpartner.CustomGallery;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public interface PhotosInterface {
    List<List<Uri>> AllPhotos = new ArrayList<>();
    List<ArrayList<Model_images>> AlbumPhotos = new ArrayList<>();
}
