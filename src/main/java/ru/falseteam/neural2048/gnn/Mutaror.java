package ru.falseteam.neural2048.gnn;

import ru.falseteam.neural2048.ga.Chromosome;

public abstract class Mutaror<C extends Chromosome<C>> {
    protected final double p;

    public Mutaror(double p) {
        this.p = p;
    }

    public  abstract C mutate();
}
