/**
 * 
 */
package main.edu.neu.coe.info6205.sudoku;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gaurang Davda
 *
 */
public class GeneticOperations {

	Random rnd = new Random();

	/**
	 * This method returns the best chromosome from the population.
	 * 
	 * @param chromosomeArr
	 * @return
	 */
	public Chromosome bestChromosome(Chromosome[] chromosomeArr) {
		sort(chromosomeArr);
		return chromosomeArr[0];
	}

	/**
	 * This method performs either crossover or mutation randomly. It generates
	 * 60% of the population which was culled using the 40% of the survivor
	 * population.
	 * 
	 * @param chromosomeArr
	 * @param populationCount
	 */
	public void performCrossover(Chromosome[] chromosomeArr, int populationCount) {

		try {
			int size = calculateActualArrayLength(chromosomeArr);
			for (int i = 0; i < size; i += 2) {
				if (rnd.nextBoolean()) {
					crossover(chromosomeArr[i], chromosomeArr[i + 1], chromosomeArr, size + i);
				} else {
					performMutation(chromosomeArr, i, size);
				}
			}
			sort(chromosomeArr);
			size = calculateActualArrayLength(chromosomeArr);
			for (int i = 0; i < populationCount - size; i += 2) {
				if (rnd.nextBoolean()) {
					crossover(chromosomeArr[i], chromosomeArr[i + 1], chromosomeArr, size + i);
				} else {
					performMutation(chromosomeArr, i, size);
				}

			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while creating new generations.", e);
		}

	}

	/**
	 * This method performs multi-point crossover between 2 chromosomes and
	 * generates 2 new chromosomes.
	 * 
	 * @param c1
	 * @param c2
	 * @param chromoArr
	 * @param index
	 */
	private void crossover(Chromosome c1, Chromosome c2, Chromosome[] chromoArr, int index) {
		try {
			int[] gene1 = c1.getGene();
			int[] gene2 = c2.getGene();
			int[] newGene1 = gene1.clone();
			int[] newGene2 = gene2.clone();
			int first = rnd.nextInt(gene1.length);
			int second = rnd.nextInt(gene1.length - first) + first;
			for (int i = 0; i < gene1.length; i++) {
				newGene1[i] = (i >= first && i <= second) ? gene2[i] : gene1[i];
				newGene2[i] = (i >= first && i <= second) ? gene1[i] : gene2[i];
			}
			chromoArr[index] = new Chromosome(c1.getBaseGene(), newGene1);
			chromoArr[index + 1] = new Chromosome(c2.getBaseGene(), newGene2);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while performing crossover", e);
		}
	}

	/**
	 * This method culls 60% of the population which are not fit for crossover
	 * or mutation.
	 * 
	 * @param chromosomeArr
	 */
	public void cullLessFitChromosomes(Chromosome[] chromosomeArr) {
		try {
			int size = calculateActualArrayLength(chromosomeArr);
			for (int i = size - 1; i >= size * 2 / 5; i--) {
				chromosomeArr[i] = null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while culling the population.", e);
		}
	}

	/**
	 * This method sorts the chromosome array using merge sort algorithm.
	 * 
	 * @param a
	 */
	public void sort(Chromosome[] a) {
		try {
			logger.log(Level.FINE, "Performing merge sort operation on the population");
			int size = calculateActualArrayLength(a);
			Chromosome[] aux = new Chromosome[size];
			sort(a, aux, 0, size - 1);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while sorting", e);
		}
	}

	/**
	 * This method sorts the array by comparisons and merging of the split
	 * arrays.
	 * 
	 * @param a
	 * @param aux
	 * @param lo
	 * @param mid
	 * @param hi
	 */
	private void merge(Chromosome[] a, Chromosome[] aux, int lo, int mid, int hi) {
		for (int k = lo; k <= hi; k++)
			aux[k] = a[k];
		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				a[k] = aux[j++];
			else if (j > hi)
				a[k] = aux[i++];
			else if (aux[j].compareTo(aux[i]) < 0)
				a[k] = aux[j++];
			else
				a[k] = aux[i++];
		}
	}

	/**
	 * This method recursively calls itself and then merges the split array to
	 * sort it.
	 * 
	 * @param a
	 * @param aux
	 * @param lo
	 * @param hi
	 */
	private void sort(Chromosome[] a, Chromosome[] aux, int lo, int hi) {
		if (hi <= lo)
			return;
		int mid = lo + (hi - lo) / 2;
		sort(a, aux, lo, mid);
		sort(a, aux, mid + 1, hi);
		merge(a, aux, lo, mid, hi);
	}

	/**
	 * This method performs a mutation of the gene by assigning a random value
	 * to 3 random bits of the gene. It mutates 2 chromosomes and generates 2
	 * new chromosomes.
	 * 
	 * @param chromosomeArr
	 * @param j
	 * @param length
	 */
	private void performMutation(Chromosome[] chromosomeArr, int j, int length) {

		try {
			logger.log(Level.FINE, "Performing mutation.");
			for (int i = 0; i < 2; i++) {
				Chromosome chromo = chromosomeArr[j + i];
				int[] gene = chromo.getGene();
				int[] newGene = Arrays.copyOf(gene, gene.length);
				for (int k = 0; k < 3; k++) {
					int index = 0;
					do {
						index = rnd.nextInt(gene.length);
					} while (chromo.getBaseGene()[index] != 0);
					newGene[index] = rnd.nextInt((int) Math.sqrt(gene.length)) + 1;
				}
				Chromosome newChromo = new Chromosome(chromo.getBaseGene(), newGene);
				chromosomeArr[length + j + i] = newChromo;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while performing mutation", e);
		}
	}

	/**
	 * This method eliminates the Chromosomes with null values from the array
	 * length and returns the actual length
	 * 
	 * @param chromosomeArr
	 * @return
	 */
	private int calculateActualArrayLength(Chromosome[] chromosomeArr) {

		int count = 0;
		for (int i = 0; i < chromosomeArr.length; i++) {
			if (chromosomeArr[i] != null) {
				count++;
			}
		}
		return count;
	}

	/**
	 * This attribute is used for logging
	 */
	private final Logger logger = Logger.getLogger(GeneticOperations.class.getName());
}
