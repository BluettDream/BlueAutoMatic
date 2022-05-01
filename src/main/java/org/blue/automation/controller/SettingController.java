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

    private final SimpleObjectProperty<Situation> situationProperty = new SimpleObjectProperty<>(new Situation());
    private SituationService situationService;
    private String preDirectoryPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        situationService = new SituationServiceImpl(IndexController.getModeService());
        initChoice();
        initInput();
        initCheck();
        initPane();
    }

    @FXML
    void chooseImage() {
        File file = UIControlFactory.createImageFileChooser("选择图片", preDirectoryPath).showOpenDialog(Main.STAGE_MAP.get("settingStage"));
        if (!StringUtil.isWrong(preDirectoryPath)) preDirectoryPath = new File(file.getAbsolutePath()).getParent();
        situationProperty.get().getImage().setPath(file.getAbsolutePath());
    }

    @FXML
    void captureSituationImage() {
        // TODO: 2022/4/29 完善记忆上一次打开文件路径功能
        File file = UIControlFactory.createImageFileChooser("保存图片", "E:\\Users\\90774\\Pictures").showSaveDialog(Main.STAGE_MAP.get("settingStage"));
        if (file != null && !StringUtil.isWrong(file.getAbsolutePath())) {
            IndexController.getOperationService().captureAndSave(file.getAbsolutePath());
            new Alert(Alert.AlertType.INFORMATION, "截屏保存成功").showAndWait();
        }
    }

    @FXML
    void deleteSituation() {
        if (!situationService.deleteSituationByName(situationProperty.get().getName())) {
            new Alert(Alert.AlertType.ERROR,"删除失败").showAndWait();
            return;
        }
        CHOICE_SITUATION_LIST.setItems(FXCollections.observableArrayList(situationService.selectAllSituations(IndexController.getCurrentMode().getName())));
        log.info("{}情景删除成功",situationProperty.get().getName());
        reset();
    }

    @FXML
    void saveSituation() {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(INPUT_IMAGE_PATH.getText()));
            situationProperty.get().getImage().setWidth(image.getWidth());
            situationProperty.get().getImage().setHeight(image.getHeight());
            //根据情景名称保存图片到项目路径下,并将名称汉字改为拼音保存(防止opencv读取失败)
            String imagePath = PathEnum.IMAGE_INNER + PinYinUtil.getInstance().getPinYin(situationProperty.get().getName()) + ".png";
            if (ImageIO.write(image, "png", new FileOutputStream(imagePath))) situationProperty.get().getImage().setPath(imagePath);
        } catch (IOException e) {
            log.error("图片读取异常:", e);
        }
        if(!situationProperty.get().getImage().getPath().contains(PathEnum.IMAGE_INNER.getPath())){
            new Alert(Alert.AlertType.ERROR,"图片读取失败").showAndWait();
            return;
        }
        if (situationService.addSituation(situationProperty.get())) {
            new Alert(Alert.AlertType.INFORMATION,situationProperty.get().getName()+"保存成功,图片已另存为:"+situationProperty.get().getImage().getPath()).showAndWait();
            log.info("情景保存成功:{}",situationProperty.get());
            CHOICE_SITUATION_LIST.setItems(FXCollections.observableArrayList(situationService.selectAllSituations(IndexController.getCurrentMode().getName())));
            reset();
            return;
        }
        new Alert(Alert.AlertType.ERROR,"保存失败").showAndWait();
    }

    @FXML
    private void reset() {
        CHOICE_SITUATION_LIST.getSelectionModel().clearSelection();
    }

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
        CHOICE_CLICK_TYPE_LIST.valueProperty().bindBidirectional(situationProperty.get().actionProperty());
        CHOICE_CLICK_TYPE_LIST.disableProperty().bind(situationProperty.get().clickProperty().not());
    }

    private void initInput() {
        INPUT_SITUATION_NAME.textProperty().bindBidirectional(situationProperty.get().nameProperty());
        INPUT_SITUATION_PRIORITY.textProperty().bindBidirectional(situationProperty.get().priorityProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_IMAGE_PATH.textProperty().bindBidirectional(situationProperty.get().getImage().pathProperty());
        INPUT_X.textProperty().bindBidirectional(situationProperty.get().getImage().xProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_Y.textProperty().bindBidirectional(situationProperty.get().getImage().yProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_LOWEST_SIMILE.textProperty().bindBidirectional(situationProperty.get().lowestSimileProperty(), new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                return object.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
        });
        INPUT_CUSTOM_X.textProperty().bindBidirectional(situationProperty.get().getCustomImage().xProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_CUSTOM_Y.textProperty().bindBidirectional(situationProperty.get().getCustomImage().yProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_CUSTOM_WIDTH.textProperty().bindBidirectional(situationProperty.get().getCustomImage().widthProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Number fromString(String string) {
                return StringUtil.isInteger(string) ? Integer.parseInt(string) : null;
            }
        });
        INPUT_CUSTOM_HEIGHT.textProperty().bindBidirectional(situationProperty.get().getCustomImage().heightProperty(), new StringConverter<Number>() {
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

    private void initCheck() {
        CHECK_CLICK.selectedProperty().bindBidirectional(situationProperty.get().clickProperty());
        CHECK_CUSTOM.selectedProperty().bindBidirectional(situationProperty.get().customProperty());
    }

    private void initPane() {
        HBOX_CUSTOM.visibleProperty().bind(situationProperty.getValue().customProperty());
    }

    private void updateSituationProperty(Situation newSituation) {
        if(newSituation == null) newSituation = new Situation();
        situationProperty.get().setName(newSituation.getName());
        situationProperty.get().setPriority(newSituation.getPriority());
        situationProperty.get().setLowestSimile(newSituation.getLowestSimile());
        situationProperty.get().setClick(newSituation.isClick());
        situationProperty.get().setAction(newSituation.getAction());
        situationProperty.get().setCustom(newSituation.isCustom());
        //image
        situationProperty.get().getImage().setPath(newSituation.getImage().getPath());
        situationProperty.get().getImage().setX(newSituation.getImage().getX());
        situationProperty.get().getImage().setY(newSituation.getImage().getY());
        situationProperty.get().getImage().setHeight(newSituation.getImage().getHeight());
        situationProperty.get().getImage().setWidth(newSituation.getImage().getWidth());
        //customImage
        situationProperty.get().getCustomImage().setX(newSituation.getCustomImage().getX());
        situationProperty.get().getCustomImage().setY(newSituation.getCustomImage().getY());
        situationProperty.get().getCustomImage().setHeight(newSituation.getCustomImage().getHeight());
        situationProperty.get().getCustomImage().setWidth(newSituation.getCustomImage().getWidth());
    }

}
