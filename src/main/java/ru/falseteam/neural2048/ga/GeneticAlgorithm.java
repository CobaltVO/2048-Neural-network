package ru.falseteam.neural2048.ga;

import java.util.*;

public class GeneticAlgorithm<C extends Chromosome<C>, T extends Comparable<T>> {

    private static final int ALL_PARENTAL_CHROMOSOMES = Integer.MAX_VALUE;//TODO что это?

    private class ChromosomesComparator implements Comparator<C> {

        private final Map<C, T> cache = new WeakHashMap<>();

        @Override
        public int compare(C chr1, C chr2) {
            T fit1 = fit(chr1);
            T fit2 = fit(chr2);
            int ret = fit1.compareTo(fit2);
            return ret;
        }

        public T fit(C chr) {
            T fit = this.cache.get(chr);
            if (fit == null) {
                fit = GeneticAlgorithm.this.fitnessFunc.calculate(chr);
                this.cache.put(chr, fit);
            }
            return fit;
        }

        public void clearCache() {
            cache.clear();
        }
    }

    private final ChromosomesComparator chromosomesComparator;

    private final Fitness<C, T> fitnessFunc;

    private Population<C> population;

    // listeners of genetic algorithm iterations (handle callback afterwards)
    private final List<IterationListener<C, T>> iterationListeners = new LinkedList<>();

    private boolean terminate = false;

    // number of parental chromosomes, which survive (and move to new
    // population)
    private int parentChromosomesSurviveCount = ALL_PARENTAL_CHROMOSOMES;

    private int iteration = 0;

    public GeneticAlgorithm(Population<C> population, Fitness<C, T> fitnessFunc) {
        this.population = population;
        this.fitnessFunc = fitnessFunc;
        this.chromosomesComparator = new ChromosomesComparator();
        this.population.sortPopulationByFitness(this.chromosomesComparator);
    }

    public void evolve() {
        int parentPopulationSize = this.population.getSize();

        Population<C> newPopulation = new Population<>();

        for (int i = 0; (i < parentPopulationSize) && (i < this.parentChromosomesSurviveCount); i++) {
            newPopulation.addChromosome(this.population.getChromosomeByIndex(i));
        }

        for (int i = 0; i < parentPopulationSize; i++) {
            C chromosome = this.population.getChromosomeByIndex(i);
            C mutated = chromosome.mutate();

            C otherChromosome = this.population.getRandomChromosome();
            List<C> crossovered = chromosome.crossover(otherChromosome);

            newPopulation.addChromosome(mutated);
            for (C c : crossovered) {
                newPopulation.addChromosome(c);
            }
        }

        newPopulation.sortPopulationByFitness(this.chromosomesComparator);
        newPopulation.trim(parentPopulationSize);
        this.population = newPopulation;
    }

    public void evolve(int count) {
        this.terminate = false;

        for (int i = 0; i < count; i++) {
            if (this.terminate) {
                break;
            }
            this.evolve();
            this.iteration = i;
            for (IterationListener<C, T> l : this.iterationListeners) {
                l.update(this);
            }
        }
    }

    public int getIteration() {
        return this.iteration;
    }

    public void terminate() {
        this.terminate = true;
    }

    public Population<C> getPopulation() {
        return this.population;
    }

    public C getBest() {
        return this.population.getChromosomeByIndex(0);
    }

    public C getWorst() {
        return this.population.getChromosomeByIndex(this.population.getSize() - 1);
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

    public T fitness(C chromosome) {
        return this.chromosomesComparator.fit(chromosome);
    }

    public void clearCache() {
        this.chromosomesComparator.clearCache();
    }
}