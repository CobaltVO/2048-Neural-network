package ru.falseteam.neural2048.logic;

import javafx.util.Pair;
import ru.falseteam.neural2048.gui.Screen;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * Логика игры. Сложно и со вкусом.
 *
 * @version 1.0
 */
public class GameLogic extends GameData {
    private static final Random random = new Random();
    private final Screen screen;

    public GameLogic(Screen screen) {
        this.screen = screen;
        restart();
    }

    public void restart() {
        score = 0;
        maxTileExp = 1;
        state = GameState.GAME;
        for (int i = 0; i < theGrid.length; ++i)
            for (int j = 0; j < theGrid[0].length; ++j)
                theGrid[i][j] = 0;
        genRandomNumber();
        genRandomNumber();
        if (screen != null) screen.redraw(this);
    }

    public boolean move(Directions direction) {
        boolean moved = false;
        // start magic
        boolean needReverse = direction == Directions.DOWN || direction == Directions.RIGHT;
        boolean needTranspose = direction == Directions.LEFT || direction == Directions.RIGHT;
        int maxSize = theGrid[0].length > theGrid.length ? theGrid[0].length : theGrid.length;
        for (int i = 0; i < theGrid.length; ++i) {
            int[] row = new int[maxSize];
            if (needTranspose) for (int k = 0; k < theGrid.length; ++k) row[k] = theGrid[k][i];
            else System.arraycopy(theGrid[i], 0, row, 0, theGrid[i].length);
            if (needReverse) reverse(row);
            if (shift(row)) {
                if (needReverse) reverse(row);
                if (needTranspose) for (int k = 0; k < row.length; ++k) theGrid[k][i] = row[k];
                else System.arraycopy(row, 0, theGrid[i], 0, theGrid[i].length);
                moved = true;
            }
        }
        // end magic
        if (moved) genRandomNumber();
        if (!canShift()) state = GameState.END;
        if (screen != null) screen.redraw(this);
        return moved;
    }

    private void genRandomNumber() {
        Vector<Pair<Integer, Integer>> vector = new Vector<>(16);
        for (int i = 0; i < theGrid.length; ++i)
            for (int j = 0; j < theGrid[0].length; ++j)
                if (theGrid[i][j] == 0) vector.add(new Pair<>(i, j));
        if (vector.size() == 0) return;
        Pair<Integer, Integer> pair = vector.get(random.nextInt(vector.size()));
        theGrid[pair.getKey()][pair.getValue()] = random.nextFloat() < 0.1f ? 2 : 1;
    }

    /**
     * @return true если есть ходы
     */
    private boolean canShift() {
        for (int i = 0; i < theGrid.length; ++i) {
            for (int j = 0; j < theGrid[i].length - 1; ++j) {
                if (theGrid[i][j] == theGrid[i][j + 1] || theGrid[i][j] == 0) return true;
                if (theGrid[j][i] == theGrid[j + 1][i]) return true;
            }
            if (theGrid[i][theGrid[i].length - 1] == 0) return true;
            if (theGrid[theGrid[i].length - 1][i] == 0) return true;
        }
        return false;
    }

    /**
     * @param row - строка, в которую записывается итоговая сдвинутая строка
     * @return true если смог сдвинуть строку
     */
    private boolean shift(int[] row) {
        int[] reRow = new int[row.length];
        // start magic
        for (int i = 0, j = 1, k = 0; k < row.length; ) {
            if (j > row.length - 1) reRow[k++] = i < row.length && row[i] != 0 ? row[i++] : 0;
            else if (row[i] == 0) j = ++i + 1;
            else if (row[j] == 0) ++j;
            else if (row[i] == row[j++]) {
                reRow[k] = row[i] + 1;
                score += 1 << reRow[k];
                if (reRow[k++] > maxTileExp) maxTileExp = reRow[k - 1];
                if (maxTileExp == 11) {
                    //state = GameState.WIN;//TODO временно отключил
                    System.out.println("WIN GAME!!!");
                }
                i = j++;
            } else {
                reRow[k++] = row[i];
                i = j - 1;
            }
        }
        // end magic
        if (Arrays.equals(reRow, row)) return false;
        System.arraycopy(reRow, 0, row, 0, reRow.length);
        return true;
    }

    private void reverse(final int[] array) {
        for (int i = 0, j = array.length - 1; j > i; ++i, --j) {
            array[i] ^= array[j];
            array[j] ^= array[i];
            array[i] ^= array[j];
        }
    }
}
