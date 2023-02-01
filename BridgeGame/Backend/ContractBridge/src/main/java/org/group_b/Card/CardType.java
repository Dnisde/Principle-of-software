package org.group_b.Card;

/**
 * All types of card suits and numbers for the Contract Bridage,
 * including 4 suits and 13 numbers,
 *
 * @author Jiawei Zhao
 * @version 1.0
 * @since 1.0
 */

public interface CardType {
	/**
	 * Suits that can be used in bridage cards.
	 */
	public enum Suit {
		clubs,
		diamonds,
		hearts,
		spades;
	}

	/**
	 * Card numbers can be used in bridge cards.
	 */
	public enum CardNumber {
		two,
		three,
		four,
		five,
		six,
		seven,
		eight,
		nine,
		ten,
		Jack,
		Queen,
		King,
		Ace;
	}
}
