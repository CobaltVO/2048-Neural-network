package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

import java.util.Random;

public class CirclePlayer {

    public CirclePlayer(GameLogic gameLogic) {
        new Thread(() -> {
            while (gameLogic.state == GameState.GAME) {
                for (Directions directions : Directions.values()) {
                    gameLogic.move(directions);
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
