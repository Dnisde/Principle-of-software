package org.groub_b.Card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.group_b.Card.*;
import org.group_b.Card.CardType.CardNumber;
import org.group_b.Card.CardType.Suit;

public class CardTest {
  Card clubs2, clubs4, clubsJack, clubsKing,
      diamonds2, diamonds4, diamondsJack, diamondsKing,
      hearts2, hearts4, heartsJack, heartsKing,
      spadesJack, spades2, spades4, spadesKing;

  @BeforeEach
  void setUp() {

    // CLUB number and face cards
    clubs2 = Card.create(CardType.Suit.clubs, CardType.CardNumber.two);
    clubs4 = Card.create(CardType.Suit.clubs, CardType.CardNumber.four);
    clubsJack = Card.create(CardType.Suit.clubs, CardType.CardNumber.Jack);
    clubsKing = Card.create(CardType.Suit.clubs, CardType.CardNumber.King);

    // DIAMONDS number and face cards
    diamonds2 = Card.create(CardType.Suit.diamonds, CardType.CardNumber.two);
    diamonds4 = Card.create(CardType.Suit.diamonds, CardType.CardNumber.four);
    diamondsJack = Card.create(CardType.Suit.diamonds, CardType.CardNumber.Jack);
    diamondsKing = Card.create(CardType.Suit.diamonds, CardType.CardNumber.King);

    // HEARTS number and face cards
    hearts2 = Card.create(CardType.Suit.hearts, CardType.CardNumber.two);
    hearts4 = Card.create(CardType.Suit.hearts, CardType.CardNumber.four);
    heartsJack = Card.create(CardType.Suit.hearts, CardType.CardNumber.Jack);
    heartsKing = Card.create(CardType.Suit.hearts, CardType.CardNumber.King);

    // SPADES number and face cards
    spades2 = Card.create(CardType.Suit.spades, CardType.CardNumber.two);
    spades4 = Card.create(CardType.Suit.spades, CardType.CardNumber.four);
    spadesJack = Card.create(CardType.Suit.spades, CardType.CardNumber.Jack);
    spadesKing = Card.create(CardType.Suit.spades, CardType.CardNumber.King);
  }

  @Test
  public void returnCorrectCardSuit() {
    assertEquals(CardType.Suit.clubs, clubs2.getSuit());
    assertEquals(CardType.Suit.diamonds, diamonds2.getSuit());
    assertEquals(CardType.Suit.hearts, hearts2.getSuit());
    assertEquals(CardType.Suit.spades, spades2.getSuit());
  }

  @Test
  public void returnCorrectCardSuitIndex() {
    assertEquals(0, clubs2.getSuitIndex());
    assertEquals(1, diamonds4.getSuitIndex());
    assertEquals(2, heartsJack.getSuitIndex());
    assertEquals(3, spadesKing.getSuitIndex());
  }

  @Test
  public void returnCorrectCardNumbers() {
    assertEquals(CardType.CardNumber.two, clubs2.getCardNumber());
    assertEquals(CardType.CardNumber.four, clubs4.getCardNumber());
    assertEquals(CardType.CardNumber.Jack, clubsJack.getCardNumber());
    assertEquals(CardType.CardNumber.King, clubsKing.getCardNumber());
  }

  @Test
  public void returnCorrectCardNumberIndex() {
    assertEquals(0, clubs2.getCardNumberIndex());
    assertEquals(2, clubs4.getCardNumberIndex());
    assertEquals(9, clubsJack.getCardNumberIndex());
    assertEquals(11, clubsKing.getCardNumberIndex());
  }

