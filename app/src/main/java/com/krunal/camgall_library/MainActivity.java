package com.krunal.camgall_library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.krunal.camgal_libs.Intermediate.PickerListener;
import com.krunal.camgal_libs.Picker.PickerBottomSheetFragment;
import com.krunal.camgal_libs.Utils.Constant;
import com.krunal.camgal_libs.Utils.Utility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PickerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLibs(View view) {

        PickerBottomSheetFragment blankFragment =
                new PickerBottomSheetFragment();// KB,MB
        blankFragment.show(getSupportFragmentManager(), blankFragment.getTag());

    }

    @Override
    public void onResultPicker(Intent data, ArrayList<String> listSaved) {
        Utility.displayToast(MainActivity.this, "Success: " + listSaved.size());

    }
}