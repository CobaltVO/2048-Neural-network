package ru.falseteam.neural2048;

import ru.falseteam.neural2048.ga.Fitness;
import ru.falseteam.neural2048.ga.GeneticAlgorithm;
import ru.falseteam.neural2048.ga.Population;
import ru.falseteam.neural2048.nn.NeuralNetwork;
import ru.falseteam.neural2048.nn.ThresholdFunction;
import ru.falseteam.neural2048.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;
import ru.falseteam.neural2048.nn.NetworkCreator;
import ru.falseteam.neural2048.players.NeuralNetworkPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomNeuralNetwork {
    private static final int[] NETWORK_CONFIG = {16, 8, 4, 2};
    private static final ThresholdFunction[] thresholdFunctions = {
            //ThresholdFunction.SIGMA,
            ThresholdFunction.LINEAR,
    };
    private static final int POPULATION_SIZE = 10;
    private static final int ITERATION = 100;

    public RandomNeuralNetwork(GameLogic gameLogic) {
        //Создаем случайную нейронную сеть с задаными параметрами
        GeneticNeuralNetwork nn = NetworkCreator.initNeuralNetwork(thresholdFunctions, NETWORK_CONFIG);

        //Заполняем популяцию
        Population<GeneticNeuralNetwork> population = new Population<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.addChromosome(nn.mutate());
        }


        Fitness<GeneticNeuralNetwork, Integer> fit = chromosome -> {
            int score = 0;
            for (int j = 0; j < ITERATION; j++) {
                new NeuralNetworkPlayer(nn).playOneGame(gameLogic);
                score += gameLogic.score;
                gameLogic.restart();
            }
            score /= ITERATION;
            return -score;
        };

        GeneticAlgorithm<GeneticNeuralNetwork, Integer> env =
                new GeneticAlgorithm<>(population, fit);

        final Random random = new Random();
        final int[] maxScore = {0};
        env.addIterationListener(environment -> {
            GeneticNeuralNetwork gene = environment.getBest();
            int d = environment.fitness(gene).intValue();
            d = -d;
            if (maxScore[0] < d) {
                maxScore[0] = d;
                System.out.println(environment.getIteration() + "\t" + d + "\t(" + maxScore[0] + ")\t NEW RECORD");
            } else
                System.out.println(environment.getIteration() + "\t" + d + "\t(" + maxScore[0] + ")\t");


//                environment.setParentChromosomesSurviveCount(
//                        random.nextInt(environment.getPopulation().getSize()) + 3);
            environment.setParentChromosomesSurviveCount(POPULATION_SIZE / 3);
            environment.clearCache();
        });
        env.evolve(10000);
    }
}
