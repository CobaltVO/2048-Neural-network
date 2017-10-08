package ru.falseteam.neural2048.gnn.mutations

import ru.falseteam.neural2048.ga.Mutation
import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork
import java.util.*

class MutationWeights : Mutation<GeneticNeuralNetwork> {
    private val random = Random()

    override fun mutate(chromosome: GeneticNeuralNetwork): GeneticNeuralNetwork {
        val weights = chromosome.weightsOfLinks
        val weightsSize = weights.size
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
            var w = weights[i]
            w += (this.random.nextGaussian() - this.random.nextGaussian()) * 1 //TODO
            weights[i] = w
            used.add(i)
        }

        val clone = chromosome.clone()
        clone.weightsOfLinks = weights
        return clone
    }
}