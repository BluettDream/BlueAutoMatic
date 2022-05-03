package org.blue.automation.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.Mode;
import org.blue.automation.services.ModeService;
import org.blue.automation.utils.FileUtil;
import org.blue.automation.utils.StringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/29 22:21
 */
public class ModeServiceImpl implements ModeService {
    private static final Logger log = LogManager.getLogger(ModeServiceImpl.class);
    private final ObjectMapper objectMapper = FileUtil.getInstance().getObjectMapper();

    private String fileName;
    public ModeServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ArrayList<Mode> selectAllModes() {
        try {
            return objectMapper.readValue(
                    new InputStreamReader(new FileInputStream(PathEnum.JSON + fileName), StandardCharsets.UTF_8)
                    , new TypeReference<ArrayList<Mode>>() {
                    }
            );
        } catch (IOException e) {
            log.error("文件读取异常:", e);
        }
        return null;
    }

    @Override
    public Mode selectModeByName(String name) {
        if (StringUtil.isWrong(name)) return null;
        ArrayList<Mode> modeProperties = selectAllModes();
        if (modeProperties == null) return null;
        for (Mode mode : modeProperties) {
            if (mode.getName().equals(name)) return mode;
        }
        return null;
    }

    @Override
    public boolean addMode(Mode mode) {
        if (mode == null || StringUtil.isWrong(mode.getName())) return false;
        ArrayList<Mode> modeProperties = selectAllModes();
        if (modeProperties == null || (modeProperties.size() > 0
                && modeProperties.stream().anyMatch(originMode -> originMode.getName().equals(mode.getName()))))
            return false;
        return modeProperties.add(mode) && write(modeProperties);
    }

    @Override
    public boolean updateMode(Mode mode) {
        if (mode == null || StringUtil.isWrong(mode.getName())) return false;
        ArrayList<Mode> modeProperties = selectAllModes();
        if(modeProperties == null) return false;
        for (int i = 0; i < modeProperties.size(); ++i) {
            if (modeProperties.get(i).getName().equals(mode.getName())) {
                modeProperties.set(i, mode);
                return write(modeProperties);
            }
        }
        return false;
    }

    @Override
    public boolean deleteModeByName(String name) {
        if (StringUtil.isWrong(name)) return false;
        ArrayList<Mode> modeProperties = selectAllModes();
        return modeProperties != null && modeProperties.removeIf(originMode -> originMode.getName().equals(name)) && write(modeProperties);
    }

    private boolean write(ArrayList<Mode> modeArrayList) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                    new OutputStreamWriter(new FileOutputStream(PathEnum.JSON + fileName), StandardCharsets.UTF_8)
                    , modeArrayList);
            return true;
        } catch (IOException e) {
            log.error("模式写入文件异常:", e);
        }
        return false;
    }

}
