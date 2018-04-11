/**
 * 
 */
package main.edu.neu.coe.info6205.sudoku;

/**
 * @author Gaurang Davda
 *
 */
public class Population {

	public Population(int populationNumber) {
		count = populationNumber;
		chromosomeArr = new Chromosome[count];
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the chromosomeArr
	 */
	public Chromosome[] getChromosomeArr() {
		return chromosomeArr;
	}

	/**
	 * @param chromosomeArr
	 *            the chromosomeArr to set
	 */
	public void setChromosomeArr(Chromosome[] chromosomeArr) {
		this.chromosomeArr = chromosomeArr;
	}

	/**
	 * This is the population count
	 */
	private int count;

	/**
	 * This is a gene pool
	 */
	private Chromosome[] chromosomeArr;
}
