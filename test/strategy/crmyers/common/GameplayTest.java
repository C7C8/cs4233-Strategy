/*
 * Copyright (c) 2019 Christopher Myers
 *
 * This file is part of cs4233-strategy.
 *
 * cs4233-strategy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * cs4233-strategy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with cs4233-strategy.  If not, see <https://www.gnu.org/licenses/>.
 * ======
 *
 * This file was developed as part of CS 4233: Object Oriented Analysis &
 * Design, at Worcester Polytechnic Institute.
 */

package strategy.crmyers.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.StrategyGame;
import strategy.crmyers.beta.BetaBoard;
import strategy.crmyers.common.pieces.*;
import strategy.required.StrategyGameFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.StrategyGame.MoveResult.*;

/**
 * Tests that should work across all game modes
 */
public abstract class GameplayTest {
	protected static StrategyBoardImpl board;
	protected static StrategyGame game;

	/**
	 * Set up a new board and a new game for every test
	 */
	@BeforeEach
	void setup_local() {
		/*
			             RG RP
			          BJ RL BF
			 BU          RA
			 BM
			 RB       BH
			 RF RB BO RH BY
		 */
		boardConfig();
		board.put(new Flag(RED), 0, 0);
		board.put(new Bomb(RED), 1, 0);
		board.put(new Bomb(RED), 0, 1);
		board.put(new Miner(BLUE), 2, 0);
		board.put(new Scout(BLUE), 3, 0);
		board.put(new Colonel(BLUE), 0, 2);
		board.put(new Marshal(RED), 0, 3);
		board.put(new Marshal(BLUE), 1, 3);
		board.put(new Spy(BLUE), 0, 4);
		board.put(new Captain(RED), 5, 5);
		board.put(new Flag(BLUE), 4, 5);
		board.put(new General(RED), 5, 4);
		board.put(new Lieutenant(RED), 4, 4);
		board.put(new Major(BLUE), 4, 3);
		board.put(new Sergeant(RED), 3, 4);
		gameConfig();
		System.out.println("Starting board state: ");
		System.out.println(board.toString());
	}

	/**
	 * Called once to create the right board type (subclass specific)
	 */
	protected abstract void boardConfig();

	/**
	 * Called once to create the right game type (subclass specific)
	 */
	protected abstract void gameConfig();

	/**
	 * Test game 1 -- Red uses a marshal to take out a piece, Blue's miner takes out one of Red's defensive bombs,
	 * the red sergeant moves one, and blue finishes the game.
	 */
	@Test
	void minerFlag() {
		assertThat(game.move(0, 3, 0, 2), equalTo(STRIKE_RED));
		assertThat(game.move(2, 0, 1, 0), equalTo(STRIKE_BLUE));
		assertThat(game.move(4, 4, 4, 3), equalTo(STRIKE_BLUE));
		assertThat(game.move(1, 0, 0, 0), equalTo(BLUE_WINS));
	}

	/**
	 * Test game 2 -- Red lieutenant tries to take out a blue major (fails), Blue tries (fails) to kill Red's marshall
	 * Red's sergeant tries (fails) to take out Blue's major, Blue's spy inches closer to Red's marshal, Red's general
	 * prepares for another strike, Blue's spy assassinates Red's marshal, Red goes for the kill and uses a captain to
	 * take blue's flag (which was there all along). Further moves result in game over.
	 */
	@Test
	void secondTimeCharm() {
		assertThat(game.move(4, 4, 4, 3), equalTo(STRIKE_BLUE));
		assertThat(game.move(0, 2, 0, 3), equalTo(STRIKE_RED));
		assertThat(game.move(3, 4, 4, 4), equalTo(STRIKE_BLUE));
		assertThat(game.move(0, 4, 0, 3), equalTo(OK));
		assertThat(game.move(5, 4, 4, 4), equalTo(OK));
		assertThat(game.move(0, 3, 0, 2), equalTo(STRIKE_BLUE));
		assertThat(game.move(5, 5, 4, 5), equalTo(RED_WINS));

		assertThat(game.move(0, 3, 0, 4), equalTo(GAME_OVER));
	}

	/**
	 * Blue moves out of turn and loses the game
	 */
	@Test
	void blueFUBAR() {
		assertThat(game.move(3, 0, 3, 1), equalTo(BLUE_WINS));
		assertThat(game.move(3, 0, 3, 1), equalTo(GAME_OVER));
	}

	/**
	 * Red tries to move a piece that doesn't exist, instantly causes Blue to win.
	 */
	@Test
	void redFUBAR() {
		assertThat(game.move(5, 0, 5, 4), equalTo(BLUE_WINS));
		assertThat(game.move(5, 0, 5, 4), equalTo(GAME_OVER));
	}

	/**
	 * Red and Blue engage in mutual annihilation when Red's marshal unknowingly tries to take out Blue's marshal.
	 * Blue then fails to understand how movement works and tries to move diagonally, causing an instant Red win.
	 *
	 * (these test names are getting INTERESTING!)
	 */
	@Test
	void redSNAFU() {
		assertThat(game.move(0, 3, 1, 3), equalTo(OK));
		assertThat(game.move(3, 0, 4, 1), equalTo(RED_WINS));
	}

	/**
	 * What happens when a Red general tries to take on a Red captain? Friendly fire and a win for Blue, that's what.
	 */
	@Test
	void friendlyFire() {
		assertThat(game.move(5,4,5,5), equalTo(BLUE_WINS));
		assertThat(game.move(5,4,5,5), equalTo(GAME_OVER));
	}

	/**
	 * When a player runs out of moves to make, the other player should win immediately, even if they themselves have
	 * no available moves to make.
	 */
	@Test
	void noMovesPossible() {
		BetaBoard tBoard = new BetaBoard();
		tBoard.put(new Bomb(BLUE), 0, 0);
		tBoard.put(new Flag(BLUE), 0, 1);
		tBoard.put(new Marshal(RED), 1, 0);
		game = StrategyGameFactory.makeGame(StrategyGame.Version.BETA, tBoard);

		assertThat(game.move(1, 0, 0, 0), equalTo(RED_WINS));
	}

	/**
	 * Make sure pieces can't move from someplace out of bounds
	 */
	@Test
	void badSourceSquare() {
		assertThat(game.move(-1, 0, 0, 0), equalTo(BLUE_WINS));
	}
}
