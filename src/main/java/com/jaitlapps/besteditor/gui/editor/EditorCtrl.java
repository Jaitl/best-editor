package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.domain.Entry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class EditorCtrl {

    public enum EditorMode {
        ADD,
        EDIT;
    }

    protected static Logger log = LoggerFactory.getLogger(EditorCtrl.class);
    protected CommonPreferences preferences = CommonPreferences.getInstance();

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
                new FileChooser.ExtensionFilter("Image Files", preferences.getIconType())
        );

        System.out.println(preferences.getRecentIconFolder());


        if(preferences.getRecentIconFolder() != null) {
            Path resentFolder = Paths.get(preferences.getRecentIconFolder());

            if (Files.exists(resentFolder))
                fileChooser.setInitialDirectory(resentFolder.toFile());
        }

        File selectedImage = fileChooser.showOpenDialog(null);
        log.info("selected icon:" + selectedImage);
        if (selectedImage != null) {
            preferences.putRecentIconFolder(selectedImage.getParent());

            BufferedImage icon = loadImage(selectedImage);

            if(validateIconSize(icon)) {
                setImage(selectedImage.getPath());
                currentIcon = icon;
            }
            else {
                AlertInfo.showAlert("Иконка слишком маленькая", "Иконка слишком маленькая, выберите иконку побольше");
                log.info("icon for group is small then " + Integer.parseInt(preferences.getIconSize()));
                setImage(null);
                currentIcon = null;
            }
        }
    }

    protected BufferedImage loadImage(File pathToImage) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(pathToImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    protected boolean validateIconSize(BufferedImage image) {
        int iconSize = Integer.parseInt(preferences.getIconSize());
        if (image != null && (image.getHeight() >= iconSize)
                || image.getWidth() >= iconSize) {
            return true;
        }

        return false;
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
