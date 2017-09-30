package ru.falseteam.neural2048;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Window extends Application implements Screen {
    private Stage primaryStage;
    private GraphicsContext gc;

    private GameLogic gl;


    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        createMainWindow();
        gl = new GameLogic(this);
    }

    @Override
    public void redraw(GameData gd) {
        drawTiles(gd);
    }

    private void createMainWindow() {
        primaryStage.setTitle("2048 Game with neural network");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);

        Label topLabel = new Label("Score: " + 0);//TODO
        topLabel.setStyle("-fx-font: bold italic 20pt \"Times New Roman\";");
        topLabel.setAlignment(Pos.CENTER);
        Button neuralButton = new Button("Launch neural network");
//        neuralButton.setOnAction(event -> {});
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(event -> {
            clearScreen();
            gl.restart();
        });

        HBox topBox = new HBox();
        topBox.setPadding(new Insets(25, 10, 10, 10));
        topBox.getChildren().addAll(topLabel);
        topBox.setAlignment(Pos.CENTER);

        HBox bottomBox = new HBox(490);
        bottomBox.setPadding(new Insets(10, 10, 10, 10));
        bottomBox.getChildren().addAll(restartButton, neuralButton);
        Canvas canvas = createGameWindow();
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    gl.move(Directions.UP);
                    break;
                case DOWN:
                    gl.move(Directions.DOWN);
                    break;
                case LEFT:
                    gl.move(Directions.LEFT);
                    break;
                case RIGHT:
                    gl.move(Directions.RIGHT);
                    break;
            }
        });
        root.setTop(topBox);
        root.setCenter(canvas);
        root.setBottom(bottomBox);
        primaryStage.show();
    }

    private Canvas createGameWindow() {
        Canvas canvas = new Canvas(420, 420);
        gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font(35));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        clearScreen();
        return canvas;
    }

    private void drawTiles(GameData gd) {
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                switch (gd.tiles[x][y]) {
                    case 0:
                        continue;
                    case 1:
                        gc.setFill(Color.LIGHTYELLOW);
                        break;
                    case 2:
                    case 3:
                        gc.setFill(Color.YELLOWGREEN);
                        break;
                    case 4:
                    case 5:
                        gc.setFill(Color.YELLOW);
                        break;
                    case 6:
                    case 7:
                        gc.setFill(Color.ORANGE);
                        break;
                    case 8:
                    case 9:
                        gc.setFill(Color.RED);
                        break;
                    case 10:
                    case 11:
                        gc.setFill(Color.DARKRED);
                        break;
                    case 12:
                    case 13:
                        gc.setFill(Color.LIGHTBLUE);
                        break;
                    case 14:
                    case 15:
                        gc.setFill(Color.BLUEVIOLET);
                        break;
                    default:
                        gc.setFill(Color.DARKSLATEBLUE);
                        break;
                }
                gc.fillRect(x * 100 + 11, y * 100 + 11, 98, 98);
                gc.setFill(Color.BLACK);
                String tmp = String.valueOf((int) Math.pow(2, gd.tiles[x][y]));
                gc.fillText(tmp, x * 100 + 11 + 49, y * 100 + 11 + 49); // to center
            }
        }
    }

    private void drawGrid() {
        gc.setFill(Color.BLACK);
        // borders
        gc.strokeLine(10, 10, 410, 10);
        gc.strokeLine(10, 10, 10, 410);
        gc.strokeLine(410, 10, 410, 410);
        gc.strokeLine(10, 410, 410, 410);
        // rows
        for (int i = 0; i < 3; i++)
            gc.strokeLine(10, (110 + i * 100), 410, (110 + i * 100));
        // columns
        for (int i = 0; i < 3; i++)
            gc.strokeLine((110 + i * 100), 10, (110 + i * 100), 410);
    }

    private void clearScreen() {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, 420, 420);
        drawGrid();
    }
}
