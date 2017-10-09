package ru.falseteam.neural2048.ga

interface Mutation<C> {
    /**
     * Создает КОПИЮ данной хромосомы, после чего мутирует ее
     * @return - мутированная копия хромосомы
     */
    fun mutate(chromosome: C): C
}