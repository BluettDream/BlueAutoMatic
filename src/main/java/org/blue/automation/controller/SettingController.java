package org.blue.automation.controller;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.enums.Action;
import org.blue.automation.entities.vo.SituationProperty;
import org.blue.automation.factories.UIControlFactory;
import org.blue.automation.services.OperationService;
import org.blue.automation.services.SituationPropertyService;
import org.blue.automation.services.impl.AdbOperationServiceImpl;
import org.blue.automation.services.impl.SituationPropertyServiceImpl;
import org.blue.automation.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ResourceBundle;

/**
 * name: MengHao Tian
 * date: 2022/4/26 08:42
 */
public class SettingController implements Initializable {
    private static final Logger log = LogManager.getLogger(SettingController.class);
    @FXML
    private ChoiceBox<SituationProperty> CHOICE_SITUATION_LIST;
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
    private TextField INPUT_LOWEST_SIMILE;
    @FXML
    private TextField INPUT_X;
    @FXML
    private TextField INPUT_Y;
    @FXML
    private CheckBox CHECK_CUSTOM;
    @FXML
    private HBox HBOX_CUSTOM;
    @FXML
    private TextField INPUT_CUSTOM_X;
    @FXML
    private TextField INPUT_CUSTOM_Y;
    @FXML
    private TextField INPUT_CUSTOM_WIDTH;
    @FXML
    private TextField INPUT_CUSTOM_HEIGHT;

