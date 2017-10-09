package ru.falseteam.neural2048

import ru.falseteam.neural2048.nn.NeuralNetworkManager
import ru.falseteam.neural2048.nn.ThresholdFunction
import java.util.*

fun main(args: Array<String>) {
    val iterations = 100
    var time2 = 0L
    for (k in 0..iterations) {
        val random = Random()
        val config = intArrayOf(16, 16, 8, 4)
        val threshold = arrayOf(ThresholdFunction.SIGMA)
        val nn = NeuralNetworkManager.createNeuralNetwork(threshold, config)

        var time = System.currentTimeMillis()
        for (i in 0..10_000) {
            for (j in 0..15) nn.putSignalToNeuron(j, random.nextDouble())
            nn.activate()
        }
        time = System.currentTimeMillis() - time
        println(time)
        time2 += time
    }
    println()
    println(time2 / iterations)
}
