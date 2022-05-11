package org.blue.automation.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.ModeBase;
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
    public ArrayList<ModeBase> selectAllModes() {
        try {
            return objectMapper.readValue(
                    new InputStreamReader(new FileInputStream(PathEnum.JSON + fileName), StandardCharsets.UTF_8)
                    , new TypeReference<ArrayList<ModeBase>>() {
                    }
            );
        } catch (IOException e) {
            log.error("文件读取异常:", e);
        }
        return null;
    }

    @Override
    public ModeBase selectModeByName(String name) {
        if (StringUtil.isWrong(name)) return null;
        ArrayList<ModeBase> modeBaseProperties = selectAllModes();
        if (modeBaseProperties == null) return null;
        for (ModeBase modeBase : modeBaseProperties) {
            if (modeBase.getName().equals(name)) return modeBase;
        }
        return null;
    }

    @Override
    public boolean addMode(ModeBase modeBase) {
        if (modeBase == null || StringUtil.isWrong(modeBase.getName())) return false;
        ArrayList<ModeBase> modeBaseProperties = selectAllModes();
        if (modeBaseProperties == null || (modeBaseProperties.size() > 0
                && modeBaseProperties.stream().anyMatch(originMode -> originMode.getName().equals(modeBase.getName()))))
            return false;
        return modeBaseProperties.add(modeBase) && write(modeBaseProperties);
    }

    @Override
    public boolean updateMode(ModeBase modeBase) {
        if (modeBase == null || StringUtil.isWrong(modeBase.getName())) return false;
        ArrayList<ModeBase> modeBaseProperties = selectAllModes();
        if(modeBaseProperties == null) return false;
        for (int i = 0; i < modeBaseProperties.size(); ++i) {
            if (modeBaseProperties.get(i).getName().equals(modeBase.getName())) {
                modeBaseProperties.set(i, modeBase);
                return write(modeBaseProperties);
            }
        }
        return false;
    }

    @Override
    public boolean deleteModeByName(String name) {
        if (StringUtil.isWrong(name)) return false;
        ArrayList<ModeBase> modeBaseProperties = selectAllModes();
        return modeBaseProperties != null && modeBaseProperties.removeIf(originMode -> originMode.getName().equals(name)) && write(modeBaseProperties);
    }

    private boolean write(ArrayList<ModeBase> modeBaseArrayList) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                    new OutputStreamWriter(new FileOutputStream(PathEnum.JSON + fileName), StandardCharsets.UTF_8)
                    , modeBaseArrayList);
            return true;
        } catch (IOException e) {
            log.error("模式写入文件异常:", e);
        }
        return false;
    }

}
