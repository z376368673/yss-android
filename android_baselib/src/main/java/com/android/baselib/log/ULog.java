package com.android.baselib.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.*;
import java.util.Locale;


/**
 * 日志维护
 *
 * @author PF-NAN
 * @date 2018/11/2
 */
public class ULog {

    private static final String mDefTag = "Log";
    private static boolean isDebug = false;

    public static void setDebug(boolean isDebug) {
        ULog.isDebug = isDebug;
    }

    public static void v(String... content) {
        if (!isDebug) return;
        for (String s :
                content) {
            print(mDefTag, LogLevel.V, s);

        }

    }

    public static void vT(String tag, String... content) {
        if (!isDebug) return;
        for (String s :
                content) {
            print(tag, LogLevel.V, s);
        }
    }

    public static void d(String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(mDefTag, LogLevel.D, s);
        }
    }

    public static void dT(String tag, String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(tag, LogLevel.D, s);
        }
    }

    public static void i(String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(mDefTag, LogLevel.I, s);
        }
    }

    public static void iT(String tag, String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(tag, LogLevel.I, s);
        }
    }

    public static void w(String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(mDefTag, LogLevel.W, s);
        }
    }

    public static void wT(String tag, String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(tag, LogLevel.W, s);
        }
    }

    public static void e(String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(mDefTag, LogLevel.E, s);
        }
    }

    public static void eT(String tag, String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(tag, LogLevel.E, s);
        }
    }

    public static void a(String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(mDefTag, LogLevel.A, s);
        }
    }

    public static void aT(String tag, String... content) {
        if (!isDebug) return;
        for (String s : content) {
            print(tag, LogLevel.A, s);
        }
    }


    public static void e(Throwable throwable) {
        if (!isDebug) return;
        print(mDefTag, LogLevel.E, Log.getStackTraceString(throwable));
    }

    public static void eT(String tag, Throwable throwable) {
        if (!isDebug) return;
        print(tag, LogLevel.E, Log.getStackTraceString(throwable));
    }

    public static void e(String content, Throwable throwable) {
        if (!isDebug) return;
        print(mDefTag, LogLevel.E, content);
        print(mDefTag, LogLevel.E, Log.getStackTraceString(throwable));
    }

    /**
     * [tag] 标识
     * [rank] 级别
     * [content] 内容
     */
    private static void print(String tag, LogLevel rank, String content) {
        String logTag = mDefTag;
        if (!TextUtils.isEmpty(tag)) logTag = tag;
        if (rank == LogLevel.V) Log.v(logTag, content);
        if (rank == LogLevel.D) Log.d(logTag, content);
        if (rank == LogLevel.I) Log.i(logTag, content);
        if (rank == LogLevel.W) Log.w(logTag, content);
        if (rank == LogLevel.E) Log.e(logTag, content);
        if (rank == LogLevel.A) Log.wtf(logTag, content);

    }

    /**
     * 保存日志到文件
     *
     * @param context
     * @param msgContent
     */
    public synchronized static void file(Context context, String msgContent) {
        File logFileDir = context.getExternalFilesDir("npfLog");
        File mLogFile = new File(logFileDir, "npglog.log");
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mLogFile, true), "UTF-8"));
            out.write(msgContent);
        } catch (Exception e) {
            e.printStackTrace();
            mLogFile = null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
