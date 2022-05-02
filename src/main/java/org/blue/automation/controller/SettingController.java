package org.blue.automation.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blue.automation.Main;
import org.blue.automation.entities.enums.Action;
import org.blue.automation.entities.enums.PathEnum;
import org.blue.automation.entities.Situation;
import org.blue.automation.factories.UIControlFactory;
import org.blue.automation.services.SituationService;
import org.blue.automation.services.impl.SituationServiceImpl;
import org.blue.automation.utils.PinYinUtil;
import org.blue.automation.utils.StringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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

    /**
     * 当前情景属性
     **/
    private final SimpleObjectProperty<Situation> currentSituationProperty = new SimpleObjectProperty<>(new Situation());
    /**
     * 情景接口
     **/
    private SituationService situationService;
    /**
     * 上一次打开的文件路径
     **/
    private String preDirectoryPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        situationService = new SituationServiceImpl(IndexController.getModeService());
        initChoice();
        initInput();
        initCheck();
        initPane();
    }

    /**
     * 选择图片
     **/
    @FXML
    void chooseImage() {
        File file = UIControlFactory.createImageFileChooser("选择图片", preDirectoryPath).showOpenDialog(Main.STAGE_MAP.get("settingStage"));
        if (!StringUtil.isWrong(preDirectoryPath)) preDirectoryPath = new File(file.getAbsolutePath()).getParent();
        currentSituationProperty.get().getImage().setPath(file.getAbsolutePath());
    }

    /**
     * 截屏保存图片
     **/
    @FXML
    void captureSituationImage() {
        // TODO: 2022/4/29 完善记忆上一次打开文件路径功能
        File file = UIControlFactory.createImageFileChooser("保存图片", "E:\\Users\\90774\\Pictures").showSaveDialog(Main.STAGE_MAP.get("settingStage"));
        if (file != null && !StringUtil.isWrong(file.getAbsolutePath())) {
            IndexController.getOperationService().captureAndSave(file.getAbsolutePath());
            new Alert(Alert.AlertType.INFORMATION, "截屏保存成功").showAndWait();
        }
    }

    /**
     * 删除情景
     **/
    @FXML
    void deleteSituation() {
        if (!situationService.deleteSituationByName(currentSituationProperty.get().getName())) {
            new Alert(Alert.AlertType.ERROR,"删除失败").showAndWait();
            return;
        }
        CHOICE_SITUATION_LIST.setItems(FXCollections.observableArrayList(situationService.selectAllSituations(IndexController.getCurrentMode().getName())));
        log.info("{}情景删除成功", currentSituationProperty.get().getName());
        reset();
    }

    /**
     * 保存情景
     **/
    @FXML
    void saveSituation() {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(INPUT_IMAGE_PATH.getText()));
            currentSituationProperty.get().getImage().setWidth(image.getWidth());
            currentSituationProperty.get().getImage().setHeight(image.getHeight());
            //根据情景名称保存图片到项目路径下,并将名称汉字改为拼音保存(防止opencv读取失败)
            String imagePath = PathEnum.IMAGE_INNER + PinYinUtil.getInstance().getPinYin(currentSituationProperty.get().getName()) + ".png";
            if (ImageIO.write(image, "png", new FileOutputStream(imagePath))) currentSituationProperty.get().getImage().setPath(imagePath);
        } catch (IOException e) {
            log.error("图片读取异常:", e);
        }
        if(!currentSituationProperty.get().getImage().getPath().contains(PathEnum.IMAGE_INNER.getPath())){
            new Alert(Alert.AlertType.ERROR,"图片读取失败").showAndWait();
            return;
        }
        if (situationService.addSituation(currentSituationProperty.get())) {
            new Alert(Alert.AlertType.INFORMATION, currentSituationProperty.get().getName()+"保存成功,图片已另存为:"+ currentSituationProperty.get().getImage().getPath()).showAndWait();
            log.info("情景保存成功:{}", currentSituationProperty.get());
            CHOICE_SITUATION_LIST.setItems(FXCollections.observableArrayList(situationService.selectAllSituations(IndexController.getCurrentMode().getName())));
            reset();
            return;
        }
        new Alert(Alert.AlertType.ERROR,"保存失败").showAndWait();
    }

    /**
     * 初始化当前页面内容
     **/
    @FXML
    private void reset() {
        CHOICE_SITUATION_LIST.getSelectionModel().clearSelection();
    }

    /**
     * 初始化所有下拉列表
     **/
    private void initChoice() {
        CHOICE_SITUATION_LIST.setConverter(new StringConverter<Situation>() {
            @Override
            public String toString(Situation object) {
                return object.getName();
            }

            @Override
            public Situation fromString(String string) {
                return new Situation().setName(string);
            }
        });
        CHOICE_SITUATION_LIST.setItems(FXCollections.observableList(situationService.selectAllSituations(IndexController.getCurrentMode().getName())));
        CHOICE_SITUATION_LIST.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateSituationProperty(newValue));
        CHOICE_SITUATION_LIST.getSelectionModel().selectFirst();
        CHOICE_CLICK_TYPE_LIST.setItems(FXCollections.observableArrayList(Action.values()));
        CHOICE_CLICK_TYPE_LIST.valueProperty().bindBidirectional(currentSituationProperty.get().actionProperty());
        CHOICE_CLICK_TYPE_LIST.disableProperty().bind(currentSituationProperty.get().clickProperty().not());
    }

    /**
     * 初始化所有输入框
     **/
    private void initInput() {
        INPUT_SITUATION_NAME.textProperty().bindBidirectional(currentSituationProperty.get().nameProperty());
        INPUT_SITUATION_PRIORITY.textProperty().bindBidirectional(currentSituationProperty.get().priorityProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_IMAGE_PATH.textProperty().bindBidirectional(currentSituationProperty.get().getImage().pathProperty());
        INPUT_X.textProperty().bindBidirectional(currentSituationProperty.get().getImage().xProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_Y.textProperty().bindBidirectional(currentSituationProperty.get().getImage().yProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_LOWEST_SIMILE.textProperty().bindBidirectional(currentSituationProperty.get().lowestSimileProperty(), new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                return object.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
        });
        INPUT_CUSTOM_X.textProperty().bindBidirectional(currentSituationProperty.get().getCustomImage().xProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_CUSTOM_Y.textProperty().bindBidirectional(currentSituationProperty.get().getCustomImage().yProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_CUSTOM_WIDTH.textProperty().bindBidirectional(currentSituationProperty.get().getCustomImage().widthProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_CUSTOM_HEIGHT.textProperty().bindBidirectional(currentSituationProperty.get().getCustomImage().heightProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
    }

    /**
     * 初始化所有单选框
     **/
    private void initCheck() {
        CHECK_CLICK.selectedProperty().bindBidirectional(currentSituationProperty.get().clickProperty());
        CHECK_CUSTOM.selectedProperty().bindBidirectional(currentSituationProperty.get().customProperty());
    }

    /**
     * 初始化所有布局
     **/
    private void initPane() {
        HBOX_CUSTOM.visibleProperty().bind(currentSituationProperty.getValue().customProperty());
    }

    /**
     * 更新情景属性,以便于响应页面
     **/
    private void updateSituationProperty(Situation newSituation) {
        if(newSituation == null) newSituation = new Situation();
        currentSituationProperty.get().setName(newSituation.getName());
        currentSituationProperty.get().setPriority(newSituation.getPriority());
        currentSituationProperty.get().setLowestSimile(newSituation.getLowestSimile());
        currentSituationProperty.get().setClick(newSituation.isClick());
        currentSituationProperty.get().setAction(newSituation.getAction());
        currentSituationProperty.get().setCustom(newSituation.isCustom());
        //image
        currentSituationProperty.get().getImage().setPath(newSituation.getImage().getPath());
        currentSituationProperty.get().getImage().setX(newSituation.getImage().getX());
        currentSituationProperty.get().getImage().setY(newSituation.getImage().getY());
        currentSituationProperty.get().getImage().setHeight(newSituation.getImage().getHeight());
        currentSituationProperty.get().getImage().setWidth(newSituation.getImage().getWidth());
        //customImage
        currentSituationProperty.get().getCustomImage().setX(newSituation.getCustomImage().getX());
        currentSituationProperty.get().getCustomImage().setY(newSituation.getCustomImage().getY());
        currentSituationProperty.get().getCustomImage().setHeight(newSituation.getCustomImage().getHeight());
        currentSituationProperty.get().getCustomImage().setWidth(newSituation.getCustomImage().getWidth());
    }

}
