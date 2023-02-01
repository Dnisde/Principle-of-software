package org.group_b.Bid;

public interface BidType {
    /**
     * Suits that can be used in bid.
     */
    enum BidSuit {
        clubs,
        diamonds,
        hearts,
        spades,
        NT;
    }

    /**
     * Card numbers can be used in bridge cards.
     */
    enum BidNumber {
        one,
        two,
        three,
        four,
        five,
        six,
        seven;
    }

    enum Operation {
        simplebid,
        doubled,
        redoubled,
        pass;
    }

}
