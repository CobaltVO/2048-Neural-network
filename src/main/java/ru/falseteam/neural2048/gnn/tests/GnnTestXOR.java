package ru.falseteam.neural2048.gnn.tests;

import ru.falseteam.neural2048.ga.Fitness;
import ru.falseteam.neural2048.ga.GeneticAlgorithm;
import ru.falseteam.neural2048.ga.MutatorCrossover;
import ru.falseteam.neural2048.gnn.mutations.MutationWeights;
import ru.falseteam.neural2048.nn.NeuralNetwork;
import ru.falseteam.neural2048.nn.NeuralNetworkManager;
import ru.falseteam.neural2048.nn.ThresholdFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GnnTestXOR {
    private static final int[] NETWORK_CONFIG = {2, 1};
    private static final ThresholdFunction[] thresholdFunctions = {
            ThresholdFunction.SIGMA
    };
    private static final int POPULATION_SIZE = 20;

    private static final Random RANDOM = new Random();

    private static final int a[] = {0, 0, 1, 1};
    private static final int b[] = {0, 1, 0, 1};

    public static void main(String[] args) {
        // Создаем случайную нейронную сеть с задаными параметрами
        NeuralNetwork nn = NeuralNetworkManager.INSTANCE.createNeuralNetwork(thresholdFunctions, NETWORK_CONFIG);
        nn.setNeuronFunction(nn.getNeuronsCount() - 1,
                ThresholdFunction.SIGN, ThresholdFunction.SIGN.getDefaultParams());

        List<Double> weightsOfLinks = new ArrayList<>(
                Collections.nCopies(nn.getWeightsOfLinks().size(), 1d));
        nn.setWeightsOfLinks(weightsOfLinks);


        Fitness<NeuralNetwork, Integer> fit = (chromosome, threadNumber) -> {
            int score = 0;
            for (int i = 0; i < 4; i++) {
                chromosome.putSignalToNeuron(0, a[i]);
                chromosome.putSignalToNeuron(1, b[i]);

                chromosome.activate();

                int c = ((int) chromosome.getAfterActivationSignal(chromosome.getNeuronsCount() - 1));
                if (c == (a[i] ^ b[i])) score++;
            }
            return 4 - score;
        };

        MutatorCrossover<NeuralNetwork> mutatorCrossover = new MutatorCrossover<>();
        mutatorCrossover.addMutations(new MutationWeights());

        GeneticAlgorithm<NeuralNetwork, Integer> env =
                new GeneticAlgorithm<>(fit, mutatorCrossover);
        // Заполняем популяцию
        for (int i = 0; i < POPULATION_SIZE; i++) {
            env.addChromosome(mutatorCrossover.mutate(nn));
        }

        env.addIterationListener(environment -> {
            int score = environment.getBestFitness();

            System.out.print(environment.getIteration() + "\t");
            System.out.println(score);

            if (score == 0) env.terminate();

            environment.setParentChromosomesSurviveCount(POPULATION_SIZE / 3);
        });
        env.evolve(100000);

        nn = env.getBest();

        for (int i = 0; i < 4; i++) {
            nn.putSignalToNeuron(0, a[i]);
            nn.putSignalToNeuron(1, b[i]);
            nn.activate();
            int c = ((int) nn.getAfterActivationSignal(nn.getNeuronsCount() - 1));
            if (c != (a[i] ^ b[i])) throw new RuntimeException();
            System.out.println(a[i] + " ^ " + b[i] + " = " + c);
        }
    }
}
