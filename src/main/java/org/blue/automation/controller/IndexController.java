package org.blue.automation.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.Mode;
import org.blue.automation.factories.UIControlFactory;
import org.blue.automation.thread.ModeCallable;
import org.blue.automation.services.ModeService;
import org.blue.automation.services.impl.ModeServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * name: MengHao Tian
 * date: 2022/4/24 15:08
 */
public class IndexController implements Initializable {
    private static final Logger log = LogManager.getLogger(IndexController.class);
    @FXML
    private ChoiceBox<Mode> CHOICE_MODE_LIST;

    @FXML
    private Button BUTTON_SWITCH;

    private final ExecutorService THREAD_POOL = Main.THREAD_POOL;
    private static final SimpleObjectProperty<Mode> CURRENT_MODE = new SimpleObjectProperty<>();
    private ModeService modeService;
    private Future<Boolean> runningMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modeService = new ModeServiceImpl();
        initChoiceModeList(modeService.getAllModes());
        initButtonSwitch();
    }

    @FXML
    void addMode() {
        TextInputDialog dialog = UIControlFactory.createTestInputDialog("添加模式", null, "请输入模式名称");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (modeService.addMode(new Mode(name, new ArrayList<>()))) {
                log.info("模式添加成功,新增模式:{}", name);
                updateChoiceModeList(modeService.getAllModes());
                return;
            }
            log.info("{}添加失败", name);
        });
    }

    @FXML
    void configureMode() {
        Stage settingStage = Main.STAGE_MAP.get("settingStage");
        if (settingStage == null) {
            settingStage = new Stage();
            settingStage.setTitle("模式配置");
            settingStage.setResizable(false);
            settingStage.setOnCloseRequest(event -> CURRENT_MODE.set(modeService.getModeByName(CURRENT_MODE.get().getName())));
            Main.STAGE_MAP.put("settingStage", settingStage);
        }
        try {
            settingStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/views/setting.fxml")).load(), 600, 400));
            settingStage.show();
        } catch (IOException e) {
            log.error("创建settingStage异常:", e);
        }
    }

    @FXML
    void deleteMode() {
        if (modeService.deleteMode(CURRENT_MODE.get())) {
            log.info("{}删除成功", CURRENT_MODE.get().getName());
            updateChoiceModeList(modeService.getAllModes());
            return;
        }
        log.info("模式删除失败");
    }

    @FXML
    void switchOnAndOff() {
        BUTTON_SWITCH.setDisable(true);
        switch (BUTTON_SWITCH.getText()) {
            case "运行":
                ModeCallable modeCallable = new ModeCallable(CURRENT_MODE.get());
                runningMode = THREAD_POOL.submit(modeCallable);
                BUTTON_SWITCH.setText("结束");
                break;
            case "结束":
                if (!runningMode.isDone()) runningMode.cancel(true);
                BUTTON_SWITCH.setText("运行");
                break;
        }
        BUTTON_SWITCH.setDisable(false);
    }

    @FXML
    void exportFile() {

    }

    @FXML
    void importFile() {

    }

    @FXML
    void openHelp() {
        log.info("打开帮助界面");
    }

    private void updateChoiceModeList(ArrayList<Mode> modeArrayList) {
        CHOICE_MODE_LIST.setItems(FXCollections.observableArrayList(modeArrayList));
        CHOICE_MODE_LIST.getSelectionModel().selectFirst();
    }

    /**
     * 初始化模式列表,并返回当前列表中选中的可观察模式对象(默认为第一个选中)
     *
     * @param modeArrayList 模式列表
     **/
    private void initChoiceModeList(ArrayList<Mode> modeArrayList) {
        CHOICE_MODE_LIST.setConverter(new StringConverter<Mode>() {
            @Override
            public String toString(Mode object) {
                return object.getName();
            }

            @Override
            public Mode fromString(String string) {
                return new Mode(string);
            }
        });
        CHOICE_MODE_LIST.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //将当前可观察模式与模式列表中被选中的模式进行单向绑定
            CURRENT_MODE.set(newValue);
        });
        updateChoiceModeList(modeArrayList);
    }

    /**
     * 初始化运行按钮文本监听事件,自动监听按钮是否被点击"运行",如果开始运行则启动线程监听模式的运行状态,运行结束时自动将结束文本更改为运行
     **/
    private void initButtonSwitch() {
        BUTTON_SWITCH.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("结束")) {
                THREAD_POOL.execute(() -> {
                    log.debug("开始监听模式线程");
                    try {
                        //如果刚添加的模式或者模式不完整,则提示先设置再运行,如果模式正常运行结束,则自动跳转
                        if (!runningMode.get())
                            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "请先将模式进行设置后再运行").showAndWait());
                        log.debug("线程检测完毕,结束按钮已自动更新");
                        Platform.runLater(() -> BUTTON_SWITCH.setText("运行"));
                    } catch (InterruptedException | ExecutionException | CancellationException e) {
                        log.warn("模式线程抛出异常:", e);
                        Thread.currentThread().interrupt();
                    }
                });
            }
        });
    }

    public static SimpleObjectProperty<Mode> getCurrentModeProperty() {
        return CURRENT_MODE;
    }
}
