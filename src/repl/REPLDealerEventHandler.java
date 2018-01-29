package repl;

import blackjack.Dealer;
import blackjack.Hand;
import blackjack.IDealerEventHandler;
import blackjack.IPlayer;

public class REPLDealerEventHandler implements IDealerEventHandler {

	int numPlayers;
	int currPlayer = 0;
	
	@Override
	public int getNumPlayers() {
		REPL.println("Welcome to Virtual BlackJack! How many players would like to play?");
		int out;
		while(true) {
			String input = REPL.readLine();
			try {
				out = Integer.valueOf(input);
			} catch (NumberFormatException e) {
				out = 0;
			}
			
			if(out >= 1 && out <= Dealer.MAX_PLAYERS) { break; }
			REPL.println("Please enter a number between 1 and " + Dealer.MAX_PLAYERS);
		}
		numPlayers = out;
		REPL.println("");
		return out;
	}
	
	@Override
	public IPlayer getNewPlayer() {
		while(true) {
			REPL.println("Player " + ++currPlayer + " please enter your name. "
					+ "(Try a name with 'robot' in it for an automated player.)");
			String name = REPL.readLine();
			if(name == null || name.length() == 0) {
				REPL.println("Invalid name.");
				currPlayer--;
				continue;
			}
			if(name.toLowerCase().contains("robo")) {
				return new REPLRoboPlayer(name);
			}
			return new REPLPlayer(name);
		}
	}
	
	@Override
	public void onPlayersAdded(int numPlayers) {
		REPL.println(numPlayers + " players added. You each start with $" + (int) Dealer.INITIAL_CASH);
	}

	@Override
	public void onReshuffle() {
		REPL.println("Reshuffling the deck.");
	}

	@Override
	public void onEveryoneBroke() {
		REPL.println("Everyone is broke. The dealer is bored and idly browses facebook on a smartphone. "
				+ "An advertisement suggests, based on prior interest in a variety of gambling self-help books, "
				+ "that the dealer enroll in an AA course. The dealer shuts off the phone in "
				+ "disgust and surreptitiously takes a swig from his hip flask.");
		
	}

	@Override
	public void onDealerResults(Hand dealerHand) {
		REPL.println("");
		REPL.println("The dealer reveals their cards in quick succession.");
		for(int i = 0; i < dealerHand.size(); i++) {
			REPL.println(dealerHand.get(i).toString());
		}
		
		REPL.println("Dealer hand: " + dealerHand + " with " + dealerHand.getBestSum() + " total.");
		
	}
}
