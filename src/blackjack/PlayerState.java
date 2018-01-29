package blackjack;

import java.util.List;

class PlayerState {
	IPlayer player;
	double cash;
	double currentInitialBet;
	List<HandState> hands; // a player can have multiple hands/bets with splitting
	
	double firstBet() {
		return hands.isEmpty() ? 0.0 : hands.get(0).currentBet;
	}
	
	Hand firstHand() {
		return hands.isEmpty() ? null : hands.get(0).hand;
	}
}
