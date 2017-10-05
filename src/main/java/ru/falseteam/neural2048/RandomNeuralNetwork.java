package ru.falseteam.neural2048;

import ru.falseteam.neural2048.nn.ThresholdFunction;
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.nn.NetworkCreator;
import ru.falseteam.neural2048.players.NeuralNetworkPlayer;

public class RandomNeuralNetwork{
    // НАСТРОЙКИ
    private static final int[] NETWORK_CONFIG = {16, 10, 6, 4};
    private static final ThresholdFunction[] thresholdFunctions = {
            ThresholdFunction.SIGMA,
            //ThresholdFunction.LINEAR,
    };
    // КОНЕЦ НАСТРОЕК


    public RandomNeuralNetwork(GameLogic gameLogic) {
        //Создаем случайную нейронную сеть с задаными параметрами
        GeneticNeuralNetwork nn =
                NetworkCreator.initNeuralNetwork(thresholdFunctions, NETWORK_CONFIG);

        new NetworkTrainer(gameLogic,nn);
    }

}
