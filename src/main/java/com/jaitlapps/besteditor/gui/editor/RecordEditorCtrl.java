package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.saver.RecordSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class RecordEditorCtrl extends EditorCtrl {

    private RecordEntry recordEntry = new RecordEntry();

    @Override
    public void start(Stage primaryStage) throws Exception {

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
}
