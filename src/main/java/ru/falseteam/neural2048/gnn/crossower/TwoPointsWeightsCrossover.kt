package ru.falseteam.neural2048.gnn.crossower

import ru.falseteam.neural2048.ga.MutatorCrossover
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork
import java.util.*

/**
 * Автор: Евгений Рудзянский
 * Дата : 09.10.17
 */

class TwoPointsWeightsCrossover : MutatorCrossover.Crossing<GeneticNeuralNetwork> {
    private val random = Random()
    override fun crossing(chromosome1: GeneticNeuralNetwork, chromosome2: GeneticNeuralNetwork): List<GeneticNeuralNetwork> {
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
        val ret = ArrayList<GeneticNeuralNetwork>()
        ret.add(anotherClone)
        ret.add(thisClone)
        //ret.add(anotherClone.mutate_());
        //ret.add(thisClone.mutate_());//TODO мазафака
        return ret
    }
}