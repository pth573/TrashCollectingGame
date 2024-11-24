module com.example.gametrashcollecting {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires jdk.security.jgss;
    requires mysql.connector.j;
//
//    opens com.example.gametrashcollecting to javafx.fxml;
//    exports com.example.gametrashcollecting.client.controller;



    opens com.example.gametrashcollecting.client to javafx.fxml;
    opens com.example.gametrashcollecting.client.controller to javafx.fxml;
    exports com.example.gametrashcollecting.client;
    exports com.example.gametrashcollecting.client.controller;

    opens com.example.gametrashcollecting.server to javafx.fxml; // Thêm dòng này
    opens com.example.gametrashcollecting.server.handlers to javafx.fxml; // Thêm dòng này
    exports com.example.gametrashcollecting.server.handlers;
    exports com.example.gametrashcollecting.server;

    opens com.example.gametrashcollecting.client.model to javafx.base;

    opens com.example.gametrashcollecting.model to javafx.base;


//    requires javafx.graphics;
//    exports com.example.gametrashcollecting;


}

