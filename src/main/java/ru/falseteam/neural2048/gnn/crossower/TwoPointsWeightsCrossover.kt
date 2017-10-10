package ru.falseteam.neural2048.gnn.crossower

import ru.falseteam.neural2048.ga.MutatorCrossover
import ru.falseteam.neural2048.nn.NeuralNetwork
import java.util.*

/**
 * Автор: Евгений Рудзянский
 * Дата : 09.10.17
 */

class TwoPointsWeightsCrossover : MutatorCrossover.Crossing<NeuralNetwork> {
    private val random = Random()
    override fun crossing(chromosome1: NeuralNetwork, chromosome2: NeuralNetwork): List<NeuralNetwork> {
        val anotherClone = chromosome2.clone()
        val thisClone = chromosome1.clone()

        // case of switch
        val thisWeights = thisClone.neuronsLinks.getAllWeights()
        val anotherWeights = anotherClone.neuronsLinks.allWeights

        //twoPointsWeightsCrossover(thisWeights, anotherWeights)
        //twoPointsWeightsCrossover(thisWeights: MutableList<Double>, anotherWeights: MutableList<Double>)
        var left = this.random.nextInt(thisWeights.size)
        var right = this.random.nextInt(thisWeights.size)
        if (left > right) {
            val tmp = right
            right = left
            left = tmp
        }
        for (i in left until right) {
            val thisWeight = anotherWeights[i]
            thisWeights[i] = anotherWeights[i]
            anotherWeights[i] = thisWeight
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