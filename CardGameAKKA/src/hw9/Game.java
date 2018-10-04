package hw9;

import java.util.LinkedList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import hw9.Messages.PlayerValuation;

/**
 * 
 * @author Gaurang Davda
 *
 */
public class Game extends UntypedActor {

	private static ActorRef deckSender = null;

	@Override
	public void onReceive(Object deck) throws Exception {

		List<Card> cardDeck = (List<Card>) deck;
		this.deckSender = getSender();

		LinkedList<Card> deck1 = new LinkedList<Card>();
		LinkedList<Card> deck2 = new LinkedList<Card>();

		int player1Points = 0;
		int player2Points = 0;
		deck1.addAll(cardDeck.subList(0, 26));
		deck2.addAll(cardDeck.subList(26, cardDeck.size()));

		for (int i = 0; i < cardDeck.size() / 2; i++) {
			Card p1Card = deck1.pop();
			Card p2Card = deck2.pop();
			System.out.println("-------------------------");
			System.out.println("Round " + (i + 1));
			System.out.println("-------------------------");
			System.out.println("Player 1 : Card is " + p1Card.toString());
			System.out.println("Player 2 : Card is " + p2Card.toString());

			if (p1Card.getCard() > p2Card.getCard()) {
				deck1.addLast(p1Card);
				deck1.addLast(p2Card);
				System.out.println("PLayer 1 wins the round");
			} else if (p1Card.getCard() < p2Card.getCard()) {
				deck2.addLast(p1Card);
				deck2.addLast(p2Card);
				System.out.println("PLayer 2 wins the round");
			} else {
				System.out.println("Same card rank. Considering suit.");
				int p1Points = p1Card.getCard() + p1Card.getSuit();
				int p2Points = p2Card.getCard() + p2Card.getSuit();

				if (p1Points < p2Points) {
					deck1.addLast(p1Card);
					deck1.addLast(p2Card);
					System.out.println("PLayer 1 wins the round");
				}

				else {// if player 2 win
					deck2.addLast(p1Card);
					deck2.addLast(p2Card);
					System.out.println("PLayer 2 wins the round");
				}
			}
		}

		for (Card card : deck1) {
			player1Points += card.getCard();
		}
		for (Card card : deck2) {
			player2Points += card.getCard();
		}
		System.out.println("-------------------------");
		System.out.println("Game Over");
		System.out.println("-------------------------");
		System.out.println("Total Player 1 points:" + player1Points);
		System.out.println("Total Player 2 points:" + player2Points);
		deckSender.tell(new PlayerValuation("Player 1", player1Points, "Player 2", player2Points, deck1, deck2),
				getSelf());
	}

}