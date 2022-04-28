package org.blue.automation.utils;

import com.mayabot.nlp.module.pinyin.PinyinResult;
import com.mayabot.nlp.module.pinyin.Pinyins;

/**
 * name: MengHao Tian
 * date: 2022/4/28 17:50
 */
public class PinYinUtil {

    public String getPinYin(String hanZi){
        PinyinResult result = Pinyins.convert(hanZi).keepNum(true).keepPunctuation(true);
        String[] split = result.asString().split(" ");
        StringBuilder builder = new StringBuilder();
        for (String str : split) {
            char[] chars = str.toCharArray();
            chars[0] = chars[0] >= 'a' && chars[0] <= 'z' ? (char) (chars[0] ^ 32) : chars[0];
            builder.append(chars);
        }
        return builder.toString();
    }

    private PinYinUtil(){}
    private static class PinYinUtilHolder{
        private final static PinYinUtil INSTANCE = new PinYinUtil();
    }
    public static PinYinUtil getInstance(){
        return PinYinUtilHolder.INSTANCE;
    }
}
