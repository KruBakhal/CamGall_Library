package com.krunal.camgal_libs.LibsCall;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.krunal.camgal_libs.Picker.PickerBottomSheetFragment;
import com.krunal.camgal_libs.View.CameraActivity;
import com.krunal.camgal_libs.View.GalleryActivity;
import com.krunal.camgal_libs.LibsCall.Type.CameraMode;
import com.krunal.camgal_libs.LibsCall.Type.Gallery_MIME_Type;
import com.krunal.camgal_libs.LibsCall.Type.PickerOption;
import com.krunal.camgal_libs.Utils.Constant;
import com.krunal.camgal_libs.Utils.Utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;


public class CamGal_Picker {

    public static final int REQUEST_CODE = 2404;
    public static final int RESULT_ERROR = 64;
    public static final CamGal_Picker.Companion Companion = new CamGal_Picker.Companion();

    @JvmStatic
    @NotNull
    public static final CamGal_Picker.Builder with(@NotNull Activity activity) {
        return Companion.with(activity);
    }

    @JvmStatic
    @NotNull
    public static final CamGal_Picker.Builder with(@NotNull Fragment fragment) {
        return Companion.with(fragment);
    }

    @JvmStatic
    @NotNull
    public static final String getError(@Nullable Intent data) {
        return Companion.getError(data);
    }

    public static final class Builder {
        private static final int REQ_CAMERA = 333;
        private Fragment fragment;
        private Activity activity;
        String arry_facing_mode[] = {"Both", "BACK", "FRONT"}; // 0-"Both", 1-"BACK", 2-"FRONT"
        private String saveDir;
        private int imageCount = 5;
        private PickerOption pickerOption = PickerOption.BOTH;
        private Gallery_MIME_Type mimeTypes = Gallery_MIME_Type.ALL;
        private DismissListener dismissListener;
        private boolean compressionStatus = true;
        private long compressionSize = Constant.DEFAULT_COMRESSION_SIZE;
        private boolean crop = true;
        private int cropX = 0;
        private int cropY = 0;
        private CameraMode camera_mode = CameraMode.BOTH;
        private boolean maintainAspectRatio = true;
        private int resizePercentage = 10;
        private String width_height_Resize = "720%720";

