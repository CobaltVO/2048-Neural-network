package ru.falseteam.neural2048.ga

interface Crossing<C : Chromosome<C>> {
    /**
     * Возвращает скрещенные КОПИИ хромосом
     */
    fun crossing(chromosome1: C, chromosome2: C): List<C>
}