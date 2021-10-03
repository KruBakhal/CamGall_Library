package com.krunal.camgall_library;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.krunal.camgal_libs.Intermediate.PickerListener;
import com.krunal.camgal_libs.LibsCall.CamGal_Picker;
import com.krunal.camgal_libs.LibsCall.Type.CameraMode;
import com.krunal.camgal_libs.LibsCall.Type.Gallery_MIME_Type;
import com.krunal.camgal_libs.Picker.PickerBottomSheetFragment;
import com.krunal.camgal_libs.Utils.Constant;
import com.krunal.camgal_libs.Utils.Utility;
import com.krunal.camgall_library.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PickerListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.btn1.setOnClickListener(this);
        activityMainBinding.btn2.setOnClickListener(this);
        activityMainBinding.btn3.setOnClickListener(this);
        activityMainBinding.btn4.setOnClickListener(this);
        activityMainBinding.btn5.setOnClickListener(this);
        activityMainBinding.btn6.setOnClickListener(this);
        activityMainBinding.btn7.setOnClickListener(this);
        activityMainBinding.btn8.setOnClickListener(this);
        activityMainBinding.btn9.setOnClickListener(this);
        activityMainBinding.btn10.setOnClickListener(this);
        activityMainBinding.btn11.setOnClickListener(this);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK

            ArrayList<String> listSaved = data.getStringArrayListExtra("selectedImages");

//            setResult(RESULT_OK, new Intent().putStringArrayListExtra("selectedImages", listSaved));
//            finish();
            Toast.makeText(this, "Size: " + listSaved.size(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == CamGal_Picker.RESULT_ERROR) {
            Toast.makeText(this, CamGal_Picker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                CamGal_Picker.with(this).start();
                break;
            case R.id.btn2:
                CamGal_Picker.with(this)
                        .galleryOnly()    //User can only select image from Gallery
                        .start();    //Default Request Code is CamGal_Picker.REQUEST_CODE
                break;
            case R.id.btn3:
                CamGal_Picker.with(this)
                        .cameraOnly()    //User can only capture image using Camera
                        .start();
                break;
            case R.id.btn4:
                CamGal_Picker.with(this)
                        .crop(false)        //select image from gallery/Camera only
                        .start();
                break;
            case R.id.btn5:
                CamGal_Picker.with(this)
                        .crop(16, 9)    //Crop image with 16:9 aspect ratio
                        .start();
                break;
            case R.id.btn6:
                CamGal_Picker.with(this)
                        .cropSquare()    //Crop square image, its same as crop(1f, 1f)
                        .start();

                break;

            case R.id.btn7:
                CamGal_Picker.with(this)
                        .compressSize(5)    //Final image size will be less than 5 MB
                        .start();

                break;
            case R.id.btn8:
                CamGal_Picker.with(this)
                        /// Provide directory path to save images, Added example saveDir method. You can choose directory as per your need.

                        //  Path: /storage/sdcard0/Android/data/package/files
                        .saveDir(getExternalFilesDir(null))
                        //  Path: /storage/sdcard0/Android/data/package/files/DCIM
                        .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
                        //  Path: /storage/sdcard0/Android/data/package/files/Download
                        .saveDir(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))
                        //  Path: /storage/sdcard0/Android/data/package/files/Pictures
                        .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                        //  Path: /storage/sdcard0/Android/data/package/files/Pictures/ImagePicker
                        .saveDir(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImagePicker"))
                        //  Path: /storage/sdcard0/Android/data/package/files/ImagePicker
                        .saveDir(getExternalFilesDir("ImagePicker"))
                        //  Path: /storage/sdcard0/Android/data/package/cache/ImagePicker
                        .saveDir(new File(getExternalCacheDir(), "ImagePicker"))
                        //  Path: /data/data/package/cache/ImagePicker
                        .saveDir(new File(getCacheDir(), "ImagePicker"))
                        //  Path: /data/data/package/files/ImagePicker
                        .saveDir(new File(getFilesDir(), "ImagePicker"))

                        // Below saveDir path will not work, So do not use it
                        //  Path: /storage/sdcard0/DCIM
                        //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
                        //  Path: /storage/sdcard0/Pictures
                        //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
                        //  Path: /storage/sdcard0/ImagePicker
                        //  .saveDir(File(Environment.getExternalStorageDirectory(), "ImagePicker"))

                        .start();

                break;
            case R.id.btn9:
                CamGal_Picker.with(this)
                        .galleryMimeTypes(Gallery_MIME_Type.ALL)
                        .start();
                break;
            case R.id.btn10: // set image count
                CamGal_Picker.with(this)
                        .galleryMimeTypes(Gallery_MIME_Type.ALL)
                        .setImageMaxCount(8)
                        .start();
                break;
            case R.id.btn11: // set Camera facing mode
                CamGal_Picker.with(this)
                        .galleryMimeTypes(Gallery_MIME_Type.ALL)
                        .setCameraFacingMode(CameraMode.FRONT)
                        .start();
                break;
        }

    }

}