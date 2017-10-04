package ru.falseteam.neural2048.players;

import javafx.util.Pair;
import ru.falseteam.neural2048.GeneticNeuralNetwork;
import ru.falseteam.neural2048.logic.Directions;
import ru.falseteam.neural2048.logic.GameLogic;
import ru.falseteam.neural2048.logic.GameState;

import java.util.*;

public class NeuralNetworkPlayer implements Player {
    private GeneticNeuralNetwork nn;

    public NeuralNetworkPlayer(GeneticNeuralNetwork nn) {
        this.nn = nn;
    }

    @Override
    public void playOneGame(GameLogic gameLogic) {
        while (gameLogic.state == GameState.GAME) {
            //Утсановка входных значений нейронов
            for (int i = 0; i < 16; ++i) {
                nn.putSignalToNeuron(i, gameLogic.theGrid[i / 4][i % 4]);
            }

            //Активация
            nn.activate();

            List<Pair<Directions, Double>> moves = new ArrayList<>();
            moves.add(new Pair<>(Directions.UP, nn.getAfterActivationSignal(nn.getNeuronsCount() - 1)));
            moves.add(new Pair<>(Directions.DOWN, nn.getAfterActivationSignal(nn.getNeuronsCount() - 1)));
            moves.add(new Pair<>(Directions.LEFT, nn.getAfterActivationSignal(nn.getNeuronsCount() - 1)));
            moves.add(new Pair<>(Directions.RIGHT, nn.getAfterActivationSignal(nn.getNeuronsCount() - 1)));
            moves.sort(Comparator.comparingDouble(Pair::getValue));
            for (Pair<Directions, Double> move : moves) {
                if (gameLogic.move(move.getKey())) break;
            }
        }
    }

    public void setNeuralNetwork(GeneticNeuralNetwork nn) {
        this.nn = nn;
    }
}
