package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.GroupManager;
import com.jaitlapps.besteditor.domain.Group;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GroupList extends Application {

    @FXML
    private ListView<Group> groupListView;

    ObservableList<Group> data;

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void loadList() {
        GroupManager groupManager = GroupManager.getInstance();

        data = FXCollections.observableArrayList(groupManager.getGroups());

        groupListView.setItems(data);
    }

    @FXML
    private void editGroupDouble(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            editGroup(null);
        }
    }

    @FXML
    private void editGroup(ActionEvent event) {
        Group item = groupListView.getFocusModel().getFocusedItem();

        if(item != null) {
            System.out.println(item);
        }

    }

    @FXML
    private void deleteGroup(ActionEvent event) {

    }


}
