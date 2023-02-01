package org.group_b.Referee;

import java.util.*;

import org.group_b.Bid.Bid;
import org.group_b.Bid.BidType;
import org.group_b.Player.TrivialPlayer;

public class SimpleReferee extends Referee {

  // player index start from 0 to 3
  private int startBidPlayerIndex;
  private int declarerIndex;
  private List<Bid> curtBidsList = new ArrayList<>();
  private int countPass = 0;
  private boolean isBidEnd = false;
  private Bid preSimpleBid = null;

  /**
   * Constructor
   * 
   * @param players four players in bidding process
   */
  public SimpleReferee(TrivialPlayer[] players) {
    super(players);
  }

  /**
   ** @return a list of the bids made so far in the deal. If the bidding process
   **         has completed, then this contains the entire history of bids made.
   */
  public List<Bid> getBids() {
    String response = "";
    if (curtBidsList.size() == 3 && isBidEnd) {
      response = "No winner in this bidding process, please start a new bid.";
    } else if (isBidEnd) {
      response = "Bidding process has completed, here are all bids made: ";
    } else {
      response = "Bidding process has not completed, here are the bids made so far: ";
    }
    System.out.println(response);

    return new ArrayList<>(curtBidsList);
  }

  /**
   * Records the declarer with contract
   */
  private class Contract {
    private final Bid winBid;
    private final int declarerIndex;

    /**
     * Constructor of Contract
     * 
     * @param winBid        contract
     * @param declarerIndex the index of declarer in players list
     */
    public Contract(Bid winBid, int declarerIndex) {
      this.winBid = winBid;
      this.declarerIndex = declarerIndex;
    }

    @Override
    public String toString() {
      return "Declarer: player[" + declarerIndex + "]; Contract: " + winBid;
    }
  }

  /**
   ** @return the contract that was decided during bidding in this deal, together
   *         with the declarer who won it.
   **         If the bidding process has not yet completed, then this method will
   *         return an empty Optional.
   */
  public Optional<Contract> getContract() {
    Optional<Contract> contract = Optional.empty();
    if (!isBidEnd || curtBidsList.size() <= 3) {
      return contract;
    }

    for (int bidIndex = curtBidsList.size() - 4; bidIndex >= 0; bidIndex--) {
      if ((curtBidsList.get(bidIndex)).getBidOperation() == BidType.Operation.simplebid) {
        Bid winBid = curtBidsList.get(bidIndex);
        declarerIndex = (bidIndex + startBidPlayerIndex) % 4;
        contract = Optional.of(new Contract(winBid, declarerIndex));
        break;
      }
    }
    return contract;
  }

  /**
   ** @return true if the specified Player has been made the declarer after the
   *         bidding process.
   **         If the bidding process has not yet completed, then this method will
   *         return an empty Optional.
   */
  public Optional<Boolean> amIDeclarerQ(TrivialPlayer thePlayer) {
    Optional<Boolean> isDeclarer = Optional.empty();
    if (getContract().isPresent()) {
      isDeclarer = Optional.of(players[declarerIndex] == thePlayer);
    }
    return isDeclarer;
  }

  /**
   ** @return true if the specified Player has been made the _dummy_ after the
   *         bidding process.
   **         If the bidding process has not yet completed, then this method will
   *         return an empty Optional.
   */
  public Optional<Boolean> amIDummyQ(TrivialPlayer thePlayer) {
    Optional<Boolean> isDummyQ = Optional.empty();
    if (getContract().isPresent()) {
      int dummyIndex = (declarerIndex + 2) % 4;
      isDummyQ = Optional.of(players[dummyIndex] == thePlayer);
    }
    return isDummyQ;
  }

  /**
   * Manage the bidding process:
   * Starting with a randomly chosen player, the Referee should request a bid from
   * each player, in order, verifying that bids are consistent with the bidding
   * rules.
   * 
   * After three passes in a row, it should display:
   * The winning bid that made the contract.
   * The team that won the contract.
   * The person who declared the contract.
   */

  @Override
  public Score play() {
    super.play();
    List<Integer> givenList = Arrays.asList(0, 1, 2, 3);
    Random rand = new Random();
    this.startBidPlayerIndex = givenList.get(rand.nextInt(givenList.size()));

    while (!isBidEnd) {
      playModule();
      System.out.println("Current bids: " + curtBidsList.toString());
      System.out.println("--------------------------------------------");
    }
    if (curtBidsList.size() == 3) {
      System.out.println("Contract failed, start a new deal!");
      System.out.println("--------------------------------------------");
      countPass = 0;
      curtBidsList.clear();
      isBidEnd = false;
      preSimpleBid = null;
      return play();
    }
    System.out.println("Start Player: Player " + getStartBidPlayerIndex());
    System.out.println("Finial bids: " + getBids());
    System.out.println(getContract());
    System.out.println("Winning team: " + (declarerIndex % 2 == 1 ? "oddTeam" : "evenTeam"));

    return new Score(gameScore);
  }

  /**
   * rules for bidding process
   */
  public void playModule() {
    Bid curtBid = players[(curtBidsList.size() + startBidPlayerIndex) % 4].makeBid();
    if (isBidEnd) {
      System.out.println("Bid is Ended!");
      return;
    }

    // Cur Bid is Pass
    if (curtBid.getBidOperation() == BidType.Operation.pass) {
      countPass++;
      if (countPass == 3) {
        curtBidsList.add(curtBid);
        isBidEnd = true;
        return;
      }
    }
    // other than pass
    else if (curtBidsList.size() > 0) {
      // simple bid
      if (curtBid.getBidOperation() == BidType.Operation.simplebid) {
        // simple bid needs to compared with pre simple bid
        if (preSimpleBid != null && preSimpleBid.compareTo(curtBid) >= 0) {
          System.out.println("Invalid Bid! Please make a larger bid or pass!");
          return;
        }
      }
      // double and redouble
      else {
        // check the consistency:
        // simple bid and double
        // double and re double
        try {
          // make sure the bid is larger than the pre simple bid
          if (curtBidsList.get(curtBidsList.size() - 1).compareTo(curtBid) >= 0) {
            System.out.println("Invalid Bid! Please make a legal double or redouble!");
            return;
          }
        } catch (Exception e) {
          System.out.println("Invalid Bid! Please make a legal double or redouble!");
          return;
        }
      }
    }
    // size = 0
    else {
      if (curtBid.getBidOperation() != BidType.Operation.simplebid) {
        System.out.println("Invalid Bid! Please make a simple bid or pass!");
        return;
      }
    }
    // reset countPass to 0
    if (curtBid.getBidOperation() != BidType.Operation.pass) {
      countPass = 0;
    }
    // log the most recent simple bid
    if (curtBid.getBidOperation() == BidType.Operation.simplebid) {
      preSimpleBid = curtBid;
    }
    // log valid bid
    curtBidsList.add(curtBid);

    return;
  }

  /**
   * @return index of player who start the bidding process
   */
  public int getStartBidPlayerIndex() {
    return startBidPlayerIndex;
  }

}
