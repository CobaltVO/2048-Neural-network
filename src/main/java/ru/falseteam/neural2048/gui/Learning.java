package ru.falseteam.neural2048.gui;

import ru.falseteam.neural2048.NeuralNetworkTrainer;
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.nn.NeuralNetworkManager;
import ru.falseteam.neural2048.nn.ThresholdFunction;

import java.io.File;

class Learning {
    // НАСТРОЙКИ
    private static final int[] NETWORK_CONFIG = {16, 10, 6, 4};
    private static final ThresholdFunction[] thresholdFunctions = {
            ThresholdFunction.SIGMA,
            //ThresholdFunction.LINEAR,
    };
    // КОНЕЦ НАСТРОЕК

    private final LearningWindow window;
    private NeuralNetworkTrainer trainer;
    private final GameLogic gameLogic = new GameLogic(null);

    Learning(LearningWindow window) {
        this.window = window;
    }

    void saveNn(File file) {

    }

    void loadFromNm(File file) {

    }

    void savePopulation(File file) {

    }

    void loadPopulation(File file) {

    }

    void createPopulation() {
        new Thread(() -> {
            System.out.println("Creating population...");
            GeneticNeuralNetwork nn = new GeneticNeuralNetwork(
                    NeuralNetworkManager.createNeuralNetwork(thresholdFunctions, NETWORK_CONFIG));
            trainer = new NeuralNetworkTrainer(gameLogic, nn);
            System.out.println("Population created");
        }).start();
    }

    void play() {
        if (trainer != null) trainer.strart();
    }

    void pause() {
        if (trainer != null) trainer.stop();//TODO Пофиксить баг с паузой
    }
}
