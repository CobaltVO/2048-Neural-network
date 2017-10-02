package ru.falseteam.neural2048.logic;

class Utils {
    static void reverse(final int[] array) {
        if (array == null) return;

        for (int i = 0, j = array.length - 1; j > i; ++i, --j) {
            array[i] ^= array[j];
            array[j] ^= array[i];
            array[i] ^= array[j];
        }
    }

    static int[][] transpose(final int[][] matrix) {
        int[][] out = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; ++i)
            for (int j = 0; j < matrix[0].length; ++j)
                out[j][i] = matrix[i][j];
        return out;
    }
}
