package ru.falseteam.neural2048.ga;

/**
 * Функция тренеровки для определения лучшей особи
 *
 * @param <C> - особь
 * @param <T> - степень не справляемости с задачем, чем меньше тем лучше
 */
public interface Fitness<C extends Chromosome<C>, T extends Comparable<T>> {

    /**
     * Assume that chromosome1 is better than chromosome2 <br/>
     * fit1 = calculate(chromosome1) <br/>
     * fit2 = calculate(chromosome2) <br/>
     * So the following condition must be true <br/>
     * fit1.compareTo(fit2) <= 0 <br/>
     */
    T calculate(C chromosome, int threadNumber);

}
