package ru.falseteam.neural2048

import ru.falseteam.neural2048.ga.Fitness
import ru.falseteam.neural2048.ga.GeneticAlgorithm
import ru.falseteam.neural2048.ga.MutatorCrossover
import ru.falseteam.neural2048.gnn.crossower.*
import ru.falseteam.neural2048.gnn.mutations.MutationWeights
import ru.falseteam.neural2048.logic.GameLogic
import ru.falseteam.neural2048.nn.NeuralNetwork
import ru.falseteam.neural2048.nn.NeuralNetworkManager
import ru.falseteam.neural2048.nn.ThresholdFunction
import ru.falseteam.neural2048.players.NeuralNetworkPlayer

/**
 * Класс для теренеровки нейронной сети.
 *
 * @author Vladislav Sumin
 * @version 1.2
 */
class NeuralNetworkTrainer(nn:NeuralNetwork) : Fitness<NeuralNetwork, Int>, GeneticAlgorithm.IterationListener<NeuralNetwork, Int> {

    companion object {
        private val ITERATION = 100
        private val POPULATION_SURVIVE = 5
        private val POPULATION_SIZE = POPULATION_SURVIVE * 4
        private val NEURAL_CONFIG = intArrayOf(16, 32, 16, 4)
        private val thresholdFunctions = arrayOf(ThresholdFunction.SIGMA)//ThresholdFunction.LINEAR,

        /**
         * Созадает тренера по умолчанию
         */
        fun getDefault(): NeuralNetworkTrainer {
            val nn = NeuralNetworkManager.createNeuralNetwork(thresholdFunctions, NEURAL_CONFIG)
            for (i in 0 until 15)
                nn.setNeuronFunction(i, ThresholdFunction.LINEAR, ThresholdFunction.LINEAR.defaultParams)
            for (i in nn.neuronsCount - 4 until nn.neuronsCount - 1)
                nn.setNeuronFunction(i, ThresholdFunction.LINEAR, ThresholdFunction.LINEAR.defaultParams)
            return NeuralNetworkTrainer(nn)
        }
    }

    private val players: Array<NeuralNetworkPlayer> = arrayOf(
            NeuralNetworkPlayer(null, GameLogic(null)),
            NeuralNetworkPlayer(null, GameLogic(null)),
            NeuralNetworkPlayer(null, GameLogic(null)),
            NeuralNetworkPlayer(null, GameLogic(null)))

    private val env: GeneticAlgorithm<NeuralNetwork, Int>
    @Volatile private var work = false


    private val lock = Any()
    private val runner: Runnable
    private var run: Thread? = null

    private var counter = 0
    private val scores = IntArray(10)
    private var time: Long = 0L

    val best: NeuralNetwork
        get() = synchronized(lock) {
            if (work) throw RuntimeException()
            return env.getBest()
        }

    init {
        val mutatorCrossover = MutatorCrossover<NeuralNetwork>()
        mutatorCrossover.addMutations(MutationWeights())
        mutatorCrossover.addCrosses(TwoPointsWeightsCrossover())
        mutatorCrossover.addCrosses(UniformelyDistributedWeightsCrossover())
        mutatorCrossover.addCrosses(TwoPointsNeuronsCrossover())
        mutatorCrossover.addCrosses(UniformelyDistributedNeuronsCrossover())
        env = GeneticAlgorithm(this, mutatorCrossover)
        for (i in 0 until POPULATION_SIZE) {
            env.addChromosome(mutatorCrossover.mutate(nn))
        }
        env.addIterationListener(this)
        env.parentChromosomesSurviveCount = POPULATION_SURVIVE

        runner = Runnable {
            while (work) env.evolve(1)
        }
    }

    fun start() {
        synchronized(lock) {
            if (work) return
            work = true
            time = System.currentTimeMillis()
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
    override fun calculate(chromosome: NeuralNetwork, threadNumber: Int): Int {
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

    override fun update(environment: GeneticAlgorithm<NeuralNetwork, Int>) {
        val score = -environment.getBestFitness()

        scores[counter++] = score
        counter %= 10
        var scoreAvg = (0..9).sumBy { scores[it] }
        scoreAvg /= 10

        println("${environment.iteration}.    $score (avg $scoreAvg)" +
                " time ${System.currentTimeMillis() - time}ms")
        time = System.currentTimeMillis()
    }
}
