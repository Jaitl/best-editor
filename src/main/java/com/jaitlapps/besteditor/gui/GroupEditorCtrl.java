package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.GroupSaver;
import com.jaitlapps.besteditor.domain.GroupEntry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class GroupEditorCtrl extends Application {

    private static Logger log = Logger.getLogger(GroupEditorCtrl.class.getName());

    public enum EditorMode {
        ADD,
        EDIT;
    }
    private EditorMode currentMode = EditorMode.ADD;

    private GroupEntry groupEntry  = new GroupEntry();;

    private File currentImage;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField groupTitleField;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @FXML
    private void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выбор иконки для группы");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File selectedImage = fileChooser.showOpenDialog(null);
        log.info("selected icon for group:" + selectedImage);
        if (selectedImage != null) {
            if(validateIconSize(selectedImage)) {
                setImage(selectedImage.getPath());
                currentImage = selectedImage;
            }
            else {
                AlertInfo.showAlert("Иконка группы слишком маленькая", "Иконка группы слишком маленькая, выберите иконку побольше");
                log.info("icon for group is small then " + GroupSaver.IMAGE_HEIGHT);
                setImage(null);
                currentImage = null;
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


        groupEntry.setTitle(groupTitleField.getText());

        if (groupEntry.getTitle() != null && groupEntry.getTitle().length() > 0) {
            if (currentImage != null) {
                GroupSaver groupSaver = new GroupSaver();
                try {
                    if(currentMode == EditorMode.ADD) {
                        log.info("save group: " + groupEntry.getTitle());
                        groupSaver.saveGroup(groupEntry, currentImage);
                        clearDialog();
                        groupEntry = new GroupEntry();
                    } else if(currentMode == EditorMode.EDIT) {

                        groupSaver.updateGroup(groupEntry, currentImage);
                        log.info("update group: " + groupEntry.getTitle());

                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    }
                } catch (IOException e) {
                    AlertInfo.showAlert("Ошибка", e.getMessage());
                }
            } else {
                AlertInfo.showAlert("Иконка группы не выбрана", "Иконка группы не выбрана!");
            }
        } else {
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Название группы\" не заполнено!");
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

    public void enableAddMode() {
        currentMode = EditorMode.ADD;
        log.info("group editor in add mode");
    }

    public void enableEditMode() {
        currentMode = EditorMode.EDIT;
        log.info("group editor in edit mode");
    }

    public void setGroupEntry(GroupEntry groupEntry) {
        log.info("set group entry for edit:" + groupEntry.getId());

        if(currentMode != EditorMode.EDIT)
            enableEditMode();

        this.groupEntry = groupEntry;

        groupTitleField.setText(groupEntry.getTitle());

        CommonPreferences preferences = CommonPreferences.getInstance();
        String pathToImage = preferences.getWorkFolder() + File.separator + groupEntry.getPathToImage();
        setImage(pathToImage);

        currentImage = Paths.get(pathToImage).toFile();
    }
}
