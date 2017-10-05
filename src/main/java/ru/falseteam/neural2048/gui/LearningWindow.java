package ru.falseteam.neural2048.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class LearningWindow extends Application {

    Learning learning;
    Stage primaryStage;
    TextArea console; // окно для вывода информации

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        setStage();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        learning = new Learning();

        console = new TextArea();
        writeInConsole();

        MenuBar menuBar = new MenuBar(); // верхнее меню
        Menu fileContainer = createFileContainer(); // создать контейнер (с кнопками) верхнего меню
        Menu NNContainer = createNNContainer();
        MenuItem mn = new MenuItem("hee");
        menuBar.getMenus().setAll(fileContainer, NNContainer);

        root.setTop(menuBar);
        root.setCenter(console);
        primaryStage.show();
    }

    private void writeInConsole() {
        console.setText(learning.toPrintOnConsole());
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

        MenuItem menuItemNew = new MenuItem("New");
        MenuItem menuItemSave = new MenuItem("Save");
        MenuItem menuItemLoad = new MenuItem("Load");
        // New listener
        menuItemNew.setOnAction(event -> learning.create());
        // Save listener
        menuItemSave.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file to ...");
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) learning.save(file);
        });
        // Load listener
        menuItemLoad.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open file from ...");
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) learning.load(file);
        });

        menu.getItems().addAll(menuItemNew, menuItemSave, menuItemLoad);
        return menu;
    }

    private Menu createNNContainer() {

        Menu menu = new Menu("Neural network");

        MenuItem playItem = new MenuItem("Play");
        MenuItem pauseItem = new MenuItem("Pause");
        MenuItem savePopItem = new MenuItem("Save population");
        MenuItem loadPopItem = new MenuItem("Load population");

        playItem.setOnAction(event -> {

        });

        pauseItem.setOnAction(event -> {

        });
        // save population listener
        savePopItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save population to ...");
            fileChooser.setInitialDirectory(new File("."));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) learning.savePopulation(file);
        });
        // load population listener
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
