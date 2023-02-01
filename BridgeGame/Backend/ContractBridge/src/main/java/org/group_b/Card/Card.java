package org.group_b.Card;

import java.util.Iterator;

/**
 * Create cards for the Contract Bridage,
 * including 4 suits * 13 numbers,
 * which could be sorted in increasing order of value:
 * clubs, diamonds, hearts, spades;
 * 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.
 *
 * @author Jiawei Zhao, Yuxiang Wan, Cameron Cipriano, Chelsea Lau
 * @version 1.0
 * @since 1.0
 */
public class Card implements Comparable<Card>, Iterable<Card> {
	/**
	 * Represents card's suit and number.
	 */
	private final CardType.Suit suit;
	private final CardType.CardNumber cardNumber;
	public static final Card FIRST_CARD = new Card(CardType.Suit.clubs, CardType.CardNumber.two);

	/**
	 * Constructor for Card class.
	 *
	 * @param suit       The card's suit type.
	 * @param cardNumber The card's number.
	 */
	private Card(CardType.Suit suit, CardType.CardNumber cardNumber) {
		this.suit = suit;
		this.cardNumber = cardNumber;
	}

	/**
	 * Create card with sepecific suit and number.
	 *
	 * @param suit       The card's suit type.
	 * @param cardNumber The card's number.
	 * @return an object of Card.
	 */
	public static Card create(CardType.Suit suit, CardType.CardNumber cardNumber) {
		return new Card(suit, cardNumber);
	}

	/**
	 * This interface imposes an ordering on the objects of Card that implements it.
	 *
	 * Lists (and arrays) of Card s can be sorted automatically by Collections.sort
	 * (and Arrays.sort) in increasing order of the following:
	 * clubs, diamonds, hearts, spades;
	 * 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.
	 *
	 * @param c2 the Card object to be compared
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(Card c2) {
		final int indexDiff = this.getSuitIndex() - c2.getSuitIndex();
		if (indexDiff != 0) {
			return indexDiff;
		} else {
			return this.getCardNumberIndex() - c2.getCardNumberIndex();
		}
	}

	/**
	 * Get the index of this object in increasing suit order
	 *
	 * @return the index of this object in increasing suit order.
	 */
	public int getSuitIndex() {
		return suit.ordinal();
	}

	/**
	 * Get the suit of this card
	 *
	 * @return the suit of this card
	 */
	public CardType.Suit getSuit() {
		return suit;
	}

	/**
	 * Get the index of this object in increasing number order
	 *
	 * @return the index of this object in increasing number order.
	 */
	public int getCardNumberIndex() {
		return cardNumber.ordinal();
	}

	/**
	 * Get the number of this card
	 *
	 * @return the number of this card
	 */
	public CardType.CardNumber getCardNumber() {
		return cardNumber;
	}

	/**
	 * Return a string representation of the Card object, including the number and
	 * suit for cards.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "[" + cardNumber.toString() + " " + suit.toString() + "]";
	}

	@Override
	public Iterator<Card> iterator() {
		return new CardIterator(this);
	}
}