        @NotNull
        public final CamGal_Picker.Builder provider(@NotNull PickerOption pickerOption) {
            this.pickerOption = pickerOption;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder cameraOnly() {
            this.pickerOption = PickerOption.CAMERA;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder galleryOnly() {
            this.pickerOption = PickerOption.GALLERY;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder galleryMimeTypes(@NotNull Gallery_MIME_Type mimeTypes) {
            this.mimeTypes = mimeTypes;
            return this;
        }

        public int getGalleryType(Gallery_MIME_Type pos) {
            int type = 0;
            if (Gallery_MIME_Type.PNG == pos)
                type = 1;
            else if (Gallery_MIME_Type.JPG == pos)
                type = 2;
            else if (Gallery_MIME_Type.JPEG == pos)
                type = 3;
            else if (Gallery_MIME_Type.ALL == pos)
                type = 0;
            return type;
        }


        @NotNull
        public final CamGal_Picker.Builder crop(int x, int y) {
            this.cropX = x;
            this.cropY = y;
            return this.crop();
        }

        @NotNull
        public final CamGal_Picker.Builder crop() {
            this.crop = true;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder crop(boolean ss) {
            this.crop = ss;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder cropSquare() {
            return this.crop(1, 1);
        }

        @NotNull
        public final CamGal_Picker.Builder compressSize(int maxSize) {
            this.compressionSize = maxSize * 1024L;
            setCompression(true);
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder setImageMaxCount(int maxSize) {
            this.imageCount = maxSize;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder setCameraFacingMode(CameraMode maxSize) {
            this.camera_mode = maxSize;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder setMaintainAspectRatio(boolean aspectRatio) {
            this.maintainAspectRatio = aspectRatio;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder setResizePercentage(int aspectRatio) {
            this.resizePercentage = aspectRatio;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder setResize_Width_Height(String width_height_Resize) {
            this.width_height_Resize = width_height_Resize;
            setMaintainAspectRatio(false);
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder setCompression(boolean compression) {
            this.compressionStatus = compression;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder saveDir(@NotNull String path) {
            this.saveDir = path;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder saveDir(@NotNull File file) {
            this.saveDir = file.getAbsolutePath();
            return this;
        }


        @NotNull
        public final CamGal_Picker.Builder setDismissListener(@NotNull DismissListener listener) {
            this.dismissListener = listener;
            return this;
        }

        @NotNull
        public final CamGal_Picker.Builder setDismissListener(@NotNull final Function0 listener) {
            this.dismissListener = new DismissListener() {
                public void onDismiss() {
                    listener.invoke();
                }
            };
            return this;
        }

        public final void start() {
            this.start(REQUEST_CODE);
        }

        public final void start(int reqCode) {
            if (this.pickerOption == PickerOption.BOTH) {
                this.showPickerBottomSheet(reqCode);
            } else {
                this.startActivity(reqCode);
            }

        }


        private final void showPickerBottomSheet(final int reqCode) {
            PickerBottomSheetFragment blankFragment = new PickerBottomSheetFragment(new ResultListener() {
                public void onResult(@Nullable PickerOption t) {
                    if (t != null) {
                        Builder.this.pickerOption = t;

                        Builder.this.startActivity(reqCode);
                    }
                }

                public void onResult(Object var1) {
                    this.onResult((PickerOption) var1);
                }

            }, dismissListener);
            blankFragment.show(((AppCompatActivity) activity).getSupportFragmentManager(), blankFragment.getTag());
        }

        private final Bundle getBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.GALLERY_TYPE, getGalleryType(mimeTypes));// 0-ALL,1-PNG,2-JPG,3-JPEG
            bundle.putInt(Constant.CAMERA_FACING_MODE, getCameraMode(camera_mode));// 0-ALL,1-PNG,2-JPG,3-JPEG
            bundle.putInt(Constant.IMAGE_Selection_COUNT, imageCount);//
            bundle.putBoolean(Constant.COMPRESS_STATUS, compressionStatus);// active or inactive
            bundle.putLong(Constant.COMPRESSION_SIZE, compressionSize);// long
            bundle.putBoolean(Constant.MAINTAIN_ASPECT_RATIO, maintainAspectRatio);// long
            bundle.putInt(Constant.ASPECT_RATIO_RESIZE_PERCENTAGE, resizePercentage);// long
            bundle.putString(Constant.RESIZE_WIDTH_HEIGHT_RESOLUTION, width_height_Resize);// long
            bundle.putBoolean(Constant.CROP, crop);
            bundle.putString(Constant.CROP_RATIO, cropX + "%" + cropY);
            return bundle;
        }

        private int getCameraMode(CameraMode camera_mode) {
            String mode = null;
            if (CameraMode.BOTH == camera_mode)
                return 0;
            else if (CameraMode.BACK == camera_mode)
                return 1;
            else if (CameraMode.FRONT == camera_mode)
                return 2;
            else
                return 0;
        }

        private final void startActivity(int reqCode) {
            Intent intent = null;
            Activity activity;
            if (fragment != null)
                activity = fragment.getActivity();
            else activity = this.activity;

            if (pickerOption == PickerOption.GALLERY) {

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Utility.displayToast(activity, "Take & ALlow Permission of External Storgae First !");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CAMERA);
                    }
                    return;
                }
                intent = new Intent(activity, GalleryActivity.class);

            } else if (pickerOption == PickerOption.CAMERA) {

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    Utility.displayToast(activity, "Take & ALlow Permission of External Storgae First !");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
                    }
                    return;
                }
                intent = new Intent(activity, CameraActivity.class);

            }
            intent.putExtras(this.getBundle());
            if (this.fragment != null) {
                fragment.startActivityForResult(intent, reqCode);
            } else {
                this.activity.startActivityForResult(intent, reqCode);
            }
        }


        private void openCamera(int reqCode) {
            Intent intent = new Intent(this.activity, CameraActivity.class);
            intent.putExtras(this.getBundle());
            if (this.fragment != null) {
                fragment.startActivityForResult(intent, reqCode);
            } else {
                this.activity.startActivityForResult(intent, reqCode);
            }
        }


        private void openGallery(int reqCode) {
            Intent intent = new Intent(this.activity, GalleryActivity.class);
            intent.putExtras(this.getBundle());
            if (this.fragment != null) {
                fragment.startActivityForResult(intent, reqCode);
            } else {
                this.activity.startActivityForResult(intent, reqCode);
            }
        }


        public Builder(@NotNull Activity activity) {
            this.activity = activity;
            this.pickerOption = PickerOption.BOTH;
            this.mimeTypes = Gallery_MIME_Type.ALL;
        }


        public Builder(@NotNull Fragment fragment) {
            this.fragment = fragment;
        }

    }

    public static final class Companion {

        @JvmStatic
        @NotNull
        public final CamGal_Picker.Builder with(@NotNull Activity activity) {
            return new CamGal_Picker.Builder(activity);
        }

        @JvmStatic
        @NotNull
        public final CamGal_Picker.Builder with(@NotNull Fragment fragment) {
            return new CamGal_Picker.Builder(fragment);
        }

        @JvmStatic
        @NotNull
        public final String getError(@Nullable Intent data) {
            String error = data != null ? data.getStringExtra("extra.error") : null;
            return error != null ? error : "Unknown Error!";
        }
    }


}
