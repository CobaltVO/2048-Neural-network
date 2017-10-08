package ru.falseteam.neural2048.gnn;

import ru.falseteam.neural2048.ga.Chromosome;
import ru.falseteam.neural2048.nn.NeuralNetwork;
import ru.falseteam.neural2048.nn.Neuron;
import ru.falseteam.neural2048.nn.ThresholdFunction;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

@XmlRootElement
public class GeneticNeuralNetwork extends NeuralNetwork
        implements Chromosome<GeneticNeuralNetwork>, Cloneable {

    private static double weightsMutationInterval = 1;

    private static double neuronParamsMutationInterval = 1;

    @XmlTransient
    private Random random = new Random();

    public GeneticNeuralNetwork() {
        // Required by JAXB
    }

    public GeneticNeuralNetwork(int numberOfNeurons) {
        super(numberOfNeurons);
    }

    public GeneticNeuralNetwork(NeuralNetwork nn) {
        this.activationIterations = nn.getActivationIterations();
        this.neurons = nn.getNeurons();
        this.neuronsLinks = nn.getNeuronsLinks();
    }

    @Override
    public List<GeneticNeuralNetwork> crossover(GeneticNeuralNetwork anotherChromosome) {
        GeneticNeuralNetwork anotherClone = anotherChromosome.clone();
        GeneticNeuralNetwork thisClone = this.clone();

        switch (this.random.nextInt(4)) {
            case 0: {
                List<Double> thisWeights = thisClone.neuronsLinks.getAllWeights();
                List<Double> anotherWeights = anotherClone.neuronsLinks.getAllWeights();
                this.twoPointsWeightsCrossover(thisWeights, anotherWeights);
                thisClone.neuronsLinks.setAllWeights(thisWeights);
                anotherClone.neuronsLinks.setAllWeights(anotherWeights);
            }
            break;
            case 1: {
                List<Double> thisWeights = thisClone.neuronsLinks.getAllWeights();
                List<Double> anotherWeights = anotherClone.neuronsLinks.getAllWeights();
                this.uniformelyDistributedWeightsCrossover(thisWeights, anotherWeights);
                thisClone.neuronsLinks.setAllWeights(thisWeights);
                anotherClone.neuronsLinks.setAllWeights(anotherWeights);
            }
            break;
            case 2: {
                this.twoPointsNeuronsCrossover(thisClone.neurons, anotherClone.neurons);
            }
            break;
            case 3: {
                this.uniformelyDistributedNeuronsCrossover(thisClone.neurons, anotherClone.neurons);
            }
            break;
            // TODO
            // case 4: {
            // this.activationIterations += this.random.nextInt(2) -
            // this.random.nextInt(2);
            // this.activationIterations = (this.activationIterations < 1) ? 1 :
            // this.activationIterations;
            // }
            // break;
        }

        List<GeneticNeuralNetwork> ret = new ArrayList<GeneticNeuralNetwork>();
        ret.add(anotherClone);
        ret.add(thisClone);
        ret.add(anotherClone.mutate());
        ret.add(thisClone.mutate());
        return ret;
    }

    private void twoPointsWeightsCrossover(List<Double> thisWeights, List<Double> anotherWeights) {
        int left = this.random.nextInt(thisWeights.size());
        int right = this.random.nextInt(thisWeights.size());
        if (left > right) {
            int tmp = right;
            right = left;
            left = tmp;
        }
        for (int i = left; i < right; i++) {
            double thisWeight = anotherWeights.get(i);
            thisWeights.set(i, anotherWeights.get(i));
            anotherWeights.set(i, thisWeight);
        }
    }

    private void uniformelyDistributedWeightsCrossover(List<Double> thisWeights, List<Double> anotherWeights) {
        int weightsSize = thisWeights.size();
        int itersCount = this.random.nextInt(weightsSize);
        if (itersCount == 0) {
            itersCount = 1;
        }
        Set<Integer> used = new HashSet<Integer>();
        for (int iter = 0; iter < itersCount; iter++) {
            int i = this.random.nextInt(weightsSize);
            if (weightsSize > 1) {
                while (used.contains(i)) {
                    i = this.random.nextInt(weightsSize);
                }
            }
            double thisWeight = thisWeights.get(i);
            double anotherWeight = anotherWeights.get(i);

            anotherWeights.set(i, thisWeight);
            thisWeights.set(i, anotherWeight);
            used.add(i);
        }
    }

    private void twoPointsNeuronsCrossover(List<Neuron> thisNeurons, List<Neuron> anotherNeurons) {
        int left = this.random.nextInt(thisNeurons.size());
        int right = this.random.nextInt(thisNeurons.size());
        if (left > right) {
            int tmp = right;
            right = left;
            left = tmp;
        }
        for (int i = left; i < right; i++) {
            Neuron thisNeuron = thisNeurons.get(i);
            thisNeurons.set(i, anotherNeurons.get(i));
            anotherNeurons.set(i, thisNeuron);
        }
    }

    private void uniformelyDistributedNeuronsCrossover(List<Neuron> thisNeurons, List<Neuron> anotherNeurons) {
        int neuronsSize = thisNeurons.size();
        int itersCount = this.random.nextInt(neuronsSize);
        if (itersCount == 0) {
            itersCount = 1;
        }
        Set<Integer> used = new HashSet<Integer>();
        for (int iter = 0; iter < itersCount; iter++) {
            int i = this.random.nextInt(neuronsSize);
            if (neuronsSize > 1) {
                while (used.contains(i)) {
                    i = this.random.nextInt(neuronsSize);
                }
            }
            Neuron thisNeuron = thisNeurons.get(i);
            Neuron anotherNeuron = anotherNeurons.get(i);

            anotherNeurons.set(i, thisNeuron);
            thisNeurons.set(i, anotherNeuron);
            used.add(i);
        }
    }

    //@Override //TODO
    public GeneticNeuralNetwork mutate() {
        GeneticNeuralNetwork mutated = this.clone();

        switch (this.random.nextInt(4)) {
            case 0: {
                List<Double> weights = mutated.neuronsLinks.getAllWeights();
                this.mutateWeights(weights);
                mutated.neuronsLinks.setAllWeights(weights);
            }
            break;
            case 1: {
                this.mutateNeuronsFunctionsParams(mutated.neurons);
            }
            break;
            case 2: {
                //this.mutateChangeNeuronsFunctions(mutated.neurons);//TODO
            }
            //break;
            case 3: {
                List<Double> weights = mutated.neuronsLinks.getAllWeights();
                this.shuffleWeightsOnSubinterval(weights);
                mutated.neuronsLinks.setAllWeights(weights);
            }
            break;
        }

        return mutated;
    }

    private void mutateWeights(List<Double> weights) {
        int weightsSize = weights.size();
        int itersCount = this.random.nextInt(weightsSize);
        if (itersCount == 0) {
            itersCount = 1;
        }
        Set<Integer> used = new HashSet<Integer>();
        for (int iter = 0; iter < itersCount; iter++) {
            int i = this.random.nextInt(weightsSize);
            if (weightsSize > 1) {
                while (used.contains(i)) {
                    i = this.random.nextInt(weightsSize);
                }
            }
            double w = weights.get(i);
            w += (this.random.nextGaussian() - this.random.nextGaussian()) * weightsMutationInterval;
            // w += (this.random.nextDouble() - this.random.nextDouble()) *
            // weightsMutationInterval;
            weights.set(i, w);
            used.add(i);
        }
    }

    private void mutateNeuronsFunctionsParams(List<Neuron> neurons) {
        int neuronsSize = neurons.size();
        int itersCount = this.random.nextInt(neuronsSize);
        if (itersCount == 0) {
            itersCount = 1;
        }
        Set<Integer> used = new HashSet<Integer>();
        for (int iter = 0; iter < itersCount; iter++) {
            int i = this.random.nextInt(neuronsSize);
            if (neuronsSize > 1) {
                while (used.contains(i)) {
                    i = this.random.nextInt(neuronsSize);
                }
            }
            Neuron n = neurons.get(i);

            List<Double> params = n.getParams();
            for (int j = 0; j < params.size(); j++) {
                double param = params.get(j);
                param += (this.random.nextGaussian() - this.random.nextGaussian()) * neuronParamsMutationInterval;
                // param += (this.random.nextDouble() -
                // this.random.nextDouble()) * neuronParamsMutationInterval;
                params.set(j, param);
            }
            n.setFunctionAndParams(n.getFunction(), params);
            used.add(i);
        }
    }

    private void mutateChangeNeuronsFunctions(List<Neuron> neurons) {
        int neuronsSize = neurons.size();
        int itersCount = this.random.nextInt(neuronsSize);
        if (itersCount == 0) {
            itersCount = 1;
        }
        Set<Integer> used = new HashSet<Integer>();
        for (int iter = 0; iter < itersCount; iter++) {
            int i = this.random.nextInt(neuronsSize);
            if (neuronsSize > 1) {
                while (used.contains(i)) {
                    i = this.random.nextInt(neuronsSize);
                }
            }
            Neuron n = neurons.get(i);
            ThresholdFunction f = ThresholdFunction.getRandomFunction();
            n.setFunctionAndParams(f, f.getDefaultParams());
            used.add(i);
        }
    }

    private void shuffleWeightsOnSubinterval(List<Double> weights) {
        int left = this.random.nextInt(weights.size());
        int right = this.random.nextInt(weights.size());
        if (left > right) {
            int tmp = right;
            right = left;
            left = tmp;
        }
        List<Double> subListOfWeights = new ArrayList<Double>((right - left) + 1);
        for (int i = 0; i < ((right - left) + 1); i++) {
            subListOfWeights.add(weights.get(left + i));
        }
        Collections.shuffle(subListOfWeights);
        for (int i = 0; i < ((right - left) + 1); i++) {
            weights.set(left + i, subListOfWeights.get(i));
        }
    }

    @Override
    public GeneticNeuralNetwork clone() {
        GeneticNeuralNetwork clone = new GeneticNeuralNetwork(this.neurons.size());
        clone.neuronsLinks = this.neuronsLinks.clone();
        clone.activationIterations = this.activationIterations;
        clone.neurons = new ArrayList<Neuron>(this.neurons.size());
        for (Neuron neuron : this.neurons) {
            clone.neurons.add(neuron.clone());
        }
        return clone;
    }
}
