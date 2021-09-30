package com.krunal.camgal_libs.Intermediate;

import com.krunal.camgal_libs.Model.GalleryModel;

public interface onAddItemListner {
        void onAdd(GalleryModel galleryModel);

        void onRemove();

        void onUpdate(int finalCount, GalleryModel galleryModel);
    }