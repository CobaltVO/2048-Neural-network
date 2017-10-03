package ru.falseteam.neural2048.players;

import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.GeneticAlgorithm;
import com.lagodiuk.ga.Population;
import ru.falseteam.neural2048.nn.ThresholdFunction;
import com.lagodiuk.nn.genetic.OptimizableNeuralNetwork;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;
import ru.falseteam.neural2048.nn.NetworkCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNeuralNetwork {
    public RandomNeuralNetwork(GameLogic gameLogic) {
        new Thread(() -> {
            Population<OptimizableNeuralNetwork> population = new Population<>();

            List<ThresholdFunction> functions = new ArrayList<>();
            functions.add(ThresholdFunction.SIGMA);
            functions.add(ThresholdFunction.TANH);
            OptimizableNeuralNetwork nn = NetworkCreator.initNeuralNetwork(functions, 16, 8, 4, 2);

            for (int i = 0; i < 80; i++) {
                population.addChromosome(nn.mutate());
            }

            Fitness<OptimizableNeuralNetwork, Double> fit = chromosome -> {
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

            GeneticAlgorithm<OptimizableNeuralNetwork, Double> env =
                    new GeneticAlgorithm<>(population, fit);

            final Random random = new Random();
            env.addIterationListener(environment -> {
                OptimizableNeuralNetwork gene = environment.getBest();
                Double d = environment.fitness(gene);
                System.out.println(environment.getIteration() + "\t" + (0 - d));

                environment.setParentChromosomesSurviveCount(
                        random.nextInt(environment.getPopulation().getSize()) + 3);
                //environment.setParentChromosomesSurviveCount(20);
            });

            env.evolve(10000);
        }).start();
    }
}
