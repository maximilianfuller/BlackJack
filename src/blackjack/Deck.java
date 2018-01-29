package blackjack;

import java.util.ArrayList;
import java.util.Collections;

class Deck {
	
	private ArrayList<Card> cards;
	
	public Deck(int numDecks) {
		cards = new ArrayList<>();
		for(int i = 0; i < numDecks; i++) {
			for(int n = 1; n <= 13; n++) {
				cards.add(new Card(n, Suit.HEARTS));
				cards.add(new Card(n, Suit.SPADES));
				cards.add(new Card(n, Suit.CLUBS));
				cards.add(new Card(n, Suit.DIAMONDS));
			}
		}
		Collections.shuffle(cards);
	}
	
	public boolean isEmpty() {
		return cards.size() == 0;
	}
	
	public Card draw() {
		if(isEmpty()) { return null; }
		return cards.remove(cards.size()-1);
	}
}
