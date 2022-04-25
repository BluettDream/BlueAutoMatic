package org.blue.automation.controller;

import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.entities.Mode;
import org.blue.automation.services.ModeService;
import org.blue.automation.services.impl.ModeServiceImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * name: MengHao Tian
 * date: 2022/4/24 15:08
 */
public class IndexController implements Initializable {
    private static final Logger log = LogManager.getLogger(IndexController.class);
    @FXML
    private ChoiceBox<Mode> CHOICE_MODE_LIST;

    @FXML
    private Button BUTTON_ADD_MODE;

    @FXML
    private Button BUTTON_CHANGE_MODE;

    @FXML
    private Button BUTTON_SWITCH;

    private ModeService modeService;
    private SimpleObjectProperty<Mode> currentMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modeService = new ModeServiceImpl();
        ArrayList<Mode> modeArrayList = modeService.getAllModes();
        currentMode = initChoiceModeList(modeArrayList);
        //将当前可观察模式与模式列表中被选中的模式进行单向绑定
        currentMode.bind(CHOICE_MODE_LIST.getSelectionModel().selectedItemProperty());
    }

    @FXML
    void addMode(ActionEvent event) {

    }

    @FXML
    void switchOnAndOff(ActionEvent event) {
        BUTTON_SWITCH.setDisable(true);
        switch (BUTTON_SWITCH.getText()){
            case "运行":
                log.info("开始模式运行:{}",currentMode.get());
                BUTTON_SWITCH.setText("结束");
                break;
            case "结束":
                log.info("结束模式运行:{}",currentMode.get());
                BUTTON_SWITCH.setText("运行");
                break;
        }
        BUTTON_SWITCH.setDisable(false);
    }

    @FXML
    void updateMode(ActionEvent event) {

    }

    /**
     * 初始化模式列表,并返回当前列表中选中的可观察模式对象(默认为第一个选中)
     *
     * @param modeArrayList 模式列表
     * @return 列表大小大于0返回可观察的模式,否则创建一个新的可观察模式
     **/
    private SimpleObjectProperty<Mode> initChoiceModeList(ArrayList<Mode> modeArrayList) {
        CHOICE_MODE_LIST.getItems().addAll(modeArrayList);
        log.debug("模式列表为:{}",modeArrayList);
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
        SimpleObjectProperty<Mode> modeProperty = new SimpleObjectProperty<>();
        if(modeArrayList.size() > 0) {
            CHOICE_MODE_LIST.getSelectionModel().selectFirst();
            modeProperty.set(CHOICE_MODE_LIST.getValue());
        }
        return modeProperty;
    }
}
