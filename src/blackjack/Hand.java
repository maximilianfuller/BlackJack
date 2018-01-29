package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Hand {
	private List<Card> cards;
	private boolean isSplit; //true when the hand is the result of a split
	private int splitID;
	
	public Hand(List<Card> cards, boolean isSplit, int splitID) {
		this.cards = new ArrayList<>(cards);
		this.isSplit = isSplit;
		this.splitID = splitID;
	}
	
	public Hand(List<Card> cards) {
		this(cards, false, 0);
	}
	
	public Hand(Hand hand, Card newCard) {
		this.cards = new ArrayList<>();
		for(int i = 0; i < hand.size(); i++) {
			cards.add(hand.get(i));
		}
		cards.add(newCard);
		isSplit = hand.isSplit();
		splitID = hand.getSplitID();
	}
	
	public List<Integer> getSums() {
		int sum = 0;
		boolean containsAce = false;
		for(Card c : cards) {
			int val = c.getNumberValue();
			sum += val;
			if(val == 1) { containsAce = true; }
		}
		ArrayList<Integer> out = new ArrayList<>();
		out.add(sum);
		if(containsAce) { out.add(sum+10); } //ace
		return out;
	}
	
	public int getMinSum() {
		return getSums().stream().reduce(Dealer.GOAL_NUM+100,(a, b) -> Math.min(a, b));
	}
	
	public int getMaxSum() {
		return getSums().stream().reduce(0,(a, b) -> Math.max(a, b));
	}
	
	/*
	 * returns the sum that does best against an opponent 
	 * (i.e. the highest non busting sum). Result can be busting.
	 */
	public int getBestSum() {
		int max = getMaxSum();
		return max > Dealer.GOAL_NUM ? getMinSum() : max;
	}
	
	public Card get(int index) {
		return cards.get(index);
	}
	
	public int size() {
		return cards.size();
	}
	
	public boolean isSplit() {
		return isSplit;
	}
	
	public int getSplitID() {
		if (!isSplit()) {
			return -1;
		}
		return splitID;
	}
	
	public boolean isBlackJack() {
		if(cards.size() != 2) return false;
		boolean firstIsAce = cards.get(0).getNumber() == 1;
		boolean secondIsAce = cards.get(1).getNumber() == 1;
		boolean firstIsFace = cards.get(0).getNumber() >= 10 && cards.get(0).getNumber() <= 13;
		boolean secondIsFace = cards.get(1).getNumber() >= 10 && cards.get(1).getNumber() <= 13;
		return (firstIsAce && secondIsFace) || (firstIsFace && secondIsAce);
	}
	
	
	@Override public String toString() {
		String out = "";
		if(cards.size() == 0) { return out; }
		for(Card card : cards) {
			out += card.getNumberString() + " ";
		}
		return out.substring(0, out.length()-1);
	}
}
