package ru.falseteam.neural2048.gnn.crossower

import ru.falseteam.neural2048.ga.MutatorCrossover
import ru.falseteam.neural2048.nn.NeuralNetwork
import java.util.*

/**
 * Автор: Евгений Рудзянский
 * Дата : 09.10.17
 */
class UniformelyDistributedWeightsCrossover : MutatorCrossover.Crossing<NeuralNetwork> {
    private val random = Random()
    override fun crossing(chromosome1: NeuralNetwork, chromosome2: NeuralNetwork): List<NeuralNetwork> {
        val anotherClone = chromosome2.clone()
        val thisClone = chromosome1.clone()

        // case of switch
        val thisWeights = thisClone.neuronsLinks.getAllWeights()
        val anotherWeights = anotherClone.neuronsLinks.allWeights

        //uniformelyDistributedWeightsCrossover(thisWeights, anotherWeights)
        //uniformelyDistributedWeightsCrossover(thisWeights: MutableList<Double>, anotherWeights: MutableList<Double>)
        val weightsSize = thisWeights.size
        var itersCount = this.random.nextInt(weightsSize)
        if (itersCount == 0) {
            itersCount = 1
        }
        val used = HashSet<Int>()
        for (iter in 0 until itersCount) {
            var i = this.random.nextInt(weightsSize)
            if (weightsSize > 1) {
                while (used.contains(i)) {
                    i = this.random.nextInt(weightsSize)
                }
            }
            val thisWeight = thisWeights[i]
            val anotherWeight = anotherWeights[i]

            anotherWeights[i] = thisWeight
            thisWeights[i] = anotherWeight
            used.add(i)
        }
        // end func

        thisClone.neuronsLinks.setAllWeights(thisWeights)
        anotherClone.neuronsLinks.allWeights = anotherWeights

        // after switch
        val ret = ArrayList<NeuralNetwork>()
        ret.add(anotherClone)
        ret.add(thisClone)
        //ret.add(anotherClone.mutate_());
        //ret.add(thisClone.mutate_());//TODO мазафака
        return ret
    }
}