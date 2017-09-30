package ru.falseteam.neural2048;

import javafx.util.Pair;

import java.util.Random;
import java.util.Vector;

public class GameLogic {
    private static final Random random = new Random();
    private GameData gd = new GameData();
    private Screen screen;

    GameLogic(Screen screen) {
        this.screen = screen;
        genRandomNumber();
        genRandomNumber();
        screen.redraw(gd);
    }

    public void restart() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                gd.tiles[x][y] = 0;
            }
        }
        genRandomNumber();
        genRandomNumber();
        screen.redraw(gd);
    }

    private boolean genRandomNumber() {
        Vector<Pair<Integer, Integer>> vector = new Vector<>();
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                if (gd.tiles[x][y] == 0) vector.add(new Pair<>(x, y));
            }
        }
        if (vector.size() == 0) return false;
        Pair<Integer, Integer> pair = vector.get(random.nextInt(vector.size()));
        gd.tiles[pair.getKey()][pair.getValue()] = random.nextFloat() < 0.2f ? 2 : 1;
        /*
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                gd.tiles[i][j] = i + j + 10;
            }
        }
        */
        return true;
    }

    public void move(Directions direction) {
        //screen.redraw(gd);
    }

}
