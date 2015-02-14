package com.jaitlapps.besteditor.gui.list;

import com.jaitlapps.besteditor.AlertInfo;
import com.jaitlapps.besteditor.domain.Entry;
import com.jaitlapps.besteditor.manager.EntryManager;
import com.jaitlapps.besteditor.saver.EntrySaver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class ListCtrl<T extends Entry> {

    protected static Logger log = LoggerFactory.getLogger(ListCtrl.class);

    @FXML
    protected ListView<T> listView;

    protected ObservableList<T> data;

    protected EntryManager<T> entryManager;

    protected EntrySaver entrySaver;

    protected void setManager(EntryManager<T> entryManager) {
        this.entryManager = entryManager;
    }

    public void setSaver(EntrySaver entrySaver) {
        this.entrySaver = entrySaver;
    }

    public void loadList() {
        data = FXCollections.observableArrayList(entryManager.getList());

        listView.setItems(data);
    }

    protected void updateList() {
        data.clear();
        data.addAll(entryManager.getList());
        log.info("update list");
    }

    @FXML
    protected void closeDialog(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    protected void editDouble(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            edit();
        }
    }

    @FXML
    protected void edit() {
        T item = listView.getFocusModel().getFocusedItem();

        if(item != null) {
            openEditor(item, true);
        } else {
            AlertInfo.showAlert("Запись для редактирования не выбрана", "Запись для редактирования не выбрана. Выберите запись для редактирования.");
        }
    }

    @FXML
    protected void delete() {
        T item = listView.getFocusModel().getFocusedItem();

        if(item != null) {
            try {
                entrySaver.delete(item);
            } catch (Exception e) {
                log.error("delete error: " + e.getMessage());
                AlertInfo.showAlert("Ошибка при удалении", e.getMessage());
            }
            updateList();
        } else {
            AlertInfo.showAlert("Запись для удаления не выбрана", "Запись для удаления не выбрана. Выберите запись для удаления.");
        }
    }

    @FXML
    protected void add() {
        openEditor(null, false);
    }

    protected abstract void openEditor(Entry entry, boolean openForEdit);

}
