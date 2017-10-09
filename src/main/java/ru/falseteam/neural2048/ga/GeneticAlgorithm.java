package ru.falseteam.neural2048.ga;


import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GeneticAlgorithm<C extends Chromosome<C>, T extends Comparable<T>> {

    private static class Pair<C extends Chromosome<C>, T extends Comparable<T>> {
        final C chromosome;
        T fitness;

        Pair(C chromosome) {
            this.chromosome = chromosome;
        }
    }


    private final Fitness<C, T> fitnessFunc;
    private final MutatorCrossover<C> mutatorCrossover;
    private List<Pair<C, T>> chromosomes = new ArrayList<>();
    private final List<IterationListener<C, T>> iterationListeners = new LinkedList<>();
    private boolean terminate = false;
    //Количество родительских хромосом, которые выживают и учавствуют в размножении
    private int parentChromosomesSurviveCount = Integer.MAX_VALUE;
    //Кол-во прошедших итераций
    private int iteration = 0;

    public GeneticAlgorithm(Fitness<C, T> fitnessFunc, MutatorCrossover<C> mutatorCrossover) {
        this.fitnessFunc = fitnessFunc;
        this.mutatorCrossover = mutatorCrossover;
        sortPopulationByFitness(chromosomes);//TODO мазафака
    }

    private final Random random = new Random();

    private void evolve() {
        int parentPopulationSize = chromosomes.size();

        List<Pair<C, T>> newChromosomes = new ArrayList<>();

        //Копируем лучшие хромосомы
        for (int i = 0; (i < parentPopulationSize) && (i < parentChromosomesSurviveCount); i++) {
            newChromosomes.add(chromosomes.get(i));
        }
        int newPopulationSize = newChromosomes.size();

        //Мутируем лучшие хромосомы
        for (int i = 0; i < newPopulationSize; i++) {
            newChromosomes.add(new Pair<>(
                    mutatorCrossover.mutate(
                            newChromosomes.get(i).chromosome)));
        }

        for (int i = 0; i < newPopulationSize; i++) {
            List<C> crossover = mutatorCrossover.crossover(
                    newChromosomes.get(i).chromosome,
                    newChromosomes.get(random.nextInt(newPopulationSize)).chromosome);
            for (C c : crossover) {
                newChromosomes.add(new Pair<>(c));
            }
        }
        while (newChromosomes.size() < parentPopulationSize) { //TODO написать красиво
            List<C> crossover = mutatorCrossover.crossover(
                    newChromosomes.get(random.nextInt(newPopulationSize)).chromosome,
                    newChromosomes.get(random.nextInt(newPopulationSize)).chromosome);
            for (C c : crossover) {
                newChromosomes.add(new Pair<>(c));
            }
        }

        sortPopulationByFitness(newChromosomes);
        chromosomes = trim(newChromosomes, parentPopulationSize);
    }

    public void evolve(int count) {
        this.terminate = false;

        for (int i = 0; i < count; i++) {
            if (this.terminate) {
                break;
            }
            this.evolve();
            this.iteration++;
            for (IterationListener<C, T> l : this.iterationListeners) {
                l.update(this);
            }
        }
    }

    /**
     * Сортирует особей с помощью компаратора
     */
    private void sortPopulationByFitness(List<Pair<C, T>> chromosomes) {
        //Collections.shuffle(chromosomes);//TODO зачем перемешивать перед сортировкой?
        chromosomes.forEach(ctPair -> ctPair.fitness = fitnessFunc.calculate(ctPair.chromosome, 0));//TODO переделать
        chromosomes.sort(Comparator.comparing(o -> o.fitness));
    }

    /**
     * Сокращает количество особей до указанного
     *
     * @param len - количество особей
     */
    @NotNull
    private List<Pair<C, T>> trim(List<Pair<C, T>> chromosomes, int len) {
        return chromosomes.subList(0, len);
    }

    public int getIteration() {
        return this.iteration;
    }

    public void terminate() {
        this.terminate = true;
    }

//    public List<C> getChromosomes() {
//        return chromosomes;
//    }

    public C getBest() {
        return chromosomes.get(0).chromosome;
    }

    public C getWorst() {
        return chromosomes.get(chromosomes.size() - 1).chromosome;
    }

    public void setParentChromosomesSurviveCount(int parentChromosomesCount) {
        this.parentChromosomesSurviveCount = parentChromosomesCount;
    }

    public int getParentChromosomesSurviveCount() {
        return this.parentChromosomesSurviveCount;
    }

    public void addIterationListener(IterationListener<C, T> listener) {
        this.iterationListeners.add(listener);
    }

    public void removeIterationListener(IterationListener<C, T> listener) {
        this.iterationListeners.remove(listener);
    }

    public T getBestFitness() {
        return chromosomes.get(0).fitness;
    }


    public void addChromosome(C chromosome) {
        chromosomes.add(new Pair<>(chromosome));
    }
}
