package ru.falseteam.neural2048.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ru.falseteam.neural2048.players.RandomNeuralNetwork;
import ru.falseteam.neural2048.players.RandomPlayer;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameData;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Window extends Application implements Screen {
    private Stage primaryStage;
    private GraphicsContext context;
    private GameLogic gameLogic;

    private Label topLabel;
    private Label scoreLabel;
    private Label maxTileLabel;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("2048 Game with a neural network");
        primaryStage.centerOnScreen();
        //primaryStage.setMaximized(true);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(700);
        createMainWindow();
        gameLogic = new GameLogic(this);
        //new RandomPlayer(gameLogic);
        //new CirclePlayer(gameLogic);
        new RandomNeuralNetwork(gameLogic);
    }

    @Override
    public void redraw(GameData gameData) {
        Platform.runLater(() -> {
            clearScreen();
            drawTiles(gameData);
            topLabel.setText((gameData.state.equals(GameState.WIN) ? "YOU WON!!! " :
                gameData.state.equals(GameState.END) ? "GAME OVER. " : ""));
            scoreLabel.setText("Score: " + gameData.score);
            maxTileLabel.setText("The highest tile: " + (int)(Math.pow(2, gameData.maxTileExp)));
        });
    }

    private void createMainWindow() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 750, 750);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();

        createLabels();
        VBox labelBox = new VBox(10);
        labelBox.getChildren().addAll(scoreLabel, maxTileLabel, topLabel);

        Button restartButton = new Button("Restart");
        restartButton.setPrefSize(100, 30);
        restartButton.setOnAction(event -> {
            clearScreen();
            gameLogic.score = 0;
            gameLogic.restart();
        });

        CheckBox checkBox = new CheckBox("Fullscreen");
        checkBox.setPrefSize(100, 30);
        checkBox.setOnAction(event -> {
            if (!primaryStage.isFullScreen()) primaryStage.setFullScreen(true);
            else primaryStage.setFullScreen(false);
        });

        Canvas playButton = createGUIButton("play");
        Canvas pauseButton = createGUIButton("pause");
        Canvas stopButton = createGUIButton("stop");
        ComboBox<String> comboBox = createComboBox();
        Label testLabel = new Label();
        testLabel.setPrefSize(100,30);
        HBox neuralBox = new HBox(10);
        neuralBox.getChildren().addAll(comboBox, playButton, pauseButton, stopButton, testLabel);

        playButton.setOnMouseClicked(event -> testLabel.setText("playing " + comboBox.getSelectionModel().getSelectedItem()));
        pauseButton.setOnMouseClicked(event -> testLabel.setText("pause " + comboBox.getSelectionModel().getSelectedItem()));
        stopButton.setOnMouseClicked(event -> testLabel.setText("stopped " + comboBox.getSelectionModel().getSelectedItem()));

        // top toolbar
        HBox topBox = new HBox(25);
        topBox.setPadding(new Insets(25, 10, 10, 10));
        topBox.getChildren().addAll(labelBox, neuralBox);
        topBox.setAlignment(Pos.CENTER);
        // bottom toolbar
        HBox bottomBox = new HBox(25);
        bottomBox.setPadding(new Insets(0, 10, 5, 10));
        bottomBox.getChildren().addAll(restartButton, checkBox);

        Canvas canvas = createGameWindow();
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    gameLogic.move(Directions.UP);
                    break;
                case DOWN:
                    gameLogic.move(Directions.DOWN);
                    break;
                case LEFT:
                    gameLogic.move(Directions.LEFT);
                    break;
                case RIGHT:
                    gameLogic.move(Directions.RIGHT);
                    break;
            }
        });
        root.setTop(topBox);
        root.setCenter(canvas);
        root.setBottom(bottomBox);
        primaryStage.show();
    }

    private void createLabels() {
        topLabel = new Label();
        topLabel.setStyle("-fx-font: bold italic 20pt \"Times New Roman\";");
        topLabel.setAlignment(Pos.CENTER);
        topLabel.setPrefSize(250, 30);
        scoreLabel = new Label();
        scoreLabel.setStyle("-fx-font: bold italic 20pt \"Times New Roman\";");
        scoreLabel.setPrefSize(250, 30);
        maxTileLabel = new Label();
        maxTileLabel.setStyle("-fx-font: bold italic 20pt \"Times New Roman\";");
        maxTileLabel.setPrefSize(250, 30);
    }

    private ComboBox<String> createComboBox() {
        ObservableList<String> observableList = FXCollections.observableArrayList(
                "lol",
                        "kek",
                        "cheburek"
        );
        final ComboBox<String> comboBox = new ComboBox<>(observableList);
        comboBox.setPromptText("Choose one state");
        comboBox.setPrefWidth(150);
        comboBox.setPrefHeight(30);
        return comboBox;
    }

    private Canvas createGUIButton(String which) {
        Canvas canvas = new Canvas(30, 30);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image play = null;
        try {
            switch (which) {
                case "play":
                    play = new Image(new FileInputStream("play.png"));
                    break;
                case "pause":
                    play = new Image(new FileInputStream("pause.png"));
                    break;
                case "stop":
                    play = new Image(new FileInputStream("stop.png"));
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println(which + ".png not found");
        }
        gc.drawImage(play, 0,0);
        return canvas;
    }

    private Canvas createGameWindow() {
        Canvas canvas = new Canvas(420, 420);
        context = canvas.getGraphicsContext2D();
        context.setFont(Font.font(35));
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);
        clearScreen();
        return canvas;
    }

    private void drawTiles(GameData gameData) {
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                switch (gameData.theGrid[x][y]) {
                    case 0:
                        continue;
                    case 1:
                        context.setFill(Color.rgb(255, 252, 0, 0.7));
                        break;
                    case 2:
                        context.setFill(Color.rgb(255, 234, 0, 0.7));
                        break;
                    case 3:
                        context.setFill(Color.rgb(255, 216, 0, 0.7));
                        break;
                    case 4:
                        context.setFill(Color.rgb(255, 198, 0, 0.7));
                        break;
                    case 5:
                        context.setFill(Color.rgb(255, 180, 0, 0.7));
                        break;
                    case 6:
                        context.setFill(Color.rgb(255, 162, 0, 0.7));
                        break;
                    case 7:
                        context.setFill(Color.rgb(255, 144, 0, 0.7));
                        break;
                    case 8:
                        context.setFill(Color.rgb(255, 126, 0, 0.7));
                        break;
                    case 9:
                        context.setFill(Color.rgb(255, 108, 0, 0.7));
                        break;
                    case 10:
                        context.setFill(Color.rgb(255, 90, 0, 0.7));
                        break;
                    case 11:
                        context.setFill(Color.rgb(255, 72, 0, 0.7));
                        break;
                    case 12:
                        context.setFill(Color.rgb(255, 54, 0, 0.7));
                        break;
                    case 13:
                        context.setFill(Color.rgb(255, 36, 0, 0.7));
                        break;
                    case 14:
                        context.setFill(Color.rgb(255, 18, 0, 0.7));
                        break;
                    case 15:
                        context.setFill(Color.rgb(127, 0, 127, 0.7));
                        break;
                    case 16:
                        context.setFill(Color.rgb(63, 0, 191, 0.7));
                        break;
                    case 17:
                        context.setFill(Color.rgb(0, 127, 255, 0.7));
                        break;
                    default:
                        context.setFill(Color.rgb(0, 255, 127, 0.7));
                }
                context.fillRect(x * 100 + 11, y * 100 + 11, 98, 98);
                context.setFill(Color.BLACK);
                // String tmp = String.valueOf((int) Math.pow(2, gameData.theGrid[x][y]));
                // эффективнее так:
                String tmp = String.valueOf(1 << gameData.theGrid[x][y]);
                context.fillText(tmp, x * 100 + 11 + 49, y * 100 + 11 + 49); // to center
            }
        }
    }

    private void drawGrid() {
        context.setFill(Color.BLACK);
        // borders
        context.strokeLine(10, 10, 410, 10);
        context.strokeLine(10, 10, 10, 410);
        context.strokeLine(410, 10, 410, 410);
        context.strokeLine(10, 410, 410, 410);
        // rows
        for (int i = 0; i < 3; i++)
            context.strokeLine(10, (110 + i * 100), 410, (110 + i * 100));
        // columns
        for (int i = 0; i < 3; i++)
            context.strokeLine((110 + i * 100), 10, (110 + i * 100), 410);
    }

    private void clearScreen() {
        context.setFill(Color.LIGHTGRAY);
        context.fillRect(0, 0, 420, 420);
        drawGrid();
    }
}
