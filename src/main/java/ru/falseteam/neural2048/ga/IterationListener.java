package ru.falseteam.neural2048.ga;

import com.lagodiuk.ga.GeneticAlgorithm;

/**
 * @param <C> - тип особи
 * @param <T> - тип не справляемости с задачей
 */
public interface IterationListener<C extends Chromosome<C>, T extends Comparable<T>> {
    void update(GeneticAlgorithm<C, T> environment);
}
