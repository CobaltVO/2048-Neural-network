package ru.falseteam.neural2048.nn;

import com.lagodiuk.nn.ThresholdFunction;
import com.lagodiuk.nn.genetic.OptimizableNeuralNetwork;

import java.util.Random;


public class NetworkCreator {
    private static final Random rnd = new Random();

    public static OptimizableNeuralNetwork initNeuralNetwork(int... config) {
        int countOfNeuron = 0;
        for (int i : config) {
            countOfNeuron += i;
        }
        OptimizableNeuralNetwork nn = new OptimizableNeuralNetwork(countOfNeuron);

        for (int i = 0; i < countOfNeuron; i++) {
            ThresholdFunction f = ThresholdFunction.getRandomFunction();
            nn.setNeuronFunction(i, f, f.getDefaultParams());
        }


        int offset = config[0];
        for (int i = 1; i < config.length; i++) {
            for (int j = 0; j < config[i - 1]; j++) {
                for (int k = 0; k < config[i]; k++) {
                    nn.addLink(offset-1-j, offset + k, getRandomWeight());
                }
            }
            offset+=config[i];
        }
        return nn;
    }

    private static int getRandomWeight() {
        final int maxWeightNum = 10;
        return rnd.nextInt(maxWeightNum) - rnd.nextInt(maxWeightNum);
    }
}
