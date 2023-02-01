package org.groub_b.Bid;

import org.group_b.Bid.Bid;
import org.group_b.Bid.BidType;
import org.group_b.Card.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;

import org.group_b.Bid.*;

public class BidTest {

    Bid clubs2, clubs4, clubs3, clubs6,
            diamonds2, diamonds4, diamonds1, diamonds7,
            hearts2, hearts4, hearts3, hearts6,
            spades5, spades2, spades4, spades1,
            NT1, NT5, NT2, NT7,
            NT1Doubled, NT3Doubled, spades1Doubled, spades2Doubled,
            clubs2Doubled, clubs3Doubled, diamonds1Doubled, diamonds2Doubled,
            hearts2Doubled, hearts3Doubled,clubs4ReDouble,clubs6ReDouble,
            diamonds4ReDouble, diamonds7ReDouble, hearts4ReDouble, hearts6ReDouble,
            spades4ReDouble, spades5ReDouble, NT5ReDouble, NT7ReDouble, Pass;

    @BeforeEach
    void setUp() {

        // Bid: clubs and number
        clubs2 = Bid.createBid(BidType.BidSuit.clubs, BidType.BidNumber.two);
        clubs3 = Bid.createBid(BidType.BidSuit.clubs, BidType.BidNumber.three);
        clubs4 = Bid.createBid(BidType.BidSuit.clubs, BidType.BidNumber.four);
        clubs6 = Bid.createBid(BidType.BidSuit.clubs, BidType.BidNumber.six);

        // Bid: diamonds and number
        diamonds1 = Bid.createBid(BidType.BidSuit.diamonds, BidType.BidNumber.one);
        diamonds2 = Bid.createBid(BidType.BidSuit.diamonds, BidType.BidNumber.two);
        diamonds4 = Bid.createBid(BidType.BidSuit.diamonds, BidType.BidNumber.four);
        diamonds7 = Bid.createBid(BidType.BidSuit.diamonds, BidType.BidNumber.seven);

        // Bid: hearts and number
        hearts2 = Bid.createBid(BidType.BidSuit.hearts, BidType.BidNumber.two);
        hearts3 = Bid.createBid(BidType.BidSuit.hearts, BidType.BidNumber.three);
        hearts4 = Bid.createBid(BidType.BidSuit.hearts, BidType.BidNumber.four);
        hearts6 = Bid.createBid(BidType.BidSuit.hearts, BidType.BidNumber.six);

        // Bid: spades and number
        spades1 = Bid.createBid(BidType.BidSuit.spades, BidType.BidNumber.one);
        spades2 = Bid.createBid(BidType.BidSuit.spades, BidType.BidNumber.two);
        spades4 = Bid.createBid(BidType.BidSuit.spades, BidType.BidNumber.four);
        spades5 = Bid.createBid(BidType.BidSuit.spades, BidType.BidNumber.five);

        // Bid: NT and number
        NT1 = Bid.createBid(BidType.BidSuit.NT, BidType.BidNumber.one);
        NT2 = Bid.createBid(BidType.BidSuit.NT, BidType.BidNumber.two);
        NT5 = Bid.createBid(BidType.BidSuit.NT, BidType.BidNumber.five);
        NT7 = Bid.createBid(BidType.BidSuit.NT, BidType.BidNumber.seven);

        // double:
        NT1Doubled = Bid.createDouble(BidType.BidSuit.NT, BidType.BidNumber.one);
        NT3Doubled = Bid.createDouble(BidType.BidSuit.NT, BidType.BidNumber.three);

        clubs2Doubled = Bid.createDouble(BidType.BidSuit.clubs, BidType.BidNumber.two);
        clubs3Doubled = Bid.createDouble(BidType.BidSuit.clubs, BidType.BidNumber.three);

        diamonds1Doubled = Bid.createDouble(BidType.BidSuit.diamonds, BidType.BidNumber.one);
        diamonds2Doubled = Bid.createDouble(BidType.BidSuit.diamonds, BidType.BidNumber.two);

        hearts2Doubled = Bid.createDouble(BidType.BidSuit.hearts, BidType.BidNumber.two);
        hearts3Doubled = Bid.createDouble(BidType.BidSuit.hearts, BidType.BidNumber.three);

        spades1Doubled = Bid.createDouble(BidType.BidSuit.spades, BidType.BidNumber.one);
        spades2Doubled = Bid.createDouble(BidType.BidSuit.spades, BidType.BidNumber.two);

        // redouble:
        clubs4ReDouble = Bid.createReDouble(BidType.BidSuit.clubs, BidType.BidNumber.four);
        clubs6ReDouble = Bid.createReDouble(BidType.BidSuit.clubs, BidType.BidNumber.six);

        diamonds4ReDouble = Bid.createReDouble(BidType.BidSuit.diamonds, BidType.BidNumber.four);
        diamonds7ReDouble = Bid.createReDouble(BidType.BidSuit.diamonds, BidType.BidNumber.seven);

        hearts4ReDouble = Bid.createReDouble(BidType.BidSuit.hearts, BidType.BidNumber.four);
        hearts6ReDouble = Bid.createReDouble(BidType.BidSuit.hearts, BidType.BidNumber.six);

        spades4ReDouble = Bid.createReDouble(BidType.BidSuit.spades, BidType.BidNumber.four);
        spades5ReDouble = Bid.createReDouble(BidType.BidSuit.spades, BidType.BidNumber.five);

        NT5ReDouble = Bid.createReDouble(BidType.BidSuit.NT, BidType.BidNumber.five);
        NT7ReDouble = Bid.createReDouble(BidType.BidSuit.NT, BidType.BidNumber.seven);

        //Pass
        Pass = Bid.createPass();
    }

