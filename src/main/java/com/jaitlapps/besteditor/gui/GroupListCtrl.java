package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.EntryManager;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.saver.GroupSaver;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class GroupListCtrl extends Application {

    private static Logger log = Logger.getLogger(GroupListCtrl.class.getName());

    @FXML
    private ListView<GroupEntry> groupListView;

    private ObservableList<GroupEntry> data;

    private EntryManager<GroupEntry> entryManager;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void loadGroupsList() {
        entryManager = EntryManager.createGroupManager();

        data = FXCollections.observableArrayList(entryManager.getList());

        groupListView.setItems(data);
    }

    private void updateGroupsList() {
        data.clear();
        data.addAll(entryManager.getList());
        log.info("update group list");
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void editGroupDouble(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            editGroup();
        }
    }

    @FXML
    private void editGroup() {
        GroupEntry item = groupListView.getFocusModel().getFocusedItem();

        if(item != null) {
            openGroupEditor(item, true);
        } else {
            AlertInfo.showAlert("Группа для редактирования не выбрана", "Группа для редактирования не выбрана. Выберите группу для редактирования.");
        }
    }

    @FXML
    private void deleteGroup() {
        GroupEntry item = groupListView.getFocusModel().getFocusedItem();

        if(item != null) {
            GroupSaver groupSaver = new GroupSaver();
            groupSaver.deleteGroup(item);
            updateGroupsList();
        } else {
            AlertInfo.showAlert("Группа для удаления не выбрана", "Группа для удаления не выбрана. Выберите группу для удаления.");
        }
    }

    @FXML
    private void addGroup() {
        openGroupEditor(null, false);
    }

    private void openGroupEditor(GroupEntry groupEntry, boolean openForEdit) {
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
            groupEditorCtrl.setGroupEntry(groupEntry);
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

        updateGroupsList();
    }
}
