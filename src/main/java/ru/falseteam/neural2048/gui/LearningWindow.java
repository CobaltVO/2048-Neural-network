package ru.falseteam.neural2048.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LearningWindow extends Application {

    private Learning learning;
    private Stage primaryStage;
    private TextArea textArea; // окно для вывода информации

    private final OutputStream console = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            Platform.runLater(() -> textArea.appendText(String.valueOf((char) b)));
        }
    };

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        setStage();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        learning = new Learning(this);

        textArea = new TextArea();

        MenuBar menuBar = new MenuBar(); // верхнее меню
        Menu fileContainer = createFileContainer(); // создать контейнер (с кнопками) верхнего меню
        menuBar.getMenus().setAll(fileContainer);

        System.setOut(new PrintStream(console, true));
        root.setTop(menuBar);
        root.setCenter(textArea);
        primaryStage.show();
    }

    private void setStage() {
        primaryStage.setTitle("Learning neural network");
        primaryStage.setWidth(700);
        primaryStage.setHeight(700);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }

    private Menu createFileContainer() {

        Menu menu = new Menu("File");

        MenuItem menuItemNew = new MenuItem("New population");
        MenuItem menuItemSave = new MenuItem("Save as nn");
        MenuItem menuItemLoad = new MenuItem("Load from nn");
        // New listener
        menuItemNew.setOnAction(event -> learning.createPopulation());
        // Save listener
        menuItemSave.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file to ...");
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) learning.saveNn(file);
        });
        // Load listener
        menuItemLoad.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open file from ...");
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) learning.loadFromNm(file);
        });
        menu.getItems().addAll(menuItemNew, menuItemSave, menuItemLoad);


        MenuItem playItem = new MenuItem("Play");
        MenuItem pauseItem = new MenuItem("Pause");
        MenuItem savePopItem = new MenuItem("Save population");
        MenuItem loadPopItem = new MenuItem("Load population");

        playItem.setOnAction(event -> learning.play());
        pauseItem.setOnAction(event -> learning.pause());
        // saveNn population listener
        savePopItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save population to ...");
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) learning.savePopulation(file);
        });
        // loadFromNm population listener
        loadPopItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load population from ...");
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) learning.loadPopulation(file);
        });
        menu.getItems().addAll(playItem, pauseItem, savePopItem, loadPopItem);

        return menu;
    }
}
