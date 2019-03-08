package com.android.baselib.util;

import java.util.regex.Pattern;

/**
 * 常用的正则验证
 *
 * @author PF-NAN
 * @date 2018/3/12
 */

public class URegexUtil {

    /**
     * 手机号码.
     */
    private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    /**
     * 电话号码.
     */
    private static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * 15位身份证号码.
     */
    private static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 18位身份证号码.
     */
    private static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * email.
     */
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * url.
     */
    private static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    /**
     * 中文字符.
     */
    private static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 用户名只能是大小写字母,数字,和下划线"_",并且长度6~20位
     */
    private static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 时间格式 "yyyy-MM-dd".
     */
    private static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * IP地址.
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * QQ号码.
     */
    private static final String REGEX_QQ_NUM = "[1-9][0-9]{4,}";
    /**
     * 中国邮政编码.
     */
    private static final String REGEX_CHINA_POSTAL_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * 正整数.
     */
    private static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * 负整数.
     */
    private static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * 整数.
     */
    private static final String REGEX_INTEGER = "^-?[1-9]\\d*$";


    /**
     * 验证是否是手机号码
     *
     * @param input
     * @return
     */
    public static boolean isMobileExact(final CharSequence input) {
        return isMatch(REGEX_MOBILE_EXACT, input);
    }

    /**
     * 验证是否是电话号码
     *
     * @param input
     * @return
     */
    public static boolean isTel(final CharSequence input) {
        return isMatch(REGEX_TEL, input);
    }

    /**
     * 验证是否是15身份证
     *
     * @param input
     * @return
     */
    public static boolean isIDCard15(final CharSequence input) {
        return isMatch(REGEX_ID_CARD15, input);
    }

    /**
     * 验证是否是18位身份证号码
     *
     * @param input
     * @return
     */
    public static boolean isIDCard18(final CharSequence input) {
        return isMatch(REGEX_ID_CARD18, input);
    }

    /**
     * 验证是否是邮箱
     *
     * @param input
     * @return
     */
    public static boolean isEmail(final CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    /**
     * 验证是否是 Url
     *
     * @param input
     * @return
     */
    public static boolean isURL(final CharSequence input) {
        return isMatch(REGEX_URL, input);
    }

    /**
     * 验证是否是中文
     *
     * @param input
     * @return
     */
    public static boolean isZh(final CharSequence input) {
        return isMatch(REGEX_ZH, input);
    }

    /**
     * 用户名只能是大小写字母,数字,和下划线"_",并且长度6~20位
     *
     * @param input
     * @return
     */
    public static boolean isUserName(final CharSequence input) {
        return isMatch(REGEX_USERNAME, input);
    }

    /**
     * 验证日期格式
     *
     * @param input
     * @return
     */
    public static boolean isDate(final CharSequence input) {
        return isMatch(REGEX_DATE, input);
    }

    /**
     * 是否是 IP地址
     *
     * @param input
     * @return
     */
    public static boolean isIP(final CharSequence input) {
        return isMatch(REGEX_IP, input);
    }

    /**
     * 正则验证匹配
     *
     * @param regex 正则表达式
     * @param input 验证内容
     * @return boolean 值
     */
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }
}
