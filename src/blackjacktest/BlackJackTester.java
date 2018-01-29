package blackjacktest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import blackjack.BetResult;
import blackjack.Card;
import blackjack.Dealer;
import blackjack.Hand;
import blackjack.Suit;

public class BlackJackTester {
	@Test
	public void testHandSumTrivial() {
		ArrayList<Card> cards = new ArrayList<>();
		Hand h1 = new Hand(cards);
		assertEquals(h1.getSums(), Arrays.asList(0));
	}
	
	@Test
	public void testHandSumFace() {
		ArrayList<Card> cards = new ArrayList<>();
		cards.add(new Card(12, Suit.HEARTS));
		Hand h1 = new Hand(cards);
		assertEquals(h1.getSums(), Arrays.asList(10));
	}
	
	@Test
	public void testHandSumAce() {
		ArrayList<Card> cards = new ArrayList<>();
		cards.add(new Card(1));
		Hand h1 = new Hand(cards);
		assertEquals(h1.getSums(), Arrays.asList(1, 11));
	}
	
	@Test
	public void testHandSumComplex1() {
		Hand h1 = new Hand(Arrays.asList(new Card(1), new Card(12)));
		assertEquals(h1.getSums(), Arrays.asList(11, 21));
	}
	
	@Test
	public void testHandSumComplex2() {
		Hand h1 = new Hand(Arrays.asList(new Card(1), new Card(1), new Card(1)));
		assertEquals(h1.getSums(), Arrays.asList(3, 13));
	}
	
	@Test
	public void testHandSumComplex3() {
		Hand h1 = new Hand(Arrays.asList(new Card(2), new Card(2), new Card(3), new Card(4), new Card(5)));
		assertEquals(h1.getSums(), Arrays.asList(16));
	}
	
	@Test
	public void testHandSumComplex4() {
		Hand h1 = new Hand(Arrays.asList(new Card(2), new Card(2), new Card(3), new Card(4), new Card(5)));
		assertEquals(h1.getSums(), Arrays.asList(16));
	}
	
	@Test
	public void testHandSumComplex5() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(11), new Card(3)));
		assertEquals(h1.getSums(), Arrays.asList(23));
	}
	
	@Test
	public void testHandSumComplex6() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(1), new Card(3)));
		assertEquals(h1.getSums(), Arrays.asList(14, 24));
	}
	
	@Test
	public void testHandSumMax() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(1), new Card(3)));
		assertEquals(h1.getMaxSum(), 24);
	}
	
	@Test
	public void testHandSumMin() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(1), new Card(3)));
		assertEquals(h1.getMinSum(), 14);
	}
	
	@Test
	public void testHandMinMaxSum() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(3)));
		assertEquals(h1.getMaxSum(), 17);
		assertEquals(h1.getMinSum(), 17);
	}
	
	@Test
	public void testBlackJack1() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(1)));
		assertTrue(h1.isBlackJack());
	}
	
	@Test
	public void testBlackJack2() {
		Hand h1 = new Hand(Arrays.asList(new Card(9), new Card(1)));
		assertFalse(h1.isBlackJack());
	}
	
	@Test
	public void testResultDraw() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(3)));
		Hand h2 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(3)));
		assertTrue(Dealer.getResult(h1, h2) == BetResult.DRAW);
	}
	
	@Test
	public void testResultBust() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(5), new Card(3)));
		Hand h2 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(5), new Card(10)));
		assertTrue(Dealer.getResult(h1, h2) == BetResult.LOSS);
	}
	
	@Test
	public void testResultWin() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(3)));
		Hand h2 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(2)));
		assertTrue(Dealer.getResult(h1, h2) == BetResult.WIN);
	}
	
	@Test
	public void resultLoss() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(1)));
		Hand h2 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(3)));
		assertTrue(Dealer.getResult(h1, h2) == BetResult.LOSS);
	}
	
	@Test
	public void resultBlackJack() {
		Hand h1 = new Hand(Arrays.asList(new Card(1), new Card(13)));
		Hand h2 = new Hand(Arrays.asList(new Card(13), new Card(5), new Card(3)));
		assertTrue(Dealer.getResult(h1, h2) == BetResult.BLACKJACK);
	}
	
	@Test
	public void resultBlackJackPush() {
		Hand h1 = new Hand(Arrays.asList(new Card(1), new Card(13)));
		Hand h2 = new Hand(Arrays.asList(new Card(1), new Card(10)));
		assertTrue(Dealer.getResult(h1, h2) == BetResult.DRAW);
	}
	
	@Test
	public void resultDealerBust() {
		Hand h1 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(3)));
		Hand h2 = new Hand(Arrays.asList(new Card(13), new Card(4), new Card(3), new Card(10)));
		assertTrue(Dealer.getResult(h1, h2) == BetResult.WIN);
	}
	
}
