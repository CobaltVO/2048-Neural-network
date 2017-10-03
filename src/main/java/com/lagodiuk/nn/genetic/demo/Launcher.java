/*******************************************************************************
 * Copyright 2012 Yuriy Lagodiuk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.lagodiuk.nn.genetic.demo;

import ru.falseteam.neural2048.ga.Fitness;
import com.lagodiuk.ga.GeneticAlgorithm;
import ru.falseteam.neural2048.ga.IterationListener;
import com.lagodiuk.ga.Population;
import ru.falseteam.neural2048.nn.ThresholdFunction;
import com.lagodiuk.nn.genetic.GeneticNeuralNetwork;

import java.util.Random;

public class Launcher {

	private static final int maxWeightNum = 10;

	public static void main(String[] args) {
		Population<GeneticNeuralNetwork> population = new Population<GeneticNeuralNetwork>();
		GeneticNeuralNetwork nn = initilNeuralNetwork();
		for (int i = 0; i < 20; i++) {
			population.addChromosome(nn.mutate());
		}

		Fitness<GeneticNeuralNetwork, Double> fit = new Fitness<GeneticNeuralNetwork, Double>() {
			@Override
			public Double calculate(GeneticNeuralNetwork nn) {
				double delt = 0;
				for (int i = -5; i < 6; i++) {
					for (int j = -5; j < 6; j++) {
						double target;
						if (i == j) {
							target = 0;
						} else {
							target = 1;
						}

						nn.putSignalToNeuron(0, i);
						nn.putSignalToNeuron(1, j);

						nn.activate();

						double nnOutput = nn.getAfterActivationSignal(5);

						double d = nnOutput - target;
						delt += d * d;
					}
				}
				return delt;
			}
		};

		GeneticAlgorithm<GeneticNeuralNetwork, Double> env =
				new GeneticAlgorithm<GeneticNeuralNetwork, Double>(population, fit);

		env.addIterationListener(new IterationListener<GeneticNeuralNetwork, Double>() {
			private Random random = new Random();

			@Override
			public void update(GeneticAlgorithm<GeneticNeuralNetwork, Double> environment) {
				GeneticNeuralNetwork gene = environment.getBest();
				Double d = environment.fitness(gene);
				System.out.println(environment.getIteration() + "\t" + d);

				if (d <= 0.1) {
					environment.terminate();
				}

				environment.setParentChromosomesSurviveCount(this.random.nextInt(environment.getPopulation().getSize()));
			}
		});

		env.evolve(5500);

		GeneticNeuralNetwork evoNn = env.getBest();
		for (int i = -10; i < -6; i++) {
			System.out.println();
			for (int j = -10; j < -6; j++) {
				evoNn.putSignalToNeuron(0, i);
				evoNn.putSignalToNeuron(1, j);
				evoNn.activate();
				System.out.println(i + " XOR " + j + " = " + evoNn.getAfterActivationSignal(5));
			}
		}
	}

	private static GeneticNeuralNetwork initilNeuralNetwork() {
		GeneticNeuralNetwork nn = new GeneticNeuralNetwork(6);
		for (int i = 0; i < 6; i++) {
			ThresholdFunction f = ThresholdFunction.getRandomFunction();
			nn.setNeuronFunction(i, f, f.getDefaultParams());
		}
		nn.setNeuronFunction(0, ThresholdFunction.LINEAR, ThresholdFunction.LINEAR.getDefaultParams());
		nn.setNeuronFunction(1, ThresholdFunction.LINEAR, ThresholdFunction.LINEAR.getDefaultParams());

		Random rnd = new Random();
		nn.addLink(0, 2, getRandomWeight(rnd));
		nn.addLink(0, 3, getRandomWeight(rnd));
		nn.addLink(0, 4, getRandomWeight(rnd));
		nn.addLink(1, 2, getRandomWeight(rnd));
		nn.addLink(1, 3, getRandomWeight(rnd));
		nn.addLink(1, 4, getRandomWeight(rnd));
		nn.addLink(2, 5, getRandomWeight(rnd));
		nn.addLink(3, 5, getRandomWeight(rnd));
		nn.addLink(4, 5, getRandomWeight(rnd));
		return nn;
	}

	private static int getRandomWeight(Random random) {
		return random.nextInt(maxWeightNum) - random.nextInt(maxWeightNum);
	}

}
