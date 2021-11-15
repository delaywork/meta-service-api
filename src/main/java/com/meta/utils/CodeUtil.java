package com.meta.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;

public class CodeUtil {

    /**
     * 生成指定位数的纯数字图形验证码
     * */
    public static String getNumberCodeCaptcha(int length){
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", length);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(randomGenerator);
        lineCaptcha.createCode();
        String code = lineCaptcha.getCode();
        return code;
    }

    /**
     * 生成指定位数的数字验证码
     * */
    public static String getNumberCode(int length) {
        String retStr = "";
        String strTable = "1234567890";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }


}
