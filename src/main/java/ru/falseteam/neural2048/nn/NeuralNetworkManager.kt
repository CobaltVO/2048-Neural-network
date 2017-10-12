package ru.falseteam.neural2048.nn

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.util.Random

/**
 * Сохранение, загрузка и генерация нейронных сетей
 *
 * @author Vladislav Sumin
 * @version 1.0
 */
object NeuralNetworkManager {
    private val rnd = Random()

    /**
     * Создает нейронную сеть по заданным параметрам.
     * Каждый нейрон связывается со всеми нейронами следующего слоя
     *
     * @param functions - список функций активации (функции из этого списка выбираются случайно для каждого нейрона)
     * @param config    - конфигурация нейронов
     */
    fun createNeuralNetwork(functions: Array<ThresholdFunction>, config: IntArray): NeuralNetwork {
        val countOfNeuron = config.sum()
        val nn = NeuralNetwork(countOfNeuron)

        for (i in 0 until countOfNeuron) {
            val f = functions[rnd.nextInt(functions.size)]
            nn.setNeuronFunction(i, f, f.defaultParams)
        }

        var offset = config[0]
        for (i in 1 until config.size) {
            for (j in 0 until config[i - 1]) {
                for (k in 0 until config[i]) {
                    nn.addLink(offset - 1 - j, offset + k, getRandomWeight().toDouble())
                }
            }
            offset += config[i]
        }
        return nn
    }

    @Throws(JAXBException::class)
    fun saveAsXML(nn: NeuralNetwork, stream: OutputStream) {
        val context = JAXBContext.newInstance(NeuralNetwork::class.java)
        val marshaller = context.createMarshaller()
        marshaller.marshal(nn, stream)
    }

    @Throws(JAXBException::class)
    fun loadFromXML(stream: InputStream): NeuralNetwork {
        val context = JAXBContext.newInstance(NeuralNetwork::class.java)
        val marshaller = context.createUnmarshaller()
        return marshaller.unmarshal(stream) as NeuralNetwork
    }

    fun saveAsBinary(nn: NeuralNetwork, stream: OutputStream) {
        val out = ObjectOutputStream(stream)
        out.writeObject(nn)
        out.close()
    }

    fun loadFromBinary(stream: InputStream): NeuralNetwork {
        val input = ObjectInputStream(stream)
        val nn = input.readObject() as NeuralNetwork
        input.close()
        return nn
    }

    /**
     * Генерирует случайное значение веса для нейрона
     *
     * @return - вес
     */
    private fun getRandomWeight(): Int {
        val maxWeightNum = 10
        return rnd.nextInt(maxWeightNum) - rnd.nextInt(maxWeightNum)
    }
}
