package blackjack;

public interface IPlayer {
	double getBetAmount(double cashOnHand);
	void onInvalidBet(double amount, double cashOnHand);
	void onInsufficientFundsToContinue(double cashOnHand);
	
	Action getNextAction(Hand currentHand, Card dealer1);
	
	void onInvalidAction(Action action);
	void onHitNoBust(Hand currentHand, Card dealer);
	void onHitBust(Hand currentHand);
	void onStand(Hand currentHand, Card dealer1);
	void onSplit(Hand h1, Hand h2);
	void onDoubleDownBust(Hand h1, Card dealer1, double newBetAmount);
	void onDoubleDownNoBust(Hand h1, Card dealer1, double newBetAmount);
	void onBetResult(Hand hand, Hand dealerHand, BetResult result, double betAmount, double newCashTotal);
	
}
