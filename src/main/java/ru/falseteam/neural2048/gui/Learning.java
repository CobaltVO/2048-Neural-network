package ru.falseteam.neural2048.gui;

import ru.falseteam.neural2048.NeuralNetworkTrainer;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.nn.NeuralNetwork;
import ru.falseteam.neural2048.nn.NeuralNetworkManager;
import ru.falseteam.neural2048.nn.ThresholdFunction;

import javax.xml.bind.JAXBException;
import java.io.*;

class Learning {
    private final LearningWindow window;
    private NeuralNetworkTrainer trainer;
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
            trainer = new NeuralNetworkTrainer(NeuralNetworkManager.load(new FileInputStream(file)));
            System.out.println("Loaded");
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void savePopulation(File file) {

    }

    void loadPopulation(File file) {

    }

    /**
     * Создает тренера по умолчанию.
     */
    void createPopulation() {
        new Thread(() -> {
            System.out.println("Creating population...");
            trainer = NeuralNetworkTrainer.Companion.getDefault();
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
