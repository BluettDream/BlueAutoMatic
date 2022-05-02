package org.blue.automation.utils;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:53
 */
public class StringUtil {

    /**
     * 判断字符串是否为null,""或包含空格
     *
     * @param str 字符串
     * @return 字符串如果满足条件中的一种则返回true,都不满足则返回false
     **/
    public static boolean isWrong(String str){
        return str == null || str.equals("") || str.contains(" ");
    }

    /**
     * 判断字符串是否是小数
     *
     * @param str 字符串
     * @return 是小数/整数返回true,否则为false
     **/
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

    /**
     * 判断字符串是否为整数
     *
     * @param str 字符串
     * @return 整数返回true,否则返回false
     **/
    public static boolean isInteger(String str){
        if(isWrong(str)) return false;
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if(c < '0' || c > '9') return false;
        }
        return true;
    }

}
