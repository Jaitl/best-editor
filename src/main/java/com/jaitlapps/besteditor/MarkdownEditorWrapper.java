package com.jaitlapps.besteditor;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class MarkdownEditorWrapper extends Thread {

    private static CommonPreferences preferences = CommonPreferences.getInstance();
    private static Logger log = LoggerFactory.getLogger(MarkdownEditorWrapper.class);

    private Callback callbackFunction;

    private String markdownFile;

    public void setMarkdownFile(String markdownFile) {
        this.markdownFile = markdownFile;
    }

    public void setCallbackFunction(Callback callbackFunction) {
        this.callbackFunction = callbackFunction;
    }

    public static void selectPathToMarkdownPad(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор пути до Markdown Editor");


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*.exe", "*.exe")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);

        log.info("selected Markdown Editor exe: " + selectedFile);

        if(selectedFile != null) {
            preferences.putMarkdownEditor(selectedFile.getPath());
        }
    }

    public void run() {
        String pathToMarkdownPad = preferences.getMarkdownEditor();
        String pathToFile = preferences.getWorkFolder() + "/" + markdownFile;

        ProcessBuilder pb = new ProcessBuilder(pathToMarkdownPad, pathToFile);
        Process process = null;

        try {
            process = pb.start();
            process.waitFor();
            callbackFunction.callback();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
        void callback();
    }
}
