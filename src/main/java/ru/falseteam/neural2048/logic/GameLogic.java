package ru.falseteam.neural2048.logic;

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import ru.falseteam.neural2048.gui.Screen;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class GameLogic extends GameData {
    private static final Random random = new Random();
    private final Screen screen;

    public GameLogic(Screen screen) {
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

    private int mainFlag = 0;

    public boolean move(Directions direction) {
        boolean moved = false;
        switch (direction) {
            case UP:
            case DOWN:
                for (int i = 0; i < 4; ++i) {
                    int[] row = Arrays.copyOf(theGrid[i], theGrid.length);
                    if (direction.equals(Directions.DOWN)) ArrayUtils.reverse(row);
                    if (shift(row)) {
                        if (direction.equals(Directions.DOWN)) ArrayUtils.reverse(row);
                        theGrid[i] = Arrays.copyOf(row, row.length);
                        moved = true;
                        mainFlag = 0;
                    } else mainFlag |= direction.equals(Directions.UP) ? 0b1000 : 0b0100;
                }
                break;
            case LEFT:
            case RIGHT:
                for (int i = 0; i < 4; ++i) {
                    int[] row = new int[theGrid.length];
                    for (int k = 0; k < row.length; ++k) row[k] = theGrid[k][i];
                    if (direction.equals(Directions.DOWN) || direction.equals(Directions.RIGHT))
                        ArrayUtils.reverse(row);
                    if (shift(row)) {
                        if (direction.equals(Directions.RIGHT)) ArrayUtils.reverse(row);
                        for (int k = 0; k < row.length; ++k) theGrid[k][i] = row[k];
                        moved = true;
                        mainFlag = 0;
                    } else mainFlag |= direction.equals(Directions.LEFT) ? 0b0010 : 0b0001;
                }
        }
        if (mainFlag == 0b1111) state = GameState.END;
        else if (moved) {
            genRandomNumber();
            screen.redraw(this);
        }
        return moved;
    }

    private boolean shift(int[] row) {
        int[] reRow = new int[row.length];
        for (int i = 0, j = 1, k = 0; k < row.length; ) {
            if (j > row.length - 1) reRow[k++] = i < row.length && row[i] != 0 ? row[i++] : 0;
            else if (row[i] == 0) j = ++i + 1;
            else if (row[j] == 0) ++j;
            else if (row[i] == row[j++]) {
                reRow[k] = row[i] + 1;
                score += 1 << reRow[k];
                if (reRow[k++] > maxTileExp) maxTileExp = reRow[k - 1];
                if (maxTileExp == 11) state = GameState.WIN;
                i = j++;
            } else {
                reRow[k++] = row[i];
                i = j - 1;
            }
        }
        if (Arrays.equals(reRow, row)) return false;
        System.arraycopy(reRow, 0, row, 0, reRow.length);
        return true;
    }
}
