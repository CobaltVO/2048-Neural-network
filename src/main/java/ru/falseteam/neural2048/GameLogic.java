package ru.falseteam.neural2048;

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class GameLogic extends GameData {
    private static final Random random = new Random();
    private Screen screen;

    GameLogic(Screen screen) {
        this.screen = screen;
        restart();
    }

    public void restart() {
        state = GameState.GAME;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                theGrid[x][y] = 0;
            }
        }
        genRandomNumber();
        genRandomNumber();
        screen.redraw(this);
    }

    private boolean genRandomNumber() {
        Vector<Pair<Integer, Integer>> vector = new Vector<>(16);
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < 4; ++y) {
                if (theGrid[x][y] == 0) vector.add(new Pair<>(x, y));
            }
        }
        if (vector.size() == 0) return false;
        Pair<Integer, Integer> pair = vector.get(random.nextInt(vector.size()));
        theGrid[pair.getKey()][pair.getValue()] = random.nextFloat() < 0.1f ? 2 : 1;
        return true;
    }

    public void move(Directions direction) {
        boolean flag = true;
        switch (direction) {
            case UP:
            case DOWN:
                for (int i = 0; i < 4; ++i) {
                    int[] r = Arrays.copyOf(theGrid[i], theGrid.length);
                    if (direction.equals(Directions.DOWN)) ArrayUtils.reverse(r);
                    Shifted s = shift(r);
                    if (s.changed) {
                        if (direction.equals(Directions.DOWN)) ArrayUtils.reverse(s.row);
                        theGrid[i] = Arrays.copyOf(s.row, r.length);
                        score += s.score;
                        flag = false;
                    }
                }
                break;
            case LEFT:
            case RIGHT:
                for (int i = 0; i < 4; ++i) {
                    int[] r = new int[theGrid.length];
                    for (int k = 0; k < r.length; ++k) r[k] = theGrid[k][i];
                    if (direction.equals(Directions.DOWN) || direction.equals(Directions.RIGHT)) ArrayUtils.reverse(r);
                    Shifted s = shift(r);
                    if (s.changed) {
                        if (direction.equals(Directions.RIGHT)) ArrayUtils.reverse(s.row);
                        for (int k = 0; k < r.length; ++k) theGrid[k][i] = s.row[k];
                        score += s.score;
                        flag = false;
                    }
                }
        }
        if (flag) return;
        if (!genRandomNumber()) state = GameState.END;
        screen.redraw(this);
    }

    private class Shifted {
        boolean changed = false;
        int[] row = new int[4];
        int score = 0;

        void change() {
            changed = true;
        }

        Shifted(int length) {
            this.row = new int[length];
        }
    }

    // TODO: 01.10.17 пофиксить сдвиг
    private Shifted shift(int[] row) {
        Shifted result = new Shifted(row.length);

        for (int i = 0, j = 1, k = 0; k < row.length; ) {
            if (j > row.length - 1) {
                result.row[k++] = i < row.length && row[i] != 0 ? row[i++] : 0;
            } else if (row[i] == 0) {
                j = ++i + 1;
            } else if (row[j] == 0) {
                ++j;
            } else if (row[i] == row[j++]) {
                result.row[k] = row[i] + 1;
                result.score += 1 << result.row[k];
                if (result.row[k] > maxTileExp) maxTileExp = result.row[k];
                i = j++;
                ++k;
                result.change();
            } else {
                result.row[k++] = row[i];
                i = j - 1;
            }
        }
        result.changed = true;

        return result;
    }
}
