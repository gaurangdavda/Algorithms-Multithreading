package hw7;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This file needs to hold your solver to be tested. You can alter the class to
 * extend any class that extends PuzzleSolver. It must have a constructor that
 * takes in a Puzzle. It must have a solve() method that returns the datatype
 * List<Direction> which will either be a reference to a list of steps to take
 * or will be null if the puzzle cannot be solved.
 */
public class StudentMultiPuzzleSolver extends SkippingPuzzleSolver {
	public ExecutorService executor;

	public StudentMultiPuzzleSolver(Puzzle puzzle) {
		super(puzzle);
	}

	public List<Direction> solve() {
		// TODO: Implement your code here
		LinkedList<DepthFirstSearch> tasks = new LinkedList<DepthFirstSearch>();
		List<Future<List<Direction>>> futureList = new LinkedList<Future<List<Direction>>>();
		List<Direction> result = null;
		executor = Executors.newCachedThreadPool();
		// executor = Executors.newFixedThreadPool(10);
		long finalCount = 0;
		try {
			Choice start = firstChoice(puzzle.getStart());

			int size = start.choices.size();
			for (int index = 0; index < size; index++) {
				Choice currentChoice = follow(start.at, start.choices.peek());
				tasks.add(new DepthFirstSearch(currentChoice, start.choices.pop()));
			}
		} catch (SolutionFound e) {
			e.printStackTrace();
		}
		try {
			futureList = executor.invokeAll(tasks);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		executor.shutdown();
		for (Future<List<Direction>> future : futureList) {
			try {
				if (future.get() != null) {
					result = future.get();
				}
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			} catch (ExecutionException ee) {
				ee.printStackTrace();
			}
		}
		for (DepthFirstSearch dfs : tasks) {
			finalCount += dfs.count;
		}
		System.out.println("Total Choices made are " + (finalCount - 1));
		if (result != null)
			System.out.println("Length of the path is  " + result.size());
		return result;
	}

	private class DepthFirstSearch implements Callable<List<Direction>> {
		private Choice head;
		private Direction choiceDirection;
		private long count = 0;

		public DepthFirstSearch(Choice head, Direction choiceDir) {
			this.head = head;
			this.choiceDirection = choiceDir;

		}

		@Override
		public List<Direction> call() {
			LinkedList<Choice> choiceStack = new LinkedList<Choice>();
			Choice currentChoice;
			try {
				choiceStack.push(this.head);
				while (!choiceStack.isEmpty()) {
					currentChoice = choiceStack.peek();
					count++;
					if (currentChoice.isDeadend()) {
						choiceStack.pop();
						if (!choiceStack.isEmpty()) {
							Choice newChoice = choiceStack.peek();
							newChoice.choices.pop();
						}
						continue;
					}
					Choice nextChoice = follow(currentChoice.at, currentChoice.choices.peek());
					choiceStack.push(nextChoice);
				}
				return null;
			} catch (SolutionFound e) {
				Iterator<Choice> iterator = choiceStack.iterator();
				LinkedList<Direction> solutionPath = new LinkedList<Direction>();
				while (iterator.hasNext()) {
					currentChoice = iterator.next();
					solutionPath.push(currentChoice.choices.peek());
				}
				solutionPath.push(choiceDirection);
				if (puzzle.display != null) {
					puzzle.display.updateDisplay();
				}
				return pathToFullPath(solutionPath);
			}
		}
	}
}
