package com.krunal.camgal_libs.Intermediate;

import com.krunal.camgal_libs.Model.ImageModel;

public interface OnImageListner {
        void onImageClick(int position);

        void onAdd_Item(int finalCount, ImageModel imageModel);
    }