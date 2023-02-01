package org.group_b.Referee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

import org.group_b.Card.Card;
import org.group_b.Card.Deck;
import org.group_b.Player.TrivialPlayer;

public class Referee {

  protected TrivialPlayer[] players;
  private Deck gameDeck;
  protected Score gameScore;

  public Referee(TrivialPlayer[] players) {
    if (players.length != 4) {
      throw new IllegalArgumentException("There must be 4 players in a game");
    }
    this.players = players;
    this.gameDeck = Deck.create();
    this.gameScore = new Score();
  }

  public Score play() {
    Card[][] partitionedDeck = splitDeck();

    // Gives each player a different partition of the deck to add some additional
    // randomness
    ArrayList<Integer> handPartition = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
    Collections.shuffle(handPartition);

    for (int i = 0; i < 4; i++) {
      this.players[i].newDeal(Arrays.asList(partitionedDeck[handPartition.get(i)]));
    }
    // defensive copy to not allow players to change the game's score
    return new Score(gameScore);
  }

  private Card[][] splitDeck() {
    ArrayList<Card> cards = this.gameDeck.getCards();
    Card[] localDeck = cards.toArray(new Card[cards.size()]);

    return IntStream.iterate(0, i -> i + 13)
        .limit(4)
        .mapToObj(j -> Arrays.copyOfRange(localDeck, j, j + 13))
        .toArray(Card[][]::new);
  }

  public class Score {
    private int evenScore;
    private int oddScore;

    public Score() {
      this.evenScore = 0;
      this.oddScore = 0;
    }

    public Score(Score scoreToCopy) {
      this.evenScore = scoreToCopy.evenScore;
      this.oddScore = scoreToCopy.oddScore;
    }

    public int getEvenScore() {
      return this.evenScore;
    }

    public int getOddScore() {
      return this.oddScore;
    }

    public void setEvenScore(int evenScore) {
      this.evenScore = evenScore;
    }

    public void setOddScore(int oddScore) {
      this.oddScore = oddScore;
    }

    public void addEvenScore(int evenScore) {
      this.evenScore += evenScore;
    }

    public void addOddScore(int oddScore) {
      this.oddScore += oddScore;
    }

    @Override
    public String toString() {
      return "Score [evenScore=" + evenScore + ", oddScore=" + oddScore + "]";
    }
  }
}
