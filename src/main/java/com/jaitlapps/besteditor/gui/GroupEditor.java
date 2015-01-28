package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.GroupSaver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GroupEditor extends Application {

    Stage primaryStage;

    @FXML
    ImageView imageView;

    @FXML
    TextField groupTitleField;

    File iconFile;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
    }

    @FXML
    private void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выбор иконки для группы");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File selectedImage = fileChooser.showOpenDialog(primaryStage);

        if (selectedImage != null) {
            if(validateIconSize(selectedImage)) {
                setImage(selectedImage.getPath());
                iconFile = selectedImage;
            }
            else {
                Dialogs.create()
                        .owner(primaryStage)
                        .title("Иконка группы слишком маленькая")
                        .message("Иконка группы слишком маленькая, выберите иконку побольше")
                        .showInformation();
                setImage(null);
                iconFile = null;
            }
        }
    }

    private boolean validateIconSize(File selectedImage) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null && (image.getHeight() < GroupSaver.IMAGE_HEIGHT
                || image.getWidth() < GroupSaver.IMAGE_HEIGHT)) {
            return false;
        }

        return true;
    }

    @FXML
    private void cancelDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void saveGroup(ActionEvent event) {
        String groupTitle = groupTitleField.getText();

        if (groupTitle != null && groupTitle.length() > 0) {
            if (iconFile != null) {
                GroupSaver groupSaver = new GroupSaver();
                try {
                    groupSaver.saveGroup(groupTitle, iconFile);
                    clearDialog();
                } catch (IOException e) {
                    Dialogs.create()
                            .owner(primaryStage)
                            .title("Ошибка")
                            .message(e.getMessage())
                            .showInformation();
                }
            } else {
                Dialogs.create()
                        .owner(primaryStage)
                        .title("Иконка группы не выбрана")
                        .message("Иконка группы не выбрана!")
                        .showInformation();
            }
        } else {
            Dialogs.create()
                    .owner(primaryStage)
                    .title("Поле не заполнено")
                    .message("Поле \"Название группы\" не заполнено!")
                    .showInformation();
        }
    }



    private void setImage(String pathToIcon) {
        Image icon = new Image("file:///" + pathToIcon);
        imageView.setImage(icon);
    }

    private void clearDialog() {
        groupTitleField.setText("");
        imageView.setImage(null);
    }
}
