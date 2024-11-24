package com.example.gametrashcollecting.client.controller;

import com.example.gametrashcollecting.client.model.Client;
import com.example.gametrashcollecting.model.User;
import com.example.gametrashcollecting.server.request.Request;
import com.example.gametrashcollecting.server.request.RequestStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class UserController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, Integer> totalPointsColumn;

    @FXML
    private TableColumn<User, String> statusColumn;

    @FXML
    private TableColumn<User, String> lastLoginColumn;

    @FXML
    private TableColumn<User, Integer> currentRoomIdColumn;

    private Client client;

    private User thisUser;

    private final ObservableList<User> userData = FXCollections.observableArrayList();

    public void setClient(Client client) {
        this.client = client;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    public void setUserData(List<User> userData) {
        this.userData.clear();
        this.userData.addAll(userData);
    }

    @FXML
    void clickOnToBackBtn(ActionEvent event) throws IOException {
        Request request = new Request(RequestStatus.BACK_MAIN_SCREEN, thisUser);
        client.sendToServer(request);
    }

    @FXML
    public void initialize() {
        // Cấu hình cột với thuộc tính trong lớp User
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        totalPointsColumn.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        lastLoginColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
        currentRoomIdColumn.setCellValueFactory(new PropertyValueFactory<>("currentRoomId"));

        // Thiết lập ObservableList cho bảng
        FilteredList<User> filteredData = new FilteredList<>(userData, p -> true);
        SortedList<User> sortedData = new SortedList<>(filteredData);

        // Liên kết dữ liệu đã sắp xếp với TableView
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());
        userTable.setItems(sortedData);

        // Đặt thứ tự mặc định là cột tổng điểm giảm dần
        totalPointsColumn.setSortType(TableColumn.SortType.DESCENDING);
        userTable.getSortOrder().add(totalPointsColumn);
    }
}
