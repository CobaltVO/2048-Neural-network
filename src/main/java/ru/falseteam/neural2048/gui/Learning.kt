package ru.falseteam.neural2048.gui

import ru.falseteam.neural2048.NeuralNetworkTrainer
import ru.falseteam.neural2048.nn.NeuralNetworkManager

import javax.xml.bind.JAXBException
import java.io.*

internal class Learning{
    private var trainer: NeuralNetworkTrainer? = null
    @Volatile private var started = false

    fun saveNn(file: File) {
        if (started) {
            println("Can not saveAsXML when evolve")
            return
        }
        if (trainer == null) {
            println("Can not saveAsXML, population not crated")
            return
        }
        println("Saving...")
        NeuralNetworkManager.saveAsBinary(trainer!!.best, FileOutputStream(file))
        println("Saved")
    }

    fun loadFromNn(file: File) {
        if (started) {
            println("Yet works")
            return
        }
        println("Loading...")
        trainer = NeuralNetworkTrainer(NeuralNetworkManager.loadFromBinary(FileInputStream(file)))
        println("Loaded")
    }

    fun savePopulation(file: File) {
        //TODO

    }

    fun loadPopulation(file: File) {
        //TODO
    }

    /**
     * Создает тренера по умолчанию.
     */
    fun createPopulation() {
        Thread {
            println("Creating population...")
            trainer = NeuralNetworkTrainer.getDefault()
            println("Population created")
        }.start()
    }

    fun play() {
        if (started) return
        if (trainer != null) {
            trainer!!.start()
            started = true
            println("Training begin")
        } else
            println("Population not created")
    }

    fun pause() {
        if (!started || trainer == null) return
        Thread {
            println("Wait before evolve cycle finish...")
            trainer!!.stop()
            println("Pause")
            started = false
        }.start()
    }
}
