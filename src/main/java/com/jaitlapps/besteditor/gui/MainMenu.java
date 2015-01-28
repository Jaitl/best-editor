package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.CommonPreferences;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenu extends Application {

    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/main_menu.fxml"));

        Scene scene = new Scene(root, 300, 250);

        scene.getStylesheets().add
                (getClass().getClassLoader().getResource("gui/style.css").toExternalForm());

        primaryStage.setTitle("Главное Меню");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private void AddGroup(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/group_editor.fxml"));

        stage.setTitle("Добавление группы");
        Scene scene = new Scene(root, 400, 450);
        stage.setMinWidth(450);
        stage.setMinHeight(470);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void selectWorkFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выбор рабочей папки");

        CommonPreferences commonPreferences = new CommonPreferences();

        String workFolder = commonPreferences.getWorkFolder();

        if(workFolder != null) {
            directoryChooser.setInitialDirectory(new File(workFolder));
        }

        File selectedDirectory =
                directoryChooser.showDialog(primaryStage);

        if(selectedDirectory != null) {
            commonPreferences.putWorkFolder(selectedDirectory.getPath());
            System.out.println(selectedDirectory.getPath());
        }
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        Platform.exit();
    }
}
