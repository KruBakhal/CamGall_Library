package com.krunal.camgal_libs.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krunal.camgal_libs.Adapter.Original_Image_Adapter;
import com.krunal.camgal_libs.Model.ImageModel;

import com.krunal.camgal_libs.Utils.Constant;
import com.krunal.camgal_libs.Utils.FileUtils;
import com.krunal.camgal_libs.Utils.HelperResizer;
import com.krunal.camgal_libs.databinding.ActivityCompressionResizerBinding;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

import static com.krunal.camgal_libs.Utils.Utility.ARG_PARAM1;
import static com.krunal.camgal_libs.Utils.Utility.ARG_PARAM2;
import static com.krunal.camgal_libs.Utils.Utility.getPicNameByPosition;
import static com.krunal.camgal_libs.Utils.Utility.saveImageToCache;

public class CompressionResizerActivity extends AppCompatActivity {
    private String mParam1;
    private String mParam2;
    private @NonNull
    ActivityCompressionResizerBinding viewBinding;
    private ArrayList<ImageModel> listImages = new ArrayList<>();
    private Original_Image_Adapter adpter;
    private FinalCompressResizerTask compressResizerTask;
    private Type typetype = new TypeToken<ArrayList<ImageModel>>() {
    }.getType();
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
        viewBinding = ActivityCompressionResizerBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        resize();
        initData();
        click();
        setAdapter();
    }

    private void initData() {
        mParam1 = getIntent().getStringExtra(ARG_PARAM1);
        mParam2 = getIntent().getStringExtra(ARG_PARAM2);
        listImages.addAll(new Gson().fromJson(mParam1, typetype));

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


    }

    private void setAdapter() {
        adpter = new Original_Image_Adapter(CompressionResizerActivity.this, listImages, new Original_Image_Adapter.Original_ImageListener() {
            @Override
            public void onCropImageClick(ImageModel image, int position) {

                Bundle bundle = new Bundle();
                bundle.putInt(Constant.GALLERY_TYPE, mineType);// 0-ALL,1-PNG,2-JPG,3-JPEG
                bundle.putInt(Constant.CAMERA_FACING_MODE, Facing_MODE);// 0-ALL,1-PNG,2-JPG,3-JPEG
                bundle.putInt(Constant.IMAGE_Selection_COUNT, ImgCount);//
                bundle.putBoolean(Constant.COMPRESS_STATUS, compressStatus);// active or inactive
                bundle.putLong(Constant.COMPRESSION_SIZE, compressionSize);// long
                bundle.putBoolean(Constant.MAINTAIN_ASPECT_RATIO, maintainAspectRatio);// long
                bundle.putInt(Constant.ASPECT_RATIO_RESIZE_PERCENTAGE, resizePercentage);// long
                bundle.putString(Constant.RESIZE_WIDTH_HEIGHT_RESOLUTION, resizeWidthHeight);// long
                bundle.putBoolean(Constant.CROP, croppingStatus);
                bundle.putString(Constant.CROP_RATIO, croppingRatio);


                someActivityResultLauncher.launch(new Intent(CompressionResizerActivity.this, CropperActivity.class)
                        .putExtra(ARG_PARAM1, new Gson().toJson(listImages)).putExtra(ARG_PARAM2, position).putExtras(bundle)
                );
            }

            @Override
            public void onIteamClick(ImageModel image, int position) {

            }
        });
        viewBinding.rvImageList.setAdapter(adpter);
    }


    private void click() {

        viewBinding.layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*
                Navigation.findNavController(viewBinding.getRoot())
                        .navigate(R.id.action_imagesFragment_to_homeFragment);*/
                setResult(RESULT_FIRST_USER);
                finish();

            }
        });

        viewBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*

                someActivityResultLauncher.launch(new Intent(
                        CompressionResizerActivity.this, SettingActivity.class)
                        .putExtra(ARG_PARAM1, new Gson().toJson(listImages))
                );
