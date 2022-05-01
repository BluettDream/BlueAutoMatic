package org.blue.automation.utils;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:53
 */
public class StringUtil {

    public static boolean isWrong(String str){
        return str == null || str.equals("") || str.contains(" ");
    }

    public static boolean isDecimal(String str){
        if(isWrong(str)) return false;
        char[] chars = str.toCharArray();
        int dotCount = 0;
        for (char c : chars) {
            if(c < '0' || c > '9'){
                if(c == '.' && dotCount++ < 1) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public static boolean isInteger(String str){
        if(isWrong(str)) return false;
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if(c < '0' || c > '9') return false;
        }
        return true;
    }

}
