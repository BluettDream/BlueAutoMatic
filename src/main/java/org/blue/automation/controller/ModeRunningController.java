package org.blue.automation.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.SituationBase;
import org.blue.automation.thread.ModeCallable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * name: MengHao Tian
 * date: 2022/5/6 18:47
 */
public class ModeRunningController implements Initializable {
    private final static Logger log = LogManager.getLogger(ModeRunningController.class);
    @FXML
    private ProgressBar PROGRESS_MAX_WAIT_TIME;
    @FXML
    private ListView<SituationBase> LISTVIEW_SITUATION;
    @FXML
    private Label LABEL_RESULT_SHOW;
    @FXML
    private Label LABEL_TIME;

    private static final SimpleStringProperty RESULT = new SimpleStringProperty();
    private static final SimpleDoubleProperty WAIT_TIME = new SimpleDoubleProperty();
    private static final SimpleListProperty<SituationBase> SITUATION_LIST = new SimpleListProperty<>(FXCollections.observableArrayList());
    private static GlobalKeyboardHook keyboardHook;

    /**
     * 主线程池
     **/
    private final ExecutorService THREAD_POOL = Main.THREAD_POOL;
    /**
     * 正在运行的模式线程
     **/
    private static Future<Boolean> RUNNING_MODE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ModeCallable callable = new ModeCallable();
        RUNNING_MODE = THREAD_POOL.submit(callable);
        LISTVIEW_SITUATION.setCellFactory(TextFieldListCell.forListView(new StringConverter<SituationBase>() {
            @Override
            public String toString(SituationBase object) {
                return object.getName()+":"+object.getRealSimile();
            }

            @Override
            public SituationBase fromString(String string) {
                return null;
            }
        }));
        Main.STAGE_MAP.get("modeRunningStage").setOnCloseRequest(event -> RUNNING_MODE.cancel(true));
        RESULT.bindBidirectional(LABEL_RESULT_SHOW.textProperty());
        PROGRESS_MAX_WAIT_TIME.progressProperty().bindBidirectional(WAIT_TIME);
        PROGRESS_MAX_WAIT_TIME.progressProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue() >= 1){
                RUNNING_MODE.cancel(true);
                LABEL_RESULT_SHOW.setText("运行结束");
            }
        });
        LISTVIEW_SITUATION.itemsProperty().bindBidirectional(SITUATION_LIST);
        THREAD_POOL.execute(()->{
            try {
                RUNNING_MODE.get();
                //模式运行完毕自动关闭线程
                RUNNING_MODE.cancel(true);
                Platform.runLater(()->{
                    PROGRESS_MAX_WAIT_TIME.setProgress(1.0);
                    LABEL_RESULT_SHOW.setText("运行结束");
                });
                //模式取消则关闭全局键盘监听
                keyboardHook.shutdownHook();
            }catch (Exception e){
                RUNNING_MODE.cancel(true);
                keyboardHook.shutdownHook();
                Platform.runLater(()->{
                    PROGRESS_MAX_WAIT_TIME.setProgress(1.0);
                    LABEL_RESULT_SHOW.setText("运行结束");
                });
                Thread.currentThread().interrupt();
            }
            log.info("设置模式取消线程运行结束,当前全局键盘监听:{}",keyboardHook.isAlive());
        });
        THREAD_POOL.execute(()->{
            try {
                String hStr,mStr,sStr;
                int h, m, s;
                for (h = 0; h < 12 && !RUNNING_MODE.isDone(); h++) {
                    for (m = 0; m < 60 && !RUNNING_MODE.isDone(); m++) {
                        for (s = 0; s < 60 && !RUNNING_MODE.isDone(); s++) {
                            Thread.sleep(1000);
                            sStr = String.valueOf(s);
                            mStr = String.valueOf(m);
                            hStr = String.valueOf(h);
                            if(sStr.length() < 2) sStr = "0" + sStr;
                            if(mStr.length() < 2) mStr = "0" + mStr;
                            if(hStr.length() < 2) hStr = "0" + hStr;
                            String finalHStr = hStr;
                            String finalMStr = mStr;
                            String finalSStr = sStr;
                            Platform.runLater(()->LABEL_TIME.setText(finalHStr + ":" + finalMStr + ":" + finalSStr));
                        }
                    }
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            log.info("计时线程运行结束");
        });
        THREAD_POOL.execute(()->{
            keyboardHook = new GlobalKeyboardHook(true);
            //实现tab+s键关闭模式运行
            keyboardHook.addKeyListener(new GlobalKeyAdapter() {
                private int pre = 0;
                @Override
                public void keyPressed(GlobalKeyEvent event) {
                    if(pre == 0) pre = event.getVirtualKeyCode();
                    if(pre != GlobalKeyEvent.VK_TAB){
                        pre = 0;
                        return;
                    }
                    if(event.getVirtualKeyCode() == GlobalKeyEvent.VK_TAB) return;
                    if(pre == GlobalKeyEvent.VK_TAB && event.getVirtualKeyCode() == GlobalKeyEvent.VK_S){
                        RUNNING_MODE.cancel(true);
                        keyboardHook.shutdownHook();
                    }
                    pre = 0;
                }
                @Override
                public void keyReleased(GlobalKeyEvent event) {
                }
            });
            log.info("监听键盘事件线程运行开始,当前全局键盘监听:{}",keyboardHook.isAlive());
        });
    }

    public static SimpleStringProperty resultProperty() {
        return RESULT;
    }

    public static SimpleDoubleProperty WAIT_TIMEProperty() {
        return WAIT_TIME;
    }

    public static SimpleListProperty<SituationBase> SITUATION_LISTProperty() {
        return SITUATION_LIST;
    }

    public static Future<Boolean> getRunningMode() {
        return RUNNING_MODE;
    }
}
