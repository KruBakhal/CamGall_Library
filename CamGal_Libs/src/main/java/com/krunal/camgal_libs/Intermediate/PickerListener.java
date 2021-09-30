package com.krunal.camgal_libs.Intermediate;

import android.content.Intent;

import java.util.ArrayList;

public interface PickerListener {

    public void onResultPicker(Intent data, ArrayList<String> listSaved);
}
