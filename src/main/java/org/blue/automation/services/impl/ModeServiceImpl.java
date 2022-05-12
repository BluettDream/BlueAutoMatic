package org.blue.automation.services.impl;

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


    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ArrayList<ModeBase> selectAllModes() {
        String[] modeFileList = new File(PathEnum.MODE.getPath()).list();
        if (modeFileList == null) return null;
        //遍历模式文件夹中的文件
        ArrayList<ModeBase> modeBases = new ArrayList<>();
        for (String modeFile : modeFileList) {
            modeBases.add(read(modeFile));
        }
        return modeBases;
    }

    @Override
    public ModeBase selectModeByName(String name) {
        if (StringUtil.isWrong(name)) return null;
        String[] modeFileList = new File(PathEnum.MODE.getPath()).list();
        if (modeFileList == null) return null;
        for (String modeFile : modeFileList) {
            if (modeFile.equals(name + ".json")) return read(name+".json");
        }
        return null;
    }

    @Override
    public boolean addMode(ModeBase modeBase) {
        if (modeBase == null || StringUtil.isWrong(modeBase.getName())) return false;
        String[] modeFileList = new File(PathEnum.MODE.getPath()).list();
        if (modeFileList == null) return false;
        for (String modeFile : modeFileList) {
            //已经存在则更新模式
            if (modeFile.equals(modeBase.getName() + ".json")) return updateMode(modeBase);
        }
        File file = new File(PathEnum.MODE+modeBase.getName()+".json");
        boolean result = false;
        try {
            result = file.createNewFile();
        } catch (IOException e) {
            log.error("创建模式文件异常:",e);
        }
        return result && write(modeBase);
    }

    @Override
    public boolean updateMode(ModeBase modeBase) {
        if (modeBase == null || StringUtil.isWrong(modeBase.getName())) return false;
        String[] modeFileList = new File(PathEnum.MODE.getPath()).list();
        if (modeFileList == null) return false;
        for (String modeFile : modeFileList) {
            if (modeFile.equals(modeBase.getName() + ".json")) return write(modeBase);
        }
        return false;
    }

    @Override
    public boolean deleteModeByName(String name) {
        if (StringUtil.isWrong(name)) return false;
        String[] modeFileList = new File(PathEnum.MODE.getPath()).list();
        if (modeFileList == null) return true;
        for (String modeFile : modeFileList) {
            if (modeFile.equals(name + ".json")) {
                File file = new File(PathEnum.MODE + name + ".json");
                if (file.exists()) return file.delete();
            }
        }
        return false;
    }

    private ModeBase read(String fileName) {
        ModeBase modeBase = null;
        try {
            modeBase = objectMapper.readValue(
                    new InputStreamReader(new FileInputStream(PathEnum.MODE + fileName), StandardCharsets.UTF_8)
                    , ModeBase.class);
        } catch (IOException e) {
            log.error("模式文件读取异常:", e);
        }
        return modeBase;
    }

    private boolean write(ModeBase modeBase) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                    new OutputStreamWriter(new FileOutputStream(PathEnum.MODE + modeBase.getName() + ".json"), StandardCharsets.UTF_8)
                    , modeBase);
            return true;
        } catch (IOException e) {
            log.error("模式文件写入异常:", e);
        }
        return false;
    }

}
