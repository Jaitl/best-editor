package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.gui.editor.EditorCtrl;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.manager.RecordManager;
import com.jaitlapps.besteditor.saver.GroupSaver;
import com.jaitlapps.besteditor.saver.RecordSaver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

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
                    recordSaver.saveRecord(recordEntry, currentImage);
                    clearDialog();

                    recordEntry = new RecordEntry();
                } else if(currentMode == EditorMode.EDIT) {
                    log.info("update record: " + recordEntry.getTitle());
                    //recordSaver.updateRecord(recordEntry, currentImage);
                }

                cancelDialog(event);
            } else {
                AlertInfo.showAlert("Иконка статьи не выбрана", "Иконка статьи не выбрана!");
            }
        } else {
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Название статьи\" не заполнено!");
        }
    }
}
