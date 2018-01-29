package blackjack;

class DealerPlayer implements IPlayer {

	@Override
	public Action getNextAction(Hand currentHand, Card dealer1) {
		return currentHand.getMaxSum() >= 17 ? Action.STAND : Action.HIT; //Stand on soft 17
	}
	
	@Override
	public double getBetAmount(double cashOnHand) { return 0; /* not relevant */ }
	@Override
	public void onInvalidBet(double amount, double cashOnHand) {}
	@Override
	public void onInvalidAction(Action action) {}
	@Override
	public void onHitNoBust(Hand currentHand, Card dealer) {}
	@Override
	public void onHitBust(Hand currentHand) {}
	@Override
	public void onStand(Hand currentHand, Card dealer1) {}
	@Override
	public void onSplit(Hand h1, Hand h2) {}
	@Override
	public void onBetResult(Hand hand, Hand dealerHand, BetResult result, double betAmount, double newCashTotal) {}
	@Override
	public void onInsufficientFundsToContinue(double cashOnHand) {}
	@Override
	public void onDoubleDownBust(Hand h1, Card dealer1, double newBetAmount) {}
	@Override
	public void onDoubleDownNoBust(Hand h1, Card dealer1, double newBetAmount) {}
}
