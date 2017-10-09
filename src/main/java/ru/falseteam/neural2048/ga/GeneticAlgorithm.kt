package ru.falseteam.neural2048.ga


import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class GeneticAlgorithm<C, T : Comparable<T>>(
        private val fitnessFunc: Fitness<C, T>, // Функция роста
        private val mutatorCrossover: MutatorCrossover<C>// Мутатор
) {

    // Слушатель итераций
    interface IterationListener<C, T : Comparable<T>> {
        fun update(environment: GeneticAlgorithm<C, T>)
    }

    private val iterationListeners = LinkedList<IterationListener<C, T>>()

    //Набор хромосом
    private var chromosomes: MutableList<Pair<C, T>> = ArrayList()
    private var terminate = false
    //Количество родительских хромосом, которые выживают и учавствуют в размножении
    var parentChromosomesSurviveCount = Integer.MAX_VALUE
    //Кол-во прошедших итераций
    var iteration = 0
        private set
    var threadCount = 4

    private val random = Random()

    private fun evolve() {
        val parentPopulationSize = chromosomes.size
        val newChromosomes = ArrayList<Pair<C, T>>()//TODO убрать

        //Копируем лучшие хромосомы
        run {
            var i = 0
            while (i < parentPopulationSize && i < parentChromosomesSurviveCount)
                newChromosomes.add(chromosomes[i++])
        }
        val newPopulationSize = newChromosomes.size

        //Мутируем лучшие хромосомы
        for (i in 0 until newPopulationSize) {
            newChromosomes.add(Pair(mutatorCrossover.mutate(newChromosomes[i].chromosome)))
        }

        @Suppress("LoopToCallChain")
        for (i in 0 until newPopulationSize) {
            val crossover = mutatorCrossover.crossover(
                    newChromosomes[i].chromosome,
                    newChromosomes[random.nextInt(newPopulationSize)].chromosome)
            crossover.mapTo(newChromosomes) { Pair(it) }
        }
        while (newChromosomes.size < parentPopulationSize) { //TODO написать красиво
            val crossover = mutatorCrossover.crossover(
                    newChromosomes[random.nextInt(newPopulationSize)].chromosome,
                    newChromosomes[random.nextInt(newPopulationSize)].chromosome)
            crossover.mapTo(newChromosomes) { Pair(it) }
        }

        sortPopulationByFitness(newChromosomes)
        chromosomes = newChromosomes.subList(0, parentPopulationSize)
    }

    fun evolve(count: Int) {
        this.terminate = false

        for (i in 0 until count) {
            if (this.terminate) {
                break
            }
            this.evolve()
            this.iteration++
            for (l in this.iterationListeners) {
                l.update(this)
            }
        }
    }

    /**
     * Сортирует особей с помощью компаратора
     */
    private fun sortPopulationByFitness(chromosomes: MutableList<Pair<C, T>>) {
        updateFitness(chromosomes)
        chromosomes.sortWith(compareBy({ it.fitness }))
    }

    private fun updateFitness(chromosomes: List<Pair<C, T>>) {
        val counter = AtomicInteger(0)

        class MyThread constructor(private val threadNumber: Int) : Thread() {

            override fun run() {
                while (true) {
                    val chromosomeNumber = counter.getAndAdd(1)
                    if (chromosomeNumber >= chromosomes.size) return
                    val pair = chromosomes[chromosomeNumber]
                    if (pair.fitness != null) continue // избегает повторных игр
                    pair.fitness = fitnessFunc.calculate(pair.chromosome, threadNumber)
                }
            }
        }

        val threads = ArrayList<MyThread>(threadCount - 1)
        for (i in 0 until threadCount - 1) {
            threads.add(MyThread(i))
            threads[i].start()
        }

        MyThread(threadCount - 1).run()

        for (i in 0 until threadCount - 1) {
            try {
                threads[i].join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    fun terminate() {
        terminate = true
    }

    fun addIterationListener(listener: IterationListener<C, T>) {
        iterationListeners.add(listener)
    }

    @Suppress("unused")
    fun removeIterationListener(listener: IterationListener<C, T>) {
        iterationListeners.remove(listener)
    }

    fun addChromosome(chromosome: C) {
        chromosomes.add(Pair(chromosome))
    }

    fun getBest(): C = chromosomes[0].chromosome

    @Suppress("unused")
    fun getWorst(): C = chromosomes[chromosomes.size - 1].chromosome

    fun getBestFitness(): T = chromosomes[0].fitness!!

    private class Pair<out C, T : Comparable<T>>
    constructor(val chromosome: C) {
        var fitness: T? = null
    }
}
