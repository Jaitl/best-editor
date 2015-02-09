package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.CommonPreferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.pegdown.PegDownProcessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ContentPreviewCtrl {

    protected static CommonPreferences preferences = CommonPreferences.getInstance();

    @FXML
    private WebView previewWebView;

    @FXML
    private void closeDialogAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    private String content;
    private String authorName;
    private String authorUrl;
    private String title;

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String authorName, String authorUrl) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void buildPreview() {
        String pathToImageFolder = "](file:///" + preferences.getWorkFolder() + "/content/images";
        String contentWithImage = null;

        if(content != null && content.length() > 0)
            contentWithImage = content.replace("](/images", pathToImageFolder).replace("\\", "/");

        String titleH1 = null;

        if(title != null && title.length() > 0) {
            titleH1 = "#" + title;
        }

        String authorLink = null;

        if(authorName != null && authorName.length() > 0 &&  authorUrl != null && authorUrl.length() > 0) {
            authorLink = "**Автор:** [" + authorName + "](" + authorUrl + ")";
        }

        String resultMarkDown = "";

        if(titleH1 != null)
            resultMarkDown += titleH1 + "\n\n";

        if(authorLink != null)
            resultMarkDown += authorLink + "\n\n";

        if(contentWithImage != null)
            resultMarkDown += contentWithImage;

        PegDownProcessor pegDownProcessor = new PegDownProcessor();
        String html = pegDownProcessor.markdownToHtml(resultMarkDown);

        WebEngine webEngine = previewWebView.getEngine();

        Path pathToCSS = Paths.get(preferences.getWorkFolder(), "content", "css", "common_style.css");

        if(Files.notExists(pathToCSS))
            copyCSSFromResources();

        webEngine.setUserStyleSheetLocation("file:///" + pathToCSS);
        webEngine.loadContent(html);
    }

    private void copyCSSFromResources() {
        Path pathToCSSFolder = Paths.get(preferences.getWorkFolder(), "content", "css");

        if(Files.notExists(pathToCSSFolder)) {
            try {
                Files.createDirectory(pathToCSSFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("css/common_style.css");

        byte[] buffer = new byte[0];

        try {
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.write(pathToCSSFolder.resolve("common_style.css"), buffer, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
