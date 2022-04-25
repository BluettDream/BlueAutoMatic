package org.blue.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * name: MengHao Tian
 * date: 2022/4/25 10:55
 */
public class CMDUtil {
    private static final Logger log = LogManager.getLogger(CMDUtil.class);
    /**
     * 执行一个cmd命令
     *
     * @param cmdCommand cmd命令
     * @return 命令执行结果字符串，如出现异常返回null
     */
    public String executeCMDCommand(String cmdCommand) {
        StringBuilder builder = null;
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmdCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            log.debug("cmd命令执行完毕:{}",cmdCommand);
        } catch (Exception e) {
            log.error("cmd命令执行失败:",e);
        }
        return builder == null ? null : builder.toString();
    }

    private CMDUtil(){}
    private static class CMDUtilHolder {
        private static final CMDUtil INSTANCE = new CMDUtil();
    }
    public static CMDUtil getInstance(){
        return CMDUtilHolder.INSTANCE;
    }
}