  @Test
  public void returnCorrectCardOrdering() {
    // Comparison across suits
    // clubs is lower suit than diamonds
    assertTrue(clubs2.compareTo(diamonds2) < 0);
    assertTrue(clubs4.compareTo(diamonds4) < 0);
    // diamonds is higher than clubs
    assertTrue(diamonds2.compareTo(clubs2) > 0);
    assertTrue(diamonds4.compareTo(clubs4) > 0);
    // diamonds is a lower suit than hearts
    assertTrue(diamonds2.compareTo(hearts2) < 0);
    assertTrue(diamonds4.compareTo(hearts4) < 0);

    // hearts is higher than diamonds
    assertTrue(hearts2.compareTo(diamonds2) > 0);
    assertTrue(hearts4.compareTo(diamonds4) > 0);
    // hearts is a lower suit than spades
    assertTrue(hearts2.compareTo(spades2) < 0);
    assertTrue(hearts4.compareTo(spades4) < 0);

    // spades is higher than hearts
    assertTrue(spades2.compareTo(hearts2) > 0);
    assertTrue(spades4.compareTo(hearts4) > 0);

    // Comparison within suits
    // two is lower than four
    assertTrue(clubs2.compareTo(clubs4) < 0);
    assertTrue(diamonds2.compareTo(diamonds4) < 0);
    assertTrue(hearts2.compareTo(hearts4) < 0);
    assertTrue(spades2.compareTo(spades4) < 0);

    // four is higher than two
    assertTrue(clubs4.compareTo(clubs2) > 0);
    assertTrue(diamonds4.compareTo(diamonds2) > 0);
    assertTrue(hearts4.compareTo(hearts2) > 0);
    assertTrue(spades4.compareTo(spades2) > 0);

    // four is lower than Jack
    assertTrue(clubs4.compareTo(clubsJack) < 0);
    assertTrue(diamonds4.compareTo(diamondsJack) < 0);
    assertTrue(hearts4.compareTo(heartsJack) < 0);
    assertTrue(spades4.compareTo(spadesJack) < 0);

    // Jack is higher than four
    assertTrue(clubsJack.compareTo(clubs4) > 0);
    assertTrue(diamondsJack.compareTo(diamonds4) > 0);
    assertTrue(heartsJack.compareTo(hearts4) > 0);
    assertTrue(spadesJack.compareTo(spades4) > 0);

    // Jack is lower than King
    assertTrue(clubsJack.compareTo(clubsKing) < 0);
    assertTrue(diamondsJack.compareTo(diamondsKing) < 0);
    assertTrue(heartsJack.compareTo(heartsKing) < 0);
    assertTrue(spadesJack.compareTo(spadesKing) < 0);

    // King is higher than Jack
    assertTrue(clubsKing.compareTo(clubsJack) > 0);
    assertTrue(diamondsKing.compareTo(diamondsJack) > 0);
    assertTrue(heartsKing.compareTo(heartsJack) > 0);
    assertTrue(spadesKing.compareTo(spadesJack) > 0);

    // same cards are equal
    assertTrue(clubs2.compareTo(clubs2) == 0);
    assertTrue(clubs4.compareTo(clubs4) == 0);
    assertTrue(diamonds2.compareTo(diamonds2) == 0);
    assertTrue(diamonds4.compareTo(diamonds4) == 0);
    assertTrue(heartsJack.compareTo(heartsJack) == 0);
    assertTrue(heartsKing.compareTo(heartsKing) == 0);
    assertTrue(spadesJack.compareTo(spadesJack) == 0);
    assertTrue(spadesKing.compareTo(spadesKing) == 0);

  }

  @Test
  public void displayCorrectOutput() {
    // Club cards
    assertEquals("[two clubs]", clubs2.toString());
    assertEquals("[four clubs]", clubs4.toString());
    assertEquals("[Jack clubs]", clubsJack.toString());
    assertEquals("[King clubs]", clubsKing.toString());

    // Diamond cards
    assertEquals("[two diamonds]", diamonds2.toString());
    assertEquals("[four diamonds]", diamonds4.toString());
    assertEquals("[Jack diamonds]", diamondsJack.toString());
    assertEquals("[King diamonds]", diamondsKing.toString());

    // Heart cards
    assertEquals("[two hearts]", hearts2.toString());
    assertEquals("[four hearts]", hearts4.toString());
    assertEquals("[Jack hearts]", heartsJack.toString());
    assertEquals("[King hearts]", heartsKing.toString());

    // Spade cards
    assertEquals("[two spades]", spades2.toString());
    assertEquals("[four spades]", spades4.toString());
    assertEquals("[Jack spades]", spadesJack.toString());
    assertEquals("[King spades]", spadesKing.toString());
  }

  @Test
  public void returnCorrectFollowingCards() {
    Iterator<Card> cardIter = clubs2.iterator();

    // Clubs
    Card nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.three);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.four);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.five);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.six);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.seven);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.eight);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.nine);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.ten);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.Jack);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.Queen);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.King);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.clubs && nextCard.getCardNumber() == CardNumber.Ace);

    // Diamonds
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.two);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.three);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.four);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.five);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.six);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.seven);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.eight);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.nine);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.ten);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.Jack);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.Queen);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.King);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.diamonds && nextCard.getCardNumber() == CardNumber.Ace);

    // Hearts
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.two);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.three);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.four);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.five);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.six);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.seven);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.eight);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.nine);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.ten);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.Jack);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.Queen);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.King);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.hearts && nextCard.getCardNumber() == CardNumber.Ace);

    // Spades
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.two);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.three);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.four);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.five);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.six);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.seven);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.eight);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.nine);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.ten);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.Jack);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.Queen);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.King);
    nextCard = cardIter.next();
    assertTrue(nextCard.getSuit() == Suit.spades && nextCard.getCardNumber() == CardNumber.Ace);

    assertFalse(cardIter.hasNext());
  }
}
