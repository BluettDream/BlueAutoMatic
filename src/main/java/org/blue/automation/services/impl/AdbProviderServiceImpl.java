package org.blue.automation.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.AdbProvider;
import org.blue.automation.services.AdbProviderService;
import org.blue.automation.utils.FileUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * name: MengHao Tian
 * date: 2022/5/3 18:17
 */
public class AdbProviderServiceImpl implements AdbProviderService {
    private final static Logger log = LogManager.getLogger(AdbOperationServiceImpl.class);
    private final ObjectMapper objectMapper = FileUtil.getInstance().getObjectMapper();
    private String filePath;

    public AdbProviderServiceImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public AdbProvider getAdbProvider() {
        try {
            return objectMapper.readValue(
                    new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)
                    , AdbProvider.class
            );
        } catch (IOException e) {
            log.error("文件读取异常:", e);
        }
        return null;
    }

    @Override
    public boolean setAdbProvider(AdbProvider adbProvider) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(
                    new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)
                    , adbProvider);
            return true;
        } catch (IOException e) {
            log.error("adb写入文件异常:", e);
        }
        return false;
    }

}
