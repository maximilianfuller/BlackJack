package blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dealer {
	public static final int MAX_PLAYERS = 6;
	public static final double INITIAL_CASH = 1000.0;
	public static final int NUM_DECKS = 1;
	public static final int GOAL_NUM = 21;
	public static final double MINIMUM_CASH_REQUIRED = 1.0;
	
	private IDealerEventHandler handler;
	private Deck deck;
	private List<PlayerState> playerStates;
	private PlayerState dealerState;
	
	public Dealer(IDealerEventHandler handler) {
		this.handler = handler;
	}

	public void run() {
		initializePlayers();
		deck = new Deck(NUM_DECKS);
		while(true) {
			performBettingRound();
			if(playerStates.isEmpty()) {
				handler.onEveryoneBroke();
				break;
			}
		}
	}
	
	private void performBettingRound() {
		getPlayerBets();
		setupDealer();
		for(PlayerState p : playerStates) {
			dealPlayer(p, null);
		}
		dealPlayer(dealerState, dealerState.firstHand());
		handler.onDealerResults(dealerState.firstHand());
		for(PlayerState p : playerStates) {
			handlePlayerResults(p);
		}
		checkForInsufficientCash();
	}
	
	private void checkForInsufficientCash() {
		for(int i = playerStates.size()-1; i >= 0; i--) {
			PlayerState p = playerStates.get(i);
			if(p.cash < MINIMUM_CASH_REQUIRED) {
				p.player.onInsufficientFundsToContinue(p.cash);
				playerStates.remove(i);
			}
		}
	}
	
	private void initializePlayers() {
		ArrayList<PlayerState> players = new ArrayList<>();
		int val = handler.getNumPlayers();
		for(int i = 0; i < val; i++) {
			PlayerState state = new PlayerState();
			state.player = handler.getNewPlayer();
			state.cash = INITIAL_CASH;
			players.add(state);
		}
		this.playerStates = players;
		dealerState = new PlayerState();
		dealerState.player = new DealerPlayer();
	}
	
	private void getPlayerBets() {
		for(PlayerState p : playerStates) {
			while(true) {
				double betAmount = p.player.getBetAmount(p.cash);
				double cash = p.cash;
				if(betAmount <= 0.0 || cash < betAmount) {
					p.player.onInvalidBet(betAmount, p.cash);
					continue;
				}
				p.currentInitialBet = betAmount;
				p.cash -= p.currentInitialBet;
				break;
			}
		}
	}
	
	/*
	 * deals player cards according to player actions. Uses optional starting hand
	 */
	private void dealPlayer(PlayerState pState, Hand startingHand) {
		HandState firstHandInfo = new HandState();
		firstHandInfo.currentBet = pState.currentInitialBet;
		firstHandInfo.hand = startingHand == null ? new Hand(Arrays.asList(drawCard(), drawCard())) : startingHand;
		
		pState.hands = Arrays.asList(firstHandInfo);
		for(int i = 0; i < pState.hands.size(); i++) { //we may append new handInfo objects as we split
			HandState handInfo = pState.hands.get(i);
			dealHand(pState, handInfo);
		}
	}
	
	private void dealHand(PlayerState playerState, HandState handState) {
		while(true) {
			Action action = playerState.player.getNextAction(handState.hand, dealerState.firstHand().get(0));
			if(action == null) {
				playerState.player.onInvalidAction(action);
				continue;
			}
			if(action == Action.STAND) {
				playerState.player.onStand(handState.hand, dealerState.firstHand().get(0));
				break;
			}
			if(action == Action.HIT) {
				handState.hand = new Hand(handState.hand, drawCard());
				int minSum = handState.hand.getMinSum();
				if(minSum <= GOAL_NUM) {
					playerState.player.onHitNoBust(handState.hand, dealerState.firstHand().get(0));
				} else {
					playerState.player.onHitBust(handState.hand);
					break;
				}	
				continue;
			}
			if(action == Action.DOUBLE_DOWN) {
				if(playerState.cash < handState.currentBet) { //not enough cash
					playerState.player.onInvalidAction(action);
					continue;
				}
				playerState.cash -= handState.currentBet;
				handState.currentBet *= 2.0;
				handState.hand = new Hand(handState.hand, drawCard());
				int minSum = handState.hand.getMinSum();
				if(minSum <= GOAL_NUM) {
					playerState.player.onDoubleDownNoBust(handState.hand, dealerState.firstHand().get(0), handState.currentBet);
				} else {
					playerState.player.onDoubleDownBust(handState.hand, dealerState.firstHand().get(0), handState.currentBet);
				}	
				break;
			}
			if(action == Action.SPLIT) {
				if(playerState.cash < handState.currentBet) { //not enough cash
					playerState.player.onInvalidAction(action);
					continue;
				}
				if(handState.hand.size() != 2) { //can only split on 2 cards
					playerState.player.onInvalidAction(action);
					continue;
				}
				if(handState.hand.isSplit()) { //can't resplit
					playerState.player.onInvalidAction(action);
					continue;
				}
				if(handState.hand.get(0).getNumberValue() != handState.hand.get(1).getNumberValue()) {
					playerState.player.onInvalidAction(action);
					continue;
				}
				playerState.cash -= handState.currentBet;
				Card firstCard = handState.hand.get(0);
				Card secondCard = handState.hand.get(1);
				
				//modify current hand
				handState.hand = new Hand(Arrays.asList(firstCard), true, 1);
				
				//add new hand
				HandState otherHandState = new HandState();
				otherHandState.currentBet = handState.currentBet;
				otherHandState.hand = new Hand(Arrays.asList(secondCard), true, 2);
				ArrayList<HandState> newHandInfos = new ArrayList<>(playerState.hands);
				newHandInfos.add(otherHandState);
				playerState.hands = newHandInfos;
				
				playerState.player.onSplit(handState.hand, otherHandState.hand);
			}
		}
	}
	
	private void handlePlayerResults(PlayerState p) {
		for(int i = 0; i < p.hands.size(); i++) {
			HandState handInfo = p.hands.get(i);
			double bet = handInfo.currentBet;
			BetResult result = getResult(handInfo.hand, dealerState.firstHand());
			double payout = getPayout(result, bet);
			p.cash += payout;
			p.player.onBetResult(handInfo.hand, dealerState.firstHand(), result, bet, p.cash);
		}
	}
	
	private void setupDealer() {
		HandState handInfo = new HandState();
		handInfo.hand = new Hand(Arrays.asList(drawCard(), drawCard()));
		dealerState.hands = Arrays.asList(handInfo);
	}
		
	public static BetResult getResult(Hand playerHand, Hand dealerHand) {
		if(playerHand.getMinSum() > 21) {
			return BetResult.LOSS; // house edge, dealer goes first
		} 
		if(dealerHand.getMinSum() > 21) {
			return BetResult.WIN;
		}
		int pValue = playerHand.isBlackJack() ? GOAL_NUM + 1 : playerHand.getBestSum();
		int dValue = dealerHand.isBlackJack() ? GOAL_NUM + 1 : dealerHand.getBestSum();
		if(pValue > dValue) {
			if (playerHand.isBlackJack()) { 
				return BetResult.BLACKJACK; 
			}
			return BetResult.WIN;
		}
		if(pValue == dValue) {
			return BetResult.DRAW;
		}
		return BetResult.LOSS;
	}
	
	public static double getPayout(BetResult result, double betAmount) {
		switch(result) {
		case WIN:
			return 2.0*betAmount;
		case DRAW:
			return betAmount;
		case LOSS:
			return 0.0;
		case BLACKJACK:
			return 2.5*betAmount;
		}
		return 0.0; //should never happen
	}
	
	
	private Card drawCard() {
		if (deck == null || deck.isEmpty()) {
			deck = new Deck(NUM_DECKS);
			handler.onReshuffle();
		}
		return deck.draw();
	}
}
