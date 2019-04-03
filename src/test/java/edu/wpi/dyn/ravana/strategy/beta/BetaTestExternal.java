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

package edu.wpi.dyn.ravana.strategy.beta;

import edu.wpi.dyn.ravana.strategy.beta.pieces.*;
import edu.wpi.dyn.ravana.strategy.required.StrategyGameFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.Board;
import strategy.Piece;
import strategy.StrategyGame;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.StrategyGame.MoveResult.*;

/**
 * For tests that do not involve any of the underlying implementation (i.e. external)
 */
class BetaTestExternal {

	private static BetaBoard board;
	private static StrategyGame game;

	/**
	 * Set up a new board and a new game for every test
	 */
	@BeforeEach
	void setupLocal() {

		/*
			            RG RP
			          BJ RL BF
			 BU          RA
			 BM
			 RB
			 RF RB BO RH BY
		 */

		board = new BetaBoard();
		board.put(new Flag(RED), 0, 0);
		board.put(new Bomb(RED), 1, 0);
		board.put(new Bomb(RED), 0, 1);
		board.put(new Miner(BLUE), 2, 0);
		board.put(new Scout(BLUE), 3, 0);
		board.put(new Colonel(BLUE), 0, 2);
		board.put(new Marshal(RED), 0, 3);
		board.put(new Spy(BLUE), 0, 4);
		board.put(new Captain(RED), 5, 5);
		board.put(new Flag(BLUE), 4, 5);
		board.put(new General(RED), 5, 4);
		board.put(new Lieutenant(RED), 4, 4);
		board.put(new Major(BLUE), 4, 3);
		board.put(new Sergeant(RED), 3, 4);

		System.out.println("Starting board state: ");
		System.out.println(board.toString());

		game = StrategyGameFactory.makeGame(StrategyGame.Version.BETA, (Board) board);
	}

	/**
	 * Test game 1 -- Red uses a marshal to take out a piece, Blue's miner takes out one of Red's defensive bombs,
	 * the red sergeant moves one, and blue finishes the game.
	 */
	@Test
	void minerFlag() {
		//
		assertThat(game.move(0, 3, 0, 2), equalTo(STRIKE_RED));
		System.out.println(board.toString());
		Piece temp = board.getPieceAt(0, 2);
		assertThat(temp.getPieceType(), equalTo(Piece.PieceType.MARSHAL));
		assertThat(temp.getPieceColor(), equalTo(RED));

		assertThat(game.move(2, 0, 1, 0), equalTo(STRIKE_BLUE));
		System.out.println(board.toString());
		temp = board.getPieceAt(1, 0);
		assertThat(temp.getPieceType(), equalTo(Piece.PieceType.MINER));
		assertThat(temp.getPieceColor(), equalTo(BLUE));

		assertThat(game.move(4, 4, 4, 3), equalTo(STRIKE_BLUE));
		System.out.println(board.toString());
		assertThat(board.getPieceAt(4, 3), equalTo(null));
		temp = board.getPieceAt(4, 4);
		assertThat(temp.getPieceType(), equalTo(Piece.PieceType.MAJOR));
		assertThat(temp.getPieceColor(), equalTo(BLUE));

		assertThat(game.move(1, 0, 0, 0), equalTo(BLUE_WINS));
		System.out.println(board.toString());
	}

}
