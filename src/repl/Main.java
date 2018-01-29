package repl;

import blackjack.Dealer;

public class Main {
	public static void main(String[] args) {
		REPLDealerEventHandler handler = new REPLDealerEventHandler();
		Dealer d = new Dealer(handler);
		d.run();
	}
	
	
}
