package org.blue.automation.controller;

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

    private static final SimpleStringProperty RESULT = new SimpleStringProperty();
    private static final SimpleDoubleProperty WAIT_TIME = new SimpleDoubleProperty();
    private static final SimpleListProperty<SituationBase> SITUATION_LIST = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * 主线程池
     **/
    private final ExecutorService THREAD_POOL = Main.THREAD_POOL;
    /**
     * 正在运行的模式线程
     **/
    private Future<Boolean> runningMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ModeCallable callable = new ModeCallable();
        runningMode = THREAD_POOL.submit(callable);
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
        Main.STAGE_MAP.get("modeRunningStage").setOnCloseRequest(event -> runningMode.cancel(true));
        RESULT.bindBidirectional(LABEL_RESULT_SHOW.textProperty());
        PROGRESS_MAX_WAIT_TIME.progressProperty().bindBidirectional(WAIT_TIME);
        PROGRESS_MAX_WAIT_TIME.progressProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue() >= 1){
                runningMode.cancel(true);
                LABEL_RESULT_SHOW.setText("运行结束");
            }
        });
        LISTVIEW_SITUATION.itemsProperty().bindBidirectional(SITUATION_LIST);
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
}
