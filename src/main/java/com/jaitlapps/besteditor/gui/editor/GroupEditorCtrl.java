package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.saver.GroupSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class GroupEditorCtrl extends EditorCtrl {

    private GroupEntry groupEntry  = new GroupEntry();;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @FXML
    private void saveGroup(ActionEvent event) {
        groupEntry.setTitle(titleField.getText());

        if (groupEntry.getTitle() != null && groupEntry.getTitle().length() > 0) {
            if (currentImage != null) {
                GroupSaver groupSaver = new GroupSaver();

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

        titleField.setText(groupEntry.getTitle());

        CommonPreferences preferences = CommonPreferences.getInstance();
        String pathToImage = preferences.getWorkFolder() + File.separator + groupEntry.getPathToImage();
        setImage(pathToImage);

        currentImage = Paths.get(pathToImage).toFile();
    }
}
