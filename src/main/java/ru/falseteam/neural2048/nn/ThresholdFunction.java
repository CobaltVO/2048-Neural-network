package ru.falseteam.neural2048.nn;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.*;

/**
 * Перечисление содержащие набор функций-активаторов
 *
 * @author Vladislav Sumin
 * @version 1.3
 */
@XmlType(name = "basic-threshold-functions")
@XmlEnum
public enum ThresholdFunction {

    @XmlEnumValue("LINEAR")
    LINEAR {
        @Override
        public double calculate(double value, List<Double> params) {
            double a = params.get(0);
            double b = params.get(1);
            return (a * value) + b;
        }

        @Override
        public List<Double> getDefaultParams() {
            List<Double> ret = new LinkedList<>();
            ret.add(1d);
            ret.add(0d);
            return ret;
        }
    },

    @XmlEnumValue("SIGN")
    SIGN {
        @Override
        public double calculate(double value, List<Double> params) {
            double threshold = params.get(0);
            if (value > threshold) return 1;
            return 0;
        }

        @Override
        public List<Double> getDefaultParams() {
            List<Double> ret = new LinkedList<>();
            ret.add(0d);
            return ret;
        }
    },

    @XmlEnumValue("SIGMA")
    SIGMA {
        @Override
        public double calculate(double value, List<Double> params) {
            double a = params.get(0);
            double b = params.get(1);
            double c = params.get(2);
            return a / (b + Math.expm1(-value * c) + 1);
        }

        @Override
        public List<Double> getDefaultParams() {
            double a = 1;
            double b = 1;
            double c = 1;
            List<Double> ret = new ArrayList<>(3);
            ret.add(a);
            ret.add(b);
            ret.add(c);
            return ret;
        }
    };

//    TANH {
//        @Override
//        public double calculate(double value, List<Double> params) {
//            double a = params.get(0);
//            double b = params.get(1);
//            return Math.tanh(value * a + b);
//        }
//
//        @Override
//        public List<Double> getDefaultParams() {
//            List<Double> ret = new LinkedList<>();
//            ret.add(0d);
//            ret.add(0d);
//            return ret;
//        }
//    };

    private static final Random random = new Random();

    public static ThresholdFunction getRandomFunction() {
        ThresholdFunction[] allFunctions = values();
        return allFunctions[random.nextInt(allFunctions.length)];
    }

    public abstract double calculate(double value, List<Double> params);

    public abstract List<Double> getDefaultParams();
}
