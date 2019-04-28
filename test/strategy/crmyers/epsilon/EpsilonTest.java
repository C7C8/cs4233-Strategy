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

package strategy.crmyers.epsilon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.Piece;
import strategy.crmyers.common.pieces.Bomb;
import strategy.crmyers.common.pieces.Marshal;
import strategy.crmyers.common.pieces.Miner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.StrategyGame.MoveResult.*;

public class EpsilonTest {
	private EpsilonBoard board;

	@BeforeEach
	void setup_local() {
		board = new EpsilonBoard();
	}

	/**
	 * If two pieces of the same rank strike each other, the attacker wins
	 */
	@Test
	void aggressorAdvantage() {
		board.put(new Marshal(RED), 0, 0);
		board.put(new Marshal(BLUE), 0, 1);
		board.put(new Marshal(BLUE), 9, 9); // Just so red doesn't automatically win
		EpsilonGame game = new EpsilonGame(board);
		System.out.println(board.toString());

		assertThat(game.move(0, 0, 0, 1), is(equalTo(STRIKE_RED)));
		assertThat(board.getPieceAt(0, 0), is(nullValue()));
		assertThat(board.getPieceAt(0, 1).getPieceColor(), is(equalTo(RED)));
		assertThat(board.getPieceAt(0, 1).getPieceType(), is(equalTo(Piece.PieceType.MARSHAL)));
	}

	/**
	 * Bombs get only two "charges" with which to explode in Epsilon mode
	 */
	@Test
	void bombExplodiness() {
		board.put(new Bomb(BLUE), 0, 0);
		board.put(new Marshal(RED), 1, 0);
		board.put(new Marshal(BLUE), 9, 9); // Blue must have a piece to move
		board.put(new Marshal(RED), 0, 1);
		board.put(new Miner(RED), 9, 0); // Red must also have a piece to move
		board.configurePieces();
		EpsilonGame game = new EpsilonGame(board);
		System.out.println(board.toString());

		assertThat(game.move(1, 0, 0, 0), is(equalTo(STRIKE_BLUE)));
		game.move(9, 9, 9, 8);
		assertThat(game.move(0, 1, 0, 0), is(equalTo(OK)));
		assertThat(board.getPieceAt(0, 0), is(nullValue()));
		assertThat(board.getPieceAt(0, 1), is(nullValue()));
		assertThat(board.getPieceAt(1, 0), is(nullValue()));
	}
}
