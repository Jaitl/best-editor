package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.manager.GroupManager;
import com.jaitlapps.besteditor.saver.RecordSaver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class RecordEditorCtrl extends EditorCtrl {

    @FXML
    private TextField authorNameField;
    @FXML
    private TextField authorUrlField;
    @FXML
    private CheckBox isAuthorCheckBox;
    @FXML
    private ComboBox<GroupEntry> groupComboBox;

    private RecordEntry recordEntry = new RecordEntry();

    public void loadGroups() {
        EntryManager<GroupEntry> entryManager = EntryManager.createGroupManager();
        ObservableList<GroupEntry> myComboBoxData = FXCollections.observableArrayList(entryManager.getList());

        groupComboBox.setItems(myComboBoxData);
    }

    @FXML
    private void saveRecord(ActionEvent event) {
        recordEntry.setTitle(titleField.getText());

        if (recordEntry.getTitle() != null && recordEntry.getTitle().length() > 0) {
            if (currentImage != null) {
                RecordSaver recordSaver = new RecordSaver();

                if(currentMode == EditorMode.ADD) {
                    log.info("save record: " + recordEntry.getTitle());
                    recordSaver.save(recordEntry, currentImage);
                    clearDialog();

                    recordEntry = new RecordEntry();
                } else if(currentMode == EditorMode.EDIT) {
                    log.info("update record: " + recordEntry.getTitle());
                    recordSaver.update(recordEntry, currentImage);
                }

                cancelDialog(event);
            } else {
                AlertInfo.showAlert("Иконка статьи не выбрана", "Иконка статьи не выбрана!");
            }
        } else {
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Название статьи\" не заполнено!");
        }
    }

    @Override
    public void setEntry(Entry entry) {
        RecordEntry rEntry = (RecordEntry) entry;

        log.info("set record entry for edit:" + rEntry.getId());

        if(currentMode != EditorMode.EDIT)
            enableEditMode();

        this.recordEntry = rEntry;

        titleField.setText(rEntry.getTitle());

        CommonPreferences preferences = CommonPreferences.getInstance();
        String pathToImage = preferences.getWorkFolder() + File.separator + rEntry.getPathToImage();
        setImage(pathToImage);

        currentImage = Paths.get(pathToImage).toFile();
    }

    @FXML
    private void isAuthorAction() {
        if(isAuthorCheckBox.isSelected()) {
            authorNameField.setEditable(false);
            authorUrlField.setEditable(false);
        } else {
            authorNameField.setEditable(true);
            authorUrlField.setEditable(true);
        }
    }
}
