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
import strategy.crmyers.common.pieces.Marshal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static strategy.StrategyGame.MoveResult.STRIKE_RED;

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
		board.put(new Marshal(Piece.PieceColor.RED), 0, 0);
		board.put(new Marshal(Piece.PieceColor.BLUE), 0, 1);
		board.put(new Marshal(Piece.PieceColor.BLUE), 9, 9); // Just so red doesn't automatically win
		EpsilonGame game = new EpsilonGame(board);

		assertThat(game.move(0, 0, 0, 1), is(equalTo(STRIKE_RED)));
		assertThat(board.getPieceAt(0, 0), is(nullValue()));
		assertThat(board.getPieceAt(0, 1).getPieceColor(), is(equalTo(Piece.PieceColor.RED)));
		assertThat(board.getPieceAt(0, 1).getPieceType(), is(equalTo(Piece.PieceType.MARSHAL)));
	}
}
