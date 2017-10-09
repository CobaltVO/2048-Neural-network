package ru.falseteam.neural2048.ga;

/**
 * @param <C> - тип особи
 * @param <T> - тип не справляемости с задачей
 */
public interface IterationListener<C, T extends Comparable<T>> {
    void update(GeneticAlgorithm<C, T> environment);
}
