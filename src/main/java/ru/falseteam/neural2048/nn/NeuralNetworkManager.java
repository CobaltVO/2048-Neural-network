package ru.falseteam.neural2048.nn;

import ru.falseteam.neural2048.gnn.GeneticNeuralNetwork;
import ru.falseteam.neural2048.nn.ThresholdFunction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Сохранение, загрузка и генерация нейронных сетей
 *
 * @author Vladislav Sumin
 * @version 1.0
 */
public class NeuralNetworkManager {
    private static final Random rnd = new Random();

    /**
     * Создает нейронную сеть по заданным параметрам.
     * Каждый нейронн связывается со всемы нейронами следующего слоя
     *
     * @param functions - список функций активации (функции из этого списка выбираются случайно для каждого нейрона)
     * @param config    - конфигурация нейронов
     */
    public static NeuralNetwork createNeuralNetwork(ThresholdFunction[] functions, int[] config) {
        int countOfNeuron = 0;
        for (int i : config) {
            countOfNeuron += i;
        }
        NeuralNetwork nn = new GeneticNeuralNetwork(countOfNeuron);

        for (int i = 0; i < countOfNeuron; i++) {
            ThresholdFunction f = functions[rnd.nextInt(functions.length)];
            nn.setNeuronFunction(i, f, f.getDefaultParams());
        }

        int offset = config[0];
        for (int i = 1; i < config.length; i++) {
            for (int j = 0; j < config[i - 1]; j++) {
                for (int k = 0; k < config[i]; k++) {
                    nn.addLink(offset - 1 - j, offset + k, getRandomWeight());
                }
            }
            offset += config[i];
        }
        return nn;
    }

    public static void save(NeuralNetwork nn, OutputStream stream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(NeuralNetwork.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(nn, stream);
    }

    public static NeuralNetwork load(InputStream stream)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(NeuralNetwork.class);
        Unmarshaller marshaller = context.createUnmarshaller();
        NeuralNetwork unmarshal = (NeuralNetwork) marshaller.unmarshal(stream);
        return unmarshal;
    }

    /**
     * Генерирует случайное значение веса для нейрона
     *
     * @return - вес
     */
    private static int getRandomWeight() {
        final int maxWeightNum = 10;
        return rnd.nextInt(maxWeightNum) - rnd.nextInt(maxWeightNum);
    }
}
