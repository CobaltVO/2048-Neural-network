package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

import java.util.Random;

public class RandomPlayer {
    private static final Random random = new Random();

    public RandomPlayer(GameLogic gameLogic) {
        new Thread(() -> {
            while (gameLogic.state == GameState.GAME) {
                gameLogic.move(Directions.values()[random.nextInt(3)]);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
