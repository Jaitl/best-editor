package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.saver.GroupSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.File;
import java.nio.file.Paths;

public class GroupEditorCtrl extends EditorCtrl {

    private GroupEntry groupEntry  = new GroupEntry();;

    @FXML
    private void saveGroup(ActionEvent event) {
        groupEntry.setTitle(titleField.getText().trim());

        if (groupEntry.getTitle() != null && groupEntry.getTitle().length() > 0) {
            if (currentIcon != null) {
                GroupSaver groupSaver = new GroupSaver();

                if(currentMode == EditorMode.ADD) {
                    log.info("save group: " + groupEntry.getTitle());
                    groupSaver.save(groupEntry, currentIcon);

                    groupEntry = new GroupEntry();
                } else if(currentMode == EditorMode.EDIT) {
                    log.info("update group: " + groupEntry.getTitle());
                    groupSaver.update(groupEntry, currentIcon);
                }

                cancelDialog(event);
            } else {
                AlertInfo.showAlert("Поле не заполнено", "Поле \"Иконка группы\" не заполнено!\"");
            }
        } else {
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Название группы\" не заполнено!");
        }
    }

    @Override
    public void initEditorForEditMode(Entry entry) {
        GroupEntry gEntry = (GroupEntry) entry;

        log.info("set group entry for edit:" + gEntry.getId());

        if(currentMode != EditorMode.EDIT)
            enableEditMode();

        this.groupEntry = gEntry;

        titleField.setText(gEntry.getTitle());

        CommonPreferences preferences = CommonPreferences.getInstance();
        String pathToImage = preferences.getWorkFolder() + File.separator + gEntry.getPathToImage();
        setImage(pathToImage);

        currentIcon = loadImage(Paths.get(pathToImage).toFile());
    }
}
