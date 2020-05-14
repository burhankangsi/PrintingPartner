package com.lawnics.printingpartner.CustomGallery.listener;

import java.util.List;

@FunctionalInterface
public interface MediaPickerListener {

    void onSelected(List<String> paths);
}
