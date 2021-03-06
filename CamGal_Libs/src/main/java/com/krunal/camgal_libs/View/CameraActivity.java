package com.krunal.camgal_libs.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krunal.camgal_libs.Adapter.SelectedImageAdapter_Native;
import com.krunal.camgal_libs.Intermediate.SelectedAdapterListener;
import com.krunal.camgal_libs.Model.ImageModel;

import com.krunal.camgal_libs.R;
import com.krunal.camgal_libs.Utils.Constant;
import com.krunal.camgal_libs.Utils.FileUtils;
import com.krunal.camgal_libs.Utils.HelperResizer;
import com.krunal.camgal_libs.databinding.ActivityCameraBinding;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.krunal.camgal_libs.Utils.Utility.ARG_PARAM1;
import static com.krunal.camgal_libs.Utils.Utility.checkCameraFront;
import static com.krunal.camgal_libs.Utils.Utility.checkCameraRear;
import static com.krunal.camgal_libs.Utils.Utility.displayToast;
import static com.krunal.camgal_libs.Utils.Utility.getPicNameByPosition;
import static com.krunal.camgal_libs.Utils.Utility.onClickEvent;

public class CameraActivity extends AppCompatActivity implements SelectedAdapterListener {

    public int selectedPosition = 0;
    private SelectedImageAdapter_Native imageSelectedAdapter;
    private ArrayList<ImageModel> list_new_pic_selected;
    private FileUtils utilsStorage;
    private int ImgCount = 5;
    private int Facing_MODE = 0;
    Context context;
    private boolean compressStatus;
    private long compressionSize;
    private int mineType;
    private boolean maintainAspectRatio;
    private int resizePercentage;
    private String resizeWidthHeight;
    private boolean croppingStatus;
    private String croppingRatio;
    private RecyclerView rv_selectedImageList;
    private LinearLayout innerLay, lay_actionBar, layBack, layDone, lay_camera,  layBottom;
    ImageView btnBack, btnDone, capturePictureSnapshot, switchCamera;
    private TextView tv2, tv1, tv_title, textView;
    CameraView camera;
    View layText,lay_capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = this;
        UI();
        new FileUtils().cacheFolder(context);
        setupCamera();
        initData();
    }

    private void UI() {
        lay_actionBar = findViewById(R.id.lay_actionBar);
        layBack = findViewById(R.id.layBack);
        btnBack = findViewById(R.id.btnBack);
        tv_title = findViewById(R.id.tv_title);
        layDone = findViewById(R.id.layDone);
        btnDone = findViewById(R.id.btnDone);
        lay_camera = findViewById(R.id.lay_camera);
        camera = findViewById(R.id.camera);
        lay_capture = findViewById(R.id.lay_capture);
        capturePictureSnapshot = findViewById(R.id.capturePictureSnapshot);
        switchCamera = findViewById(R.id.switchCamera);
        textView = findViewById(R.id.textView);
        layBottom = findViewById(R.id.layBottom);
        layText = findViewById(R.id.layText);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        innerLay = findViewById(R.id.innerLay);
        rv_selectedImageList = findViewById(R.id.rv_selectedImageList);
    }

    private void initData() {
        utilsStorage = new FileUtils();
        utilsStorage.cacheFolder(context);


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


        switch (Facing_MODE) {
            case 0:
                switchCamera.setVisibility(View.VISIBLE);
                break;
            case 1:
                switchCamera.setVisibility(View.GONE);
                camera.setFacing(Facing.BACK);
                if (checkCameraFront(context)) {
                    camera.setFacing(Facing.BACK);
                } else if (checkCameraRear()) {
                    camera.setFacing(Facing.FRONT);
                }
                break;
            case 2:
                switchCamera.setVisibility(View.GONE);
                camera.setFacing(Facing.FRONT);
                if (checkCameraRear()) {
                    camera.setFacing(Facing.FRONT);
                } else if (checkCameraFront(context)) {
                    camera.setFacing(Facing.BACK);
                }
                break;
        }

        /*if (!checkCameraFront(context) || !checkCameraRear()) {
            switchCamera.setVisibility(View.GONE);
            if (!checkCameraFront(context)) {
                camera.setFacing(Facing.BACK);
            } else if (!checkCameraRear()) {
                camera.setFacing(Facing.FRONT);
            }
        }*/

        setSelectedImageList();
        resize();
        click();

    }

    private void click() {

        layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
                onClickEvent(v);
            }
        });

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Facing_MODE == 0)
                    if (camera.getFacing() == Facing.BACK) {
                        camera.setFacing(Facing.FRONT);
                    } else
                        camera.setFacing(Facing.BACK);

                onClickEvent(v);
            }
        });

        capturePictureSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSlot_AreFull()) {
                    displayToast(context, getResources().getString(R.string.all_image_selected));
                    return;
                } else if (!TextUtils.isEmpty(list_new_pic_selected.get(selectedPosition).getfPath())) {
                    displayToast(context, getResources().getString(R.string.str_all_selected_images));
                    return;
                }
                camera.takePictureSnapshot();
                onClickEvent(v);
            }
        });

        layDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_new_pic_selected != null && list_new_pic_selected.size() != 0) {

                    ArrayList<ImageModel> picList1 = new ArrayList<>();
                    for (int i = 0; i < list_new_pic_selected.size(); i++) {
                        if (!TextUtils.isEmpty(list_new_pic_selected.get(i).getfPath())) {
                            picList1.add(list_new_pic_selected.get(i));
                        }
                    }

                    if (picList1 == null || picList1.size() <= 0) {
                        displayToast(context, getResources().getString(R.string.capture_image_first));
                        return;
                    }

                    if (croppingStatus) {
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

                        someActivityResultLauncher.launch(new Intent(context, CompressionResizerActivity.class)
                                .putExtra(ARG_PARAM1, new Gson().toJson(picList1))
                                .putExtras(bundle));


                    } else {
                        ArrayList<String> selected = new ArrayList<>();
                        for (int i = 0; i < picList1.size(); i++) {
                            selected.add(list_new_pic_selected.get(i).getfPath());
                        }
                        setResult(RESULT_OK, new Intent().putStringArrayListExtra("selectedImages", selected));
                        finish();
                    }
                }
                onClickEvent(v);
            }
        });
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ArrayList<String> listSaved = result.getData().getStringArrayListExtra("selectedImages");
                        setResult(RESULT_OK, new Intent().putStringArrayListExtra("selectedImages", listSaved));
                        finish();
                    } else if (result.getResultCode() == Activity.RESULT_FIRST_USER) {

                    }

                }
            });

    private void resize() {
        HelperResizer.getInstance(context);
        HelperResizer.setSize(btnBack, 70, 70, true);
        HelperResizer.setSize(btnDone, 80, 80, true);


        HelperResizer.setHeight(context, innerLay, 250);
        HelperResizer.setHeight(context, lay_actionBar, 150);
        HelperResizer.setHeight(context, layText, 120);


    }

    private void setupCamera() {
        //camera
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE);
        camera.addCameraListener(new Listener());

    }


    private void setSelectedImageList() {

        textView.setText("You can take upto" + ImgCount + " images");
        list_new_pic_selected = new ArrayList<>();
        for (int i = 0; i < ImgCount; i++) {
            list_new_pic_selected.add(new ImageModel());
        }
        imageSelectedAdapter = new SelectedImageAdapter_Native(CameraActivity.this, list_new_pic_selected, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv_selectedImageList.setLayoutManager(layoutManager);
        rv_selectedImageList.setAdapter(imageSelectedAdapter);
        rv_selectedImageList.scrollToPosition(selectedPosition);
        rv_selectedImageList.setScrollX(rv_selectedImageList.computeHorizontalScrollOffset());
        setBottomText();
    }

    private void setBottomText() {
        int count = 0;
        if (list_new_pic_selected != null && list_new_pic_selected.size() != 0)
            for (int i = 0; i < list_new_pic_selected.size(); i++) {
                if (list_new_pic_selected.get(i).getfPath() != null && list_new_pic_selected.get(i).getfPath().length() != 0) {
                    count++;
                }
            }

        tv1.setText("Please Choose 1-" + list_new_pic_selected.size() + " Photos");
        tv2.setText("(" + count + "/" + ImgCount + ")");
        if (count != 0) {
            btnDone.setSelected(true);
        } else {
            btnDone.setSelected(false);
        }
    }

    public boolean checkSlot_AreFull() {
        if (list_new_pic_selected == null || list_new_pic_selected.size() == 0)
            return false;
        int count = 0;
        for (ImageModel imageModel : list_new_pic_selected) {
            if (imageModel != null && !TextUtils.isEmpty(imageModel.getfPath())) {
                count++;
            }
        }
        if (count == 0 || count < list_new_pic_selected.size()) {
            return false;
        } else
            return true;
    }

    @Override
    public void onRemove_From_Selected_list(ImageModel imageModel, int position) {
        if (!TextUtils.isEmpty(imageModel.getfPath()) && new File(imageModel.getfPath()).exists()) {
            try {
                org.apache.commons.io.FileUtils.forceDeleteOnExit(new File(imageModel.getfPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        list_new_pic_selected.set(position, new ImageModel());
        imageSelectedAdapter.notifyItemChanged(position);
        setBottomText();
    }

    class Listener extends CameraListener {

        @Override
        public void onPictureTaken(@NonNull @NotNull PictureResult result) {
            super.onPictureTaken(result);


            // If planning to save a file on a background thread,
            // just use toFile. Ensure you have permissions.

            File file = new File(getFilesDir(), "images");
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, getPicNameByPosition(selectedPosition));

            result.toFile(file, new FileCallback() {
                @Override
                public void onFileReady(@Nullable @org.jetbrains.annotations.Nullable File file) {

                    ImageModel iteam = new ImageModel(file.getName(), file.getAbsolutePath(), false);
                    if (list_new_pic_selected == null)
                        list_new_pic_selected = new ArrayList<>();

                    list_new_pic_selected.set(selectedPosition, iteam);


                    imageSelectedAdapter.notifyListChange(list_new_pic_selected, selectedPosition - 1);

                    if (!checkSlot_AreFull())
                        selectedPosition = getEmptySlotPosition(list_new_pic_selected);

                    imageSelectedAdapter.notifyDataSetChanged();
                    setBottomText();

                    rv_selectedImageList.smoothScrollToPosition(selectedPosition);

                }
            });
        }

        private int getEmptySlotPosition(ArrayList<ImageModel> selected_originalImageList) {
            for (int i = 0; i < selected_originalImageList.size(); i++) {
                ImageModel imageModel = selected_originalImageList.get(i);
                if (imageModel != null && TextUtils.isEmpty(imageModel.getfPath())) {
                    return i;
                }
            }
            return 0;
        }

        public boolean checkSlot_AreFull() {
            if (list_new_pic_selected == null || list_new_pic_selected.size() == 0)
                return false;
            int count = 0;
            for (ImageModel imageModel : list_new_pic_selected) {
                if (imageModel != null && !TextUtils.isEmpty(imageModel.getfPath())) {
                    count++;
                }
            }

            if (count == 0 || count < list_new_pic_selected.size()) {
                return false;
            } else
                return true;
        }

        @Override
        public void onPictureShutter() {
            super.onPictureShutter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        camera.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }


}