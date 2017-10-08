package ru.falseteam.neural2048.gui;

import ru.falseteam.neural2048.NeuralNetworkTrainer;
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.nn.NeuralNetwork;
import ru.falseteam.neural2048.nn.NeuralNetworkManager;
import ru.falseteam.neural2048.nn.ThresholdFunction;

import javax.xml.bind.JAXBException;
import java.io.*;

class Learning {
    // НАСТРОЙКИ
    private static final int[] NETWORK_CONFIG = {16, 16, 8, 4};
    private static final ThresholdFunction[] thresholdFunctions = {
            ThresholdFunction.SIGMA,
            //ThresholdFunction.LINEAR,
    };
    // КОНЕЦ НАСТРОЕК

    private final LearningWindow window;
    private NeuralNetworkTrainer trainer;
    private final GameLogic gameLogic = new GameLogic(null);
    private volatile boolean started = false;

    Learning(LearningWindow window) {
        this.window = window;
    }

    void saveNn(File file) {
        if (started) {
            System.out.println("Can not save when evolve");
            return;
        }
        if (trainer == null) {
            System.out.println("Can not save, population not crated");
            return;
        }

        try {
            System.out.println("Saving...");
            NeuralNetworkManager.save(trainer.getBest(), new FileOutputStream(file));
            System.out.println("Saved");
        } catch (FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
    }

    void loadFromNn(File file) {
        if (started) {
            System.out.println("Yet works");
            return;
        }
        try {
            System.out.println("Loading...");
            trainer = new NeuralNetworkTrainer(gameLogic, new GeneticNeuralNetwork(
                    NeuralNetworkManager.load(new FileInputStream(file))));
            System.out.println("Loaded");
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
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
        if (started) return;
        if (trainer != null) {
            trainer.start();
            started = true;
            System.out.println("Training begin");
        } else
            System.out.println("Population not created");
    }

    void pause() {
        if (!started || trainer == null) return;
        new Thread(() -> {
            System.out.println("Wait before evolve cycle finish...");
            trainer.stop();
            System.out.println("Pause");
            started = false;
        }).start();
    }
}
