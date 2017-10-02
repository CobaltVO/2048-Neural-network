package ru.falseteam.neural2048.logic;

/**
 * Полный набор данных, описывающих текущее состояние игры
 *
 * @version 1.0
 */
public class GameData {
    public int theGrid[][] = new int[4][4];
    public int score = 0;
    public GameState state;
    public int maxTileExp = 1; // Максимальная степень плитки за игру
}
