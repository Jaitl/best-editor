package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.GroupSaver;
import com.jaitlapps.besteditor.Saver;
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

public class GroupEditorCtrl extends EditorCtrl {

    private GroupEntry groupEntry  = new GroupEntry();;

    @Override
    public void start(Stage primaryStage) throws Exception {

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
                        log.info("update group: " + groupEntry.getTitle());
                        groupSaver.updateGroup(groupEntry, currentImage);
                    }

                    cancelDialog(event);

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
