package org.group_b.Bid;

public class Bid implements Comparable<Bid> {

    private final BidType.BidSuit suit;
    private final BidType.BidNumber bidNumber;
    private final BidType.Operation operation;

    /**
     * Constructor for Bid class.
     *
     * @param suit      The bid's suit type.
     * @param bidNumber The bid's number.
     * @param operation The bid's operation.
     */
    private Bid(BidType.BidSuit suit, BidType.BidNumber bidNumber, BidType.Operation operation) {
        this.suit = suit;
        this.bidNumber = bidNumber;
        this.operation = operation;
    }

    /**
     * Create bid with specific suit and number.
     *
     * @param suit      The bid's suit type.
     * @param bidNumber The bid's number.
     * @return an object of Bid.
     */
    public static Bid createBid(BidType.BidSuit suit, BidType.BidNumber bidNumber) {
        return new Bid(suit, bidNumber, BidType.Operation.simplebid);
    }

    /**
     * Create double bid with specific suit and number.
     *
     * @param suit      The bid's suit type.
     * @param bidNumber The bid's number.
     * @return an object of Bid.
     */

    public static Bid createDouble(BidType.BidSuit suit, BidType.BidNumber bidNumber) {
        return new Bid(suit, bidNumber, BidType.Operation.doubled);
    }

    /**
     * Create redouble bid with specific suit and number.
     *
     * @param suit      The bid's suit type.
     * @param bidNumber The bid's number.
     * @return an object of Bid.
     */
    public static Bid createReDouble(BidType.BidSuit suit, BidType.BidNumber bidNumber) {
        return new Bid(suit, bidNumber, BidType.Operation.redoubled);
    }

    /**
     * Create pass bid.
     *
     * @return an object of Bid.
     */
    public static Bid createPass() {
        return new Bid(null, null, BidType.Operation.pass);
    }

    /**
     * Return a string representation of the Bid object, including the number and
     * suit and operation for bids.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        switch (this.operation) {
            case pass: {
                return "[Pass]";
            }
            case redoubled: {
                String basic_info = this.getBidNumber().toString() + " " + this.getBidSuit().toString();
                return "[" + basic_info + " re-doubled" + "]";
            }
            case doubled: {
                String basic_info = this.getBidNumber().toString() + " " + this.getBidSuit().toString();
                return "[" + basic_info + " doubled" + "]";
            }
            default: {
                String basic_info = this.getBidNumber().toString() + " " + this.getBidSuit().toString();
                return "[" + basic_info + "]";
            }
        }
//        if (this.operation == BidType.Operation.pass) {
//            return "[Pass]";
//        } else {
//            String basic_info = this.getBidNumber().toString() + " " + this.getBidSuit().toString();
//            if (this.operation == BidType.Operation.redoubled) {
//                return "[" + basic_info + " re-doubled" + "]";
//            } else if (this.operation == BidType.Operation.doubled) {
//                return "[" + basic_info + " doubled" + "]";
//            }
//            return "[" + basic_info + "]";
//        }
    }

    private int compare(Bid b2) {
        final int indexDiff = this.getBidNumberIndex() - b2.getBidNumberIndex();
        if (indexDiff != 0) {
            return indexDiff;
        } else {
            return this.getBidSuitIndex() - b2.getBidSuitIndex();
        }
    }

    /**
     * This interface imposes an ordering on the objects of Bid that implements it.
     * simplebid, doubled, redoubled, pass;
     * one, two, three, four, five, six, seven;
     *
     * @param b2 the Bid object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Bid b2) throws BidComparisonException {
        // both of the bids are simply making bid
        if (this.operation == BidType.Operation.simplebid && b2.operation == BidType.Operation.simplebid) {
            return this.compare(b2);
        }
        // both of the bids have the same suit and number
        else if (this.bidNumber == b2.bidNumber && this.suit == b2.suit){
            // make bid and double
            if (this.operation == BidType.Operation.simplebid && b2.operation == BidType.Operation.doubled){
                return -1;
            }
            else if (this.operation == BidType.Operation.doubled && b2.operation == BidType.Operation.simplebid){
                return 1;
            }
            // double and redouble
            else if (this.operation == BidType.Operation.doubled && b2.operation == BidType.Operation.redoubled){
                return -1;
            }
            else if (this.operation == BidType.Operation.redoubled && b2.operation == BidType.Operation.doubled){
                return 1;
            }
        }
        throw new BidComparisonException("Not comparable!");
    }

    private static class BidComparisonException extends UnsupportedOperationException {
        public BidComparisonException(String message) {
            super(message);
        }
    }

    /**
     * Get the index of this object in increasing suit order
     *
     * @return the index of this object in increasing suit order.
     */
    private int getBidSuitIndex() {
        return suit.ordinal();
    }

    /**
     * Get the suit of this bid
     *
     * @return the suit of this bid
     */
    public BidType.BidSuit getBidSuit() {
        return suit;
    }

    /**
     * Get the index of this object in increasing number order
     *
     * @return the index of this object in increasing number order.
     */
    private int getBidNumberIndex() {
        return bidNumber.ordinal();
    }

    /**
     * Get the number of this bid
     *
     * @return the number of this bid
     */
    public BidType.BidNumber getBidNumber() {
        return bidNumber;
    }

    /**
     * Get the operation of this bid
     *
     * @return the operation of this bid
     */
    public BidType.Operation getBidOperation() {
        return operation;
    }

}
