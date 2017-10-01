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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Window extends Application implements Screen {
    private Stage primaryStage;
    private GraphicsContext context;
    private GameLogic gameLogic;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        createMainWindow();
        gameLogic = new GameLogic(this);
    }

    @Override
    public void redraw(GameData gameData) {
        drawTiles(gameData);
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
            gameLogic.restart();
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
        clearScreen();
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
                        context.setFill(Color.rgb(255, 0, 127, 0.7));
                        break;
                    case 16:
                        context.setFill(Color.rgb(127, 0, 255, 0.7));
                        break;
                    default:
                        context.setFill(Color.rgb(0, 0, 255, 0.7));
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
