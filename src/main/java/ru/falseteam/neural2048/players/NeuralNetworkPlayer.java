package ru.falseteam.neural2048.players;

import org.jetbrains.annotations.NotNull;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;
import ru.falseteam.neural2048.nn.NeuralNetwork;

import java.util.*;

/**
 * Игрок играющий с помощью нейронной сети.
 * Сеть должна иметь 16 входных нейронов и 4 выходных нейрона.
 *
 * @author Vladislav Sumin
 * @version 2.0
 */
public class NeuralNetworkPlayer implements Player {
    private NeuralNetwork nn;

    private final Pair pairUp = new Pair(Directions.UP);
    private final Pair pairDown = new Pair(Directions.DOWN);
    private final Pair pairLeft = new Pair(Directions.LEFT);
    private final Pair pairRight = new Pair(Directions.RIGHT);
    private final Pair[] pairs = {pairUp, pairDown, pairLeft, pairRight};


    public NeuralNetworkPlayer(NeuralNetwork nn) {
        this.nn = nn;
    }

    @Override
    public void playOneGame(GameLogic gameLogic) {
        while (gameLogic.state == GameState.GAME) {
            //Установка входных значений нейронов
            for (int i = 0; i < 16; ++i) {
                nn.putSignalToNeuron(i, gameLogic.theGrid[i / 4][i % 4]);
            }

            //Активация
            nn.activate();

            //Снятие значений с выходных нейронов
            pairUp.value = nn.getAfterActivationSignal(nn.getNeuronsCount() - 1);
            pairDown.value = nn.getAfterActivationSignal(nn.getNeuronsCount() - 2);
            pairLeft.value = nn.getAfterActivationSignal(nn.getNeuronsCount() - 3);
            pairRight.value = nn.getAfterActivationSignal(nn.getNeuronsCount() - 4);

            //Обработка хода
            Arrays.sort(pairs);
            for (Pair move : pairs) {
                if (gameLogic.move(move.direction)) break;
            }
        }
    }

    public void setNeuralNetwork(NeuralNetwork nn) {
        this.nn = nn;
    }

    /**
     * Вспомогательный класс для корректной обработки приоретета ходов.
     */
    private static class Pair implements Comparable<Pair> {
        final Directions direction;
        Double value;

        Pair(Directions direction) {
            this.direction = direction;
        }

        @Override
        public int compareTo(@NotNull Pair o) {
            //Сортировка по убыванию
            return Double.compare(o.value, value);
        }
    }
}
