package ru.falseteam.neural2048.ga

import java.util.*


class MutatorCrossover<C> {
    interface Crossing<C> {
        /**
         * Возвращает скрещенные КОПИИ хромосом
         */
        fun crossing(chromosome1: C, chromosome2: C): List<C>
    }

    interface Mutation<C> {
        /**
         * Создает КОПИЮ данной хромосомы, после чего мутирует ее
         * @return - мутированная копия хромосомы
         */
        fun mutate(chromosome: C): C
    }

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

    fun crossover(chromosome1: C, chromosome2: C): List<C> {
        val crossover = crosses[random.nextInt(crosses.size)]
        return crossover.crossing(chromosome1, chromosome2)
    }
}