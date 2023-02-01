package org.group_b.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.group_b.Bid.Bid;
import org.group_b.Bid.BidType.BidNumber;
import org.group_b.Bid.BidType.BidSuit;
import org.group_b.Bid.BidType.Operation;
import org.group_b.Card.Card;

/**
 * A simple player that takes input from the command line and returns a bid or
 * card based on that input.
 * 
 * 
 * @author Cameron Cipriano
 * @version 1.0
 * @since 2022-12-07
 */
public class TrivialPlayer {

  // Static patterns for parsing user input
  private static Pattern bidPattern = Pattern.compile(
      "(?:(?:^([1-7])|^(one|two|three|four|five|six|seven)|^(jack|queen|king|ace))\\s+(clubs|diamonds|hearts|spades|NT))(?:\\s*(double|redouble))*$|^(pass)$",
      Pattern.CASE_INSENSITIVE);
  private static Pattern playPattern = Pattern.compile("^[1-9]{1,2}$", Pattern.CASE_INSENSITIVE);

  // the player's hand
  private List<Card> hand;

  public TrivialPlayer() {
    this.hand = null;
  }

  /**
   * Sets the player's hand to the given one and sorts it in ascending order for
   * ease of decision making when trying to bid or play a card.
   * 
   * @param hand: List of cards to set the player's hand to
   */
  public void newDeal(List<Card> hand) {
    this.hand = hand;
    // Sorting the hand helps the user to keep their hand in order and see their
    // relative size
    Collections.sort(this.hand);
  }

  /**
   * Prompts the user to create a bid from the command line by looking at their
   * current
   * hand.
   * 
   * @return Bid the Bid that the user selects for their turn
   */
  public Bid makeBid() {
    System.out.println("It's your turn to make a bid! Your hand is shown below:");
    // Loop until we get a valid bid
    boolean validBid = false;
    BufferedReader bidReader = new BufferedReader(new InputStreamReader(System.in));

    String rawBidString = null;
    Bid bidToMake = null;
    Matcher bidMatcher = null;

    do {
      printHand();
      System.out.println("Enter your Bid: <[1-7]|[one-seven]> <suit|NT> [double|redouble], or pass): ");
      try {
        rawBidString = bidReader.readLine();
        bidMatcher = TrivialPlayer.bidPattern.matcher(rawBidString.trim());
        if (bidMatcher.matches()) {
          validBid = true;
          bidToMake = this.createBidFromMatcher(bidMatcher);
        } else {
          System.out.println("Sorry, that is not a valid bid. Please try again.");
        }
      } catch (Exception e) {
        System.out.println("Sorry, that is not a valid bid. Please try again.");
      }
    } while (!validBid);

    return bidToMake;
  }

  /**
   * Prompts the user via the command line to select a card from their
   * current hand using the numbers from 1 to the total number of cards in their
   * hand to play.
   * 
   * NOTE:
   * This method removes the card from the player's hand (mutates the hand)
   * 
   * @return Card: the Card from the player's hand that they select to play.
   */
  public Card playCard() {
    boolean validCard = false;
    Card cardToPlay = null;

    if (hand.size() == 0) {
      System.out.println("You have no cards left to play!");
      return null;
    }

    System.out.println("It's your turn to play a card! Your hand is shown below:");
    // Loop until we get a valid card
    BufferedReader cardReader = new BufferedReader(new InputStreamReader(System.in));
    Matcher cardMatcher = null;
    String cardIndexString = null;

    do {
      printHand();

      System.out.printf("Enter your Card: [1-%d]\n", hand.size());
      try {
        cardIndexString = cardReader.readLine();
        cardMatcher = TrivialPlayer.playPattern.matcher(cardIndexString.trim());

        if (cardMatcher.matches()) {
          int cardIndex = Integer.parseInt(cardIndexString);
          if (cardIndex < 1 || cardIndex > hand.size()) {
            System.out.println("Sorry, that is not a valid card. Please try again.");
            continue;
          }
          validCard = true;
          cardToPlay = hand.remove(cardIndex - 1);
        }
      } catch (Exception e) {
        System.out.println("Sorry, a game error occurred...");
      }
    } while (!validCard);

    return cardToPlay;
  }

  /**
   * Private helper method to convert user inputted text into a Bid object.
   * 
   * @param bidMatcher the Matcher object that contains the user's matched input
   * @return Bid: the Bid created from the user's input
   */
  private Bid createBidFromMatcher(Matcher bidMatcher) {

    for (int i = 0; i < bidMatcher.groupCount(); i++) {
      System.out.println("Group " + (i) + ": " + bidMatcher.group(i));
    }

    String inputString = bidMatcher.group(0);
    if (inputString.equalsIgnoreCase("pass")) {
      return Bid.createPass();
    } else {
      BidSuit bidSuit = null;
      BidNumber bidNumber = null;
      Operation bidOp = null;

      String numString = bidMatcher.group(1);
      String writtenNumString = bidMatcher.group(2);
      String suitString = bidMatcher.group(4);

      if (numString != null) {
        bidNumber = BidNumber.values()[Integer.parseInt(numString) - 1];
      } else {
        bidNumber = BidNumber.valueOf(writtenNumString.toLowerCase());
      }
      String opString = bidMatcher.group(5);
      if (opString != null) {
        switch (opString.toLowerCase()) {
          case "double":
            bidOp = Operation.doubled;
            break;
          case "redouble":
            bidOp = Operation.redoubled;
            break;
        }
      }
      if (suitString.equalsIgnoreCase("NT")) {
        bidSuit = BidSuit.NT;
      } else {
        bidSuit = BidSuit.valueOf(suitString.toLowerCase());
      }

      if (bidOp != null) {
        if (bidOp == Operation.doubled) {
          return Bid.createDouble(bidSuit, bidNumber);
        } else {
          return Bid.createReDouble(bidSuit, bidNumber);
        }
      } else {
        return Bid.createBid(bidSuit, bidNumber);
      }
    }
  }

  /**
   * Private helper method to print the player's hand to the console.
   */
  private void printHand() {
    int handSize = hand.size();
    for (int i = 0; i < handSize; i++) {
      System.out.printf("Card %2d: %s\n", (i + 1), hand.get(i).toString());
    }
  }
}
