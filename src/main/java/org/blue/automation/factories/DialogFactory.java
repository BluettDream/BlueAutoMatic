package org.blue.automation.factories;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextInputDialog;

import java.io.DataInput;
import java.net.SocketTimeoutException;

/**
 * name: MengHao Tian
 * date: 2022/4/26 11:23
 */
public class DialogFactory {

    public static TextInputDialog createTestInputDialog(String title, String headerText, String contentText){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.setOnCloseRequest(event -> {
            //判断不是取消键,并且输入内容不为空,否则窗口不关闭,弹出提示框要求用户输入
            if (dialog.getResult() != null && (dialog.getEditor().getText().isEmpty() || dialog.getEditor().getText().contains(" "))) {
                event.consume();
                new Alert(Alert.AlertType.WARNING,"输入为空或含有空格,请重新输入").showAndWait();
            }
        });
        return dialog;
    }

}
