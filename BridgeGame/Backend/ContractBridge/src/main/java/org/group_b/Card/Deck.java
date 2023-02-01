package org.group_b.Card;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Create a shuffled pack of 52 cards used in a deal of contract bridge.
 * 
 * @author Chelsea Lau, Jiawei Zhao
 * @version 1.0
 * @since 2022-12-03
 */
public class Deck implements Iterable<Card> {
  /**
   * List of cards in a deck
   */
  private ArrayList<Card> m_card_arr;

  /**
   * Constructor for Deck
   */
  private Deck() {
    this.m_card_arr = new ArrayList<Card>();
    m_card_arr.add(Card.FIRST_CARD);

    CardIterator m_iter = new CardIterator(Card.FIRST_CARD);
    while (m_iter.hasNext()) {
      m_card_arr.add(m_iter.next());
    }
    shuffle();
  }

  /**
   * Uniformly shuffle the card deck
   */
  public void shuffle() {
    Collections.shuffle(m_card_arr);
  }

  /**
   * Create deck.
   *
   * @return an object Deck.
   */
  public static Deck create() {
    return new Deck();
  }

  /**
   * Get card from index
   */
  public Card getCard(int index) {
    return m_card_arr.get(index);
  }

  /**
   * Get all cards in deck
   */
  public ArrayList<Card> getCards() {
    return m_card_arr;
  }

  /**
   * Make cards in deck iterable.
   */
  @Override
  public Iterator<Card> iterator() {
    return m_card_arr.iterator();
  }
}

