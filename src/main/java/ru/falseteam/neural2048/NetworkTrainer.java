package ru.falseteam.neural2048;

import ru.falseteam.neural2048.ga.Fitness;
import ru.falseteam.neural2048.ga.GeneticAlgorithm;
import ru.falseteam.neural2048.ga.IterationListener;
import ru.falseteam.neural2048.ga.Population;
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.players.NeuralNetworkPlayer;

/**
 * Класс для теренеровки нейронной сети.
 *
 * @author Vladialv Sumin
 * @version 1.1
 */
public class NetworkTrainer implements Fitness<GeneticNeuralNetwork, Integer>,
        IterationListener<GeneticNeuralNetwork, Integer> {
    // НАСТРОЙКИ
    private static final int POPULATION_SIZE = 40;
    private static final int ITERATION = 20;
    private static final int POPULATION_SURVIVE = 5;
    // КОНЕЦ НАСТРОЕК

    private final GameLogic gameLogic;
    private final NeuralNetworkPlayer player;

    public NetworkTrainer(GameLogic gameLogic, GeneticNeuralNetwork nn) {
        this.gameLogic = gameLogic;
        player = new NeuralNetworkPlayer(null);

        //Заполняем популяцию
        Population<GeneticNeuralNetwork> population = new Population<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.addChromosome(nn.mutate());
        }

        GeneticAlgorithm<GeneticNeuralNetwork, Integer> env =
                new GeneticAlgorithm<>(population, this);

        env.addIterationListener(this);
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

    private int maxScore = 0;
    private int counter = 0;
    private final int[] scores = new int[10];

    @Override
    public void update(GeneticAlgorithm<GeneticNeuralNetwork, Integer> environment) {
        GeneticNeuralNetwork gene = environment.getBest();
        int score = -environment.fitness(gene);

        System.out.print(environment.getIteration() + "\t");
        System.out.print(score);

        if (maxScore < score) {
            maxScore = score;
            System.out.print("\t(" + maxScore + ")\t NEW RECORD");
        } else
            System.out.print("\t(" + maxScore + ")\t");

        scores[counter++] = score;
        counter %= 10;
        int scoreAvg = 0;
        for (int i = 0; i < 10; i++) {
            scoreAvg += scores[i];
        }
        scoreAvg /= 10;
        System.out.println("\t" + scoreAvg);

        environment.setParentChromosomesSurviveCount(POPULATION_SURVIVE);
        environment.clearCache();//TODO Посмотреть подробнее
    }
}
