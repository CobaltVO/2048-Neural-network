package ru.falseteam.neural2048.nn;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, описывающий нейрон
 *
 * @author Vladislav Sumin
 * @version 1.1
 */
@XmlRootElement(name = "neuron")
public class Neuron implements Cloneable, Serializable {

    @XmlTransient
    private double inputSignal;

    @XmlTransient
    private double afterActivationSignal;

    @XmlElement(name = "thresholdFunction")
    private ThresholdFunction thresholdFunction;

    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "param")
    private List<Double> params;

    @SuppressWarnings("unused")
    public Neuron() {
        // Required by JAXB
    }


    /**
     * Создает нейрон с заданными параметрами
     *
     * @param function - функция активации
     * @param params   - параметры функции активации
     */
    Neuron(ThresholdFunction function, List<Double> params) {
        setFunctionAndParams(function, params);
    }

    /**
     * Устанавливает функцию активации и ее параметры.
     *
     * @param function - функция активации
     * @param params   - параметры функции активации
     * @throws IllegalArgumentException - если количество переданных аргументов не соответствует функции активации
     */
    public void setFunctionAndParams(ThresholdFunction function, List<Double> params) {
        if (params.size() != function.getDefaultParams().size())//TODO тут можно упростить
            throw new IllegalArgumentException("The function requires " + function.getDefaultParams().size()
                    + " parameters. But the number of parameters is " + params.size());
        thresholdFunction = function;
        this.params = params;
    }

    void addSignal(double value) {
        this.inputSignal += value;
    }

    void activate() {
        afterActivationSignal = thresholdFunction.calculate(inputSignal, params);
        inputSignal = 0;
    }

    double getAfterActivationSignal() {
        return this.afterActivationSignal;
    }

    public ThresholdFunction getFunction() {
        return this.thresholdFunction;
    }

    public List<Double> getParams() {
        List<Double> ret = new ArrayList<>(this.params.size());
        ret.addAll(this.params);
        return ret;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Neuron clone() {
        List<Double> cloneParams = new ArrayList<>(params.size());
        cloneParams.addAll(params);
        Neuron clone = new Neuron(this.thresholdFunction, cloneParams);
        clone.inputSignal = 0;
        clone.afterActivationSignal = 0;
        return clone;
    }

    @Override
    public String toString() {
        return "Neuron [thresholdFunction=" + thresholdFunction +
                ", params=" + params + "]";
    }
}
