package org.blue.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * name: MengHao Tian
 * date: 2022/4/25 10:55
 */
public class CMDUtil {
    private static final Logger log = LogManager.getLogger(CMDUtil.class);
    /**
     * 执行一个cmd命令
     *
     * @param command cmd命令
     * @return 命令执行结果字符串，如出现异常返回null
     */
    public String executeCMDCommand(String command) {
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        StringBuilder inputBuilder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();
        try {
            p = rt.exec(command);
            //获取进程的标准输入流
            final InputStream is1 = p.getInputStream();
            //获取进城的错误流
            final InputStream is2 = p.getErrorStream();
            //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
            new Thread(() -> {
                BufferedReader br1 = null;
                try {
                    br1 = new BufferedReader(new InputStreamReader(is1, "GB2312"));
                } catch (UnsupportedEncodingException e) {
                    log.error("字符流异常:",e);
                }
                try {
                    String line1;
                    while (br1 != null && (line1 = br1.readLine()) != null) {
                        inputBuilder.append(line1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                BufferedReader br2 = null;
                try {
                    br2 = new BufferedReader(new InputStreamReader(is2,"GB2312"));
                } catch (UnsupportedEncodingException e) {
                    log.error("字符流异常:",e);
                }
                try {
                    String line2;
                    while (br2 != null && (line2 = br2.readLine()) != null) {
                        errorBuilder.append(line2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            try {
                if(p != null){
                    p.getErrorStream().close();
                    p.getInputStream().close();
                    p.getOutputStream().close();
                }
            } catch (Exception ee) {
                log.error("命令流关闭异常:",e);
            }
        }
        return inputBuilder.length() > 0 ? inputBuilder.toString() : errorBuilder.toString();
    }

    private CMDUtil(){}
    private static class CMDUtilHolder {
        private static final CMDUtil INSTANCE = new CMDUtil();
    }
    public static CMDUtil getInstance(){
        return CMDUtilHolder.INSTANCE;
    }
}
