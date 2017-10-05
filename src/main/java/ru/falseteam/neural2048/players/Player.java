package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.logic.GameLogic;

/**
 * Интерфейс от которого должен наследоваться любой алгоритм игры.
 *
 * @author Vladislav Sumin
 * @version 1.0
 */
public interface Player {
    /**
     * Игрок играет одну игру.
     */
    void playOneGame(GameLogic gameLogic);
}
