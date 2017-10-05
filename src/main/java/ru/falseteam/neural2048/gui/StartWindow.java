package ru.falseteam.neural2048.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartWindow extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mode selection");
        primaryStage.setWidth(300);
        primaryStage.setHeight(200);
        primaryStage.setResizable(false);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        Label info = new Label("Please select mode: ");
        info.setAlignment(Pos.CENTER);

        Button buttonGUI = new Button("GUI mode");
        Button buttonTrain = new Button("Train mode");

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,10,10, 10));
        vBox.getChildren().addAll(info, buttonGUI, buttonTrain);
        vBox.setAlignment(Pos.CENTER);
        root.setCenter(vBox);

        buttonGUI.setOnAction(event -> {
            Window window = new Window();
            window.start(new Stage());
        });

        buttonTrain.setOnAction(event -> {
            LearningWindow learningWindow = new LearningWindow();
            try {
                learningWindow.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
