package com.jaitlapps.besteditor.gui.list;

import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.gui.editor.RecordEditorCtrl;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.saver.RecordSaver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RecordListCtrl extends ListCtrl<RecordEntry> {

    @FXML
    private ComboBox<GroupEntry> groupFilterComboBox;

    private GroupEntry filterGroupSelected;

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
            log.error("error", e);
        }

        RecordEditorCtrl recordEditorCtrl = loader.getController();
        recordEditorCtrl.loadGroups();

        if(openForEdit) {
            recordEditorCtrl.enableEditMode();
            recordEditorCtrl.loadGroups();
            recordEditorCtrl.initEditorForEditMode(recordEntry);
        } else {
            recordEditorCtrl.enableAddMode();
        }

        stage.setTitle("Редактирование статьи");

        Scene scene = new Scene(root, 800, 600);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        recordEditorCtrl.setStage(stage);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.showAndWait();
        stage.close();

        updateList();
    }

    @FXML
    private void selectGroupFilterAction() {
        filterGroupSelected = groupFilterComboBox.getSelectionModel().getSelectedItem();

        updateList();
    }

    @Override
    protected void updateList() {
        data.clear();

        List<RecordEntry> filterRecords;

        if(filterGroupSelected.getId() != null) {
            filterRecords = entryManager.getList()
                    .stream().filter(g -> g.getGroupId().compareTo(filterGroupSelected.getId()) == 0)
                    .collect(Collectors.toList());
        }
        else {
            filterRecords = entryManager.getList();
        }

        data.addAll(filterRecords);
        log.info("update list filter group: " + filterGroupSelected.getTitle());
    }

    public void loadGroups() {
        EntryManager<GroupEntry> entryManager = EntryManager.createGroupManager();
        ObservableList<GroupEntry> myComboBoxData = FXCollections.observableArrayList(entryManager.getList());

        GroupEntry noGroup = new GroupEntry();
        noGroup.setTitle("нет");

        myComboBoxData.add(0, noGroup);

        groupFilterComboBox.setItems(myComboBoxData);
        groupFilterComboBox.getSelectionModel().select(noGroup);
    }
}
