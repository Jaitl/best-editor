package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.ZipFolder;
import com.jaitlapps.besteditor.gui.list.GroupListCtrl;
import com.jaitlapps.besteditor.gui.list.RecordListCtrl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MainMenuCtrl extends Application {

    Stage primaryStage;

    private static Logger log = LoggerFactory.getLogger(ContentPreviewCtrl.class);

    private static CommonPreferences preferences = CommonPreferences.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/main_menu.fxml"));

        Scene scene = new Scene(root, 300, 240);

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
    private void selectWorkFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выбор рабочей папки");

        String workFolder = preferences.getWorkFolder();

        if(workFolder != null && Files.exists(Paths.get(workFolder)))
            directoryChooser.setInitialDirectory(new File(workFolder));

        File selectedDirectory =
                directoryChooser.showDialog(primaryStage);

        log.info("selected work directory: " + selectedDirectory);

        if(selectedDirectory != null) {
            preferences.putWorkFolder(selectedDirectory.getPath());
            createStructureDirectories();
        }
    }

    @FXML
    private void createZipArchiveAction() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ZIP", "*.zip")
        );

        fileChooser.setInitialFileName("BestAdvice-content.zip");

        if(preferences.getArchiveImageFolder() != null) {
            fileChooser.setInitialDirectory(Paths.get(preferences.getArchiveImageFolder()).toFile());
        }

        fileChooser.setTitle("Выребире куда сохранить даннные");
        File file = fileChooser.showSaveDialog(primaryStage);

        if(file != null) {
            preferences.putArchiveImageFolder(file.getParent());
            try {
                ZipFolder.zipDir(preferences.getWorkFolder(), file.getPath());
            } catch (Exception e) {
                log.error("zip folder error", e);
            }
        }
    }

    @FXML
    private void groupEditor() throws IOException {
        if(isWorkFolderNotConsist()) {
            selectWorkFolder();
            return;
        }

        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getClassLoader().getResourceAsStream("gui/group_list.fxml"));

        GroupListCtrl groupListCtrl = fxmlLoader.getController();

        stage.setTitle("Список групп");
        Scene scene = new Scene(root, 500, 500);
        stage.setMinWidth(500);
        stage.setMinHeight(500);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        groupListCtrl.loadList();
        stage.show();
    }

    @FXML
    private void recordEditor() throws IOException {
        if(isWorkFolderNotConsist()) {
            selectWorkFolder();
            return;
        }

        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getClassLoader().getResourceAsStream("gui/record_list.fxml"));

        RecordListCtrl recordListCtrl = fxmlLoader.getController();

        stage.setTitle("Список статей");
        Scene scene = new Scene(root, 500, 500);
        stage.setMinWidth(500);
        stage.setMinHeight(500);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        recordListCtrl.loadList();
        stage.show();
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        Platform.exit();
    }

    private void createStructureDirectories() {

        if(Files.notExists(Paths.get(preferences.getWorkFolder(), "data"))) {
            try {
                Files.createDirectory(Paths.get(preferences.getWorkFolder(), "data"));
                log.info("create directory: \\data");
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        if(Files.notExists(Paths.get(preferences.getWorkFolder(), "icon"))) {
            try {
                Files.createDirectory(Paths.get(preferences.getWorkFolder(), "icon"));
                log.info("create directory: \\icon");
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        if(Files.notExists(Paths.get(preferences.getWorkFolder(), "icon", "group"))) {
            try {
                Files.createDirectory(Paths.get(preferences.getWorkFolder(), "icon", "group"));
                log.info("create directory: \\icon\\group");
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        if(Files.notExists(Paths.get(preferences.getWorkFolder(), "icon", "record"))) {
            try {
                Files.createDirectory(Paths.get(preferences.getWorkFolder(), "icon", "record"));
                log.info("create directory: \\icon\\record");
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        if(Files.notExists(Paths.get(preferences.getWorkFolder(), "content"))) {
            try {
                Files.createDirectory(Paths.get(preferences.getWorkFolder(), "content"));
                log.info("create directory: \\content");
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        if(Files.notExists(Paths.get(preferences.getWorkFolder(), "content", "images"))) {
            try {
                Files.createDirectory(Paths.get(preferences.getWorkFolder(), "content", "images"));
                log.info("create directory: \\content\\images");
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        if(Files.notExists(Paths.get(preferences.getWorkFolder(), "content", "css", "common_style.css")))
            copyCSSFromResources();
    }

    private void copyCSSFromResources() {
        Path pathToCSSFolder = Paths.get(preferences.getWorkFolder(), "content", "css");

        if(Files.notExists(pathToCSSFolder)) {
            try {
                Files.createDirectory(pathToCSSFolder);
            } catch (IOException e) {
                log.error("error", e);
            }
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("css/common_style.css");

        byte[] buffer = new byte[0];

        try {
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
        } catch (IOException e) {
            log.error("error", e);
        }

        try {
            Files.write(pathToCSSFolder.resolve("common_style.css"), buffer, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            log.error("error", e);
        }

    }

    private boolean isWorkFolderNotConsist() {
        if(preferences.getWorkFolder() != null)
            return false;
        else
            return true;
    }
}