*/

            }
        });
        viewBinding.btnDoneFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<File> listFiles = new ArrayList<File>();
                for (ImageModel model_native : listImages) {
                    if (model_native.isCroped()) {
                        listFiles.add(new File(model_native.getCroppedPath()));
                    } else
                        listFiles.add(new File(model_native.getfPath()));
                }
                compressResizerTask = new FinalCompressResizerTask(CompressionResizerActivity.this, listFiles);
                compressResizerTask.execute();
            }
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            String list = result.getData().getStringExtra("key");
                            if (!TextUtils.isEmpty(list)) {
                                listImages.clear();
                                listImages = new ArrayList<>();
                                listImages.addAll(new Gson().fromJson(list, typetype));
                                setAdapter();
                            }
                        }
                    }
                }
            });

    private void resize() {

        viewBinding.tvTitle.setText("Images");
        HelperResizer.setHeight(this, viewBinding.actionBar, 150);
        HelperResizer.setSize(viewBinding.btnBack, 70, 70, true);
        HelperResizer.setSize(viewBinding.btnDone, 100, 100, true);

    }


    public class FinalCompressResizerTask extends AsyncTask<Void, Void, String> {
        private final List<File> selectedImageList;
        private final List<String> savedImages;
        private Context context;
        private ProgressDialog dailog_Loading;
        private int width = 720;
        private int height = 720;


        public FinalCompressResizerTask(Context context, List<File> selectedImageList) {
            this.context = context;
            this.selectedImageList = selectedImageList;
            this.savedImages = new ArrayList<String>();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dailog_Loading = new ProgressDialog(context);
            dailog_Loading.setMessage("Please Wait");
            dailog_Loading.show();
            width = 720;
            height = 720;

        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (!TextUtils.isEmpty(aVoid)) {

//                FragmentActivity activity = getActivity();
//                if (activity instanceof MainActivity) {
//                    ((MainActivity) activity).setResultSavedImage(savedImages);
//                }
                ArrayList<String> listSaved = new ArrayList<>();
                listSaved.addAll(savedImages);
                setResult(RESULT_OK, new Intent().putStringArrayListExtra("selectedImages", listSaved));
                if (dailog_Loading != null && dailog_Loading.isShowing()) {
                    dailog_Loading.dismiss();
                    dailog_Loading = null;
                }

                finish();
//                Navigation.findNavController(viewBinding.getRoot()).navigate(R.id.action_imagesFragment_to_homeFragment);

            }
            if (dailog_Loading != null && dailog_Loading.isShowing())
                dailog_Loading.dismiss();
            dailog_Loading = null;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (selectedImageList != null && selectedImageList.size() != 0) {

                for (int i = 0; i < selectedImageList.size(); i++) {
                    File file = new File(selectedImageList.get(i).getAbsolutePath());
                    // compress cropped images
                    try {
                        // first save original image from origin path to our internal storage folder
                        // i.e: data folder --> data/com.sridhar_cliend/files/Compressed
                        Bitmap bitmap = getBitmap(file.getAbsolutePath());
                        String savepath = saveImageToCache(new FileUtils().getTemp_Compressed_ImagePath(CompressionResizerActivity.this)
                                , bitmap, getPicNameByPosition(i));
                        File bitmapFile = new File(savepath);

                        width = bitmap.getWidth();
                        height = bitmap.getHeight();


                        // here it compare image restriction size with file size if more the resize operation perform.
                        if (bitmapFile.exists() && bitmapFile.length() > compressionSize) {

                            // get resize config param from settings
                            if (maintainAspectRatio) {
                                width = Integer.parseInt(getValueByPercentage(width, resizePercentage, 100, width));
                                height = Integer.parseInt(getValueByPercentage(height, resizePercentage, 100, height));
                            } else {
                                String[] size = resizeWidthHeight.split("%");
                                width = Integer.parseInt(size[0]);
                                height = Integer.parseInt(size[1]);
                            }
                            // get bitmap of saved image from data folder
                            // i.e: data folder --> data/com.sridhar_cliend/files/Compressed to avoid bitmpa recycle.
                            bitmap = getBitmap(bitmapFile.getAbsolutePath());
                            // perform resize process. save to same storage locations.
                            // i.e: data folder --> data/com.sridhar_cliend/files/Compressed to avoid bitmpa recycle.
                            bitmap = getResizedBitmap(bitmap, width, height);
                            savepath = saveImageToCache(new FileUtils().getTemp_Compressed_ImagePath(CompressionResizerActivity.this)
                                    , bitmap, getPicNameByPosition(i));
                            bitmapFile = new File(savepath);
                        }
                        // compress the image according config parma from settings for Compression.
                        if (compressStatus) {
                            // 1st library for compression.
                            /*
                                Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
                                options.width = width;
                                options.height = height;
                                BitmapResult result = Tiny.getInstance().source(file.getAbsolutePath()).asBitmap().withOptions(options).compressSync();
                            */
                            // 2nd library for compression.
                            Compressor compressor = new Compressor(context);
                            compressor.setQuality(80);
                            bitmap = compressor.compressToBitmap(bitmapFile);
                            savepath = saveImageToCache(new FileUtils()
                                            .getTemp_Compressed_ImagePath(CompressionResizerActivity.this)
                                    , bitmap, getPicNameByPosition(i));
                            bitmapFile = new File(savepath);


                        }
                        // add to file path to main list.
                        savedImages.add(bitmapFile.getAbsolutePath());
                        bitmap.recycle();
                        bitmap = null;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (savedImages != null && savedImages.size() == selectedImageList.size()) {
                    return "success";
                }
            }
            return null;


        }

        public String getValueByPercentage(int w2, int i3, int i4, int w5) {
            return String.valueOf(w5 - ((w2 * i3) / i4));
        }
    }

    public Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        // recycler
        return resizedBitmap;
    }

    @Override
    public void onDestroy() {
        try {

            if (compressResizerTask != null) {
                compressResizerTask = null;
            }
        } catch (Exception e) {
            Log.d("TAG", "onDestroy: " + e.toString());
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}