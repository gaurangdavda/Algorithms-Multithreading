package hw6;

import java.util.List;

/**
 * @Invariant i) Puzzle should not be more than 20000*20000 cells
 * 			 ii) Puzzle must return maximum 1 solution
 *          iii) Puzzles should not contain cycles and/or loops
 */
/**
 * This file needs to hold your solver to be tested. 
 * You can alter the class to extend any class that extends PuzzleSolver.
 * It must have a constructor that takes in a Puzzle.
 * It must have a solve() method that returns the datatype List<Direction>
 *   which will either be a reference to a list of steps to take or will
 *   be null if the puzzle cannot be solved.
 */
public class StudentMultiPuzzleSolver extends SkippingPuzzleSolver
{
    public StudentMultiPuzzleSolver(Puzzle puzzle)
    {
        super(puzzle);
    }

    /**
     * @Invariant - Newest is a subset of Nodes
     * @Precondition - Puzzle should not be null
     * @Postcondition - Puzzle returns maximum 1 solution
     * @exception i) InterruptedException if the thread is interrupted
     * 			 ii) NullPointerException
     */
    public List<Direction> solve()
    {
        // TODO: Implement your code here
    	/*
    	 * ******************************************************************
    	 * Pseudocode
    	 * ******************************************************************
    	 * I will use Depth first search to find the shortest path.
    	 * The efficiency of both BFS and DFS is same for calculating the shortest path. 
    	 * The difference is that DFS returns the path but BFS returns the shortest path. Since there is atmosst 1 solution,
    	 * the path length doesn't matter. 
    	 * Also BFS consumes more memory as compared to DFS
    	 * 
    	 * 1: Create a LinkedList for path
    	 * 2: Create a LinkedList for choiceStack. It contains the choices available from a position.
    	 * 	  Try starts
    	 * 3: Get the available choices from firstChoice(puzzle.getStart())
    	 * 4: Push it in the choiceStack
    	 * 5: Create an executor service.
    	 * 6: Loop the choices using for loop 
    	 * 		i) Get the currentChoice
    	 * 	   ii) Invoke DFS on currentChoice and add them to the list pathToRun 
    	 * 	  End Loop
    	 * 7: Invoke all the choices in the pathToRun list using multiple threads
    	 * 8: Use future and the get the path
    	 * 9: DFS implements the Callable function 
    	 * 		i) Check the  choicePosition and path 
    	 * 	   ii) Loop till choiceStack is empty
    	 *    iii) If dead-end reached 
    	 * 			   Backtrack the path
    	 * 			   Throw the solution found excecption
    	 * 		   else
    	 * 			   Use the current position and get new path choices
    	 * 	  End Loop
    	 * 	  Catch Exception
    	 * 		 return the path
    	 */
        throw new RuntimeException("Not yet implemented!");
    }
}
