package com.jaitlapps.besteditor.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class RecordEditorCtrl {

    @FXML
    private void selectImage() {

    }

    @FXML
    private void cancelDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void saveRecord() {

    }

}
