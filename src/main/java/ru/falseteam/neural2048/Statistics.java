package ru.falseteam.neural2048;

import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.players.Player;

/**
 * Created by admin on 03.10.2017.
 */
public class Statistics {
    private final GameLogic gameLogic;
    private final Player player;

    private long scopeSum = 0;
    private long gameCount = 0;
    private int lowest = Integer.MAX_VALUE;
    private int highest = 0;

    public Statistics(GameLogic gameLogic, Player player) {
        this.gameLogic = gameLogic;
        this.player = player;
    }

    public void playGames(int count) {
        gameLogic.restart();
        for (int i = 0; i < count; i++) {
            player.playOneGame(gameLogic);
            int score = gameLogic.score;
            scopeSum += score;
            gameCount++;

            if (lowest > score) lowest = score;
            if (highest < score) highest = score;

            gameLogic.restart();
        }
    }

    public void printStatistics() {
        System.out.println("Player name: " + player.getClass().getSimpleName());
        System.out.println("Game count: " + gameCount);
        System.out.println("Average score: " + (scopeSum / gameCount));
        System.out.println("Lowest score: " + lowest);
        System.out.println("Highest score: " + highest);

    }
}
