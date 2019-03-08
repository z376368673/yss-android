package com.android.baselib.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤工具类
 *
 * Author:37636
 * QQ:376368673
 * Time:2018/11/26
 * Description:This is UInputFilter
 */
public class UFilter {

    //输入过滤 只能输入汉字,英文，数字
    public static InputFilter Input_String= new InputFilter() {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (!matcher.find()) {
                return null;
            } else {
                UToastUtil.showToastShort("只能输入汉字,英文，数字");
                return "";
            }
        }
    };

}
