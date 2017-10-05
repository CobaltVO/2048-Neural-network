package ru.falseteam.neural2048.gnn;

import ru.falseteam.neural2048.nn.ThresholdFunction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;


public class GeneticNeuralNetworkManager {
    private static final Random rnd = new Random();

    /**
     * Создает нейронную сеть по заданным параметрам
     *
     * @param functions - список функций активации
     * @param config    - конфигурация нейронов
     */
    public static GeneticNeuralNetwork createNeuralNetwork(ThresholdFunction[] functions, int[] config) {
        int countOfNeuron = 0;
        for (int i : config) {
            countOfNeuron += i;
        }
        GeneticNeuralNetwork nn = new GeneticNeuralNetwork(countOfNeuron);

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

    public static void save(GeneticNeuralNetwork nn, OutputStream stream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(nn.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(nn, stream);
    }

    public static GeneticNeuralNetwork load(Class<GeneticNeuralNetwork> nn, InputStream stream)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(nn);
        Unmarshaller marshaller = context.createUnmarshaller();
        GeneticNeuralNetwork unmarshal = (GeneticNeuralNetwork) marshaller.unmarshal(stream);
        return unmarshal;
    }

    private static int getRandomWeight() {
        final int maxWeightNum = 10;
        return rnd.nextInt(maxWeightNum) - rnd.nextInt(maxWeightNum);
    }
}
