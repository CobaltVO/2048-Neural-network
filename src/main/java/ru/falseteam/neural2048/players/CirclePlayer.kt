package ru.falseteam.neural2048.players

import ru.falseteam.neural2048.logic.Directions
import ru.falseteam.neural2048.logic.GameLogic
import ru.falseteam.neural2048.logic.GameState

import java.util.Random

class CirclePlayer(gameLogic: GameLogic) : Player(gameLogic) {
    override fun playOneGame() {
        while (true) {
            for (directions in Directions.values()) {
                if (gameLogic.state != GameState.GAME) return
                gameLogic.move(directions)
            }
        }
    }
}
