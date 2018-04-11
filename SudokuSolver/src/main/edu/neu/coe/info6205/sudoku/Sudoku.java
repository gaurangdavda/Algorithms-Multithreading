/**
 * 
 */
package main.edu.neu.coe.info6205.sudoku;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gaurang Davda
 *
 */
public class Sudoku {

	/**
	 * This method solves a 4*4, 6*6 and 9*9 Sudoku using genetic algorithm.
	 * 
	 * @param input
	 * @param populationCount
	 * @return the best chromosome
	 * @throws Exception
	 */
	public Chromosome solveSudoku(int[] input, int populationCount) throws Exception {

		int generation = 1;
		int[] baseGene = input;
		GeneticOperations geneticOperations = new GeneticOperations();
		Chromosome c = null;
		try {
			logger.log(Level.INFO, "Initializing Chromosome and adding it to the population.");
			Population population = new Population(populationCount);
			for (int i = 0; i < populationCount; i++) {
				Chromosome chromosome = new Chromosome(baseGene);
				population.getChromosomeArr()[i] = chromosome;
			}
			c = geneticOperations.bestChromosome(population.getChromosomeArr());
			while (c.getFitness() != 0) {
				logger.log(Level.INFO, "Culling 60% of the population with worst fitness(Highest fitness value).");
				geneticOperations.cullLessFitChromosomes(population.getChromosomeArr());
				logger.log(Level.INFO,
						"Performing crossover and mutation from the best 40% of the population and generating the remaining 60%");
				geneticOperations.performCrossover(population.getChromosomeArr(), populationCount);
				logger.log(Level.INFO, "Assigning the best chromosome from the population.");
				c = geneticOperations.bestChromosome(population.getChromosomeArr());
				generation++;
				logger.log(Level.INFO, "Fitness = " + c.getFitness() + ". Generation = " + generation);
			}
			System.out.println(c);
			System.out.println("Generation " + generation);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in solving Sudoku.", e);
			throw new Exception(e);
		}
		return c;
	}

	/**
	 * This attribute is used for logging
	 */
	private final Logger logger = Logger.getLogger(Sudoku.class.getName());
}
