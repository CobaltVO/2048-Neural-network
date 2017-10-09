package ru.falseteam.neural2048.gnn.crossower

import ru.falseteam.neural2048.ga.MutatorCrossover
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork
import java.util.*

/**
 * Автор: Евгений Рудзянский
 * Дата : 09.10.17
 */
class TwoPointsNeuronsCrossover : MutatorCrossover.Crossing<GeneticNeuralNetwork> {
    private val random = Random()
    override fun crossing(chromosome1: GeneticNeuralNetwork, chromosome2: GeneticNeuralNetwork): List<GeneticNeuralNetwork> {
        val anotherClone = chromosome2.clone()
        val thisClone = chromosome1.clone()

        // case of switch
        val thisNeurons = thisClone.neurons
        val anotherNeurons = anotherClone.neurons
        //this.twoPointsNeuronsCrossover(thisClone.neurons, anotherClone.neurons)
        //this.twoPointsNeuronsCrossover(thisNeurons: MutableList<Neuron>, anotherNeurons: MutableList<Neuron>)
        var left = this.random.nextInt(thisNeurons.size)
        var right = this.random.nextInt(thisNeurons.size)
        if (left > right) {
            val tmp = right
            right = left
            left = tmp
        }
        for (i in left until right) {
            val thisNeuron = thisNeurons[i]
            thisNeurons[i] = anotherNeurons[i]
            anotherNeurons[i] = thisNeuron
        }
        // end func

        // after switch
        val ret = ArrayList<GeneticNeuralNetwork>()
        ret.add(anotherClone)
        ret.add(thisClone)
        //ret.add(anotherClone.mutate_());
        //ret.add(thisClone.mutate_());//TODO мазафака
        return ret
    }
}