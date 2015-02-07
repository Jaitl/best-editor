package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.saver.Saver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class EditorCtrl extends Application {

    public enum EditorMode {
        ADD,
        EDIT;
    }

    protected static Logger log = Logger.getLogger(EditorCtrl.class.getName());

    protected File currentImage;

    @FXML
    protected ImageView imageView;

    @FXML
    protected TextField groupTitleField;

    protected EditorMode currentMode = EditorMode.ADD;

    @FXML
    protected void cancelDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    protected void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выбор иконки");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File selectedImage = fileChooser.showOpenDialog(null);
        log.info("selected icon:" + selectedImage);
        if (selectedImage != null) {
            if(validateIconSize(selectedImage)) {
                setImage(selectedImage.getPath());
                currentImage = selectedImage;
            }
            else {
                AlertInfo.showAlert("Иконка слишком маленькая", "Иконка слишком маленькая, выберите иконку побольше");
                log.info("icon for group is small then " + Saver.IMAGE_HEIGHT);
                setImage(null);
                currentImage = null;
            }
        }
    }

    protected boolean validateIconSize(File selectedImage) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null && (image.getHeight() < Saver.IMAGE_HEIGHT
                || image.getWidth() < Saver.IMAGE_HEIGHT)) {
            return false;
        }

        return true;
    }

    protected void setImage(String pathToIcon) {
        Image icon = new Image("file:///" + pathToIcon);
        imageView.setImage(icon);
    }

    protected void clearDialog() {
        groupTitleField.setText("");
        imageView.setImage(null);
    }

    public void enableAddMode() {
        currentMode = EditorMode.ADD;
        log.info("editor in add mode");
    }

    public void enableEditMode() {
        currentMode = EditorMode.EDIT;
        log.info("editor in edit mode");
    }
}
