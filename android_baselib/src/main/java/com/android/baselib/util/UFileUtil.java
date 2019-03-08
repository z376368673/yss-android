package com.android.baselib.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.android.baselib.UBaseApplication;
import com.android.baselib.log.ULog;

import java.io.*;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 常用文件处理工具
 *
 * @author PF-NAN
 * @date 2018/3/12
 */

public class UFileUtil {
    /**
     * 获取文件大小单位为B的double值
     */
    public static final int SIZETYPE_B = 1;
    /**
     * 获取文件大小单位为KB的double值
     */
    public static final int SIZETYPE_KB = 2;
    /**
     * 获取文件大小单位为MB的double值
     */
    public static final int SIZETYPE_MB = 3;
    /**
     * 获取文件大小单位为GB的double值
     */
    public static final int SIZETYPE_GB = 4;

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return boolean 值
     */
    public boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * byte转换成为KB或者MB
     *
     * @param byteV
     * @return
     */
    public static String getB2M(long byteV) {
        String size;
        if (1024 > byteV) {
            size = byteV + "B";
        } else if (1024 * 1024 > byteV) {
            size = String.format(Locale.CHINA, "%.0fKB", byteV / 1024.0);
        } else {
            size = String.format(Locale.CHINA, "%.2fM", byteV / 1024.0 / 1024.0);
        }
        return size;
    }

    /**
     * KB转换成为MB
     *
     * @param kByteV
     * @return
     */
    public static String getKB2M(long kByteV) {
        String size;
        if (1024 > kByteV) {
            size = kByteV + "KB";
        } else {
            size = String.format(Locale.CHINA, "%.0fKB", kByteV / 1024.0);
        }
        return size;
    }

    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(File file) {
        if (null == file) return;
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
            intent.setDataAndType(/*uri*/fileToUri(file), type);
            //跳转
            UBaseApplication.getApplication().startActivity(intent);
        } catch (Exception exception) {
            exception.printStackTrace();
            ULog.e("XIAZAI-----", exception);
        }
    }


    /**
     * 获取对应文件的Uri
     *
     * @param file 文件对象
     * @return
     */
    public static Uri fileToUri(File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            uri = FileProvider.getUriForFile(UBaseApplication.getApplication(),
                    UBaseApplication.getApplication().getPackageName() + ".fileprovider",
                    file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 获取文件大小(byte)
     *
     * @param filePath 文件路径
     * @return
     */
    public static long getFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockSize;
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countFileSize(blockSize);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file 文件
     */
    private static String getMIMEType(File file) {

        String type = "*/*";
        if (null != file) {
            String fName = file.getName();
            //获取后缀名前的分隔符"."在fName中的位置。
            int dotIndex = fName.lastIndexOf(".");
            if (dotIndex < 0) {
                return type;
            }
            //获取文件的后缀名
            String end = fName.substring(dotIndex, fName.length()).toLowerCase();
            if (TextUtils.isEmpty(end)) {
                return type;
            }
            //在MIME和文件类型的匹配表中找到对应的MIME类型。
            for (String[] amimeMaptable : MIME_MAP_TABLE) {
                if (end.equals(amimeMaptable[0])) {
                    type = amimeMaptable[1];
                }
            }
        }
        return type;
    }


    /**
     * 文件路径转 Uri
     *
     * @param path
     * @return
     */
    public static Uri pathToUri(String path) {
        if (!TextUtils.isEmpty(path)) {
            return Uri.parse(path);
        }
        return null;
    }

    /**
     * Uri转文件Path
     *
     * @param uri 文件 uri
     * @return
     */
    public static String uriToPath(Uri uri) {
        String path = null;
        try {
            if (null != uri) {
                String scheme = uri.getScheme();
                if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
                    path = uri.getPath();
                } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                    Cursor cursor = UBaseApplication.getApplication().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    if (null != cursor) {
                        if (cursor.moveToFirst()) {
                            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            if (index > -1) {
                                path = cursor.getString(index);
                            }
                        }
                        cursor.close();
                    }
                    if (path == null) {
                        path = getImageAbsolutePath(UBaseApplication.getApplication(), uri);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    private static final String[][] MIME_MAP_TABLE = {
            //{后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /**
     * 获取文件大小byte值
     *
     * @param file 文件
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert fis != null;
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    /**
     * 获取文件大小
     *
     * @param path 文件路径
     * @return
     */
    public static long getFileSize(String path) {
        File file = new File(path);
        return getFileSize(file);
    }


    /**
     * 删除文件
     *
     * @param file 文件
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            // 判断是否是文件
            if (file.isFile()) {
                file.delete();
                // 否则如果它是一个目录
            } else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File[] files = file.listFiles();
                // 遍历目录下所有的文件
                for (File file1 : files) {
                    // 把每个文件 用这个方法进行迭代
                    deleteFile(file1);
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    deleteFile(file1);
                }
            }
            file.delete();
        }
    }

    /**
     * 获取指定文件夹
     *
     * @param f 文件
     * @return 文件夹大小
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                size = size + getFileSizes(file);
            } else {
                size = size + getFileSize(file);
            }
        }
        return size;
    }

    /**
     * 计算文件大小
     *
     * @param fileS
     * @return
     */
    private static String countFileSize(long fileS) {
        NumberFormat numberFormat = DecimalFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = numberFormat.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = numberFormat.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = numberFormat.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = numberFormat.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
        }
        return fileSizeLong;
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.getLastPathSegment();
            }
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    /**
     * 是否是下载文件夹
     *
     * @param uri 检查文件 Uri是否是下载文件夹.
     * @return boolean 值.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 是否是多媒体件夹
     *
     * @param uri 文件 Uri是否是多媒体件夹.
     * @return boolean 值.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 是否是 Google 照片文件夹.
     *
     * @param uri 文件 Uri
     * @return boolean 值.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 是否是外部储存文档
     *
     * @param uri 文件 uri
     * @return
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean writeToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file.getAbsoluteFile());
            byte[] bytes = new byte[1024 * 10];
            int read;
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 保存图片到文件File。
     *
     * @param src  源图片
     * @param file 要保存到的文件
     * @return true 成功 false 失败
     */
    public static boolean writeToFile(Bitmap src, File file) {
        if (isEmptyBitmap(src))
            return false;
        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(Bitmap.CompressFormat.JPEG, 100, os);
            if (!src.isRecycled())
                src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * 创建文件 -- 项目缓存中
     *
     * @return
     */
    public static File createCacheFile(Context context, String fileName) {
        WeakReference<Context> mContext = new WeakReference<>(context);
        File fileDir = mContext.get().getExternalCacheDir();
        return new File(fileDir, fileName);
    }

    /**
     * 创建缓存文件 -- 项目文件下（未验证）
     *
     * @return
     */
    public static File createFile(Context context, String fileName) {
        WeakReference<Context> mContext = new WeakReference<>(context);
        File fileDir = mContext.get().getExternalFilesDir("/");
        return new File(fileDir, fileName);
    }

    public static File createDownFile(Context context, String fileName) {
        File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!fileDir.exists()) fileDir.mkdirs();
        return new File(fileDir, fileName);
    }
}