package org.blue.automation.controller;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.vo.ModeProperty;
import org.blue.automation.factories.UIControlFactory;
import org.blue.automation.services.ModePropertyService;
import org.blue.automation.services.impl.ModePropertyServiceImpl;
import org.blue.automation.services.impl.SituationPropertyServiceImpl;

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
    private ChoiceBox<ModeProperty> CHOICE_MODE_LIST;

    @FXML
    private Button BUTTON_SWITCH;

    private static final ModePropertyService MODE_SERVICE = new ModePropertyServiceImpl("main.json");
    private static final SimpleObjectProperty<ModeProperty> CURRENT_MODE = new SimpleObjectProperty<>();
    private final Property<ObservableList<ModeProperty>> modeList = new SimpleListProperty<>();
    private final ExecutorService THREAD_POOL = Main.THREAD_POOL;
    private Future<Boolean> runningMode;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<ModeProperty> modeProperties = MODE_SERVICE.selectAllModeProperties();
        modeList.setValue(FXCollections.observableArrayList(modeProperties));
        //模式数组与模式下拉列表控件双向绑定
        CHOICE_MODE_LIST.itemsProperty().bindBidirectional(modeList);
        CHOICE_MODE_LIST.setConverter(new StringConverter<ModeProperty>() {
            @Override
            public String toString(ModeProperty object) {
                return object.getName();
            }

            @Override
            public ModeProperty fromString(String string) {
                return new ModeProperty().setName(string);
            }
        });
        //当前模式对象与模式下拉列表选中模式绑定
        CHOICE_MODE_LIST.valueProperty().bindBidirectional(CURRENT_MODE);
        CHOICE_MODE_LIST.getSelectionModel().selectFirst();
        //按钮文本监听后台模式运行,运行结束自动更新文本
        BUTTON_SWITCH.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("结束")) {
                THREAD_POOL.execute(() -> {
                    log.info("开始监听模式线程");
                    try {
                        runningMode.get();
                    } catch (InterruptedException | ExecutionException | CancellationException e) {
                        log.error("模式线程抛出异常:", e);
                        Thread.currentThread().interrupt();
                    }
                    Platform.runLater(() -> BUTTON_SWITCH.setText("运行"));
                    log.info("模式监听线程运行结束,按钮已自动更新");
                });
            }
        });
    }

    @FXML
    private void addMode() {
        TextInputDialog dialog = UIControlFactory.createTestInputDialog("添加模式", null, "请输入模式名称");
        Optional<String> nameText = dialog.showAndWait();
        nameText.ifPresent(name -> {
            ModeProperty modeProperty = new ModeProperty().setName(name);
            if(MODE_SERVICE.addModeProperty(modeProperty)) {
                modeList.setValue(FXCollections.observableArrayList(MODE_SERVICE.selectAllModeProperties()));
                CURRENT_MODE.set(modeProperty);
                log.info("{}添加成功",name);
            }else{
                new Alert(Alert.AlertType.ERROR,"添加失败").showAndWait();
                log.error("{}添加失败", name);
            }
        });
    }

    @FXML
    private void configureMode() {
        Stage settingStage = Main.STAGE_MAP.get("settingStage");
        if (settingStage == null) {
            settingStage = new Stage();
            settingStage.setTitle("模式配置");
            settingStage.setResizable(false);
            settingStage.setOnCloseRequest(event -> CURRENT_MODE.set(MODE_SERVICE.selectModePropertyByName(CURRENT_MODE.get().getName())));
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
    private void deleteMode() {
        if(MODE_SERVICE.deleteModePropertyByName(CURRENT_MODE.get().getName())){
            log.info("{}模式删除成功", CURRENT_MODE.get().getName());
            modeList.setValue(FXCollections.observableArrayList(MODE_SERVICE.selectAllModeProperties()));
            CURRENT_MODE.set(modeList.getValue().get(0));
        }else{
            log.error("{}模式删除失败", CURRENT_MODE.get().getName());
        }
    }

    @FXML
    private void switchOnAndOff() {
        BUTTON_SWITCH.setDisable(true);
        log.debug("当前模式:{}", CURRENT_MODE);
        switch (BUTTON_SWITCH.getText()) {
            case "运行":
                //ModeCallable modeCallable = new ModeCallable(currentMode.get(), new AdbOperationServiceImpl());
                //runningMode = THREAD_POOL.submit(modeCallable);
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
    public void exportFile() {

    }

    @FXML
    public void importFile() {

    }

    @FXML
    public void openHelp() {
        log.info("打开帮助界面");
    }

    public static ModeProperty getCurrentMode() {
        return CURRENT_MODE.get();
    }

    public static ModePropertyService getModeService() {
        return MODE_SERVICE;
    }
}
