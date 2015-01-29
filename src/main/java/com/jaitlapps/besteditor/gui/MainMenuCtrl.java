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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class MainMenuCtrl extends Application {

    Stage primaryStage;

    private static Logger log = Logger.getLogger(MainMenuCtrl.class.getName());

    private static CommonPreferences commonPreferences = CommonPreferences.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/main_menu.fxml"));

        Scene scene = new Scene(root, 300, 250);

        scene.getStylesheets().add("gui/style.css");

        primaryStage.setTitle("Главное Меню");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private void addGroup(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getClassLoader().getResourceAsStream("gui/group_editor.fxml"));

        GroupEditorCtrl groupEditorCtrl = loader.getController();
        groupEditorCtrl.enableAddMode();

        stage.setTitle("Добавление группы");
        Scene scene = new Scene(root, 400, 450);
        stage.setMinWidth(450);
        stage.setMinHeight(470);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.show();


    }

    @FXML
    private void selectWorkFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выбор рабочей папки");

        String workFolder = commonPreferences.getWorkFolder();

        if(workFolder != null && Files.exists(Paths.get(workFolder)))
            directoryChooser.setInitialDirectory(new File(workFolder));

        File selectedDirectory =
                directoryChooser.showDialog(primaryStage);

        log.info("selected work directory: " + selectedDirectory);

        if(selectedDirectory != null) {
            commonPreferences.putWorkFolder(selectedDirectory.getPath());
            createStructureDirectories();
        }
    }

    @FXML
    private void listGroups() throws IOException {
        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getClassLoader().getResourceAsStream("gui/group_list.fxml"));

        GroupListCtrl groupListCtrl = fxmlLoader.getController();

        stage.setTitle("Список групп");
        Scene scene = new Scene(root, 400, 500);
        stage.setMinWidth(400);
        stage.setMinHeight(400);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        groupListCtrl.loadGroupsList();
        stage.show();
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        Platform.exit();
    }

    private void createStructureDirectories() {

        if(Files.notExists(Paths.get(commonPreferences.getWorkFolder(), "data"))) {
            try {
                Files.createDirectory(Paths.get(commonPreferences.getWorkFolder(), "data"));
                log.info("create directory: \\data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Files.notExists(Paths.get(commonPreferences.getWorkFolder(), "icon"))) {
            try {
                Files.createDirectory(Paths.get(commonPreferences.getWorkFolder(), "icon"));
                log.info("create directory: \\icon");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Files.notExists(Paths.get(commonPreferences.getWorkFolder(), "icon", "group"))) {
            try {
                Files.createDirectory(Paths.get(commonPreferences.getWorkFolder(), "icon", "group"));
                log.info("create directory: \\icon\\group");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
