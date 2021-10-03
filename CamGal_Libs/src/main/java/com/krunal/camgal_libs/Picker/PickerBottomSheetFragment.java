package com.krunal.camgal_libs.Picker;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.krunal.camgal_libs.View.CreationActivity;
import com.krunal.camgal_libs.Intermediate.PickerListener;
import com.krunal.camgal_libs.LibsCall.DismissListener;
import com.krunal.camgal_libs.LibsCall.ResultListener;
import com.krunal.camgal_libs.LibsCall.Type.PickerOption;
import com.krunal.camgal_libs.R;
import com.krunal.camgal_libs.Utils.Constant;
import com.krunal.camgal_libs.Utils.Utility;
import com.krunal.camgal_libs.databinding.FragmentPickerBottomSheetBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PickerBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickerBottomSheetFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DismissListener dismissListener;
    private Intent intentCall;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentPickerBottomSheetBinding viewBinding;
    PickerListener pickerListener;

    private int imageType = 0;
    private int imageCount = 5;
    private boolean compressionStatus = true;
    private int compressionSize = Constant.DEFAULT_COMRESSION_SIZE;
    private String compressionType = "MB";
    boolean visibleCreationOptions = false;
    boolean visibleSettingOption = false;
    ResultListener listener;

    public PickerBottomSheetFragment(int imageType, int imageCount, boolean compressionStatus, int compressionSize, String compressionType) {
        this.imageType = imageType;
        this.imageCount = imageCount;
        this.compressionStatus = compressionStatus;
        this.compressionSize = compressionSize;
        this.compressionType = compressionType;
    }

    public PickerBottomSheetFragment(boolean visibleCreationOptions, boolean visibleSettingOption) {
        this.visibleCreationOptions = visibleCreationOptions;
        this.visibleSettingOption = visibleSettingOption;
    }

    public PickerBottomSheetFragment(Intent intent) {
        intentCall = intent;
    }

    public PickerBottomSheetFragment(ResultListener listener, DismissListener dismissListener) {
        this.listener = listener;
        this.dismissListener = dismissListener;
    }

    public PickerBottomSheetFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static PickerBottomSheetFragment newInstance(String param1, String param2) {
        PickerBottomSheetFragment fragment = new PickerBottomSheetFragment(0, 5, true, Constant.DEFAULT_COMRESSION_SIZE, "MB");
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewBinding = FragmentPickerBottomSheetBinding.inflate(getLayoutInflater(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickerListener = (PickerListener) getActivity();

        if (visibleCreationOptions) {
            viewBinding.creation.setVisibility(View.VISIBLE);
        } else {
            viewBinding.creation.setVisibility(View.GONE);
        }
        if (visibleSettingOption) {
            viewBinding.setting.setVisibility(View.VISIBLE);
        } else {
            viewBinding.setting.setVisibility(View.GONE);
        }

        viewBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        viewBinding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        mPermissionResultCamera.launch(Manifest.permission.CAMERA);
                    }
                } else {
                    openCamera();
                }
            }
        });

        viewBinding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                    } else {
                        mPermissionResultGallery.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    openGallery();
                }
            }
        });
        viewBinding.creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), CreationActivity.class));
            }

        });
        viewBinding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), SettingActivity.class));

            }
        });
        viewBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }

    private ActivityResultLauncher<String> mPermissionResultCamera = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), result -> {

                if (result.booleanValue()) {
                    openCamera();
                } else {
                    Utility.displayToast(getContext(), getResources().getString(R.string.permission_denied));
                    if (dismissListener != null)
                        dismissListener.onDismiss();
                    dismiss();
                }
            });
    private ActivityResultLauncher<String> mPermissionResultGallery = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), result -> {

                if (result.booleanValue()) {
                    openGallery();
                } else {
                    Utility.displayToast(getContext(), getResources().getString(R.string.permission_denied));
                    if (dismissListener != null)
                        dismissListener.onDismiss();
                    dismiss();
                }
            });


    private void openGallery() {
        listener.onResult(PickerOption.GALLERY);
        if (dismissListener != null)
            dismissListener.onDismiss();
        dismiss();
        /*
        if (intentCall == null) {
            intentCall = new Intent();
        } else {
          *//*  intentCall.putExtra(Constant.GALLERY_TYPE, imageType)// 0-ALL,1-PNG,2-JPG,3-JPEG
                    .putExtra(Constant.IMAGE_Selection_COUNT, imageCount)//
                    .putExtra(Constant.COMPRESS_STATUS, compressionStatus)// active or inactive
                    .putExtra(Constant.COMPRESSION_SIZE, compressionSize)// (int) long
                    .putExtra(Constant.COMPRESSION_SIZE_Type, compressionType);// KB,MB
*//*
        }
        intentCall.setClass(getActivity(), GalleryActivity.class);

        resultLauncher.launch(intentCall);*/

    }

    private void openCamera() {
        listener.onResult(PickerOption.CAMERA);
        dismiss();
        /*
        if (intentCall == null) {
            intentCall = new Intent();
        } else {
           *//* intentCall.putExtra(Constant.GALLERY_TYPE, imageType)// 0-ALL,1-PNG,2-JPG,3-JPEG
                    .putExtra(Constant.IMAGE_Selection_COUNT, imageCount)//
                    .putExtra(Constant.COMPRESS_STATUS, compressionStatus)// active or inactive
                    .putExtra(Constant.COMPRESSION_SIZE, compressionSize)// (int) long
                    .putExtra(Constant.COMPRESSION_SIZE_Type, compressionType);// KB,MB
*//*
        }
        intentCall.setClass(getActivity(), CameraActivity.class);

        resultLauncher.launch(intentCall);*/
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == RESULT_OK) {
                        ArrayList<String> listSaved = result.getData().getStringArrayListExtra("selectedImages");
                        pickerListener.onResultPicker(result.getData(), listSaved);
                    } else if (result.getResultCode() == Activity.RESULT_FIRST_USER
                            || result.getResultCode() == Activity.RESULT_CANCELED) {
                        pickerListener.onResultPicker(result.getData(), new ArrayList<>());
                        Utility.displayToast(getContext(), "No result");
                    }
                    dismiss();

                }
            });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        getDialog().setCancelable(false);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCancel(@NonNull @NotNull DialogInterface dialog) {
        super.onCancel(dialog);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    public void setListner(ResultListener listener) {
        this.listener = listener;
    }
}