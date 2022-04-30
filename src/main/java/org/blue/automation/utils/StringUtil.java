package org.blue.automation.utils;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:53
 */
public class StringUtil {

    public static boolean isWrong(String str){
        return str == null || str.equals("") || str.contains(" ");
    }

}
