package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.ImageEditor;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.saver.EntrySaver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

public abstract class EditorCtrl {

    public enum EditorMode {
        ADD,
        EDIT;
    }

    protected static Logger log = Logger.getLogger(EditorCtrl.class.getName());

    protected BufferedImage currentIcon;

    @FXML
    protected ImageView imageView;
    @FXML
    protected TextField titleField;
    protected EditorMode currentMode = EditorMode.ADD;

    @FXML
    protected void cancelDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public abstract void initEditorForEditMode(Entry entry);

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
            BufferedImage icon = ImageEditor.loadImage(selectedImage);

            if(validateIconSize(icon)) {
                setImage(selectedImage.getPath());
                currentIcon = icon;
            }
            else {
                AlertInfo.showAlert("Иконка слишком маленькая", "Иконка слишком маленькая, выберите иконку побольше");
                log.info("icon for group is small then " + EntrySaver.IMAGE_HEIGHT);
                setImage(null);
                currentIcon = null;
            }
        }
    }

    protected boolean validateIconSize(BufferedImage image) {
        if (image != null && (image.getHeight() < EntrySaver.IMAGE_HEIGHT
                || image.getWidth() < EntrySaver.IMAGE_HEIGHT)) {
            return false;
        }

        return true;
    }

    protected void setImage(String pathToIcon) {
        Image icon = new Image("file:///" + pathToIcon);
        imageView.setImage(icon);
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
