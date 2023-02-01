package org.groub_b.Deck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.group_b.Card.Card;
import org.group_b.Card.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckTest {
  Deck cardDeck;

  @BeforeEach
  void setUp() {
    cardDeck = Deck.create();
  }

  @Test
  public void ensureDeckHas52Cards() {
    final ArrayList<Card> cardHolder = new ArrayList<>();
    Iterator<Card> deckIter = cardDeck.iterator();
    while (deckIter.hasNext()) {
      cardHolder.add(deckIter.next());
    }
    assertEquals(52, cardHolder.size());
  }

  @Test
  public void ensureDeckGetsShuffled() {
    final ArrayList<Card> cardHolder1 = new ArrayList<>();
    Iterator<Card> deckIter = cardDeck.iterator();
    while (deckIter.hasNext()) {
      cardHolder1.add(deckIter.next());
    }
    cardDeck.shuffle();

    final ArrayList<Card> cardHolder2 = new ArrayList<>();
    Iterator<Card> deckIter2 = cardDeck.iterator();
    while (deckIter2.hasNext()) {
      cardHolder2.add(deckIter2.next());
    }

    boolean diffFlag = false;
    for (int i = 0; i < cardHolder1.size(); i++) {
      if (!cardHolder1.get(i).equals(cardHolder2.get(i))) {
        diffFlag = true;
      }
    }
    // at least one card is in different location
    assertTrue(diffFlag);
  }

  @Test
  public void ensureGetCardWorks() {
    for (int i = 0; i < 52; i++) {
      assertInstanceOf(Card.class, cardDeck.getCard(i));
    }
  }

}