    @Test
    public void returnCorrectBidSuit() {
        assertEquals(BidType.BidSuit.clubs, clubs2.getBidSuit());
        assertEquals(BidType.BidSuit.diamonds, diamonds2.getBidSuit());
        assertEquals(BidType.BidSuit.hearts, hearts2.getBidSuit());
        assertEquals(BidType.BidSuit.spades, spades2.getBidSuit());
        assertEquals(BidType.BidSuit.NT, NT2.getBidSuit());
    }


    @Test
    public void returnCorrectBidNumbers() {
        assertEquals(BidType.BidNumber.two, clubs2.getBidNumber());
        assertEquals(BidType.BidNumber.four, clubs4.getBidNumber());
        assertEquals(BidType.BidNumber.two, clubs2Doubled.getBidNumber());
        assertEquals(BidType.BidNumber.four, clubs4ReDouble.getBidNumber());
        assertEquals(null, Pass.getBidNumber());
    }

    @Test
    public void returnCorrectBidOperation() {
        assertEquals(BidType.Operation.simplebid, clubs2.getBidOperation());
        assertEquals(BidType.Operation.simplebid, clubs4.getBidOperation());
        assertEquals(BidType.Operation.doubled, clubs2Doubled.getBidOperation());
        assertEquals(BidType.Operation.redoubled, clubs4ReDouble.getBidOperation());
        assertEquals(BidType.Operation.pass, Pass.getBidOperation());
    }

    @Test
    public void returnCorrectBidOrdering() {
        assertTrue(clubs2.compareTo(diamonds2) < 0);
        assertTrue(diamonds4.compareTo(clubs4) > 0);
        assertTrue(spades2.compareTo(NT1) > 0);
        assertTrue(spades4.compareTo(clubs2) > 0);

        assertTrue(clubs2.compareTo(clubs4) < 0);
        assertTrue(diamonds2.compareTo(diamonds4) < 0);

        assertTrue(clubs4.compareTo(clubs2) > 0);
        assertTrue(diamonds4.compareTo(diamonds2) > 0);
        assertTrue(hearts4.compareTo(hearts2) > 0);
        assertTrue(spades4.compareTo(spades2) > 0);
        assertTrue(NT1.compareTo(diamonds1) > 0);
        assertTrue(NT1Doubled.compareTo(NT1) > 0);
        assertTrue(NT1.compareTo(NT1Doubled) < 0);
        assertTrue(spades2.compareTo(spades2Doubled) < 0);

        assertEquals(0, clubs2.compareTo(clubs2));
        assertEquals(0, clubs4.compareTo(clubs4));
        assertEquals(0, diamonds2.compareTo(diamonds2));
        assertEquals(0, diamonds4.compareTo(diamonds4));
        assertEquals(0, NT1.compareTo(NT1));
    }

    @Test
    public void displayCorrectOutput() {
        // make bid
        assertEquals("[two clubs]", clubs2.toString());
        assertEquals("[four clubs]", clubs4.toString());

        assertEquals("[two diamonds]", diamonds2.toString());
        assertEquals("[four diamonds]", diamonds4.toString());

        assertEquals("[two hearts]", hearts2.toString());
        assertEquals("[four hearts]", hearts4.toString());

        assertEquals("[two spades]", spades2.toString());
        assertEquals("[four spades]", spades4.toString());

        // double
        assertEquals("[two clubs doubled]", clubs2Doubled.toString());
        assertEquals("[three clubs doubled]", clubs3Doubled.toString());

        assertEquals("[one diamonds doubled]", diamonds1Doubled.toString());
        assertEquals("[two diamonds doubled]", diamonds2Doubled.toString());

        assertEquals("[two hearts doubled]", hearts2Doubled.toString());
        assertEquals("[three hearts doubled]", hearts3Doubled.toString());

        assertEquals("[one spades doubled]", spades1Doubled.toString());
        assertEquals("[two spades doubled]", spades2Doubled.toString());

        assertEquals("[one NT doubled]", NT1Doubled.toString());
        assertEquals("[three NT doubled]", NT3Doubled.toString());

        // Redouble
        assertEquals("[four clubs re-doubled]", clubs4ReDouble.toString());
        assertEquals("[six clubs re-doubled]", clubs6ReDouble.toString());

        assertEquals("[four diamonds re-doubled]", diamonds4ReDouble.toString());
        assertEquals("[seven diamonds re-doubled]", diamonds7ReDouble.toString());

        assertEquals("[four hearts re-doubled]", hearts4ReDouble.toString());
        assertEquals("[six hearts re-doubled]", hearts6ReDouble.toString());

        assertEquals("[four spades re-doubled]", spades4ReDouble.toString());
        assertEquals("[five spades re-doubled]", spades5ReDouble.toString());

        assertEquals("[five NT re-doubled]", NT5ReDouble.toString());
        assertEquals("[seven NT re-doubled]", NT7ReDouble.toString());
        assertEquals("[Pass]", Pass.toString());

    }

    @Test
    public void testException1() {

        Throwable exception = assertThrows(
                RuntimeException.class, () -> {
                    clubs2.compareTo(diamonds1Doubled);
                }
        );

        assertEquals("Not comparable!", exception.getMessage());
    }

    @Test
    public void testException2() {

        Throwable exception = assertThrows(
                RuntimeException.class, () -> {
                    diamonds1Doubled.compareTo(diamonds1Doubled);
                }
        );

        assertEquals("Not comparable!", exception.getMessage());
    }

    @Test
    public void testException3() {

        Throwable exception = assertThrows(
                RuntimeException.class, () -> {
                    Pass.compareTo(diamonds1Doubled);
                }
        );

        assertEquals("Not comparable!", exception.getMessage());
    }

    
}
