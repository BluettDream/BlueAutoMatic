package org.blue.automation.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.Mode;
import org.blue.automation.entities.Situation;
import org.blue.automation.entities.SituationImage;
import org.blue.automation.entities.enums.Action;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.factories.UIControlFactory;
import org.blue.automation.services.SituationService;
import org.blue.automation.services.impl.SituationServiceImpl;
import org.opencv.core.Rect;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * name: MengHao Tian
 * date: 2022/4/26 08:42
 */
public class SettingController implements Initializable {
    private static final Logger log = LogManager.getLogger(SettingController.class);
    @FXML
    private ChoiceBox<Situation> CHOICE_SITUATION_LIST;

    @FXML
    private TextField INPUT_SITUATION_NAME;

    @FXML
    private TextField INPUT_IMAGE_PATH;

    @FXML
    private CheckBox CHECK_CLICK;

    @FXML
    private ChoiceBox<Action> CHOICE_CLICK_TYPE_LIST;

    @FXML
    private TextField INPUT_X;

    @FXML
    private TextField INPUT_Y;

    private SimpleObjectProperty<Situation> currentSituation;
    private static SimpleObjectProperty<Mode> CURRENT_MODE;
    private SituationService situationService;

    @FXML
    void setSituationName() {
        Situation tempSituation = new Situation(INPUT_SITUATION_NAME.getText(), new SituationImage(null, new Rect()), false);
        tempSituation.setName(INPUT_SITUATION_NAME.getText());
        if (!situationService.isCompliance(tempSituation)) {
            new Alert(Alert.AlertType.WARNING, "请检查情景名称是否为空或含有空格").showAndWait();
            return;
        }
        if (situationService.isExisted(tempSituation)) {
            new Alert(Alert.AlertType.WARNING, "情景名称已存在,请重新输入").showAndWait();
            return;
        }
        currentSituation.set(tempSituation);
        CHOICE_SITUATION_LIST.getItems().add(currentSituation.get());
        log.info("当前情景的名称:{}", currentSituation.get().getName());
    }

    @FXML
    void setSituationImagePath() {
        currentSituation.get().getImage().setPath(INPUT_IMAGE_PATH.getText());
    }

    @FXML
    void chooseImage() {
        File file = UIControlFactory.createImageFileChooser("选择图片").showOpenDialog(Main.STAGE_MAP.get("settingStage"));
        currentSituation.get().getImage().setPath(file.getAbsolutePath());
        INPUT_IMAGE_PATH.setText(file.getAbsolutePath());
    }

    @FXML
    void setSituationIsClick() {
        currentSituation.get().setClick(CHECK_CLICK.isSelected());
    }

    @FXML
    void deleteSituation() {
        if (situationService.deleteSituation(currentSituation.get())) {
            new Alert(Alert.AlertType.INFORMATION,"删除成功").showAndWait();
            log.info("{}删除成功", currentSituation.get().getName());
            CHOICE_SITUATION_LIST.setItems(
                    FXCollections.observableArrayList(situationService.getAllSituationsByName(CURRENT_MODE.get().getName()))
            );
            CHOICE_SITUATION_LIST.getSelectionModel().clearSelection();
        } else {
            new Alert(Alert.AlertType.ERROR, currentSituation.get().getName() + "删除失败");
            log.info("{}删除失败", currentSituation.get().getName());
        }
    }

