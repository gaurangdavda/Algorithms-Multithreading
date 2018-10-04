package hw9;

import java.util.List;

/**
 * 
 * @author Gaurang Davda
 *
 */
public class Messages {

	public static class StartGame {

	}

	public static class PlayerValuation {

		final private int player1Points;
		final private String player1Name;

		final private int player2Points;
		final private String player2Name;
		final private List<Card> deck1;
		final private List<Card> deck2;

		public PlayerValuation(String playerName1, int playerPoints1, String playerName2, int playerPoints2, List<Card> deck1,
				List<Card> deck2) {
			this.player1Name = playerName1;
			this.player1Points = playerPoints1;
			this.player2Name = playerName2;
			this.player2Points = playerPoints2;
			this.deck1 = deck1;
			this.deck2 = deck2;
		}

		public List<Card> getDeck1() {
			return deck1;
		}

		public List<Card> getDeck2() {
			return deck2;
		}

		public int getPlayerPoints1() {
			return player1Points;
		}

		public String getPlayerName1() {
			return player1Name;
		}

		public int getPlayerPoints2() {
			return player2Points;
		}

		public String getPlayerName2() {
			return player2Name;
		}

	}

}