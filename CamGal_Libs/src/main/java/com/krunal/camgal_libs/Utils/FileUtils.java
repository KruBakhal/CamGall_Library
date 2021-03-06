package com.krunal.camgal_libs.Utils;

import android.content.Context;
import android.os.Environment;


import com.krunal.camgal_libs.R;

import java.io.File;
import java.io.IOException;

public class FileUtils {


    public String strTemp_Image = "images"; // cancelled
    public String strTemp_CroppedImage = "cropped";
    public String strTemp_CompressedImage = "Compressed";
    public String APPDIRECTORY = "";

    public String getTempImageFolder(Context context) {
        String path = context.getFilesDir().getPath() + File.separator + strTemp_Image;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public String getTemp_OriginalImage_Path(Context mContext) {
        String path = getTempFolderPath(mContext);
        File file = new File(path);
        if (file.exists()) {
            path = path + File.separator + strTemp_Image;
            file = new File(path);
            if (!file.exists())
                file.mkdir();
            return file.getAbsolutePath();
        }
        return path;
    }

    public String getTemp_CroppedImagePath(Context mContext) {
        String path = getTempFolderPath(mContext);
        File file = new File(path);
        if (file.exists()) {
            path = path + File.separator + strTemp_CroppedImage;
            file = new File(path);
            if (!file.exists())
                file.mkdir();
            return file.getAbsolutePath();
        }
        return path;
    }

    public String getTemp_Compressed_ImagePath(Context mContext) {
        String path = getTempFolderPath(mContext);
        File file = new File(path);
        if (file.exists()) {
            path = path + File.separator + strTemp_CompressedImage;
            file = new File(path);
            if (!file.exists())
                file.mkdir();
            return file.getAbsolutePath();
        }
        return path;
    }

    public String getTempFolderPath(Context mContext) {

        String path = mContext.getFilesDir().getPath();

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    public void emptyTempCompressedImages(Context context) {
        File file = new File(getTemp_Compressed_ImagePath(context));
        if (!file.exists())
            return;
        File[] list = file.listFiles();
        if (list == null || list.length <= 0) {
            return;
        }
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void emptyTempCroppedImages(Context context) {
        File file = new File(getTemp_CroppedImagePath(context));
        if (!file.exists())
            return;
        File[] list = file.listFiles();
        if (list == null || list.length <= 0) {
            return;
        }
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cacheFolder(Context context) {
        emptyTempCompressedImages(context);
        emptyTempCroppedImages(context);
    }
}
