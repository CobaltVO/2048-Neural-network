package ru.falseteam.neural2048.players;

import ru.falseteam.neural2048.ga.Fitness;
import com.lagodiuk.ga.GeneticAlgorithm;
import ru.falseteam.neural2048.ga.Population;
import ru.falseteam.neural2048.nn.ThresholdFunction;
import com.lagodiuk.nn.genetic.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;
import ru.falseteam.neural2048.nn.NetworkCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomNeuralNetwork {
    public RandomNeuralNetwork(GameLogic gameLogic) {
        new Thread(() -> {
            Population<GeneticNeuralNetwork> population = new Population<>();

            List<ThresholdFunction> functions = new ArrayList<>();
            functions.addAll(Arrays.asList(ThresholdFunction.values()));
            //functions.add(ThresholdFunction.SIGMA);
            //functions.add(ThresholdFunction.TANH);
            GeneticNeuralNetwork nn = NetworkCreator.initNeuralNetwork(functions, 16, 8, 4, 2);

            for (int i = 0; i < 50; i++) {
                population.addChromosome(nn.mutate());
            }

            Fitness<GeneticNeuralNetwork, Double> fit = chromosome -> {
                while (gameLogic.state == GameState.GAME) {
                    for (int i = 0; i < 16; ++i) {
                        nn.putSignalToNeuron(i, gameLogic.theGrid[i / 4][i % 4]);
                    }
                    nn.activate();
                    int i = nn.getAfterActivationSignal(nn.getNeuronsCount() - 1) > 0.5 ? 0b01 : 0b00;
                    i |= nn.getAfterActivationSignal(nn.getNeuronsCount() - 2) > 0.5 ? 0b10 : 0b00;
                    while (!gameLogic.move(Directions.values()[i])) {
                        ++i;
                        i &= 3;
                    }
                }
                //double score = Double.MAX_VALUE - (double) gameLogic.score;
                double score = 0 - (double) gameLogic.score;
                gameLogic.restart();
                return score;
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
                environment.setParentChromosomesSurviveCount(3);
                environment.clearCache();
            });

            env.evolve(10000);
        }).start();
    }
}
