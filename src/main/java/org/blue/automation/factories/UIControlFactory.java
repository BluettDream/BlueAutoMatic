package org.blue.automation.factories;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import org.blue.automation.utils.StringUtil;

import java.io.File;

/**
 * name: MengHao Tian
 * date: 2022/4/26 11:23
 */
public class UIControlFactory {

    /**
     * 创建一个带输入框的弹窗
     *
     * @param title 标题
     * @param headerText 头部信息
     * @param contentText 内容信息
     * @return 输入框弹窗
     **/
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

    /**
     * 创建一个图像文件选择器
     *
     * @param title 标题
     * @param preDirectoryPath 初始化打开路径
     * @return 文件选择器
     **/
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
        if(!StringUtil.isWrong(preDirectoryPath)) fileChooser.setInitialDirectory(new File(preDirectoryPath));
        return fileChooser;
    }
}
