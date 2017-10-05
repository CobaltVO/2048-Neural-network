package ru.falseteam.neural2048.gnn;

import ru.falseteam.neural2048.ga.Chromosome;

import java.util.List;

public abstract class Crossover<C extends Chromosome<C>> {
    protected final double p;

    public Crossover(double p) {
        this.p = p;
    }

    public abstract List<C> crossover(C anotherChromosome);
}
