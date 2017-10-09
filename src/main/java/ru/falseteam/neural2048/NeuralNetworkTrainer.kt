package ru.falseteam.neural2048

import ru.falseteam.neural2048.ga.Fitness
import ru.falseteam.neural2048.ga.GeneticAlgorithm
import ru.falseteam.neural2048.ga.IterationListener
import ru.falseteam.neural2048.ga.MutatorCrossover
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork
import ru.falseteam.neural2048.gnn.crossower.CrossoverAllInOne
import ru.falseteam.neural2048.gnn.mutations.MutationWeights
import ru.falseteam.neural2048.logic.GameLogic
import ru.falseteam.neural2048.nn.NeuralNetwork
import ru.falseteam.neural2048.players.NeuralNetworkPlayer

/**
 * Класс для теренеровки нейронной сети.
 *
 * @author Vladialv Sumin
 * @version 1.1
 */
class NeuralNetworkTrainer(nn: GeneticNeuralNetwork) : Fitness<GeneticNeuralNetwork, Int>, IterationListener<GeneticNeuralNetwork, Int> {
    // КОНЕЦ НАСТРОЕК


    private val players: Array<NeuralNetworkPlayer> = arrayOf(
            NeuralNetworkPlayer(null, GameLogic(null)),
            NeuralNetworkPlayer(null, GameLogic(null)),
            NeuralNetworkPlayer(null, GameLogic(null)),
            NeuralNetworkPlayer(null, GameLogic(null)))

    private val env: GeneticAlgorithm<GeneticNeuralNetwork, Int>
    @Volatile private var work = false


    private val lock = Any()
    private val runner: Runnable
    private var run: Thread? = null

    private var maxScore = 0
    private var counter = 0
    private val scores = IntArray(10)

    val best: NeuralNetwork
        get() = synchronized(lock) {
            if (work) throw RuntimeException()
            return env.best
        }

    init {
        val mutatorCrossover = MutatorCrossover<GeneticNeuralNetwork>()
        mutatorCrossover.addMutations(MutationWeights())
        mutatorCrossover.addCrosses(CrossoverAllInOne())
        env = GeneticAlgorithm(this, mutatorCrossover)
        for (i in 0 until POPULATION_SIZE) {
            env.addChromosome(mutatorCrossover.mutate(nn))
        }
        env.addIterationListener(this)

        runner = Runnable {
            while (work) env.evolve(1)
        }
    }

    fun start() {
        synchronized(lock) {
            if (work) return
            work = true
            run = Thread(runner)
            run!!.start()
        }
    }

    fun stop() {
        synchronized(lock) {
            if (!work) return
            work = false
            try {
                run!!.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * Проводит ITERATION игр для каждой хромосомы
     *
     * @return 0 - усредненный счет за ITERATION игр
     */
    override fun calculate(chromosome: GeneticNeuralNetwork, threadNumber: Int): Int? {
        val player = players[threadNumber]
        var score = 0
        for (i in 0 until ITERATION) {
            player.setNeuralNetwork(chromosome)
            player.playOneGame()
            score += player.gameLogic.score
            player.gameLogic.restart()
        }
        score /= ITERATION
        return -score
    }

    override fun update(environment: GeneticAlgorithm<GeneticNeuralNetwork, Int>) {
        val score = -environment.bestFitness

        print(environment.iteration.toString() + "\t")
        print(score)

        if (maxScore < score) {
            maxScore = score
            print("\t($maxScore)\t NEW RECORD")
        } else
            print("\t($maxScore)\t")

        scores[counter++] = score
        counter %= 10
        var scoreAvg = (0..9).sumBy { scores[it] }
        scoreAvg /= 10
        println("\t" + scoreAvg)

        environment.parentChromosomesSurviveCount = POPULATION_SURVIVE
    }

    companion object {
        // НАСТРОЙКИ
        private val POPULATION_SIZE = 60
        private val ITERATION = 100
        private val POPULATION_SURVIVE = 15
    }
}
