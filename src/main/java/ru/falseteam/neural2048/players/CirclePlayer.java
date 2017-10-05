package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

import java.util.Random;

public class CirclePlayer implements Player {
    @Override
    public void playOneGame(GameLogic gameLogic) {
        while (true) {
            for (Directions directions : Directions.values()) {
                if (gameLogic.state != GameState.GAME) return;
                gameLogic.move(directions);
            }
        }
    }
}
