package org.blue.automation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

/**
 * name: MengHao Tian
 * date: 2022/5/3 17:55
 */
public class FileUtil {
    public ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        //空对象不抛异常,取消时间戳显示
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS
                ,SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }

    private FileUtil(){}
    private static class FileUtilHolder{
        private final static FileUtil INSTANCE = new FileUtil();
    }
    public static FileUtil getInstance(){
        return FileUtilHolder.INSTANCE;
    }
}
