package ru.falseteam.neural2048.nn.xml;


public class MapElement {
    public Integer from;
    public Integer to;
    public Double weight;

    public MapElement() {
    }

    public MapElement(Integer from, Integer to, Double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
