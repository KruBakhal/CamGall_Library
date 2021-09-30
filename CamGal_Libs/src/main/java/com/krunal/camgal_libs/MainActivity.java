package com.krunal.camgal_libs;

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

    public void onClickNext(View view) {
        Intent intent = new Intent().putExtra(Constant.FACING_MODE, 0)// active or inactive
                .putExtra(Constant.IMAGE_Selection_COUNT, 5)// active or inactive
                .putExtra(Constant.COMPRESS_STATUS, true)// active or inactive
                .putExtra(Constant.COMPRESSION_SIZE, Constant.DEFAULT_COMRESSION_SIZE)//
                .putExtra(Constant.COMPRESSION_SIZE_Type, "MB");

        PickerBottomSheetFragment blankFragment =
                new PickerBottomSheetFragment(intent);
        blankFragment.show(getSupportFragmentManager(), blankFragment.getTag());

    }

    @Override
    public void onResultPicker(Intent data, ArrayList<String> listSaved) {

        Utility.displayToast(this, "Success: " + listSaved.size());


    }
}