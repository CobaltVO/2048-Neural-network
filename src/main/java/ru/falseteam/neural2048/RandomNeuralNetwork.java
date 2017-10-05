package ru.falseteam.neural2048;

import ru.falseteam.neural2048.ga.Fitness;
import ru.falseteam.neural2048.ga.GeneticAlgorithm;
import ru.falseteam.neural2048.ga.Population;
import ru.falseteam.neural2048.nn.ThresholdFunction;
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.nn.NetworkCreator;
import ru.falseteam.neural2048.players.NeuralNetworkPlayer;

public class RandomNeuralNetwork implements Fitness<GeneticNeuralNetwork, Integer> {
    // НАСТРОЙКИ
    private static final int[] NETWORK_CONFIG = {16, 10, 6, 4};
    private static final ThresholdFunction[] thresholdFunctions = {
            ThresholdFunction.SIGMA,
            //ThresholdFunction.LINEAR,
    };
    private static final int POPULATION_SIZE = 40;
    private static final int ITERATION = 20;
    private static final int POPULATION_SURVIVE = 5;
    // КОНЕЦ НАСТРОЕК

    private final GameLogic gameLogic;
    private final NeuralNetworkPlayer player;

    public RandomNeuralNetwork(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        player = new NeuralNetworkPlayer(null);

        //Создаем случайную нейронную сеть с задаными параметрами
        GeneticNeuralNetwork nn =
                NetworkCreator.initNeuralNetwork(thresholdFunctions, NETWORK_CONFIG);

        //Заполняем популяцию
        Population<GeneticNeuralNetwork> population = new Population<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.addChromosome(nn.mutate());
        }

        GeneticAlgorithm<GeneticNeuralNetwork, Integer> env =
                new GeneticAlgorithm<>(population, this);

        final int[] maxScore = {0};
        final int[] counter = {0};

        final int[] scores = new int[10];

        env.addIterationListener(environment -> {//TODO переписать
            GeneticNeuralNetwork gene = environment.getBest();
            int score = -environment.fitness(gene);

            System.out.print(environment.getIteration() + "\t");
            System.out.print(score);

            if (maxScore[0] < score) {
                maxScore[0] = score;
                System.out.print("\t(" + maxScore[0] + ")\t NEW RECORD");
            } else
                System.out.print("\t(" + maxScore[0] + ")\t");

            scores[counter[0]++] = score;
            counter[0] %= 10;
            int scoreAvg = 0;
            for (int i = 0; i < 10; i++) {
                scoreAvg += scores[i];
            }
            scoreAvg /= 10;
            System.out.println("\t" + scoreAvg);

            environment.setParentChromosomesSurviveCount(POPULATION_SURVIVE);
            environment.clearCache();//TODO Посмотреть подробнее
        });
        env.evolve(10000);
    }

    /**
     * Проводит ITERATION игр для каждой хромосомы
     *
     * @return 0 - усредненный счет за ITERATION игр
     */
    @Override
    public Integer calculate(GeneticNeuralNetwork chromosome) {
        int score = 0;
        for (int i = 0; i < ITERATION; i++) {
            player.setNeuralNetwork(chromosome);
            player.playOneGame(gameLogic);
            score += gameLogic.score;
            gameLogic.restart();
        }
        score /= ITERATION;
        return -score;
    }
}
