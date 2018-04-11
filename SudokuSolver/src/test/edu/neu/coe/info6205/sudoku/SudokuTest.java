/**
 * 
 */
package test.edu.neu.coe.info6205.sudoku;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import main.edu.neu.coe.info6205.sudoku.Chromosome;
import main.edu.neu.coe.info6205.sudoku.GeneticOperations;
import main.edu.neu.coe.info6205.sudoku.Sudoku;

/**
 * @author Gaurang Davda
 *
 */
public class SudokuTest {

	@Test
	public void testSorting() {
		try {
			int[] baseGene = new int[] { 0, 2, 0, 0, 4, 0, 0, 0, 0, 1, 0, 3, 0, 0, 1, 0 };
			int populationCount = 10;
			Chromosome[] chromosomeArr = new Chromosome[populationCount];
			for (int i = 0; i < populationCount; i++) {
				chromosomeArr[i] = new Chromosome(baseGene);
			}
			Chromosome[] newChromosomeArr = new Chromosome[populationCount];
			newChromosomeArr = Arrays.copyOf(chromosomeArr, populationCount);
			GeneticOperations go = new GeneticOperations();
			go.sort(chromosomeArr);
			Arrays.sort(newChromosomeArr);
			for (int i = 0; i < newChromosomeArr.length; i++) {
				assertEquals(newChromosomeArr[i], chromosomeArr[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIncorrectSudoku() {
		try {
			int[] baseGene = new int[] { 0, 5, 0, 0, 4, 0, 0, 0, 0, 1, 0, 3, 0, 0, 1, 0 };
			Chromosome c1 = new Chromosome(baseGene);
		} catch (Exception e) {
			assertEquals("The input sudoku is wrong", e.getMessage());
		}
	}

	@Test
	public void testSolve44Sudoku1() {
		try {
			int[] baseGene = new int[] { 0, 2, 0, 0, 4, 0, 0, 0, 0, 1, 0, 3, 0, 0, 1, 0 };
			Sudoku s = new Sudoku();
			System.out.println("Solving 4*4 Hard Sudoku.");
			Chromosome result = s.solveSudoku(baseGene, 10000);
			int[] expected = new int[] { 1, 2, 3, 4, 4, 3, 2, 1, 2, 1, 4, 3, 3, 4, 1, 2 };
			for (int i = 0; i < expected.length; i++) {
				assertEquals(expected[i], result.getGene()[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSolve44Sudoku2() {
		try {
			int[] baseGene = new int[] { 3, 0, 0, 0, 0, 2, 3, 0, 0, 0, 1, 0, 0, 0, 0, 4 };
			Sudoku s = new Sudoku();
			System.out.println("Solving 4*4 Easy Sudoku.");
			Chromosome result = s.solveSudoku(baseGene, 10000);
			int[] expected = new int[] { 3, 1, 4, 2, 4, 2, 3, 1, 2, 4, 1, 3, 1, 3, 2, 4 };
			assertArrayEquals(expected, result.getGene());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSolve66SudokuEasy() {
		try {
			int[] baseGene = new int[] { 4, 0, 0, 1, 0, 0, 0, 0, 5, 0, 0, 4, 0, 0, 0, 2, 0, 3, 3, 0, 2, 0, 0, 0, 2, 0,
					0, 3, 0, 0, 0, 0, 6, 0, 0, 2 };
			Sudoku s = new Sudoku();
			System.out.println("Solving 6*6 Easy Sudoku.");
			Chromosome result = s.solveSudoku(baseGene, 200000);
			int[] expected = new int[] { 4, 6, 3, 1, 2, 5, 1, 2, 5, 6, 3, 4, 6, 5, 1, 2, 4, 3, 3, 4, 2, 5, 6, 1, 2, 1,
					4, 3, 5, 6, 5, 3, 6, 4, 1, 2 };
			assertArrayEquals(expected, result.getGene());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSolve66SudokuHard() {
		try {
			int[] baseGene = new int[] { 0, 5, 4, 0, 0, 0, 2, 0, 0, 0, 0, 0, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0, 0,
					0, 0, 0, 5, 0, 0, 0, 3, 4, 0 };
			Sudoku s = new Sudoku();
			System.out.println("Solving 6*6 Hard Sudoku.");
			Chromosome result = s.solveSudoku(baseGene, 500000);
			int[] expected = new int[] { 1, 5, 4, 2, 6, 3, 2, 3, 6, 4, 5, 1, 4, 1, 3, 5, 2, 6, 6, 2, 5, 1, 3, 4, 3, 4,
					2, 6, 1, 5, 5, 6, 1, 3, 4, 2 };
			assertArrayEquals(expected, result.getGene());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSolve99SudokuEasy() {
		try {
			int[] baseGene = new int[] { 0, 4, 9, 7, 2, 0, 1, 5, 0, 0, 0, 0, 3, 4, 0, 0, 6, 0, 5, 0, 0, 0, 0, 8, 0, 4,
					0, 2, 0, 5, 1, 8, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 9, 2, 8, 0, 5, 0, 1, 0, 4, 0,
					0, 0, 0, 7, 0, 5, 0, 0, 7, 9, 0, 0, 0, 0, 7, 8, 0, 5, 1, 3, 9, 0 };
			Sudoku s = new Sudoku();
			System.out.println("Solving 9*9 Easy Sudoku.");
			Chromosome result = s.solveSudoku(baseGene, 2000000);
			int[] expected = new int[] { 3, 4, 9, 7, 2, 6, 1, 5, 8, 1, 8, 7, 3, 4, 5, 2, 6, 9, 5, 2, 6, 9, 1, 8, 7, 4,
					3, 2, 6, 5, 1, 8, 7, 9, 3, 4, 8, 9, 1, 5, 3, 4, 6, 7, 2, 7, 3, 4, 6, 9, 2, 8, 1, 5, 9, 1, 2, 4, 6,
					3, 5, 8, 7, 6, 5, 3, 8, 7, 9, 4, 2, 1, 4, 7, 8, 2, 5, 1, 3, 9, 6 };
			assertArrayEquals(expected, result.getGene());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSolve99SudokuHard() {
		try {
			int[] baseGene = new int[] { 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, 6, 0, 2, 5, 0, 4, 0, 0, 0, 8, 9, 0, 7, 1, 0, 5,
					6, 0, 7, 0, 0, 0, 0, 8, 0, 9, 0, 0, 0, 0, 6, 0, 0, 0, 0, 4, 0, 5, 0, 0, 0, 0, 7, 0, 7, 1, 0, 9, 4,
					0, 2, 8, 0, 0, 0, 3, 0, 2, 6, 0, 4, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0 };
			Sudoku s = new Sudoku();
			System.out.println("Solving 9*9 Hard Sudoku.");
			Chromosome result = s.solveSudoku(baseGene, 5000000);
			int[] expected = new int[] { 5, 4, 1, 6, 9, 3, 7, 2, 8, 3, 6, 7, 2, 5, 8, 4, 9, 1, 2, 8, 9, 4, 7, 1, 3, 5,
					6, 6, 7, 2, 5, 3, 4, 8, 1, 9, 1, 9, 8, 7, 6, 2, 5, 3, 4, 4, 3, 5, 8, 1, 9, 6, 7, 2, 7, 1, 6, 9, 4,
					5, 2, 8, 3, 8, 5, 3, 1, 2, 6, 9, 4, 7, 9, 2, 4, 3, 8, 7, 1, 6, 5 };
			assertArrayEquals(expected, result.getGene());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
