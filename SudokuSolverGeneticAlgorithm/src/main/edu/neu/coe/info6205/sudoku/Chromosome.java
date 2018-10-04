/**
 * 
 */
package main.edu.neu.coe.info6205.sudoku;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gaurang Davda
 *
 */
public class Chromosome implements Comparable<Chromosome> {

	Random rnd = new Random();

	/**
	 * This is the parameterized constructor with the input to sudoku as the
	 * parameter
	 * 
	 * @param baseGene
	 * @throws Exception
	 */
	public Chromosome(int[] baseGene) throws Exception {

		validateBaseGene(baseGene);
		this.baseGene = baseGene;
		gene = randomizeBaseGenes(baseGene.clone());
		size = gene.length;
		calculateFitness();
	}

	/**
	 * This is the parameterized constructor with the input to sudoku as the
	 * parameter
	 * 
	 * @param baseGene
	 * @param gene
	 */
	public Chromosome(int[] baseGene, int[] gene) {

		this.baseGene = baseGene;
		this.gene = gene;
		size = gene.length;
		calculateFitness();
	}

	/**
	 * This method is used to compare the fitness of 2 chromosomes
	 */
	@Override
	public int compareTo(Chromosome o) {

		return getFitness() - o.getFitness();
	}

	@Override
	public String toString() {
		String string = "Gene: ";
		int dimensions = (int) Math.sqrt(gene.length);
		for (int i : gene) {
			string += i;
		}
		for (int i = 0; i < gene.length; i++)
			string += ((i % dimensions == 0) ? "\n" : "") + gene[i] + " ";
		string += "\nFitness: " + fitness;

		return string;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the baseGene
	 */
	public int[] getBaseGene() {
		return baseGene;
	}

	/**
	 * @param baseGene
	 *            the baseGene to set
	 */
	public void setBaseGene(int[] baseGene) {
		this.baseGene = baseGene;
	}

	/**
	 * @return the gene
	 */
	public int[] getGene() {
		return gene;
	}

	/**
	 * @param gene
	 *            the gene to set
	 */
	public void setGene(int[] gene) {
		this.gene = gene;
	}

	/**
	 * @return the fitness
	 */
	public int getFitness() {
		return fitness;
	}

	/**
	 * @param fitness
	 *            the fitness to set
	 */
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	/**
	 * This method is used to validate the input base gene provided to the
	 * sudoku.
	 * 
	 * @param baseGene
	 * @throws Exception
	 */
	private void validateBaseGene(int[] baseGene) throws Exception {
		int root = (int) Math.sqrt(baseGene.length);
		for (int i : baseGene) {
			if (i > root) {
				throw new Exception("The input sudoku is wrong");
			}
		}
	}

	/**
	 * This method puts random numbers in place of zeros in the input gene
	 * 
	 * @param gene
	 * @return gene
	 */
	private int[] randomizeBaseGenes(int[] gene) {

		for (int i = 0; i < gene.length; i++) {
			if (gene[i] == 0) {
				gene[i] = rnd.nextInt((int) Math.sqrt(gene.length)) + 1;
			}
		}
		return gene;
	}

	/**
	 * This method calculates the fitness of this sudoku based on the 3
	 * traits(1. The rule of the row. 2. The rule of the column. 3. The rule of
	 * the individual block.)
	 * 
	 * @return the caluculated value of fitness of this sudoku.
	 */
	public void calculateFitness() {
		fitness = 0;
		int[][] baseGene2D = convertTo2D(baseGene);
		int[][] gene2D = convertTo2D(gene);
		validateAllBlocks(gene2D);
		validateRowsAndColumns(gene2D, baseGene2D);
	}

	/**
	 * This method validates the uniqueness of numbers in each row and column of
	 * the sudoku
	 * 
	 * @param gene2D
	 * @param baseGene2D
	 */
	private void validateRowsAndColumns(int[][] gene2D, int[][] baseGene2D) {

		try {
			for (int i = 0; i < gene2D.length; i++) {

				boolean[] existFlagRow = new boolean[gene2D.length];
				boolean[] existFlagColumn = new boolean[gene2D.length];
				for (int j = 0; j < gene2D.length; j++) {
					if (gene2D[i][j] == 0 || (baseGene2D[i][j] != 0 && baseGene2D[i][j] != gene2D[i][j])) {
						fitness += 100;
					}
					if (existFlagRow[gene2D[i][j] - 1]) {
						fitness++;
					}
					if (existFlagColumn[gene2D[j][i] - 1]) {
						fitness++;
					}
					existFlagRow[gene2D[i][j] - 1] = true;
					existFlagColumn[gene2D[j][i] - 1] = true;
				}
			}
			logger.log(Level.FINE, "Validated all the rows and columns. Fitness value is " + fitness);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error is validating all the rows and columns.", e);
		}
	}

	/**
	 * This method validates the uniqueness of numbers in each block of the
	 * sudoku
	 * 
	 * @param gene2D
	 */
	private void validateAllBlocks(int[][] gene2D) {
		try {
			int noOfRowsInBlocks = (int) Math.sqrt(gene2D.length);
			int noOfColumnsInBlocks = noOfRowsInBlocks;
			if (gene2D.length == 6) {
				noOfColumnsInBlocks = 3;
			}
			for (int i = 0; i < gene2D.length; i += noOfRowsInBlocks) {
				for (int j = 0; j < gene2D.length; j += noOfColumnsInBlocks) {
					boolean[] blocks = new boolean[gene2D.length];
					for (int k = 0; k < noOfRowsInBlocks; k++) {
						for (int l = 0; l < noOfColumnsInBlocks; l++) {
							if (blocks[gene2D[i + k][j + l] - 1]) {
								fitness++;
							}
							blocks[gene2D[i + k][j + l] - 1] = true;
						}
					}
				}
			}
			logger.log(Level.FINE, "Validated all the Sudoku squares. Fitness value is " + fitness);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error is validating all the Sudoku squares.", e);
		}
	}

	/**
	 * This method converts a one dimensional array to two dimensional array
	 * 
	 * @param input
	 * @return output
	 */
	private int[][] convertTo2D(int[] input) {
		int arrSize2D = (int) Math.sqrt(input.length);
		int[][] output = new int[arrSize2D][arrSize2D];
		for (int i = 0; i < arrSize2D; i++) {
			for (int j = 0; j < arrSize2D; j++) {
				output[i][j] = input[i * arrSize2D + j];
			}
		}
		return output;
	}

	/**
	 * This is the size of the genes
	 */
	private int size;
	/**
	 * This is the input of the sudoku
	 */
	private int[] baseGene;
	/**
	 * This is the current sudoku with randomized elements in places of zeros in
	 * base gene
	 */
	private int[] gene;
	/**
	 * This attribute defines the fitness of the sudoku. The lower value it has,
	 * fitter are the sudoku genes. The sudoku solution will have 0 fitness.
	 */
	private int fitness;
	/**
	 * This attribute is used for logging
	 */
	private final Logger logger = Logger.getLogger(Chromosome.class.getName());

}
