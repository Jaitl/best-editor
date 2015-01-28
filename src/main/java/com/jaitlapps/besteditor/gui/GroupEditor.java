package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.GroupSaver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class GroupEditor extends Application {

    private final static int IMAGE_HEIGHT = 350;

    Stage primaryStage;

    @FXML
    ImageView imageView;

    @FXML
    TextField groupTitleField;

    File iconFile;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
    }

    @FXML
    private void selectImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Выбор иконки для группы");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File selectedImage = fileChooser.showOpenDialog(primaryStage);

        if(selectedImage != null) {
            setImage(selectedImage.getPath());

            iconFile = selectedImage;
        }
    }

    @FXML
    private void cancelDialog(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void saveGroup(ActionEvent event) {
        String groupTitle = groupTitleField.getText();

        if(groupTitle!= null && groupTitle.length() > 0) {
            if(iconFile != null) {
                GroupSaver groupSaver = new GroupSaver();
                groupSaver.saveGroup(groupTitle, iconFile);
            }
        }
    }

    /*private BufferedImage resizeIcon(BufferedImage icon) {
        return Scalr.resize(icon, IMAGE_HEIGHT);
    }*/

    private void setImage(String pathToIcon) {
        Image icon = new Image("file:///" + pathToIcon);
        imageView.setImage(icon);
    }

    /*public void setRecordIcon(ImageIcon customIcon) {
        Image img = customIcon.getImage() ;
        Image newImg = img.getScaledInstance( 100, 100,  java.awt.Image.SCALE_SMOOTH ) ;

        ImageIcon smallIcon = new ImageIcon(newImg);

        iconLabel.setText("");
        iconLabel.setIcon(smallIcon);
    }*/
}
