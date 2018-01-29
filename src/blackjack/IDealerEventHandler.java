package blackjack;

public interface IDealerEventHandler {

	public int getNumPlayers();
	public IPlayer getNewPlayer();
	public void onPlayersAdded(int numPlayers);
	public void onDealerResults(Hand dealerHand);
	public void onReshuffle();
	public void onEveryoneBroke();
}
