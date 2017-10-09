package ru.falseteam.neural2048.gnn.crossower

import ru.falseteam.neural2048.ga.MutatorCrossover
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork
import java.util.*

/**
 * Автор: Евгений Рудзянский
 * Дата : 09.10.17
 */
class UniformelyDistributedNeuronsCrossover : MutatorCrossover.Crossing<GeneticNeuralNetwork> {
    private val random = Random()
    override fun crossing(chromosome1: GeneticNeuralNetwork, chromosome2: GeneticNeuralNetwork): List<GeneticNeuralNetwork> {
        val anotherClone = chromosome2.clone()
        val thisClone = chromosome1.clone()

        // case of switch
        val thisNeurons = thisClone.neurons
        val anotherNeurons = anotherClone.neurons
        //uniformelyDistributedNeuronsCrossover(thisClone.neurons, anotherClone.neurons)
        //uniformelyDistributedNeuronsCrossover(thisNeurons: MutableList<Neuron>, anotherNeurons: MutableList<Neuron>)
        val neuronsSize = thisNeurons.size
        var itersCount = this.random.nextInt(neuronsSize)
        if (itersCount == 0) {
            itersCount = 1
        }
        val used = HashSet<Int>()
        for (iter in 0 until itersCount) {
            var i = this.random.nextInt(neuronsSize)
            if (neuronsSize > 1) {
                while (used.contains(i)) {
                    i = this.random.nextInt(neuronsSize)
                }
            }
            val thisNeuron = thisNeurons[i]
            val anotherNeuron = anotherNeurons[i]

            anotherNeurons[i] = thisNeuron
            thisNeurons[i] = anotherNeuron
            used.add(i)
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