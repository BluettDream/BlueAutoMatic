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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.ModeBase;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.factories.UIControlFactory;
import org.blue.automation.services.ModeService;
import org.blue.automation.services.OperationService;
import org.blue.automation.services.impl.AdbOperationServiceImpl;
import org.blue.automation.services.impl.ModeServiceImpl;
import org.blue.automation.services.impl.PCOperationServiceImpl;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * name: MengHao Tian
 * date: 2022/4/24 15:08
 */
public class IndexController implements Initializable {
    private static final Logger log = LogManager.getLogger(IndexController.class);
    @FXML
    private ChoiceBox<ModeBase> CHOICE_MODE_LIST;
    @FXML
    private ChoiceBox<String> CHOICE_OPERATION_LIST;
    @FXML
    private Button BUTTON_CHOOSE_ADB_FILE;
    @FXML
    private Button BUTTON_SWITCH;
    @FXML
    private Label LABEL_POINT;

    /**
     * 当前模式属性
     **/
    private static final SimpleObjectProperty<ModeBase> CURRENT_MODE_PROPERTY = new SimpleObjectProperty<>();
    /**
     * 模式接口
     **/
    private static final ModeService MODE_SERVICE = new ModeServiceImpl("main.json");
    /**
     * 操作接口
     **/
    private static OperationService OPERATION_SERVICE;
    /**
     * 模式列表
     **/
    private final Property<ObservableList<ModeBase>> modeList = new SimpleListProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<ModeBase> modeBases = MODE_SERVICE.selectAllModes();
        modeList.setValue(FXCollections.observableArrayList(modeBases));
        //模式数组与模式下拉列表控件双向绑定
        CHOICE_MODE_LIST.itemsProperty().bindBidirectional(modeList);
        CHOICE_MODE_LIST.setConverter(new StringConverter<ModeBase>() {
            @Override
            public String toString(ModeBase object) {
                return object.getName();
            }

            @Override
            public ModeBase fromString(String string) {
                return new ModeBase().setName(string);
            }
        });
        //当前模式对象与模式下拉列表选中模式绑定
        CHOICE_MODE_LIST.valueProperty().bindBidirectional(CURRENT_MODE_PROPERTY);
        CHOICE_MODE_LIST.getSelectionModel().selectFirst();
        //初始化操作方式下拉列表
        CHOICE_OPERATION_LIST.getItems().addAll("手机", "模拟器", "电脑");
        CHOICE_OPERATION_LIST.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "手机":
                case "模拟器":
                    OPERATION_SERVICE = new AdbOperationServiceImpl();
                    break;
                case "电脑":
                    OPERATION_SERVICE = new PCOperationServiceImpl();
                    break;
            }
        });
        CHOICE_OPERATION_LIST.getSelectionModel().select("手机");
    }

    @FXML
    void chooseFile() {
        File file = UIControlFactory.createFileChooser("选择json文件", PathEnum.JSON.getPath(), false).showOpenDialog(Main.STAGE_MAP.get("primaryStage"));
        if (file != null) OPERATION_SERVICE.setFilePath(file.getAbsolutePath());
    }

    /**
     * 添加模式
     **/
    @FXML
    private void addMode() {
        TextInputDialog dialog = UIControlFactory.createTestInputDialog("添加模式", null, "请输入模式名称");
        Optional<String> nameText = dialog.showAndWait();
        nameText.ifPresent(name -> {
            ModeBase modeBase = new ModeBase().setName(name);
            if (MODE_SERVICE.addMode(modeBase)) {
                modeList.setValue(FXCollections.observableArrayList(MODE_SERVICE.selectAllModes()));
                CURRENT_MODE_PROPERTY.set(modeBase);
                log.info("{}添加成功", name);
            } else {
                new Alert(Alert.AlertType.ERROR, "添加失败").showAndWait();
                log.error("{}添加失败", name);
            }
        });
    }

    /**
     * 打开配置模式页面
     **/
    @FXML
    private void configureMode() {
        Stage settingStage = Main.STAGE_MAP.get("modeSettingStage");
        if (settingStage == null) {
            settingStage = new Stage();
            settingStage.setTitle("模式配置");
            settingStage.setResizable(false);
            settingStage.setOnCloseRequest(event -> CURRENT_MODE_PROPERTY.set(MODE_SERVICE.selectModeByName(CURRENT_MODE_PROPERTY.get().getName())));
            Main.STAGE_MAP.put("modeSettingStage", settingStage);
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/modeSetting.fxml"));
            settingStage.setScene(new Scene(fxmlLoader.load(), 600, 400));
        } catch (IOException e) {
            log.error("创建settingStage异常:", e);
        }
        settingStage.show();
    }

    /**
     * 删除模式
     **/
    @FXML
    private void deleteMode() {
        if (MODE_SERVICE.deleteModeByName(CURRENT_MODE_PROPERTY.get().getName())) {
            log.info("{}模式删除成功", CURRENT_MODE_PROPERTY.get().getName());
            modeList.setValue(FXCollections.observableArrayList(MODE_SERVICE.selectAllModes()));
            CURRENT_MODE_PROPERTY.set(modeList.getValue().get(0));
        } else {
            log.error("{}模式删除失败", CURRENT_MODE_PROPERTY.get().getName());
        }
    }

    /**
     * 运行按钮设置
     **/
    @FXML
    private void switchOnAndOff() {
        BUTTON_SWITCH.setDisable(true);
        Stage stage = Main.STAGE_MAP.get("modeRunningStage");
        log.debug("当前模式:{}", CURRENT_MODE_PROPERTY);
        try {
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/views/modeRunning.fxml")).load(),600,400));
        }catch (IOException e){
            log.error("模式运行信息展示界面启动异常:",e);
        }
        stage.show();
        BUTTON_SWITCH.setDisable(false);
    }

    @FXML
    void stopRunning() {
        if(ModeRunningController.getRunningMode() != null && !ModeRunningController.getRunningMode().isCancelled()){
            ModeRunningController.getRunningMode().cancel(true);
        }
    }

    /**
     * 导出配置文件
     **/
    @FXML
    public void exportFile() {

    }

    /**
     * 导入配置文件
     **/
    @FXML
    public void importFile() {

    }

    private volatile AtomicInteger pointNum = new AtomicInteger(0);
    private Thread temp;
    @FXML
    void getPoint() {
        if(pointNum.get() == 0){
            temp = new Thread(()->{
                while (!Thread.currentThread().isInterrupted()){
                    log.info("获取坐标启动");
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    Platform.runLater(()->LABEL_POINT.setText(point.x+" "+point.y));
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            temp.start();
        }
        switch (pointNum.get()){
            case 0:pointNum.set(1);break;
            case 1:temp.interrupt();temp = null;pointNum.set(0);break;
        }
    }
    /**
     * 打开帮助界面
     **/
    @FXML
    public void openHelp() {
        log.info("打开帮助界面");
    }

    public static SimpleObjectProperty<ModeBase> getCurrentModeProperty() {
        return CURRENT_MODE_PROPERTY;
    }

    public static ModeService getModeService() {
        return MODE_SERVICE;
    }

    public static OperationService getOperationService() {
        return OPERATION_SERVICE;
    }
}
