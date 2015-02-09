package com.jaitlapps.besteditor.gui.editor;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.ImageEditor;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.domain.GroupEntry;
import com.jaitlapps.besteditor.domain.RecordEntry;
import com.jaitlapps.besteditor.gui.ContentPreviewCtrl;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.saver.RecordSaver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class RecordEditorCtrl extends EditorCtrl {

    @FXML
    private TextField authorNameField;
    @FXML
    private TextField authorUrlField;
    @FXML
    private CheckBox isAuthorCheckBox;
    @FXML
    private ComboBox<GroupEntry> groupComboBox;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private TextArea imageTextArea;

    private ImageEditor imageEditor = new ImageEditor();

    private RecordEntry currentRecordEntry = new RecordEntry();


    public void setStage(Stage primaryStage) {
        primaryStage.setOnHiding(we -> imageEditor.deleteNoUsingImages(contentTextArea.getText()));
    }

    public void loadGroups() {
        EntryManager<GroupEntry> entryManager = EntryManager.createGroupManager();
        ObservableList<GroupEntry> myComboBoxData = FXCollections.observableArrayList(entryManager.getList());

        groupComboBox.setItems(myComboBoxData);
    }

    @FXML
    private void saveRecord(ActionEvent event) {

        if(recordEntry() != null) {

            RecordSaver recordSaver = new RecordSaver();
            recordSaver.setImageEditor(imageEditor);

            if (currentMode == EditorMode.ADD) {
                log.info("save record: " + currentRecordEntry.getTitle());
                recordSaver.save(currentRecordEntry, currentIcon, contentTextArea.getText());

                currentRecordEntry = new RecordEntry();
            } else if (currentMode == EditorMode.EDIT) {
                log.info("update record: " + currentRecordEntry.getTitle());
                recordSaver.update(currentRecordEntry, currentIcon, contentTextArea.getText());
            }

            cancelDialog(event);
        }
    }

    private RecordEntry recordEntry() {
        if(titleField.getText() != null && titleField.getText().length() > 0) {
            currentRecordEntry.setTitle(titleField.getText());
        } else {
            currentRecordEntry.setTitle(null);
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Название статьи\" не заполнено!");
            return null;
        }

        if(currentIcon == null) {
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Иконка статьи\" не заполнено!");
            return null;
        }

        GroupEntry groupEntry = groupComboBox.getSelectionModel().getSelectedItem();
        if(groupEntry != null) {
            currentRecordEntry.setGroupId(groupEntry.getId());
        } else {
            currentRecordEntry.setGroupId(null);
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Группа\" не заполнено!");
            return null;
        }

        if(isAuthorCheckBox.isSelected()){
            currentRecordEntry.setAuthorExist(false);
            currentRecordEntry.setAuthorName(null);
            currentRecordEntry.setAuthorURL(null);
        } else {
            currentRecordEntry.setAuthorExist(true);

            if(authorNameField.getText().length() > 0) {
                currentRecordEntry.setAuthorName(authorNameField.getText());
            } else {
                currentRecordEntry.setAuthorName(null);
                AlertInfo.showAlert("Поле не заполнено", "Поле \"Имя автора\" не заполнено!");
                return null;
            }

            if(authorUrlField.getText().length() > 0) {
                currentRecordEntry.setAuthorURL(authorUrlField.getText());
            } else {
                currentRecordEntry.setAuthorURL(null);
                AlertInfo.showAlert("Поле не заполнено", "Поле \"Ссылка на статью\" не заполнено!");
                return null;
            }
        }

        if(contentTextArea.getText().length() <= 0) {
            AlertInfo.showAlert("Поле не заполнено", "Поле \"Содержание статьи\" не заполнено!");
            return null;
        }

        return currentRecordEntry;
    }

    @Override
    public void initEditorForEditMode(Entry entry) {
        RecordEntry recordEntry = (RecordEntry) entry;

        log.info("set record entry for edit:" + recordEntry.getId());

        if(currentMode != EditorMode.EDIT)
            enableEditMode();

        this.currentRecordEntry = recordEntry;

        titleField.setText(recordEntry.getTitle());

        CommonPreferences preferences = CommonPreferences.getInstance();
        String pathToImage = preferences.getWorkFolder() + File.separator + recordEntry.getPathToImage();
        setImage(pathToImage);

        currentIcon = ImageEditor.loadImage(Paths.get(pathToImage).toFile());

        GroupEntry selectedGroupEntry = groupComboBox.getItems().stream()
                .filter(x -> x.getId().compareTo(recordEntry.getGroupId()) == 0).findFirst().get();

        groupComboBox.getSelectionModel().select(selectedGroupEntry);

        if(recordEntry.isAuthorExist()) {
            authorNameField.setText(recordEntry.getAuthorName());
            authorUrlField.setText(recordEntry.getAuthorURL());
        } else {
            isAuthorCheckBox.setSelected(true);
            isAuthorExistAction();
        }

        String content = RecordSaver.loadContent(recordEntry.getPathToContent());
        contentTextArea.setText(content);

        imageEditor.findAndLoadImages(content);
        List<String> markDownUrl = imageEditor.findMarkUrlInText(content);
        markDownUrl.forEach(i -> setImageToTextArea(i));
    }

    @FXML
    private void isAuthorExistAction() {
        if(isAuthorCheckBox.isSelected()) {
            authorNameField.setEditable(false);
            authorNameField.setVisible(false);

            authorUrlField.setEditable(false);
            authorUrlField.setVisible(false);

        } else {
            authorNameField.setEditable(true);
            authorNameField.setVisible(true);

            authorUrlField.setEditable(true);
            authorUrlField.setVisible(true);
        }
    }

    @FXML
    private void addImageAction() {
        String pathToImage = imageEditor.selectImage();
        setImageToTextArea(pathToImage);
    }

    private void setImageToTextArea(String pathToImage) {
        if(pathToImage != null) {
            String currentText = imageTextArea.getText();
            if(currentText.length() > 0) {
                currentText += "\n" + pathToImage;
                imageTextArea.setText(currentText);
            } else {
                imageTextArea.setText(pathToImage);
            }
        }
    }

    @FXML
    private void contentPreviewAction() {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader();

        Parent root = null;
        try {
            root = loader.load(getClass().getClassLoader().getResourceAsStream("gui/content_preview.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentPreviewCtrl contentPreviewCtrl = loader.getController();

        contentPreviewCtrl.setContent(contentTextArea.getText());
        contentPreviewCtrl.setTitle(titleField.getText());

        if(!isAuthorCheckBox.isSelected())
            contentPreviewCtrl.setAuthor(authorNameField.getText(), authorUrlField.getText());

        contentPreviewCtrl.buildPreview();

        stage.setTitle("Предпросмотр статьи");

        Scene scene = new Scene(root, 500, 600);
        stage.setMinWidth(500);
        stage.setMinHeight(600);

        scene.getStylesheets().add("gui/style.css");

        stage.setScene(scene);
        stage.showAndWait();
    }
}
