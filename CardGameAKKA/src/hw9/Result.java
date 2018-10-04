package hw9;

/**
 * 
 * @author Gaurang Davda
 *
 */
public class Result {

	private String playerName;
	private int playerPoints;

	public Result(String playerName, int playerPoints) {
		this.playerName = playerName;
		this.playerPoints = playerPoints;
	}

	public String toString() {
		return playerName + " is the winner with " + playerPoints + " points.";
	}

}