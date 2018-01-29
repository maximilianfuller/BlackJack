package blackjack;

public class Card {
	
	private Suit suit;
	private int number; //1 to 13
	
	public Card(int number, Suit suit) {
		this.suit = suit;
		this.number = Math.min(13, Math.max(1,  number));
	}
	
	public Card(int number) {
		this.suit = Suit.HEARTS;
		this.number = Math.min(13, Math.max(1,  number));
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getNumberValue() {
		return Math.min(getNumber(), 10);
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public String getNumberString() {
		if(number == 1) {
			return "Ace";
		} else if(number == 11) {
			return "Jack";
		} else if(number == 12) {
			return "Queen";
		} else if(number == 13) {
			return "King";
		} else {
			return "" + number;
		}
	}
	
	public String getSuitString() {
		switch(suit) {
		case HEARTS:
			return "Hearts";
		case CLUBS:
			return "Clubs";
		case DIAMONDS:
			return "Diamonds";
		case SPADES:
			return "Spades";
		}
		return null;
	}
	
	public String getPronoun() {
		return getNumber() == 1 || getNumber() == 8 ? "an" : "a";

	}
	
	@Override public String toString() {
		return getNumberString() + " of " + getSuitString();
	}
}
