package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.CommonPreferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.pegdown.PegDownProcessor;

import java.nio.file.Path;

public class ContentPreviewCtrl {

    protected static CommonPreferences preferences = CommonPreferences.getInstance();

    @FXML
    private WebView previewWebView;

    @FXML
    private void closeDialogAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public void setContent(String content) {

        String pathToImageFolder = "](file:///" + preferences.getWorkFolder() + "/content/images";
        String contentWithImage = content.replace("](/images", pathToImageFolder).replace("\\", "/");
        System.out.println(contentWithImage);

        PegDownProcessor pegDownProcessor = new PegDownProcessor();
        String html = pegDownProcessor.markdownToHtml(contentWithImage);



        WebEngine webEngine = previewWebView.getEngine();
        webEngine.loadContent(html);
    }
}
