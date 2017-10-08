package ru.falseteam.neural2048.gnn.crossower

import ru.falseteam.neural2048.ga.Crossing
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork
import ru.falseteam.neural2048.nn.Neuron
import java.util.*

/**
 * Created by admin on 08.10.2017.
 */
class CrossoverAllInOne:Crossing<GeneticNeuralNetwork> {
    private val random = Random()

    override fun crossing(chromosome1: GeneticNeuralNetwork, chromosome2: GeneticNeuralNetwork): List<GeneticNeuralNetwork> {
        val anotherClone = chromosome2.clone()
        val thisClone = chromosome1.clone()

        when (this.random.nextInt(4)) {
            0 -> {
                val thisWeights = thisClone.neuronsLinks.getAllWeights()
                val anotherWeights = anotherClone.neuronsLinks.allWeights
                this.twoPointsWeightsCrossover(thisWeights, anotherWeights)
                thisClone.neuronsLinks.setAllWeights(thisWeights)
                anotherClone.neuronsLinks.allWeights = anotherWeights
            }
            1 -> {
                val thisWeights = thisClone.neuronsLinks.getAllWeights()
                val anotherWeights = anotherClone.neuronsLinks.allWeights
                this.uniformelyDistributedWeightsCrossover(thisWeights, anotherWeights)
                thisClone.neuronsLinks.setAllWeights(thisWeights)
                anotherClone.neuronsLinks.allWeights = anotherWeights
            }
            2 -> {
                this.twoPointsNeuronsCrossover(thisClone.neurons, anotherClone.neurons)
            }
            3 -> {
                this.uniformelyDistributedNeuronsCrossover(thisClone.neurons, anotherClone.neurons)
            }
        }// TODO
        // case 4: {
        // this.activationIterations += this.random.nextInt(2) -
        // this.random.nextInt(2);
        // this.activationIterations = (this.activationIterations < 1) ? 1 :
        // this.activationIterations;
        // }
        // break;

        val ret = ArrayList<GeneticNeuralNetwork>()
        ret.add(anotherClone)
        ret.add(thisClone)
        //ret.add(anotherClone.mutate_());
        //ret.add(thisClone.mutate_());//TODO мазафака
        return ret
    }

    private fun twoPointsWeightsCrossover(thisWeights: MutableList<Double>, anotherWeights: MutableList<Double>) {
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
    }

    private fun uniformelyDistributedWeightsCrossover(thisWeights: MutableList<Double>, anotherWeights: MutableList<Double>) {
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
    }

    private fun twoPointsNeuronsCrossover(thisNeurons: MutableList<Neuron>, anotherNeurons: MutableList<Neuron>) {
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
    }

    private fun uniformelyDistributedNeuronsCrossover(thisNeurons: MutableList<Neuron>, anotherNeurons: MutableList<Neuron>) {
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
    }
}