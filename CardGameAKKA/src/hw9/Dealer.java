package hw9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import hw9.Messages.PlayerValuation;
import hw9.Messages.StartGame;

/**
 * 
 * @author Gaurang Davda
 *
 */
public class Dealer extends UntypedActor {

	private static ActorRef sender = null;

	@Override
	public void onReceive(Object msg) throws Exception {

		if (msg instanceof StartGame) {
			this.sender = getContext().sender();
			List<Card> cardDeck = new ArrayList<Card>();
			ActorRef game = getContext().actorOf(Props.create(Game.class), "Game");
			for (int x = 0; x < 4; x++) {
				for (int y = 2; y < 15; y++) {
					cardDeck.add(new Card(x, y));
				}
			}
			Collections.shuffle(cardDeck, new Random());
			game.tell(cardDeck, self());
		}

		if (msg instanceof PlayerValuation) {
			PlayerValuation player = (PlayerValuation) msg;
			validate(player);
			Result result = null;
			if (player.getPlayerPoints1() > player.getPlayerPoints2()) {
				result = new Result(player.getPlayerName1(), player.getPlayerPoints1());
			} else {
				result = new Result(player.getPlayerName2(), player.getPlayerPoints2());
			}
			this.sender.tell(result, getSelf());
		}

	}

	private void validate(PlayerValuation player) {
		int totalPoints = 0;
		int totalPointsPlayer1 = 0;
		int totalPointsPlayer2 = 0;

		List<Card> totalPointsDeck = new ArrayList<Card>();
		for (int x = 0; x < 4; x++) {
			for (int y = 2; y < 15; y++) {
				totalPointsDeck.add(new Card(x, y));
			}
		}

		for (Card deckCard : totalPointsDeck) {
			totalPoints += deckCard.getCard();
		}
		for (Card p1 : player.getDeck1()) {
			totalPointsPlayer1 += p1.getCard();
		}
		for (Card p1 : player.getDeck2()) {
			totalPointsPlayer2 += p1.getCard();
		}

		if (totalPointsPlayer1 == player.getPlayerPoints1()) {
			System.out.println("Player 1 reported proper score");
		} else {
			System.out.println("Player 1 is cheating");
		}

		if (totalPointsPlayer2 == player.getPlayerPoints2()) {
			System.out.println("Player 2 reported proper score");
		} else {
			System.out.println("Player 2 is cheating");
		}

		if (totalPoints == (player.getPlayerPoints1() + player.getPlayerPoints2()))
			System.out.println("Player 1 points and player 2 points is equal to total points present");

	}

}
