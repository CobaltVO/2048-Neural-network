package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

public class AdvancedCirclePlayer implements Player {
    @Override
    public void playOneGame(GameLogic gameLogic) {
        while (gameLogic.state == GameState.GAME) {
            int i = 0;
            while (!gameLogic.move(Directions.values()[i])) ++i;
        }
    }
}
