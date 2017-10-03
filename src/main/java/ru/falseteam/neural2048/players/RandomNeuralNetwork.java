package ru.falseteam.neural2048.players;

import com.lagodiuk.ga.Fitness;
import com.lagodiuk.ga.GeneticAlgorithm;
import com.lagodiuk.ga.Population;
import com.lagodiuk.nn.ThresholdFunction;
import com.lagodiuk.nn.genetic.OptimizableNeuralNetwork;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;
import ru.falseteam.neural2048.nn.NetworkCreator;

import java.util.Random;

public class RandomNeuralNetwork {
    public RandomNeuralNetwork(GameLogic gameLogic) {
        new Thread(() -> {
            Population<OptimizableNeuralNetwork> population = new Population<>();
//            OptimizableNeuralNetwork nn = initNeuralNetwork();
            OptimizableNeuralNetwork nn = NetworkCreator.initNeuralNetwork(16, 64, 32, 16, 2);

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
                    new GeneticAlgorithm<OptimizableNeuralNetwork, Double>(population, fit);

            final Random random = new Random();
            env.addIterationListener(environment -> {
                OptimizableNeuralNetwork gene = environment.getBest();
                Double d = environment.fitness(gene);
                System.out.println(environment.getIteration() + "\t" + (0 - d));

//                    if (d <= 0.1) {
//                        environment.terminate();
//                    }
                //environment.setParentChromosomesSurviveCount(random.nextInt(environment.getPopulation().getSize()));
                environment.setParentChromosomesSurviveCount(40);
                //environment.evolve(10);
            });

            env.evolve(10000);
        }).start();
    }


    private static OptimizableNeuralNetwork initNeuralNetwork() {
        OptimizableNeuralNetwork nn = new OptimizableNeuralNetwork(82);
        for (int i = 0; i < 82; i++) {
            ThresholdFunction f = ThresholdFunction.getRandomFunction();
            nn.setNeuronFunction(i, f, f.getDefaultParams());
        }

        Random rnd = new Random();

        for (int i = 0; i < 16; ++i) {
            nn.setNeuronFunction(i, ThresholdFunction.LINEAR, ThresholdFunction.LINEAR.getDefaultParams());
            for (int j = 16; j < 48; ++j) {
                nn.addLink(i, j, getRandomWeight(rnd));
            }
        }

        for (int j = 16; j < 48; ++j) {
            for (int k = 48; k < 80; ++k) {
                nn.addLink(j, k, getRandomWeight(rnd));
            }
        }

        for (int k = 48; k < 80; ++k) {
            nn.addLink(k, 80, getRandomWeight(rnd));
            nn.addLink(k, 81, getRandomWeight(rnd));
        }
        return nn;
    }

    private static int getRandomWeight(Random random) {
        final int maxWeightNum = 10;
        return random.nextInt(maxWeightNum) - random.nextInt(maxWeightNum);
    }
}
