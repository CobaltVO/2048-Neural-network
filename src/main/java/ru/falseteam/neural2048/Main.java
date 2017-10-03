package ru.falseteam.neural2048;

import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.players.RandomNeuralNetwork;
import ru.falseteam.neural2048.players.RandomPlayer;

public class Main {
    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic(null);
        Statistics statistics = new Statistics(gameLogic, new RandomPlayer());
        statistics.playGames(100_000);
    }
}