    private final Property<ObservableList<SituationProperty>> situationList = new SimpleListProperty<>();
    private final SimpleObjectProperty<SituationProperty> currentSituation = new SimpleObjectProperty<>();
    private SituationPropertyService situationService;
    private String preDirectoryPath;
    private OperationService operationService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        situationService = new SituationPropertyServiceImpl(IndexController.getModeService());
        situationList.setValue(FXCollections.observableArrayList(situationService.selectAllSituationProperties(IndexController.getCurrentMode().getName())));
        if(situationList.getValue()!=null && situationList.getValue().size() > 0) currentSituation.set(situationList.getValue().get(0));
        initChoice();
        initInput();
        initCheck();
        HBOX_CUSTOM.visibleProperty().bind(currentSituation.getValue().customProperty());
    }

    private void initChoice() {
        CHOICE_SITUATION_LIST.setConverter(new StringConverter<SituationProperty>() {
            @Override
            public String toString(SituationProperty object) {
                return object.getName();
            }

            @Override
            public SituationProperty fromString(String string) {
                return new SituationProperty().setName(string);
            }
        });
        CHOICE_SITUATION_LIST.itemsProperty().bindBidirectional(situationList);
        CHOICE_SITUATION_LIST.valueProperty().bindBidirectional(currentSituation);
        CHOICE_SITUATION_LIST.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentSituation.getValue().setName(newValue.getName());
        });
        CHOICE_CLICK_TYPE_LIST.setItems(FXCollections.observableArrayList(Action.values()));
        CHOICE_CLICK_TYPE_LIST.valueProperty().bindBidirectional(currentSituation.get().actionProperty());
        CHOICE_CLICK_TYPE_LIST.disableProperty().bind(currentSituation.get().clickProperty().not());
    }

    private void initInput() {
        INPUT_SITUATION_NAME.textProperty().bindBidirectional(currentSituation.getValue().nameProperty());
        INPUT_SITUATION_PRIORITY.textProperty().bindBidirectional(currentSituation.get().priorityProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
        INPUT_LOWEST_SIMILE.textProperty().bindBidirectional(currentSituation.get().lowestSimileProperty(), new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                return object.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return null;
            }
        });
        INPUT_CUSTOM_X.textProperty().bindBidirectional(currentSituation.get().getImage().xProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
        INPUT_CUSTOM_Y.textProperty().bindBidirectional(currentSituation, new StringConverter<SituationProperty>() {
            @Override
            public String toString(SituationProperty object) {
                return String.valueOf(object.getImage().getY());
            }

            @Override
            public SituationProperty fromString(String string) {
                return null;
            }
        });
        INPUT_CUSTOM_WIDTH.textProperty().bindBidirectional(currentSituation, new StringConverter<SituationProperty>() {
            @Override
            public String toString(SituationProperty object) {
                return String.valueOf(object.getImage().getWidth());
            }

            @Override
            public SituationProperty fromString(String string) {
                return null;
            }
        });
        INPUT_CUSTOM_HEIGHT.textProperty().bindBidirectional(currentSituation.get().getCustomImage().heightProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
        INPUT_IMAGE_PATH.textProperty().bindBidirectional(currentSituation.get().getImage().pathProperty());
        INPUT_X.textProperty().bindBidirectional(currentSituation.get().getImage().xProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        });
        INPUT_Y.textProperty().bindBidirectional(currentSituation.get().getImage().yProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        });
    }

    private void initCheck() {
        CHECK_CLICK.selectedProperty().bindBidirectional(currentSituation.get().clickProperty());
        CHECK_CUSTOM.selectedProperty().bindBidirectional(currentSituation.get().customProperty());
    }

    @FXML
    private void addSituationProperty() {
        log.info("当前情景:{}",currentSituation.get());
        //SituationProperty tempSituation = new SituationProperty(INPUT_SITUATION_NAME.getText(), new SituationImage(null,0,0), false);
        //tempSituation.setName(INPUT_SITUATION_NAME.getText());
        //if (!situationService.isCompliance(tempSituation)) {
        //    new Alert(Alert.AlertType.WARNING, "请检查情景名称是否为空或含有空格").showAndWait();
        //    return;
        //}
        //if (situationService.isExisted(tempSituation)) {
        //    new Alert(Alert.AlertType.WARNING, "情景名称已存在,请重新输入").showAndWait();
        //    return;
        //}
        //currentSituation.set(tempSituation);
        //CHOICE_SITUATION_LIST.getItems().add(currentSituation.get());
        //log.info("当前情景的名称:{}", currentSituation.get().getName());
    }

    @FXML
    void chooseImage() {
        File file = UIControlFactory.createImageFileChooser("选择图片", preDirectoryPath).showOpenDialog(Main.STAGE_MAP.get("settingStage"));
        if (StringUtil.isWrong(preDirectoryPath)) preDirectoryPath = new File(file.getAbsolutePath()).getParent();
        currentSituation.get().getImage().setPath(file.getAbsolutePath());
    }

    @FXML
    void captureSituationImage() {
        operationService = operationService == null ? new AdbOperationServiceImpl() : operationService;
        // TODO: 2022/4/29 完善记忆上一次打开文件路径功能
        File file = UIControlFactory.createImageFileChooser("保存图片", "E:\\Users\\90774\\Pictures").showSaveDialog(Main.STAGE_MAP.get("settingStage"));
        if (file != null && !StringUtil.isWrong(file.getAbsolutePath())) {
            operationService.captureAndSave(file.getAbsolutePath());
            new Alert(Alert.AlertType.INFORMATION, "截屏保存成功").showAndWait();
        }
    }

    @FXML
    void deleteSituation() {
        if (situationService.deleteSituationPropertyByName(currentSituation.get().getName())) {
            situationList.setValue(FXCollections.observableArrayList(situationService.selectAllSituationProperties(IndexController.getCurrentMode().getName())));
            CHOICE_SITUATION_LIST.getSelectionModel().clearSelection();
            log.info("{}删除成功", currentSituation.get().getName());
        } else {
            new Alert(Alert.AlertType.ERROR, currentSituation.get().getName() + "删除失败").showAndWait();
            log.info("{}删除失败", currentSituation.get().getName());
        }
    }

    @FXML
    void saveSituation() {
        //boolean needSave = true;
        //currentSituation.get().setPriority(Integer.parseInt(INPUT_SITUATION_PRIORITY.getText()));
        //currentSituation.get().getImage().setX(Integer.parseInt(INPUT_X.getText()));
        //currentSituation.get().getImage().setY(Integer.parseInt(INPUT_Y.getText()));
        //if (!StringUtils.isBlank(INPUT_IMAGE_PATH.getText())) {
        //    try {
        //        BufferedImage image = ImageIO.read(new FileInputStream(INPUT_IMAGE_PATH.getText()));
        //        currentSituation.get().getImage().setWidth(image.getWidth());
        //        currentSituation.get().getImage().setHeight(image.getHeight());
        //        //根据情景名称保存图片到项目路径下,并将名称汉字改为拼音保存(防止opencv读取失败)
        //        String imagePath = PathEnum.IMAGE_INNER + PinYinUtil.getInstance().getPinYin(currentSituation.get().getName()) + ".png";
        //        boolean res = ImageIO.write(image, "png", new FileOutputStream(imagePath));
        //        if (res) currentSituation.get().getImage().setPath(imagePath);
        //        else needSave = false;
        //    } catch (IOException e) {
        //        needSave = false;
        //        log.error("读取图片异常:", e);
        //        new Alert(Alert.AlertType.ERROR, "图片读取异常").showAndWait();
        //    }
        //}
        //if (needSave && situationService.isExisted(currentSituation.get()) && situationService.updateSituation(currentSituation.get())) {
        //    log.info("情景更新成功:{}", currentSituation.get());
        //    new Alert(Alert.AlertType.INFORMATION, "更新成功").showAndWait();
        //    return;
        //}
        //if (needSave && !situationService.isExisted(currentSituation.get()) && situationService.addSituation(currentSituation.get())) {
        //    log.info("情景添加成功:{}", currentSituation.get());
        //    new Alert(Alert.AlertType.INFORMATION, "保存成功").showAndWait();
        //    return;
        //}
        //log.info("情景保存失败:{}", currentSituation.get());
        //new Alert(Alert.AlertType.WARNING, "保存失败").showAndWait();
    }

    @FXML
    private void reset() {
        currentSituation.set(null);
    }
}
