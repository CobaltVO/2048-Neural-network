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
    public RandomNeuralNetwork(GameLogic gameLogic) {

        Population<GeneticNeuralNetwork> population = new Population<>();

        List<ThresholdFunction> functions = new ArrayList<>();
        functions.addAll(Arrays.asList(ThresholdFunction.values()));
        //functions.add(ThresholdFunction.SIGMA);
        //functions.add(ThresholdFunction.TANH);
        GeneticNeuralNetwork nn = NetworkCreator.initNeuralNetwork(functions, 16, 8, 4, 2);

        for (int i = 0; i < 10; i++) {
            population.addChromosome(nn.mutate());
        }

        Fitness<GeneticNeuralNetwork, Double> fit = chromosome -> {
            double score = 0;
            for (int j = 0; j < 30; j++) {

                new NeuralNetworkPlayer(nn).playOneGame(gameLogic);
                //double score = Double.MAX_VALUE - (double) gameLogic.score;
                score += (double) gameLogic.score;
                gameLogic.restart();
            }
            score /= 30;
            return -score;
        };

        GeneticAlgorithm<GeneticNeuralNetwork, Double> env =
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
            environment.setParentChromosomesSurviveCount(2);
            environment.clearCache();
        });
        env.evolve(10000);
    }
}
