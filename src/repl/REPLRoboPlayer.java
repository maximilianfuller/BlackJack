package repl;

import java.util.Random;

import blackjack.Action;

//automatic player used as a sort of end to end test
public class REPLRoboPlayer extends REPLPlayer{
	
	private final static int BET_DEFAULT = 1;
	private final static Random r = new Random();

	public REPLRoboPlayer(String name) {
		super(name);
	}
	
	@Override
	protected Action readAction() {
		switch(r.nextInt(4)) {
		case 0:
			REPL.println("Stand");
			return Action.STAND;
		case 1:
			REPL.println("Split");
			return Action.SPLIT;
		case 2:
			REPL.println("Hit");
			return Action.HIT;
		case 3:
			REPL.println("Double Down");
			return Action.DOUBLE_DOWN;
		}
		return Action.STAND;
	}
	
	@Override
	protected int readBetAmount() {
		REPL.println("" + BET_DEFAULT);
		return BET_DEFAULT;
	}

}
