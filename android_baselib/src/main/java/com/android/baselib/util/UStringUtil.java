package com.android.baselib.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.android.baselib.UBaseApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String常用方法工具类
 *
 * @author PF-NAN
 * @date 2018/3/9
 */

public class UStringUtil {
    private static Pattern IS_NORMAL_STRING = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
    private static Pattern ALPHA = Pattern.compile("^[A-Za-z]+$");

    /**
     * 根据id获取strings中定义的string
     *
     * @param resId 指向string的id
     * @return String
     */
    public static String getString(@StringRes int resId) {
        return UBaseApplication.getApplication().getResources().getString(resId);
    }

    /**
     * 获取字符串数组资源
     *
     * @param resId
     * @return String[]
     */
    public static String[] getStringArray(@ArrayRes int resId) {
        return UBaseApplication.getApplication().getResources().getStringArray(resId);
    }

    /**
     * 校验String只能是数字,英文字母和中文
     */
    public static boolean isNormalStr(@NonNull String s) {
        Pattern p = IS_NORMAL_STRING;
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 获取汉语拼音首字母
     *
     * @param str
     * @return
     */
    public static String getAlpha(@NonNull String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = ALPHA;
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }

    /**
     * 判断是否为手机号
     *
     * @param phoneStr
     * @return
     */
    public static boolean isPhoneNumber(@NonNull String phoneStr) {
        if (TextUtils.isEmpty(phoneStr)) {
            return false;
        } else {
            String replace = phoneStr.replace(" ", "");
            return replace.matches("[1][3456789]\\d{9}");
        }
    }

    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(@NonNull String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 实现文本复制功能
     *
     * @param text 文本内容
     */
    public static void copy(@NonNull String text) {
        // 得到剪贴板管理器
        ClipboardManager clipboard = (ClipboardManager)
                UBaseApplication.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }

    /**
     * 实现粘贴功能
     *
     * @return
     */
    public static String paste() {
        // 得到剪贴板管理器
        ClipboardManager clipboard = (ClipboardManager)
                UBaseApplication.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        // 粘贴板有数据，并且是文本
        if (null != clipboard && clipboard.hasPrimaryClip()
                && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            CharSequence text = item.getText();
            return text.toString();
        }
        return "";
    }

    /**
     * 截取小数点后两位
     *
     * @param money 金额
     * @return
     */
    public static String cutOutDoubleMoney(double money) {
        String strMoney = money + "";
        String newMoney = strMoney;
        if (!TextUtils.isEmpty(strMoney)) {
            if (strMoney.contains(".")) {
                int index = strMoney.indexOf('.');
                String substring = strMoney.substring(index, strMoney.length());
                if (substring.length() > 3) {
                    newMoney = strMoney.substring(0, index) + substring.substring(0, 3);
                } else if (substring.length() == 2) {
                    newMoney = strMoney.substring(0, index) + substring + "0";
                } else if (substring.length() == 1) {
                    newMoney = strMoney.substring(0, index) + substring + "00";
                }
            } else {
                newMoney = strMoney + ".00";
            }
        }
        return newMoney;
    }

    /**
     * 截取小数点后两位
     *
     * @param money string 类型金额
     * @return
     */
    public static String cutOutDoubleMoney(@NonNull String money) {
        String newMoney = money;
        if (!TextUtils.isEmpty(money)) {
            if (money.contains(".")) {
                int index = money.indexOf('.');
                String substring = money.substring(index, money.length());
                if (substring.length() > 3) {
                    newMoney = money.substring(0, index) + substring.substring(0, 3);
                } else if (substring.length() == 2) {
                    newMoney = money.substring(0, index) + substring + "0";
                } else if (substring.length() == 1) {
                    newMoney = money.substring(0, index) + substring + "00";
                }
            } else {
                newMoney = money + ".00";
            }
        }
        return newMoney;
    }

    /**
     * 是否为 "" or null.
     *
     * @param s The string.
     * @return boolean 值
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 去空格后,是否为是否为 "" or null.
     *
     * @param s The string.
     * @return boolean 值
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 是否是空白字符串.
     *
     * @param s 字符串.
     * @return boolean 值
     */
    public static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两 string 是否相同.
     *
     * @param s1 string.
     * @param s2 string.
     * @return boolean 值
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) {
            return true;
        }
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 比较两 string 是否相同(忽略大小写).
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return boolean 值
     */
    public static boolean equalsIgnoreCase(final String s1, final String s2) {
        return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
    }

    /**
     * 获取首字母大写.
     *
     * @param s The string.
     * @return 大写首字母.
     */
    public static String upperFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 获取首字母小写.
     *
     * @param s The string.
     * @return 小写首字母.
     */
    public static String lowerFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 将String 反向序列.
     *
     * @param s The string.
     * @return 反向 string.
     */
    public static String reverse(final String s) {
        if (isEmpty(s)) {
            return "";
        }
        int len = s.length();
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }


    /**
     * *半角转换为全角
     * *
     * *@paraminput
     * *@return
     */
    public static String toDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }
}
