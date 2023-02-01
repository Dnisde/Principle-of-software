package org.group_b.Card;

import java.util.Iterator;
import org.group_b.Card.CardType.*;

/**
 * The Iterable program implements an application that:
 * 1, It is possible to start at a given Card and iteratively
 * view the next Card in increasing order of game value.
 * 2, A static field or method providing access to the first Card
 *
 *
 * @author Yuxiang Wan, Cameron Cipriano,
 * @version 1.0
 * @since 2022-12-02
 */

public class CardIterator implements Iterator<Card> {

  private int suitIndex;
  private int cardNumberIndex;
  private int lastSuitIndex;
  private int lastCardNumberIndex;

  public CardIterator(Card startCard) {
    this.suitIndex = startCard.getSuitIndex();
    this.cardNumberIndex = startCard.getCardNumberIndex();
    this.lastSuitIndex = Suit.values().length - 1;
    this.lastCardNumberIndex = CardNumber.values().length - 1;
  }

  @Override
  public boolean hasNext() {
    return cardNumberIndex < lastCardNumberIndex || suitIndex < lastSuitIndex;
  }

  @Override
  public Card next() {
    if (cardNumberIndex < lastCardNumberIndex) {
      cardNumberIndex++;
    } else if (suitIndex < lastSuitIndex) {
      suitIndex++;
      cardNumberIndex = 0;
    }
    return Card.create(Suit.values()[suitIndex], CardNumber.values()[cardNumberIndex]);
  }
}
