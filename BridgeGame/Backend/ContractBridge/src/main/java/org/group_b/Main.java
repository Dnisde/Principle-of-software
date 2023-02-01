package org.group_b;

import org.group_b.Database.ConnectToDatabase;
import org.group_b.Database.ConnectToRedis;
import org.group_b.Player.TrivialPlayer;
import org.group_b.Referee.SimpleReferee;

import java.security.NoSuchAlgorithmException;

public class Main {
	public static void main(String[] args) throws NoSuchAlgorithmException {

		ConnectToDatabase newConnect = new ConnectToRedis();

		TrivialPlayer[] players = new TrivialPlayer[4];
		for (int i = 0; i < 4; i++) {
			players[i] = new TrivialPlayer();
		}
		SimpleReferee ref = new SimpleReferee(players);

		System.out.println(ref.play());
		System.out.println("----------------------------------");

		for (int i = 0; i < 4; i++) {
			if (ref.amIDeclarerQ(players[i]).get()) {
				System.out.println("Declarer: player[" + i + "]");
			}
			if (ref.amIDummyQ(players[i]).get())
				System.out.println("Dummy hand: player[" + i + "]");
		}

	}
}
