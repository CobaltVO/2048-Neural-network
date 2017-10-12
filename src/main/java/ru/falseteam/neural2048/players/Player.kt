package ru.falseteam.neural2048.players

import ru.falseteam.neural2048.logic.GameLogic

/**
 * Интерфейс, от которого должен наследоваться любой алгоритм игры.
 *
 * @author Vladislav Sumin
 * @version 1.0
 */
abstract class Player(val gameLogic: GameLogic) {
    /**
     * Игрок играет одну игру.
     */
    abstract fun playOneGame()
}
