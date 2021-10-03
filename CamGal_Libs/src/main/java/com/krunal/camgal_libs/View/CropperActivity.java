package com.krunal.camgal_libs.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krunal.camgal_libs.Model.ImageModel;

import com.krunal.camgal_libs.R;
import com.krunal.camgal_libs.Utils.Constant;
import com.krunal.camgal_libs.Utils.FileUtils;
import com.krunal.camgal_libs.Utils.HelperResizer;
import com.krunal.camgal_libs.databinding.ActivityCropperBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.krunal.camgal_libs.Utils.Utility.ARG_PARAM1;
import static com.krunal.camgal_libs.Utils.Utility.ARG_PARAM2;
import static com.krunal.camgal_libs.Utils.Utility.getPicNameByPosition;
import static com.krunal.camgal_libs.Utils.Utility.saveImageToCache;
import static com.krunal.camgal_libs.Utils.Utility.scanMedia;

public class CropperActivity extends AppCompatActivity {
    private String mParam1;
    private int currentIndex;
    private @NonNull
    ActivityCropperBinding viewBinding;
    private Context mContext;
    private int width = 0;
    private int hight = 0;
    private long mLastClickTime = 0;
    private ArrayList<ImageModel> orginalImageList = new ArrayList<>();
    private int orginal_width;
    private int orginal_hight;

    private int ImgCount = 5;
    private int Facing_MODE = 0;
    private boolean compressStatus;
    private long compressionSize;
    private int mineType;
    private boolean maintainAspectRatio;
    private int resizePercentage;
    private String resizeWidthHeight;
    private boolean croppingStatus;
    private String croppingRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cropper);
        viewBinding = ActivityCropperBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        mContext = this;
        mParam1 = getIntent().getStringExtra(ARG_PARAM1);
        currentIndex = getIntent().getIntExtra(ARG_PARAM2, 0);
        Type type = new TypeToken<ArrayList<ImageModel>>() {
        }.getType();
        orginalImageList.addAll(new Gson().fromJson(mParam1, type));

        xml();
        initParameter();
        clickPart();

    }

    private void clickPart() {
        viewBinding.frameBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToImageFrag();
            }
        });

        viewBinding.cropBtn.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       // Preventing multiple clicks, using threshold of 1.5 second
                                                       if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                                                           return;
                                                       }
                                                       mLastClickTime = SystemClock.elapsedRealtime();

                                                       call_single_Cropping_Event();
                                                   }
                                               }
        );

    }

    private void backToImageFrag() {
       /* NavController navController = Navigation.findNavController(viewBinding.getRoot());
        navController.getPreviousBackStackEntry().getSavedStateHandle()
                .set("key", new Gson().toJson(orginalImageList));
        navController.popBackStack();
*/
        if (orginalImageList != null && orginalImageList.size() > 0) {
            setResult(RESULT_OK, new Intent()
                    .putExtra("key", new Gson().toJson(orginalImageList)));
        }
        finish();


    }


    private void call_single_Cropping_Event() {
        Bitmap b = viewBinding.imageCropView.getCroppedImage();
        if (b != null) {
            bitmapConvertToFile(b);
        } else {
            Toast.makeText(this, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
        }
    }


    private void initParameter() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(orginalImageList.get(currentIndex).getfPath()).getAbsolutePath(), options);
        orginal_hight = options.outHeight;
        orginal_width = options.outWidth;


        mineType = getIntent().getExtras().getInt(Constant.GALLERY_TYPE, 0);//, getGalleryType(mimeTypes));// 0-ALL,1-PNG,2-JPG,3-JPEG
        Facing_MODE = getIntent().getExtras().getInt(Constant.CAMERA_FACING_MODE, 0);//, getCameraMode(camera_mode));// 0-ALL,1-PNG,2-JPG,3-JPEG
        ImgCount = getIntent().getExtras().getInt(Constant.IMAGE_Selection_COUNT, 5);//, imageCount);//
        compressStatus = getIntent().getExtras().getBoolean(Constant.COMPRESS_STATUS, true);//, compressionStatus);// active or inactive
        compressionSize = getIntent().getExtras().getLong(Constant.COMPRESSION_SIZE, 5000);//, compressionSize);// long
        maintainAspectRatio = getIntent().getExtras().getBoolean(Constant.MAINTAIN_ASPECT_RATIO, true);//, maintainAspectRatio);// long
        resizePercentage = getIntent().getExtras().getInt(Constant.ASPECT_RATIO_RESIZE_PERCENTAGE, 10);//, resizePercentage);// long
        resizeWidthHeight = getIntent().getExtras().getString(Constant.RESIZE_WIDTH_HEIGHT_RESOLUTION, "720%720");//, width_height_Resize);// long
        croppingStatus = getIntent().getExtras().getBoolean(Constant.CROP, true);//, crop);
        croppingRatio = getIntent().getExtras().getString(Constant.CROP_RATIO, "9%16");//, cropX + "%" + cropY);

        if (maintainAspectRatio) {
            width = orginal_width;
            hight = orginal_hight;
        } else {
            String[] size = resizeWidthHeight.split("%");
            width = Integer.parseInt(size[0]);
            hight = Integer.parseInt(size[1]);
        }
        try {
            viewBinding.imageCropView.setImageUriAsync(Uri.fromFile(new File(orginalImageList.get(currentIndex).getfPath())));
        } catch (Exception e) {
            Log.d("cropper", "initParameter: " + e.toString());
        }
        String[] cropRatio = croppingRatio.split("%");
        if (cropRatio[0].equals("0") && cropRatio[1].equals("0")) {
            viewBinding.imageCropView.setAspectRatio(width, hight);
        } else
            viewBinding.imageCropView.setAspectRatio(Integer.parseInt(cropRatio[0]), Integer.parseInt(cropRatio[1]));
    }


    private void xml() {


        viewBinding.tvTitle.setText("Crop");

        HelperResizer.setHeight(mContext, viewBinding.actionBar, 150);
        HelperResizer.setSize(viewBinding.ivBack, 70, 70, true);
        HelperResizer.setSize(viewBinding.ivDone, 100, 100, true);

        HelperResizer.setHeightWidthAsWidth(mContext, viewBinding.cropBtn, 150, 150);
        HelperResizer.setHeightWidthAsWidth(mContext, viewBinding.resetBtn, 100, 100);


    }


    public File bitmapConvertToFile(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        File bitmapFile = null;
        try {
            //save cropped image
            String savepath = saveImageToCache(new FileUtils().getTemp_CroppedImagePath(this),
                    bitmap, getPicNameByPosition(currentIndex));
            bitmapFile = new File(savepath);
            scanMedia(mContext, savepath);
            bitmap.recycle();
            bitmap = null;
            ImageModel imageModel = orginalImageList.get(currentIndex);
            imageModel.setCroppedPath(savepath);
            imageModel.setCroped(true);
            orginalImageList.set(currentIndex, imageModel);
            moveBackToBasePart();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }

        return bitmapFile;
    }


    private void moveBackToBasePart() {

        backToImageFrag();


    }

}