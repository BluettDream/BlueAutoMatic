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
import org.blue.automation.services.OperationService;
import org.blue.automation.services.SituationService;
import org.blue.automation.services.impl.AdbOperationServiceImpl;
import org.blue.automation.services.impl.SituationServiceImpl;
import org.blue.automation.utils.PinYinUtil;

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
    private TextField INPUT_SITUATION_PRIORITY;

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
    private static String PRE_DIRECTORY_PATH;
    private OperationService operationService;

    @FXML
    void setSituationName() {
        Situation tempSituation = new Situation(INPUT_SITUATION_NAME.getText(), new SituationImage(null,0,0), false);
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
    void chooseImage() {
        File file = UIControlFactory.createImageFileChooser("选择图片", PRE_DIRECTORY_PATH).showOpenDialog(Main.STAGE_MAP.get("settingStage"));
        if (StringUtils.isBlank(PRE_DIRECTORY_PATH)) PRE_DIRECTORY_PATH = new File(file.getAbsolutePath()).getParent();
        currentSituation.get().getImage().setPath(file.getAbsolutePath());
        INPUT_IMAGE_PATH.setText(file.getAbsolutePath());
    }

    @FXML
    void setSituationIsClick() {
        currentSituation.get().setClick(CHECK_CLICK.isSelected());
    }

    @FXML
    void captureSituationImage() {
        operationService = operationService == null ? new AdbOperationServiceImpl() : operationService;
        // TODO: 2022/4/29 完善记忆上一次打开文件路径功能
        File file = UIControlFactory.createImageFileChooser("保存图片", "E:\\Users\\90774\\Pictures").showSaveDialog(Main.STAGE_MAP.get("settingStage"));
        if (file != null && !StringUtils.isBlank(file.getAbsolutePath())) {
            operationService.captureAndSave(file.getAbsolutePath());
            new Alert(Alert.AlertType.INFORMATION, "截屏保存成功").showAndWait();
        }
    }

    @FXML
    void deleteSituation() {
        if (situationService.deleteSituation(currentSituation.get())) {
            new Alert(Alert.AlertType.INFORMATION, "删除成功").showAndWait();
            log.info("{}删除成功", currentSituation.get().getName());
            CHOICE_SITUATION_LIST.setItems(
                    FXCollections.observableArrayList(situationService.getAllSituationsByName(CURRENT_MODE.get().getName()))
            );
            CHOICE_SITUATION_LIST.getSelectionModel().clearSelection();
        } else {
            new Alert(Alert.AlertType.ERROR, currentSituation.get().getName() + "删除失败").showAndWait();
            log.info("{}删除失败", currentSituation.get().getName());
        }
    }

    @FXML
    void saveSituation() {
        boolean needSave = true;
        currentSituation.get().setPriority(Integer.parseInt(INPUT_SITUATION_PRIORITY.getText()));
        currentSituation.get().getImage().setX(Integer.parseInt(INPUT_X.getText()));
        currentSituation.get().getImage().setY(Integer.parseInt(INPUT_Y.getText()));
        if (!StringUtils.isBlank(INPUT_IMAGE_PATH.getText())) {
            try {
                BufferedImage image = ImageIO.read(new FileInputStream(INPUT_IMAGE_PATH.getText()));
                currentSituation.get().getImage().setWidth(image.getWidth());
                currentSituation.get().getImage().setHeight(image.getHeight());
                //根据情景名称保存图片到项目路径下,并将名称汉字改为拼音保存(防止opencv读取失败)
                String imagePath = PathEnum.IMAGE_INNER + PinYinUtil.getInstance().getPinYin(currentSituation.get().getName()) + ".png";
                boolean res = ImageIO.write(image, "png", new FileOutputStream(imagePath));
                if (res) currentSituation.get().getImage().setPath(imagePath);
                else needSave = false;
            } catch (IOException e) {
                needSave = false;
                log.error("读取图片异常:", e);
                new Alert(Alert.AlertType.ERROR, "图片读取异常").showAndWait();
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
        initCheckClick();
        initChoiceClickTypeList();
        initCurrentSituation();
        initChoiceSituationList();
    }

    private void initCheckClick() {
        CHECK_CLICK.selectedProperty().addListener((observable, oldValue, newValue) -> CHOICE_CLICK_TYPE_LIST.setDisable(!newValue));
    }

    private void initCurrentSituation() {
        currentSituation.addListener((observable, oldValue, newValue) -> {
            CHOICE_SITUATION_LIST.getSelectionModel().select(newValue);
            if (newValue == null) {
                INPUT_SITUATION_NAME.clear();
                INPUT_SITUATION_PRIORITY.clear();
                INPUT_IMAGE_PATH.clear();
                CHECK_CLICK.setSelected(false);
                CHOICE_CLICK_TYPE_LIST.getSelectionModel().clearSelection();
                CHOICE_CLICK_TYPE_LIST.setDisable(true);
                INPUT_X.clear();
                INPUT_Y.clear();
                return;
            }
            INPUT_SITUATION_NAME.setText(newValue.getName());
            INPUT_SITUATION_PRIORITY.setText(String.valueOf(newValue.getPriority()));
            CHECK_CLICK.setSelected(newValue.isClick());
            CHOICE_CLICK_TYPE_LIST.getSelectionModel().select(newValue.getAction());
            CHOICE_CLICK_TYPE_LIST.setDisable(!newValue.isClick());
            if (newValue.getImage() == null) {
                INPUT_IMAGE_PATH.clear();
                INPUT_X.clear();
                INPUT_Y.clear();
                return;
            }
            INPUT_IMAGE_PATH.setText(newValue.getImage().getPath());
            INPUT_X.setText(String.valueOf(newValue.getImage().getX()));
            INPUT_Y.setText(String.valueOf(newValue.getImage().getY()));
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
