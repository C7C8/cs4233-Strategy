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

package strategy.crmyers.gamma;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.Board;
import strategy.StrategyException;
import strategy.crmyers.common.pieces.Marshal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static strategy.Board.SquareType.CHOKE;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.StrategyGame.MoveResult.*;

public class GammaTest {
	private static GammaBoard board;

	@BeforeEach
	void setup_local() {
		board = spy(GammaBoard.class);
		doReturn(true).when(board).validateBoard(any(Integer[].class), any(Integer.class));
	}

	/**
	 * Test initial board state
	 */
	@Test()
	void boardInit() {
		// GammaBoard should start with choke points in a square in the center and should be completely empty
		assertThat(board.getRows(), is(equalTo(6)));
		assertThat(board.getCols(), is(equalTo(6)));
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getRows(); j++) {
				assertThat(board.getPieceAt(i, j), is(nullValue()));
			}
		}

		assertThat(board.getSquareTypeAt(2, 2), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(2, 3), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(3, 2), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(3, 3), is(equalTo(CHOKE)));
	}

	/**
	 * Make sure moving into choke points is disallowed
	 */
	@Test
	void chokePoints() {
		board.put(new Marshal(RED), 2, 1);
		board.put(new Marshal(BLUE), 4, 3);

		// Red tries to move into a choke point, so Blue wins
		GammaGame game = new GammaGame(board);
		assertThat(game.move(2, 1, 2, 2), is(equalTo(BLUE_WINS)));

		// Blue tries to move into a choke point, so Red wins
		game = new GammaGame(board);
		assertThat(game.move(2,1,1,1), is(equalTo(OK)));
		assertThat(game.move(4, 3, 3, 3), is(equalTo(RED_WINS)));
	}

	/**
	 * Make sure gamma rejects bad boards.
	 * Test is simple since we already have a test of the validation function elsewhere.
	 */
	@Test
	void simpleGammaValidation() {
		Board tBoard = mock(Board.class);
		doReturn(null).when(tBoard).getPieceAt(anyInt(), anyInt());
		assertThrows(StrategyException.class, () -> new GammaBoard(tBoard));
	}
}
