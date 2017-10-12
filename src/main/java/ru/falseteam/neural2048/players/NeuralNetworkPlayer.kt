package ru.falseteam.neural2048.players

import ru.falseteam.neural2048.logic.Directions
import ru.falseteam.neural2048.logic.GameLogic
import ru.falseteam.neural2048.logic.GameState
import ru.falseteam.neural2048.nn.NeuralNetwork

import java.util.*

/**
 * Игрок, играющий с помощью нейронной сети.
 * Сеть должна иметь 16 входных и 4 выходных нейрона.
 *
 * @author Vladislav Sumin
 * @version 2.0
 */
class NeuralNetworkPlayer(private var nn: NeuralNetwork?, gameLogic: GameLogic) : Player(gameLogic) {

    private val pairUp = Pair(Directions.UP)
    private val pairDown = Pair(Directions.DOWN)
    private val pairLeft = Pair(Directions.LEFT)
    private val pairRight = Pair(Directions.RIGHT)
    private val pairs = arrayOf(pairUp, pairDown, pairLeft, pairRight)

    override fun playOneGame() {
        val nn = nn ?: throw IllegalStateException()
        while (gameLogic.state == GameState.GAME) {
            val maxTile: Double = gameLogic.maxTileExp.toDouble()
            // Установка входных значений нейронов
            for (i in 0..15) {
                nn.putSignalToNeuron(i, gameLogic.theGrid[i / 4][i % 4].toDouble() / maxTile)
            }

            // Активация
            nn.activate()

            // Снятие значений с выходных нейронов
            pairUp.value = nn.getAfterActivationSignal(nn.neuronsCount - 1)
            pairDown.value = nn.getAfterActivationSignal(nn.neuronsCount - 2)
            pairLeft.value = nn.getAfterActivationSignal(nn.neuronsCount - 3)
            pairRight.value = nn.getAfterActivationSignal(nn.neuronsCount - 4)

            // Обработка хода
            Arrays.sort(pairs)
            @Suppress("LoopToCallChain")
            for (move in pairs) {
                if (gameLogic.move(move.direction)) break
            }
        }
    }

    fun setNeuralNetwork(nn: NeuralNetwork) {
        this.nn = nn
    }

    /**
     * Вспомогательный класс для корректной обработки приоретета ходов.
     */
    private class Pair(val direction: Directions) : Comparable<Pair> {
        var value: Double? = null

        override fun compareTo(other: Pair): Int {
            //Сортировка по убыванию
            return java.lang.Double.compare(other.value!!, value!!)
        }
    }
}
