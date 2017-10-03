package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

/**
 * Created by admin on 03.10.2017.
 */
public class NeuralNetworkPlayer implements Player {
    private GeneticNeuralNetwork nn;

    public NeuralNetworkPlayer(GeneticNeuralNetwork nn) {
        this.nn = nn;
    }

    @Override
    public void playOneGame(GameLogic gameLogic) {
        while (gameLogic.state == GameState.GAME) {
            //Утсановка входных значений нейронов
            for (int i = 0; i < 16; ++i) {
                nn.putSignalToNeuron(i, gameLogic.theGrid[i / 4][i % 4]);
            }

            //Активация
            nn.activate();

            //Вычисление хода
            //TODO попробовать упростить
            int i = nn.getAfterActivationSignal(nn.getNeuronsCount() - 1) > 0.5 ? 0b01 : 0b00;
            i |= nn.getAfterActivationSignal(nn.getNeuronsCount() - 2) > 0.5 ? 0b10 : 0b00;

            //Корректировка хода
            while (!gameLogic.move(Directions.values()[i])) {
                ++i;
                i &= 3;
            }
        }
    }

    public void setNeuralNetwork(GeneticNeuralNetwork nn) {
        this.nn = nn;
    }
}
