package org.blue.automation;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;

import java.util.List;
import java.util.concurrent.*;

public class Main extends Application {
    private static final Logger log = LogManager.getLogger(Main.class);
    public static Stage PRIMARY_STAGE;
    public static ExecutorService THREAD_POOL = new ThreadPoolExecutor(10, 16, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50), new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void init() {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            log.info("opencv链接库加载完成");
        }catch (Exception e){
            log.error("opencv链接库加载失败");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        PRIMARY_STAGE = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/index.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("蓝色");
        primaryStage.setScene(new Scene(root, 300, 240));
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
        log.info("程序启动完成");

        primaryStage.setOnCloseRequest(event -> {
            log.info("程序运行结束");
            THREAD_POOL.shutdown();
            try {
                if(THREAD_POOL.awaitTermination(3,TimeUnit.SECONDS)){
                    List<Runnable> runnableList = THREAD_POOL.shutdownNow();
                    log.debug("强制关闭的线程为:{}",runnableList);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("线程池已关闭");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
