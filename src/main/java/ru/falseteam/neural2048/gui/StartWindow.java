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

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton selectGUI = new RadioButton("GUI");
        selectGUI.setToggleGroup(toggleGroup);
        selectGUI.setSelected(true);
        RadioButton selectTrain = new RadioButton("Train");
        selectTrain.setToggleGroup(toggleGroup);
        VBox buttonBox = new VBox(10);
        buttonBox.setPadding(new Insets(10,10,10, 10));
        buttonBox.getChildren().addAll(selectGUI, selectTrain);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10,10,10, 10));
        vBox.getChildren().addAll(info, buttonBox);
        vBox.setAlignment(Pos.CENTER);

        root.setCenter(vBox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
