package repl;

import blackjack.Action;
import blackjack.BetResult;
import blackjack.Card;
import blackjack.Dealer;
import blackjack.Hand;
import blackjack.IPlayer;

public class REPLPlayer implements IPlayer{
	
	private String name;
	boolean retryAction = false;
	
	public REPLPlayer(String name) {
		this.name = name;
	}

	@Override
	public double getBetAmount(double cashOnHand) {
		REPL.println("");
		while(true) {
			REPL.println("How much would " + name + " like to bet? They have $" + (int)cashOnHand + " left.");
			return readBetAmount();
		}
	}

	@Override
	public void onInvalidBet(double amount, double cashOnHand) {
		REPL.println("That bet is invalid. Please enter a valid number. You only have $" + (int) cashOnHand + " to spend.");
		
	}

	@Override
	public Action getNextAction(Hand currentHand, Card dealer1) {
		printHand(currentHand);
		REPL.println("The dealer's face up card is a " + dealer1);
		printActionsMenu();
		retryAction = false;
		return readAction();
	}

	@Override
	public void onInvalidAction(Action action) {
		if(action == null) {
			REPL.println("Invalid action. Please check spelling.");
		} else if (action == Action.DOUBLE_DOWN) {
			REPL.println("You don't have enough cash to double down.");
		} else if (action == Action.SPLIT) {
			REPL.println("You can't split if you don't have enough cash. Splitting is only allowed on cards of the same rank "
					+ "as the first action. Resplitting is not allowed.");;
		}
		retryAction = true;
	}

	@Override
	public void onHitNoBust(Hand currentHand, Card dealer) {
		//do nothing for now
	}

	@Override
	public void onHitBust(Hand currentHand) {
		printHand(currentHand);
		REPL.println("Oops " + name + " busted with " + currentHand.getBestSum() + ".");
	}
	
	@Override
	public void onDoubleDownBust(Hand h1, Card dealer1, double newBetAmount) {
		printDoubleDownText(h1, newBetAmount);
		onHitBust(h1);
	}

	@Override
	public void onDoubleDownNoBust(Hand h1, Card dealer1, double newBetAmount) {
		printDoubleDownText(h1, newBetAmount);
		onHitNoBust(h1, dealer1);
	}

	@Override
	public void onStand(Hand currentHand, Card dealer1) {
		REPL.println(name + " stays at " + currentHand.getBestSum() + ".");
	}

	@Override
	public void onSplit(Hand h1, Hand h2) {
		REPL.println(name + " has split the hand.");
		
	}

	@Override
	public void onBetResult(Hand hand, Hand dealerHand, BetResult result, double betAmount, double newCashTotal) {
		REPL.println("");
		String handString = hand.isSplit() ? "split hand " + hand.getSplitID() + ": " : "hand: ";
		REPL.println(name + "'s " + handString + hand + " with " + hand.getBestSum() + " total.");
		switch(result) {
		case BLACKJACK:
			REPL.println("BlackJack! 1.5x payout.");
			break;
		case WIN:
			REPL.println(name + " wins!");
			break;
		case DRAW:
			REPL.println("Push. " + name + " tied the dealer and gets their money back.");
			break;
		case LOSS:
			REPL.println(name + " loses.");
			break;
		}
		REPL.println(name + " bet $" + (int)betAmount + " and their payout is $" + (int)Dealer.getPayout(result, betAmount) + ".");
		REPL.println(name + "'s new cash total is $" + (int) newCashTotal + ".");
	}
	
	@Override
	public void onInsufficientFundsToContinue(double cashOnHand) {
		REPL.println(name + " is all out of cash and leaves the table.");
		
	}
	
	protected int readBetAmount() {
		try {
			return Integer.valueOf(REPL.readLine().replace("$", ""));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	protected Action readAction() {
		try {
			String str = REPL.readLine().toLowerCase();
			if(str.equals("hit") || str.equals("h")) {
				return Action.HIT;
			}
			if(str.equals("split") || str.equals("p")) {
				return Action.SPLIT;
			}
			if(str.equals("stand") || str.equals("s")) {
				return Action.STAND;
			}
			if(str.equals("double") || str.equals("double down") || str.equals("doubledown") || str.equals("d")) {
				return Action.DOUBLE_DOWN;
			}
			
		} catch (NumberFormatException e) {
			return null;
		}
		return null;
	}
	
	private void printActionsMenu() {
		REPL.println("The dealer looks at " + name + " expectingly.");
		REPL.println("(h)it (s)tand s(p)lit or (d)oubledown?");
	}
	
	private void printHand(Hand hand) {
		if(!retryAction) {
			if(hand.size() == 1) { //split hand
				REPL.println("");
				REPL.println("Split hand " + hand.getSplitID() + ":");
			} else if(hand.size() == 2 && !hand.isSplit()) {
				REPL.println("");
				REPL.println("The dealer turns to " + name + " and deals two cards.");
				REPL.println(hand.get(0).toString());
				REPL.println(hand.get(1).toString());
			} else {
				Card card = hand.get(hand.size()-1);
				REPL.println(name + " is hit with " + card.getPronoun() + " " + card + ".");
			}
		}
		REPL.println("Current hand: " + hand + " with " + hand.getBestSum() +  " total.");
	}
	
	private void printDoubleDownText(Hand h, double newBetAmount) {
		REPL.println(name + " doubles down, increasing their bet from "
	+ (int)(newBetAmount/2.0) + " to " + (int)newBetAmount);
		Card card = h.get(h.size()-1);
		REPL.println("A bead of sweat falls from " + name + "'s brow. The new card is " + card.getPronoun() + " " + card + ".");
		REPL.println("Current hand: " + h + " with " + h.getBestSum() +  " total.");

	}
}
