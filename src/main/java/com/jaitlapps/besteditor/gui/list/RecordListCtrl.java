package com.jaitlapps.besteditor.gui.list;

import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.gui.editor.RecordEditorCtrl;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.saver.RecordSaver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RecordListCtrl extends ListCtrl<RecordEntry> {

    public RecordListCtrl() {
        setManager(EntryManager.createRecordManager());
        setSaver(new RecordSaver());
    }

    @Override
    protected void openEditor(Entry entry, boolean openForEdit) {
        RecordEntry recordEntry = (RecordEntry) entry;

        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();

        Parent root = null;
        try {
            root = loader.load(getClass().getClassLoader().getResourceAsStream("gui/record_editor.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecordEditorCtrl recordEditorCtrl = loader.getController();
        recordEditorCtrl.loadGroups();

        if(openForEdit) {
            recordEditorCtrl.enableEditMode();
            recordEditorCtrl.setEntry(recordEntry);
        } else {
            recordEditorCtrl.enableAddMode();
        }

        stage.setTitle("Редактирование статьи");

        Scene scene = new Scene(root, 800, 600);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.showAndWait();

        updateList();
    }
}
