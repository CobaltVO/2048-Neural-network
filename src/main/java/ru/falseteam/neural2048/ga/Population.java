package ru.falseteam.neural2048.ga;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Класс описывает популяццию особей
 * хромосома == особь
 *
 * @param <C> - тип особи
 */
public class Population<C extends Chromosome<C>> implements Iterable<C> {

    private static final int DEFAULT_NUMBER_OF_CHROMOSOMES = 32;

    // список особей
    private List<C> chromosomes = new ArrayList<>(DEFAULT_NUMBER_OF_CHROMOSOMES);

    private final Random random = new Random();

    /**
     * Добавляет новую особь к популяции
     *
     * @param chromosome - особь
     */
    public void addChromosome(C chromosome) {
        chromosomes.add(chromosome);
    }

    public int getSize() {
        return chromosomes.size();
    }

    /**
     * Возвращает случайную особь
     *
     * @return - особь
     */
    public C getRandomChromosome() {
        int numOfChromosomes = chromosomes.size();
        int index = random.nextInt(numOfChromosomes);
        return chromosomes.get(index);
    }

    public C getChromosomeByIndex(int index) {
        return chromosomes.get(index);
    }

    /**
     * Сортирует особей с помощью компаратора
     *
     * @param chromosomesComparator - компаратор для соритровки
     */
    public void sortPopulationByFitness(Comparator<C> chromosomesComparator) {
        Collections.shuffle(this.chromosomes);//TODO зачем перемешивать перед сортировкой?
        chromosomes.sort(chromosomesComparator);
    }

    /**
     * Сокращает количество особей до указанного
     *
     * @param len - количество особей
     */
    public void trim(int len) {
        chromosomes = chromosomes.subList(0, len);
    }

    @NotNull
    @Override
    public Iterator<C> iterator() {
        return this.chromosomes.iterator();
    }

}
