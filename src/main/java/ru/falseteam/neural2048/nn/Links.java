package ru.falseteam.neural2048.nn;

import ru.falseteam.neural2048.nn.serializing.xml.MapAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.*;

public class Links implements Cloneable, Serializable {

    @XmlJavaTypeAdapter(value = MapAdapter.class)
    private Map<Integer, Map<Integer, Double>> links = new LinkedHashMap<>();

    @XmlElement(name = "linksCount")
    private int totalLinksCount = 0;

    /**
     * Возвращает список нейронов-приемников
     *
     * @param activatorNeuronNumber - номер нейрона-активатора
     */
    public Collection<Integer> getReceivers(int activatorNeuronNumber) {
        Collection<Integer> ret;
        if (links.containsKey(activatorNeuronNumber)) {
            ret = Collections.unmodifiableSet(links.get(activatorNeuronNumber).keySet());
        } else {
            ret = Collections.emptySet();
        }
        return ret;
    }

    /**
     * Возвращает вес связи между двумя нейронами
     *
     * @param activatorNeuronNumber - номер нейрона-активатора
     * @param receiverNeuronNumber  - номер нейрона-приемника
     * @return - вес сзавязи
     * @throws IllegalAccessException - если между данными нейронами связи нет
     */
    public Double getWeight(int activatorNeuronNumber, int receiverNeuronNumber) {
        double weight;
        //TODO Переписать проще.
        if (links.containsKey(activatorNeuronNumber)) {
            Map<Integer, Double> receiverNumToWeight = links.get(activatorNeuronNumber);

            if (receiverNumToWeight.containsKey(receiverNeuronNumber)) {
                weight = receiverNumToWeight.get(receiverNeuronNumber);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
        return weight;
    }

    /**
     * Устанавливает связь между нейронами с заданным весом.
     *
     * @param activatorNeuronNumber - номер нейрона-активатора
     * @param receiverNeuronNumber  - номер нейрона-приемника
     * @param weight                - значение веса
     * @throws IllegalStateException - если связь между данными нейронами уже существует
     */
    public void addWeight(int activatorNeuronNumber, int receiverNeuronNumber, double weight) {
        if (!links.containsKey(activatorNeuronNumber)) {
            links.put(activatorNeuronNumber, new LinkedHashMap<>());
        }
        if (links.get(activatorNeuronNumber).put(receiverNeuronNumber, weight) == null)
            totalLinksCount++;
        else
            throw new IllegalStateException("This link already exists");
    }

    /**
     * Возвращает список всех весов в особом порядке
     *
     * @return - список всех весов
     */
    @XmlTransient
    public List<Double> getAllWeights() {
        List<Double> weights = new ArrayList<>(totalLinksCount);

        for (Integer activatorIndex : links.keySet()) {
            Map<Integer, Double> receiverIndexToWeight = links.get(activatorIndex);

            for (Integer receiverIndex : receiverIndexToWeight.keySet()) {
                weights.add(receiverIndexToWeight.get(receiverIndex));
            }
        }
        return weights;
    }

    /**
     * Устанавливет новые веса для всех связей
     *
     * @param weights - список весов в особом порядке
     */
    public void setAllWeights(List<Double> weights) {
        if (weights.size() != this.totalLinksCount)
            throw new IllegalArgumentException("Number of links: " + totalLinksCount
                    + ". The size of weight list: " + weights.size());


        int index = 0;
        for (Integer activatorIndex : links.keySet()) {
            Map<Integer, Double> receiverIndexToWeight = links.get(activatorIndex);

            for (Integer receiverIndex : receiverIndexToWeight.keySet()) {
                receiverIndexToWeight.put(receiverIndex, weights.get(index));
                index++;
            }
        }
    }

    @Override
    public Links clone() {
        Links clone = new Links();
        clone.totalLinksCount = this.totalLinksCount;
        clone.links = new LinkedHashMap<>();
        for (int key : this.links.keySet()) {
            Map<Integer, Double> val = new LinkedHashMap<>();
            for (int valKey : this.links.get(key).keySet()) {
                val.put(valKey, this.links.get(key).get(valKey));
            }
            clone.links.put(key, val);
        }
        return clone;
    }

    @Override
    public String toString() {
        return "Links [links=" + links + ", totalLinksCount=" + totalLinksCount + "]";
    }
}
