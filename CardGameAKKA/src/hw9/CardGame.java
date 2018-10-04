package hw9;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import hw9.Messages.StartGame;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * 
 * @author Gaurang Davda
 *
 */
public class CardGame {

	public static void main(String[] args) throws Exception {

		ActorSystem system = ActorSystem.create("User");
		ActorRef dealerNode = system.actorOf(Props.create(Dealer.class));
		final Timeout timeout = new Timeout(Duration.create(1000, TimeUnit.SECONDS));
		final Future<Object> future = Patterns.ask(dealerNode, new StartGame(), timeout);
		Result result = (Result) Await.result(future, timeout.duration());
		System.out.println(result);
		system.shutdown();
	}
}