    @FXML
    void saveSituation() {
        boolean needSave = true;
        currentSituation.get().getImage().getRect().x = Integer.parseInt(INPUT_X.getText());
        currentSituation.get().getImage().getRect().y = Integer.parseInt(INPUT_Y.getText());
        if (!StringUtils.isBlank(INPUT_IMAGE_PATH.getText())) {
            try {
                BufferedImage image = ImageIO.read(new FileInputStream(INPUT_IMAGE_PATH.getText()));
                currentSituation.get().getImage().getRect().width = image.getWidth();
                currentSituation.get().getImage().getRect().height = image.getHeight();
                String imagePath = PathEnum.IMAGE_INNER + currentSituation.get().getName()+".png";
                boolean res = ImageIO.write(image, "png", new FileOutputStream(imagePath));
                if (res) currentSituation.get().getImage().setPath(imagePath);
                else needSave = false;
            } catch (IOException e) {
                needSave = false;
                log.error("读取图片异常:", e);
                new Alert(Alert.AlertType.ERROR, "图片读取异常");
            }
        }
        if (needSave && situationService.isExisted(currentSituation.get()) && situationService.updateSituation(currentSituation.get())) {
            log.info("情景更新成功:{}", currentSituation.get());
            new Alert(Alert.AlertType.INFORMATION, "更新成功").showAndWait();
            return;
        }
        if (needSave && !situationService.isExisted(currentSituation.get()) && situationService.addSituation(currentSituation.get())) {
            log.info("情景添加成功:{}", currentSituation.get());
            new Alert(Alert.AlertType.INFORMATION, "保存成功").showAndWait();
            return;
        }
        log.info("情景保存失败:{}", currentSituation.get());
        new Alert(Alert.AlertType.WARNING, "保存失败").showAndWait();
    }

    @FXML
    void reset() {
        currentSituation.set(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        situationService = new SituationServiceImpl();
        CURRENT_MODE = IndexController.getCurrentModeProperty();
        currentSituation = new SimpleObjectProperty<>();
        initCurrentSituation();
        initChoiceSituationList();
        initChoiceClickTypeList();
        CHECK_CLICK.selectedProperty().addListener((observable, oldValue, newValue) -> CHOICE_CLICK_TYPE_LIST.setDisable(!newValue));
    }

    private void initCurrentSituation() {
        currentSituation.addListener((observable, oldValue, newValue) -> {
            CHOICE_SITUATION_LIST.getSelectionModel().select(newValue);
            if (newValue == null) {
                INPUT_SITUATION_NAME.clear();
                INPUT_IMAGE_PATH.clear();
                CHECK_CLICK.setSelected(false);
                CHOICE_CLICK_TYPE_LIST.getSelectionModel().clearSelection();
                INPUT_X.clear();
                INPUT_Y.clear();
                return;
            }
            INPUT_SITUATION_NAME.setText(newValue.getName());
            CHECK_CLICK.setSelected(newValue.isClick());
            CHOICE_CLICK_TYPE_LIST.getSelectionModel().select(newValue.getAction());
            if(newValue.getImage() == null){
                INPUT_IMAGE_PATH.clear();
                INPUT_X.clear();
                INPUT_Y.clear();
                return;
            }
            INPUT_IMAGE_PATH.setText(newValue.getImage().getPath());
            INPUT_X.setText(String.valueOf(newValue.getImage().getRect().x));
            INPUT_Y.setText(String.valueOf(newValue.getImage().getRect().y));
        });
    }

    private void initChoiceSituationList() {
        CHOICE_SITUATION_LIST.setConverter(new StringConverter<Situation>() {
            @Override
            public String toString(Situation object) {
                return object == null ? null : object.getName();
            }

            @Override
            public Situation fromString(String string) {
                return null;
            }
        });
        CHOICE_SITUATION_LIST.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //更新当前情景
            currentSituation.set(newValue);
            log.debug("当前选中情景为:{}", currentSituation);
        });
        CHOICE_SITUATION_LIST.setItems(
                FXCollections.observableArrayList(situationService.getAllSituationsByName(CURRENT_MODE.get().getName()))
        );
        CHOICE_SITUATION_LIST.getSelectionModel().selectFirst();
    }

    private void initChoiceClickTypeList() {
        CHOICE_CLICK_TYPE_LIST.setItems(FXCollections.observableArrayList(Action.values()));
        CHOICE_CLICK_TYPE_LIST.setConverter(new StringConverter<Action>() {
            @Override
            public String toString(Action object) {
                return object == null ? null : object.getDescription();
            }

            @Override
            public Action fromString(String string) {
                return null;
            }
        });
        CHOICE_CLICK_TYPE_LIST.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (currentSituation.get() != null) {
                currentSituation.get().setAction(newValue);
            }
        });
    }
}
