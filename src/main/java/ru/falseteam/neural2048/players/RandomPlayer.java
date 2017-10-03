package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

import java.util.Random;

/**
 * Игрок делает случайные ходы
 */
public class RandomPlayer implements Player {
    private final Random random = new Random();

    @Override
    public void playOneGame(GameLogic gameLogic) {
        while (gameLogic.state == GameState.GAME) {
            gameLogic.move(Directions.values()[random.nextInt(4)]);
        }
    }
}
