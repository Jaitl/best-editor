package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.saver.RecordSaver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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

    private RecordEntry currentRecordEntry = new RecordEntry();

    public void loadGroups() {
        EntryManager<GroupEntry> entryManager = EntryManager.createGroupManager();
        ObservableList<GroupEntry> myComboBoxData = FXCollections.observableArrayList(entryManager.getList());

        groupComboBox.setItems(myComboBoxData);
    }

    @FXML
    private void saveRecord(ActionEvent event) {

        if(recordEntry() != null) {

            RecordSaver recordSaver = new RecordSaver();

            if (currentMode == EditorMode.ADD) {
                log.info("save record: " + currentRecordEntry.getTitle());
                recordSaver.save(currentRecordEntry, currentImage);
                clearDialog();

                currentRecordEntry = new RecordEntry();
            } else if (currentMode == EditorMode.EDIT) {
                log.info("update record: " + currentRecordEntry.getTitle());
                recordSaver.update(currentRecordEntry, currentImage);
            }

            cancelDialog(event);
        }

    }

    private RecordEntry recordEntry() {
        if(titleField.getText() != null && titleField.getText().length() > 0) {
            currentRecordEntry.setTitle(titleField.getText());
        } else {
            currentRecordEntry.setTitle(null);
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Название статьи\" не заполнено!");
            return null;
        }

        if(currentImage == null) {
            AlertInfo.showAlert("Иконка статьи не выбрана", "Иконка статьи не выбрана!");
            return null;
        }

        GroupEntry groupEntry = groupComboBox.getSelectionModel().getSelectedItem();
        if(groupEntry != null) {
            currentRecordEntry.setGroupId(groupEntry.getId());
        } else {
            currentRecordEntry.setGroupId(null);
            AlertInfo.showAlert("Группа для статьи не выбрана", "Группа для статьи не выбрана!");
            return null;
        }

        if(isAuthorCheckBox.isSelected()){
            currentRecordEntry.setAuthorExist(false);
            currentRecordEntry.setAuthorName(null);
            currentRecordEntry.setAuthorURL(null);
        } else {
            currentRecordEntry.setAuthorExist(true);

            if(authorNameField.getText().length() > 0) {
                currentRecordEntry.setAuthorName(authorNameField.getText());
            } else {
                currentRecordEntry.setAuthorName(null);
                AlertInfo.showAlert("Поле \"Имя автора\" не заполнено", "Поле \"Имя автора\" не заполнено!");
                return null;
            }

            if(authorUrlField.getText().length() > 0) {
                currentRecordEntry.setAuthorURL(authorUrlField.getText());
            } else {
                currentRecordEntry.setAuthorURL(null);
                AlertInfo.showAlert("Поле \"Ссылка на статью\" не заполнено", "Поле \"Ссылка на статью\" не заполнено!");
                return null;
            }
        }

        return currentRecordEntry;
    }

    @Override
    public void setEntry(Entry entry) {
        RecordEntry recordEntry = (RecordEntry) entry;

        log.info("set record entry for edit:" + recordEntry.getId());

        if(currentMode != EditorMode.EDIT)
            enableEditMode();

        this.currentRecordEntry = recordEntry;

        titleField.setText(recordEntry.getTitle());

        CommonPreferences preferences = CommonPreferences.getInstance();
        String pathToImage = preferences.getWorkFolder() + File.separator + recordEntry.getPathToImage();
        setImage(pathToImage);

        currentImage = Paths.get(pathToImage).toFile();

        GroupEntry selectedGroupEntry = groupComboBox.getItems().stream()
                .filter(x -> x.getId().compareTo(recordEntry.getGroupId()) == 0).findFirst().get();

        groupComboBox.getSelectionModel().select(selectedGroupEntry);

        if(recordEntry.isAuthorExist()) {
            authorNameField.setText(recordEntry.getAuthorName());
            authorUrlField.setText(recordEntry.getAuthorURL());
        } else {
            isAuthorCheckBox.setSelected(true);
            isAuthorExistAction();
        }
    }

    @FXML
    private void isAuthorExistAction() {
        if(isAuthorCheckBox.isSelected()) {
            authorNameField.setEditable(false);
            authorNameField.setVisible(false);

            authorUrlField.setEditable(false);
            authorUrlField.setVisible(false);

        } else {
            authorNameField.setEditable(true);
            authorNameField.setVisible(true);

            authorUrlField.setEditable(true);
            authorUrlField.setVisible(true);
        }
    }
}
