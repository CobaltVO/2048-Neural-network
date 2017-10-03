package ru.falseteam.neural2048;

import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.players.AdvancedCirclePlayer;
import ru.falseteam.neural2048.players.CirclePlayer;
import ru.falseteam.neural2048.players.RandomPlayer;

public class Main {
    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic(null);
//        Statistics statistics = new Statistics(gameLogic, new RandomPlayer());
//        Statistics statistics = new Statistics(gameLogic, new CirclePlayer());
//        Statistics statistics = new Statistics(gameLogic, new AdvancedCirclePlayer());
//        statistics.playGames(300_000);
//        statistics.printStatistics();
        new RandomNeuralNetwork(gameLogic);
    }
}
