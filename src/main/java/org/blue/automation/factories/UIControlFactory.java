package org.blue.automation.factories;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.Struct;

/**
 * name: MengHao Tian
 * date: 2022/4/26 11:23
 */
public class UIControlFactory {

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

    public static FileChooser createImageFileChooser(String title){
        return createImageFileChooser(title,null);
    }

    public static FileChooser createImageFileChooser(String title,String preDirectoryPath){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                //new FileChooser.ExtensionFilter("All Images", "*.*"),
                //new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                //new FileChooser.ExtensionFilter("GIF", "*.gif"),
                //new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        if(!StringUtils.isBlank(preDirectoryPath)) fileChooser.setInitialDirectory(new File(preDirectoryPath));
        return fileChooser;
    }
}
