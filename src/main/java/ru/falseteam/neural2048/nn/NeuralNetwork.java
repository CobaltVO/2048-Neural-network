package ru.falseteam.neural2048.nn;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Основной класс нейронной сети
 *
 * @implNote нейроны могут быть связанны только с нейронами с большим номером,
 * в противном случае сеть будет работать не корректно (см. функцию activate())
 */
@XmlRootElement
public class NeuralNetwork implements Cloneable, Serializable {

    @XmlElementWrapper(name = "neurons")
    @XmlElement(name = "neuron")
    protected List<Neuron> neurons;

    @XmlElement
    protected Links neuronsLinks = new Links();

    @XmlElement
    protected int activationIterations = 1;

    @SuppressWarnings("WeakerAccess")
    public NeuralNetwork() {
        // Required by JAXB
    }

    /**
     * Создает сеть из заданного кол-ва нейронов без связей между ними
     *
     * @param numberOfNeurons - количество нейронов в сети
     */
    public NeuralNetwork(int numberOfNeurons) {
        neurons = new ArrayList<>(numberOfNeurons);
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.add(new Neuron(ThresholdFunction.SIGN, ThresholdFunction.SIGN.getDefaultParams()));
        }
    }

    /**
     * Устанавливает новую функцию активации для заданного нейрона
     *
     * @param neuronNumber - номер нейрона
     * @param function     - функция активации
     * @param params       - параметры функции активации
     * @throws IllegalArgumentException - если данного нейрона не существует
     */
    public void setNeuronFunction(int neuronNumber, ThresholdFunction function, List<Double> params) {
        if (neuronNumber >= neurons.size()) {
            throw new IllegalArgumentException("Neural network has " + neurons.size()
                    + " neurons. But there was trying to access neuron with index " + neuronNumber);
        }
        neurons.get(neuronNumber).setFunctionAndParams(function, params);
    }

    /**
     * Устанавливает свзяь между заданными нейронами с заданным весом
     *
     * @param activatorNeuronNumber - нейрон активатор
     * @param receiverNeuronNumber  - нейрон приемник
     * @param weight                - вес
     */
    public void addLink(int activatorNeuronNumber, int receiverNeuronNumber, double weight) {
        neuronsLinks.addWeight(activatorNeuronNumber, receiverNeuronNumber, weight);
    }

    /**
     * Прибавляет сигнал к заданному нейрону
     *
     * @param neuronIndex - номер нейрона
     * @param signalValue - значение сигнала
     */
    public void putSignalToNeuron(int neuronIndex, double signalValue) {
        if (neuronIndex >= neurons.size())
            throw new IllegalArgumentException();
        neurons.get(neuronIndex).addSignal(signalValue);
    }

    /**
     * Возвращает послеактивационные сигнал данного нейрона
     *
     * @param neuronIndex - номер нейрона
     * @return - сигнал нейрона
     */
    public double getAfterActivationSignal(int neuronIndex) {
        if (neuronIndex >= neurons.size())
            throw new IllegalArgumentException();
        return neurons.get(neuronIndex).getAfterActivationSignal();
    }

    /**
     * Активирет сеть
     */
    public void activate() {
        for (int iteration = 0; iteration < activationIterations; iteration++) {

            for (int i = 0; i < neurons.size(); i++) {

                Neuron activator = neurons.get(i);
                activator.activate();
                double activatorSignal = activator.getAfterActivationSignal();

                for (Integer receiverNum : neuronsLinks.getReceivers(i)) {
                    if (receiverNum >= neurons.size()) {
                        throw new RuntimeException("Neural network has " + neurons.size()
                                + " neurons. But there was trying to access neuron with index " + receiverNum);
                    }
                    Neuron receiver = neurons.get(receiverNum);
                    double weight = neuronsLinks.getWeight(i, receiverNum);
                    receiver.addSignal(activatorSignal * weight);
                }
            }
        }
    }

    @XmlTransient //TODO разобраться что это
    public List<Double> getWeightsOfLinks() {
        return neuronsLinks.getAllWeights();
    }

    public void setWeightsOfLinks(List<Double> weights) {
        neuronsLinks.setAllWeights(weights);
    }

    /**
     * Создает копию всех нейронов
     *
     * @return - копия всех нейронов
     */
    @XmlTransient
    public List<Neuron> getNeurons() {
        List<Neuron> ret = new ArrayList<>(neurons.size());
        for (Neuron n : neurons) {
            ret.add(n.clone());
        }
        return ret;
    }

    public int getNeuronsCount() {
        return neurons.size();
    }

    /**
     * Кто знает зачем это здесь?
     */
    @Deprecated
    public void setNeurons(List<Neuron> newNeurons) {
        this.neurons = newNeurons;
    }

    @XmlTransient
    public int getActivationIterations() {
        return activationIterations;
    }

    /**
     * Вот тут вообще не понятно что это TODO
     *
     * @param activationIterations
     */
    public void setActivationIterations(int activationIterations) {
        this.activationIterations = activationIterations;
    }

    /**
     * Возвращает копию нейронных связей
     *
     * @return - копия нейронных связей
     */
    public Links getNeuronsLinks() {
        return neuronsLinks.clone();
    }

    @Override
    public NeuralNetwork clone() {
        NeuralNetwork clone = new NeuralNetwork(this.neurons.size());
        clone.neuronsLinks = this.neuronsLinks.clone();
        clone.activationIterations = this.activationIterations;
        clone.neurons = new ArrayList<>(this.neurons.size());
        for (Neuron neuron : this.neurons) {
            clone.neurons.add(neuron.clone());
        }
        return clone;
    }

    @Override
    public String toString() {
        return "NeuralNetwork [neurons=" + this.neurons + ", links=" + this.neuronsLinks + ", activationIterations=" + this.activationIterations + "]";
    }

    public static void marsall(NeuralNetwork nn, OutputStream out) throws Exception {
        // TODO refactoring
        JAXBContext context = JAXBContext.newInstance(NeuralNetwork.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(nn, out);
        out.flush();
    }

    public static NeuralNetwork unmarsall(InputStream in) throws Exception {
        // TODO refactoring
        JAXBContext context = JAXBContext.newInstance(NeuralNetwork.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        NeuralNetwork unmarshalledNn = (NeuralNetwork) unmarshaller.unmarshal(in);
        return unmarshalledNn;
    }
}
