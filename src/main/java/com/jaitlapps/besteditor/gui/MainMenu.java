package com.jaitlapps.besteditor.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/main_menu.fxml"));



        Scene scene = new Scene(root, 300, 300);

        scene.getStylesheets().add
                (getClass().getClassLoader().getResource("gui/style.css").toExternalForm());

        primaryStage.setTitle("Главное Меню");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private void AddGroup(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/group.fxml"));

        stage.setTitle("Добавление группы");
        Scene scene = new Scene(root, 400, 400);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.show();
    }
}
