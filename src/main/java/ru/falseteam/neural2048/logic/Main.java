package ru.falseteam.neural2048.logic;

import ru.falseteam.neural2048.players.RandomNeuralNetwork;

public class Main {
    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic(null);
        new RandomNeuralNetwork(gameLogic);
    }
}