package com.android.baselib.imageSelect.bean;


import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 图片文件夹实体类
 * @author PF-NAN
 * @date 2018/11/13
 */
public class MImageFolderObj {

    private String name;
    private ArrayList<MImageFileObj> images;

    public MImageFolderObj(String name) {
        this.name = name;
    }

    public MImageFolderObj(String name, ArrayList<MImageFileObj> images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MImageFileObj> getImages() {
        return images;
    }

    public void setImages(ArrayList<MImageFileObj> images) {
        this.images = images;
    }

    public void addImage(MImageFileObj image) {
        if (image != null && !TextUtils.isEmpty(image.getPath())) {
            if (images == null) {
                images = new ArrayList<>();
            }
            images.add(image);
        }
    }

    @Override
    public String toString() {
        return "Folder{" +
                "name='" + name + '\'' +
                ", images=" + images +
                '}';
    }
}
