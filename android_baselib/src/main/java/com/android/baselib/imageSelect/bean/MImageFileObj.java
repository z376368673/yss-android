package com.android.baselib.imageSelect.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 片实体类
 * @author PF-NAN
 * @date 2018/11/13
 */
public class MImageFileObj implements Parcelable {

    private String path;
    private long time;
    private String name;

    public MImageFileObj(String path, long time, String name) {
        this.path = path;
        this.time = time;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.time);
        dest.writeString(this.name);
    }

    protected MImageFileObj(Parcel in) {
        this.path = in.readString();
        this.time = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<MImageFileObj> CREATOR = new Creator<MImageFileObj>() {
        @Override
        public MImageFileObj createFromParcel(Parcel source) {
            return new MImageFileObj(source);
        }

        @Override
        public MImageFileObj[] newArray(int size) {
            return new MImageFileObj[size];
        }
    };
}
