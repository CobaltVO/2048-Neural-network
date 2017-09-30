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

    private int score = 0;

    @Override
    public void start(Stage stage) throws Exception {
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

        Label topLabel = new Label("Score: " + score);
        topLabel.setStyle("-fx-font: bold italic 20pt \"Times New Roman\";");
        topLabel.setAlignment(Pos.CENTER);
        Button neuralButton = new Button("Launch neural network");
        Button restartButton = new Button("Restart");


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
                    gl.move(Direction.UP);
                    break;
                case DOWN:
                    gl.move(Direction.DOWN);
                    break;
                case LEFT:
                    gl.move(Direction.LEFT);
                    break;
                case RIGHT:
                    gl.move(Direction.RIGHT);
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

        drawGrid();
        redraw(new GameData());//TODO Переделать

        return canvas;
    }

    private void drawTiles(GameData gd) {
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                if (gd.tiles[x][y] == 0) continue;
                gc.setFill(Color.YELLOW);
                gc.fillRect(x * 100 + 11, y * 100 + 11, 98, 98);
                gc.setFill(Color.BLACK);
                String tmp = String.valueOf((int) Math.pow(2, gd.tiles[x][y]));
                gc.fillText(tmp, x * 100 + 11 + 50, y * 100 + 11 + 47);//TODO подумать
            }
        }
    }

    private void drawGrid() {//TODO Переделать
        gc.setFill(Color.BLACK);
        // borders
        gc.strokeLine(10, 10, 410, 10);
        gc.strokeLine(10, 10, 10, 410);
        gc.strokeLine(410, 10, 410, 410);
        gc.strokeLine(10, 410, 410, 410);
        // rows
        gc.strokeLine(10, 110, 410, 110);
        gc.strokeLine(10, 210, 410, 210);
        gc.strokeLine(10, 310, 410, 310);
        // columns
        gc.strokeLine(110, 10, 110, 410);
        gc.strokeLine(210, 10, 210, 410);
        gc.strokeLine(310, 10, 310, 410);
    }
}
