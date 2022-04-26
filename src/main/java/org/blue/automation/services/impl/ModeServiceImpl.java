package org.blue.automation.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.services.ModeService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * name: MengHao Tian
 * date: 2022/4/24 18:22
 */
public class ModeServiceImpl implements ModeService {
    private static final Logger log = LogManager.getLogger(ModeServiceImpl.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private String File_Name = "test.json";

    @Override
    public ArrayList<Mode> getAllModes() {
        try {
            return OBJECT_MAPPER.readValue(new InputStreamReader(
                    new FileInputStream(PathEnum.JSON + File_Name), StandardCharsets.UTF_8
            ), new TypeReference<ArrayList<Mode>>() {
            });
        } catch (IOException e) {
            log.error("模式获取异常", e);
        }
        return new ArrayList<>();
    }

    @Override
    public Mode getModeByName(String name) {
        //获取所有的模式，根据名称返回指定的模式
        for (Mode mode : getAllModes()) {
            if (mode.getName().equals(name)) {
                return mode;
            }
        }
        return null;
    }

    @Override
    public boolean addMode(Mode mode) {
        if (mode == null) return false;
        try {
            //获取所有的模式
            ArrayList<Mode> modeArrayList = getAllModes();
            //添加新模式到列表中
            modeArrayList.add(mode);
            writeToFile(PathEnum.JSON + File_Name, modeArrayList);
            return true;
        } catch (IOException e) {
            log.error("添加模式异常:", e);
        }
        return false;
    }

    @Override
    public boolean updateMode(Mode mode) {
        if (mode == null) return false;
        try {
            //获取所有的模式
            ArrayList<Mode> modeArrayList = getAllModes();
            //遍历模式，根据模式名称找到要更新的模式，替换为对应模式
            for (int i = 0; i < modeArrayList.size(); ++i) {
                if (modeArrayList.get(i).getName().equals(mode.getName())) {
                    modeArrayList.set(i, mode);
                }
            }
            writeToFile(PathEnum.JSON + File_Name, modeArrayList);
            return true;
        } catch (IOException e) {
            log.error("更新模式异常:", e);
        }
        return false;
    }

    @Override
    public boolean deleteMode(Mode comparedMode) {
        if (comparedMode == null) return false;
        try {
            //获取模式列表
            ArrayList<Mode> modeArrayList = getAllModes();
            //根据模式的名称删除对应的模式
            if (modeArrayList.removeIf(mode -> mode.getName().equals(comparedMode.getName()))) {
                writeToFile(PathEnum.JSON + File_Name, modeArrayList);
                return true;
            }
        } catch (IOException e) {
            log.error("模式删除异常:", e);
        }
        return false;
    }

    @Override
    public void setFileName(String fileName) {
        this.File_Name = fileName;
    }

    /**
     * 将模式列表写入文件
     *
     * @param path          文件路径
     * @param modeArrayList 模式列表
     **/
    private void writeToFile(String path, ArrayList<Mode> modeArrayList) throws IOException {
        OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new OutputStreamWriter(
                new FileOutputStream(path), StandardCharsets.UTF_8
        ), modeArrayList);
    }
}
