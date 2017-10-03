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

    public Statistics(GameLogic gameLogic, Player player) {
        this.gameLogic = gameLogic;
        this.player = player;
    }

    public void playGames(int count) {
        gameLogic.restart();
        for (int i = 0; i < count; i++) {
            player.playOneGame(gameLogic);
            scopeSum += gameLogic.score;
            gameCount++;
            gameLogic.restart();
        }
        System.out.println("Average score: " + (scopeSum / gameCount));
    }
}
