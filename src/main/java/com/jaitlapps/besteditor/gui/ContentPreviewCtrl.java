package com.jaitlapps.besteditor.gui;

import com.jaitlapps.besteditor.CommonPreferences;
import com.jaitlapps.besteditor.ContentRender;
import com.jaitlapps.besteditor.domain.RecordEntry;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

public class ContentPreviewCtrl {

    private static Logger log = Logger.getLogger(MainMenuCtrl.class.getName());

    @FXML
    private WebView previewWebView;

    @FXML
    private void closeDialogAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    private RecordEntry recordEntry;

    private String content;

    public void setRecordEntry(RecordEntry recordEntry) {
        this.recordEntry = recordEntry;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void buildPreview() {
        openUrlInExternalBrowser();
        WebEngine webEngine = previewWebView.getEngine();

        ContentRender contentRender = new ContentRender();
        String html = contentRender.render(recordEntry, content);

        webEngine.loadContent(html);
    }

    private void openUrlInExternalBrowser() {
        previewWebView.getEngine().setCreatePopupHandler(
                config -> {
                    // grab the last hyperlink that has :hover pseudoclass
                    Object o = previewWebView
                            .getEngine()
                            .executeScript(
                                    "var list = document.querySelectorAll( ':hover' );"
                                            + "for (i=list.length-1; i>-1; i--) "
                                            + "{ if ( list.item(i).getAttribute('href') ) "
                                            + "{ list.item(i).getAttribute('href'); break; } }");

                    // open in native browser
                    try {
                        if (o != null) {
                            Desktop.getDesktop().browse(
                                    new URI(o.toString()));
                        } else {
                            //log.error("No result from uri detector: " + o);
                        }
                    } catch (IOException e) {
                        //log.error("Unexpected error obtaining uri: " + o, e);
                    } catch (URISyntaxException e) {
                        //log.error("Could not interpret uri: " + o, e);
                    }

                    // prevent from opening in webView
                    return null;
                });
    }
}
