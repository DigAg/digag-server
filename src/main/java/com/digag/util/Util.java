package com.digag.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuicon on 2017/8/8.
 * https://github.com/Yuicon
 */
public class Util {

    private Util() {
    }

    /**
     * 验证邮箱
     *
     * @param email 邮箱
     * @return 正确返回 true
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static void main(String[] args) {
        System.out.print(checkEmail("910@q.com"));
    }
}
