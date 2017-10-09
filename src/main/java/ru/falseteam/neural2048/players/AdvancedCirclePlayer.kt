package ru.falseteam.neural2048.players

import ru.falseteam.neural2048.logic.Directions
import ru.falseteam.neural2048.logic.GameLogic
import ru.falseteam.neural2048.logic.GameState

class AdvancedCirclePlayer(gameLogic: GameLogic) : Player(gameLogic) {
    override fun playOneGame() {
        while (gameLogic.state == GameState.GAME) {
            var i = 0
            while (!gameLogic.move(Directions.values()[i])) ++i
        }
    }
}
