package ru.falseteam.neural2048.players

import ru.falseteam.neural2048.logic.Directions
import ru.falseteam.neural2048.logic.GameLogic
import ru.falseteam.neural2048.logic.GameState

import java.util.Random

/**
 * Игрок делает случайные ходы
 */
class RandomPlayer(gameLogic: GameLogic) : Player(gameLogic) {
    private val random = Random()

    override fun playOneGame() {
        while (gameLogic.state == GameState.GAME) {
            gameLogic.move(Directions.values()[random.nextInt(4)])
        }
    }
}
