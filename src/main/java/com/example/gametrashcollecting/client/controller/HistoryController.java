package com.example.gametrashcollecting.client.controller;

import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.client.model.HistoryRecord;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class HistoryController {

    @FXML
    private TableColumn<HistoryRecord, String> sessionIdColumn;
    @FXML
    private TableColumn<HistoryRecord, String> meColumn;
    @FXML
    private TableColumn<HistoryRecord, String> opponentColumn;

    @FXML
    private TableColumn<HistoryRecord, String> roundNameColumn;
    @FXML
    private TableColumn<HistoryRecord, String> timeStartColumn;
    @FXML
    private TableColumn<HistoryRecord, String> timeEndColumn;
    @FXML
    private TableColumn<HistoryRecord, String> resultColumn;

    @FXML
    private TableView<HistoryRecord> historyTable;
    @FXML
    private Label resultSessionHeader;

    @FXML
    private ComboBox<String> filterComboBox;

    private Client client;

    private User thisUser;


    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private Button backBtn;

    @FXML
    void clickOnToBackBtn(ActionEvent event) throws IOException {
        Request request = new Request(RequestStatus.BACK_MAIN_SCREEN, thisUser);
        client.sendToServer(request);
    }

    private final ObservableList<HistoryRecord> historyData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Thêm dữ liệu từng cột
        sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        meColumn.setCellValueFactory(new PropertyValueFactory<>("me"));
        opponentColumn.setCellValueFactory(new PropertyValueFactory<>("opponent"));
        roundNameColumn.setCellValueFactory(new PropertyValueFactory<>("roundName"));
        timeStartColumn.setCellValueFactory(new PropertyValueFactory<>("timeStart"));
        timeEndColumn.setCellValueFactory(new PropertyValueFactory<>("timeEnd"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

//        historyData.addAll(
//                new HistoryRecord("001", 1200, 2042, "Round 1", "2024-10-03 10:15:00", "2024-10-03 10:45:00", "Win"),
//                new HistoryRecord("002", 1203, 2045, "Round 2", "2024-10-24 22:50:06", "2024-10-24 22:50:06", "Lose"),
//                new HistoryRecord("003", 1294, 2495, "Round 3", "2024-10-24 22:50:06", "2024-10-24 22:56:33", "Draw")
//        );

        // Tạo FilteredList để áp dụng bộ lọc
        FilteredList<HistoryRecord> filteredData = new FilteredList<>(historyData, p -> true);



        // Sắp xếp danh sách theo thời gian bắt đầu mới nhất
        SortedList<HistoryRecord> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(historyTable.comparatorProperty());

        historyTable.setItems(sortedData);
//        historyTable.setItems(filteredData);

        filterComboBox.setItems(FXCollections.observableArrayList("All", "Win", "Lose", "Draw"));
        filterComboBox.setValue("All");
        filterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(record -> {
                if ("All".equals(newValue)) {
                    return true;
                }
                return record.getResult().equalsIgnoreCase(newValue);
            });
        });

        historyTable.getSortOrder().add(timeStartColumn);
        timeStartColumn.setSortType(TableColumn.SortType.DESCENDING);
    }

    public void setHistoryData(List<HistoryRecord> historyData) {
        this.historyData.clear();
        this.historyData.addAll(historyData);
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }
}





