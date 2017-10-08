package ru.falseteam.neural2048.ga

import java.util.*


class MutatorCrossover<C : Chromosome<C>> {
    private val mutations = ArrayList<Mutation<C>>()
    private val crosses = ArrayList<Crossing<C>>()
    private val random = Random()

    fun addMutations(mutation: Mutation<C>) {
        mutations.add(mutation)
    }

    fun addCrosses(crossing: Crossing<C>) {
        crosses.add(crossing)
    }

    fun mutate(chromosome: C): C {
        val mutation = mutations[random.nextInt(mutations.size)]
        return mutation.mutate(chromosome)
    }
}