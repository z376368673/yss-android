package com.android.baselib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.baselib.UBaseApplication;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UToolsUtils {
    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context){
        return context.getPackageName();
    }

    /**
     * 获取VersionName(版本名称)
     * @param context
     * @return
     * 失败时返回""
     */
    public static String getVersionName(Context context){
        PackageManager packageManager = getPackageManager(context);
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(context), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取VersionCode(版本号)
     * @param context
     * @return
     * 失败时返回-1
     */
    public static int getVersionCode(Context context){
        PackageManager packageManager = getPackageManager(context);
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(context), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取所有安装的应用程序,不包含系统应用
     * @param context
     * @return
     */
    public static List<PackageInfo> getInstalledPackages(Context context){
        PackageManager packageManager = getPackageManager(context);
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<PackageInfo> packageInfoList  = new ArrayList<PackageInfo>();
        for(int i=0; i < packageInfos.size();i++){
            if ((packageInfos.get(i).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                packageInfoList.add(packageInfos.get(i));
            }
        }
        return packageInfoList;
    }

    /**
     * 获取应用程序的icon图标
     * @param context
     * @return
     * 当包名错误时，返回null
     */
    public static Drawable getApplicationIcon(Context context){
        PackageManager packageManager = getPackageManager(context);
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(context), 0);
            return packageInfo.applicationInfo.loadIcon(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 启动安装应用程序
     * @param activity
     * @param path	应用程序路径
     */
    public static void installApk(Activity activity, String path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    /**
     * 获取PackageManager对象
     * @param context
     * @return
     */
    private static PackageManager getPackageManager(Context context){
        return context.getPackageManager();
    }

    /**
     * 获取assets目录下的单个文件转string
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFileFromAssetsFile(Context context, String fileName) {
        //这种方式不能用，只能用于webview加载，直接取路径是不行的
        String strings="";
        try {
            InputStream is = context.getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            strings=new String(buffer, "utf8");

        }catch (IOException e){
            e.printStackTrace();
        }
        return strings;

    }


    public static void setTextRegularStyle(TextView view, boolean isBold){
        //设置字体
        Typeface typeface=Typeface.createFromAsset(UBaseApplication.getApplication().getAssets(),"PingFang Regular.ttf");
        if(isBold) {
            view.setTypeface(typeface, Typeface.BOLD);
        }else{
            view.setTypeface(typeface,Typeface.NORMAL);
        }
    }

    public static void setTextMediumStyle(TextView view, boolean isBold){
        //设置字体
        Typeface typeface=Typeface.createFromAsset(UBaseApplication.getApplication().getAssets(),"PingFang Medium.ttf");
        if(isBold) {
            view.setTypeface(typeface, Typeface.BOLD);
        }else{
            view.setTypeface(typeface,Typeface.NORMAL);
        }
    }

    //保留小数点后几位
    public static Double getDoubleValue(double cent, int length){
        BigDecimal b=new BigDecimal(cent);
        return b.setScale(length,BigDecimal.ROUND_HALF_UP).doubleValue();//保留小数点后几位（length）
    }


    /**
     * Desc: 获取虚拟按键高度 放到工具类里面直接调用即可
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if(Build.VERSION.SDK_INT >Build.VERSION_CODES.N){
            if (hasNavBar(context)) {
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId);
                }
           }
        }
        Log.e("getNavigationBarHeight","虚拟键盘高度"+result);
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
//        Resources res = context.getResources();
//        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
//        if (resourceId != 0) {
//            boolean hasNav = res.getBoolean(resourceId);
//            // check override flag
//            String sNavBarOverride = getNavBarOverride();
//            if ("1".equals(sNavBarOverride)) {
//                hasNav = false;
//            } else if ("0".equals(sNavBarOverride)) {
//                hasNav = true;
//            }
//            return hasNav;
//        } else { // fallback
//            return !ViewConfiguration.get(context).hasPermanentMenuKey();
//        }

        WindowManager windowManager = ((Activity)context).getWindowManager();

        Display d = windowManager.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;


        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }
}
