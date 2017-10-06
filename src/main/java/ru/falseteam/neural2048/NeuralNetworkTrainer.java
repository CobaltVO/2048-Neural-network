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
public class NeuralNetworkTrainer implements Fitness<GeneticNeuralNetwork, Integer>,
        IterationListener<GeneticNeuralNetwork, Integer> {
    // НАСТРОЙКИ
    private static final int POPULATION_SIZE = 40;
    private static final int ITERATION = 20;
    private static final int POPULATION_SURVIVE = 5;
    // КОНЕЦ НАСТРОЕК

    private final GameLogic gameLogic;
    private final NeuralNetworkPlayer player;
    private GeneticAlgorithm<GeneticNeuralNetwork, Integer> env;
    private volatile boolean work = false;


    private final Object lock = new Object();
    private final Runnable runner = new Runnable() {
        @Override
        public void run() {
            while (work) {
                env.evolve(1);
            }
        }
    };
    private Thread run;

    public NeuralNetworkTrainer(GameLogic gameLogic, GeneticNeuralNetwork nn) {
        this.gameLogic = gameLogic;
        player = new NeuralNetworkPlayer(null);

        //Заполняем популяцию
        Population<GeneticNeuralNetwork> population = new Population<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.addChromosome(nn.mutate());
        }

        env = new GeneticAlgorithm<>(population, this);
        env.addIterationListener(this);
    }

    public void start() {
        synchronized (lock) {
            if (work) return;
            work = true;
            run = new Thread(runner);
            run.start();
        }
    }

    public void stop() {
        synchronized (lock) {
            if (!work) return;
            work = false;
            try {
                run.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
