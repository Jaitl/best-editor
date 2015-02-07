package com.jaitlapps.besteditor.gui.list;

import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.gui.editor.GroupEditorCtrl;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.saver.GroupSaver;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GroupListCtrl extends ListCtrl<GroupEntry> {

    public GroupListCtrl() {
        setManager(EntryManager.createGroupManager());
        setSaver(new GroupSaver());
    }

    @Override
    protected void openEditor(Entry entry, boolean openForEdit) {
        GroupEntry groupEntry = (GroupEntry) entry;

        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();

        Parent root = null;
        try {
            root = loader.load(getClass().getClassLoader().getResourceAsStream("gui/group_editor.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GroupEditorCtrl groupEditorCtrl = loader.getController();

        if(openForEdit) {
            groupEditorCtrl.enableEditMode();
            groupEditorCtrl.setEntry(groupEntry);
        } else {
            groupEditorCtrl.enableAddMode();
        }

        stage.setTitle("Редактирование группы");
        Scene scene = new Scene(root, 400, 450);
        stage.setMinWidth(450);
        stage.setMinHeight(470);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.showAndWait();

        updateList();
    }
}
