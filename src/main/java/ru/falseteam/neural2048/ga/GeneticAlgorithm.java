package ru.falseteam.neural2048.ga;

import java.util.*;

public class GeneticAlgorithm<C extends Chromosome<C>, T extends Comparable<T>> {

    private static final int ALL_PARENTAL_CHROMOSOMES = Integer.MAX_VALUE;

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

    //private Population<C> population;
    private List<C> chromosomes = new ArrayList<>();

    // listeners of genetic algorithm iterations (handle callback afterwards)
    private final List<IterationListener<C, T>> iterationListeners = new LinkedList<>();

    private boolean terminate = false;

    // number of parental chromosomes, which survive (and move to new
    // population)
    private int parentChromosomesSurviveCount = ALL_PARENTAL_CHROMOSOMES;

    private int iteration = 0;

    public GeneticAlgorithm(Fitness<C, T> fitnessFunc) {
        this.fitnessFunc = fitnessFunc;
        this.chromosomesComparator = new ChromosomesComparator();
        sortPopulationByFitness();//TODO мазафака
    }

//    private void evolveOld() {
//        int parentPopulationSize = this.population.getSize();
//
//        Population<C> newPopulation = new Population<>();
//
//        for (int i = 0; (i < parentPopulationSize) && (i < this.parentChromosomesSurviveCount); i++) {
//            newPopulation.addChromosome(this.population.getChromosomeByIndex(i));
//        }
//
//        for (int i = 0; i < parentPopulationSize; i++) {
//            C chromosome = this.population.getChromosomeByIndex(i);//TODO автор петух
//            C mutated = chromosome.mutate();
//
//            C otherChromosome = this.population.getRandomChromosome();
//            List<C> crossovered = chromosome.crossover(otherChromosome);
//
//            newPopulation.addChromosome(mutated);
//            for (C c : crossovered) {
//                newPopulation.addChromosome(c);
//            }
//        }
//
//        newPopulation.sortPopulationByFitness(this.chromosomesComparator);
//        newPopulation.trim(parentPopulationSize);
//        this.population = newPopulation;
//    }

    private final Random random = new Random();

    private void evolve() {
        int parentPopulationSize = chromosomes.size();

        List<C> newChromosomes = new ArrayList<>();

        //Копируем лучшие хромосомы
        for (int i = 0; (i < parentPopulationSize) && (i < parentChromosomesSurviveCount); i++) {
            newChromosomes.add(chromosomes.get(i));
        }
        int newPopulationSize = newChromosomes.size();

        //Мутируем лучшие хромосомы
        for (int i = 0; i < newPopulationSize; i++) {
            newChromosomes.add(newChromosomes.get(i).mutate());
        }

        for (int i = 0; i < newPopulationSize; i++) {
            List<C> crossover = newChromosomes.get(i).crossover(
                    newChromosomes.get(random.nextInt(newPopulationSize)));
            newChromosomes.addAll(crossover);
        }
        while (newChromosomes.size() < parentPopulationSize) { //TODO написать красиво
            List<C> crossover = newChromosomes.get(random.nextInt(newPopulationSize)).crossover(
                    newChromosomes.get(random.nextInt(newPopulationSize)));
            newChromosomes.addAll(crossover);
        }

        sortPopulationByFitness();
        trim(parentPopulationSize);
        chromosomes = newChromosomes;
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
    public void sortPopulationByFitness() {
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

    public int getIteration() {
        return this.iteration;
    }

    public void terminate() {
        this.terminate = true;
    }

//    public Population<C> getPopulation() {
//        return this.population;
//    }

    public C getBest() {
        return chromosomes.get(0);
    }

    public C getWorst() {
        return chromosomes.get(chromosomes.size() - 1);
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

    public void addChromosome(C chromosome){
        chromosomes.add(chromosome);
    }
}
