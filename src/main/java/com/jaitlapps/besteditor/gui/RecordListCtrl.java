package com.jaitlapps.besteditor.gui;

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
import java.util.Objects;

public class RecordListCtrl {

    private ListView<Objects> recordListView;

    public void loadListRecords() {

    }

    @FXML
    private void addRecord() {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();

        Parent root = null;
        try {
            root = loader.load(getClass().getClassLoader().getResourceAsStream("gui/record_editor.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //GroupEditorCtrl groupEditorCtrl = loader.getController();

        //stage.setTitle("Редактирование группы");
        Scene scene = new Scene(root, 800, 600);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void editRecord() {

    }

    @FXML
    private void deleteRecord() {

    }

    @FXML
    private void editRecordDouble(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            editRecord();
        }
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
