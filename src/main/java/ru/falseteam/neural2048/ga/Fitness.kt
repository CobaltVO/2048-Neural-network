package ru.falseteam.neural2048.ga

/**
 * Функция тренеровки для определения лучшей особи
 *
 * @param <C> - особь
 * @param <T> - степень не справляемости с задачем, чем меньше тем лучше
</T></C> */
interface Fitness<C : Chromosome<C>, T : Comparable<T>> {

    /**
     * Assume that chromosome1 is better than chromosome2 <br></br>
     * fit1 = calculate(chromosome1) <br></br>
     * fit2 = calculate(chromosome2) <br></br>
     * So the following condition must be true <br></br>
     * fit1.compareTo(fit2) <= 0 <br></br>
     */
    fun calculate(chromosome: C, threadNumber: Int): T

}
