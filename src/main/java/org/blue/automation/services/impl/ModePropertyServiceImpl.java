package org.blue.automation.services.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.vo.ModeProperty;
import org.blue.automation.services.ModePropertyService;
import org.blue.automation.utils.StringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:21
 */
public class ModePropertyServiceImpl implements ModePropertyService {
    private static final Logger log = LogManager.getLogger(ModePropertyServiceImpl.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        //空对象不抛异常,取消时间戳显示,取消空对象转换失败
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS
                ,SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
                ,SerializationFeature.FAIL_ON_EMPTY_BEANS);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //设置空时为空对象不为null
        OBJECT_MAPPER.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeObject(new Object());//空对象
            }
        });
    }

    private String fileName;
    public ModePropertyServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ArrayList<ModeProperty> selectAllModeProperties() {
        try {
            return OBJECT_MAPPER.readValue(
                    new InputStreamReader(new FileInputStream(PathEnum.JSON + fileName), StandardCharsets.UTF_8)
                    , new TypeReference<ArrayList<ModeProperty>>() {
                    }
            );
        } catch (IOException e) {
            log.error("文件读取异常:", e);
        }
        return null;
    }

    @Override
    public ModeProperty selectModePropertyByName(String name) {
        if (StringUtil.isWrong(name)) return null;
        ArrayList<ModeProperty> modeProperties = selectAllModeProperties();
        if (modeProperties == null) return null;
        for (ModeProperty modeProperty : modeProperties) {
            if (modeProperty.getName().equals(name)) return modeProperty;
        }
        return null;
    }

    @Override
    public boolean addModeProperty(ModeProperty modeProperty) {
        if (modeProperty == null || StringUtil.isWrong(modeProperty.getName())) return false;
        ArrayList<ModeProperty> modeProperties = selectAllModeProperties();
        if (modeProperties == null || (modeProperties.size() > 0
                && modeProperties.stream().anyMatch(originMode -> originMode.getName().equals(modeProperty.getName()))))
            return false;
        return modeProperties.add(modeProperty) && write(modeProperties);
    }

    @Override
    public boolean updateModeProperty(ModeProperty modeProperty) {
        if (modeProperty == null || StringUtil.isWrong(modeProperty.getName())) return false;
        ArrayList<ModeProperty> modeProperties = selectAllModeProperties();
        if(modeProperties == null) return false;
        for (int i = 0; i < modeProperties.size(); ++i) {
            if (modeProperties.get(i).getName().equals(modeProperty.getName())) {
                modeProperties.set(i, modeProperty);
                return write(modeProperties);
            }
        }
        return false;
    }

    @Override
    public boolean deleteModePropertyByName(String name) {
        if (StringUtil.isWrong(name)) return false;
        ArrayList<ModeProperty> modeProperties = selectAllModeProperties();
        return modeProperties != null && modeProperties.removeIf(originMode -> originMode.getName().equals(name)) && write(modeProperties);
    }

    private boolean write(ArrayList<ModeProperty> modeArrayList) {
        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(
                    new OutputStreamWriter(new FileOutputStream(PathEnum.JSON + fileName), StandardCharsets.UTF_8)
                    , modeArrayList);
            return true;
        } catch (IOException e) {
            log.error("模式写入文件异常:", e);
        }
        return false;
    }

}
