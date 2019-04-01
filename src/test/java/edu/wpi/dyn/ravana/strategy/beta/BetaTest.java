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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.Piece;
import strategy.StrategyException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * For implementation-specific tests of internal components
 */
class BetaTest {

	private static Board board;
	private static Piece m1, m2, m3;

	@BeforeAll
	static void setup_global() {
		// Make some dummy pieces we can use that will respond based on their type without us actually caring about the
		// underlying implementation.
		m1 = mock(Piece.class);
		when(m1.getPieceColor()).thenReturn(Piece.PieceColor.BLUE);
		when(m1.getPieceType()).thenReturn(Piece.PieceType.BOMB);

		m2 = mock(Piece.class);
		when(m2.getPieceColor()).thenReturn(Piece.PieceColor.RED);
		when(m2.getPieceType()).thenReturn(Piece.PieceType.CAPTAIN);

		m3 = mock(Piece.class);
		when(m3.getPieceColor()).thenReturn(Piece.PieceColor.BLUE);
		when(m3.getPieceType()).thenReturn(Piece.PieceType.SPY);
	}

	@BeforeEach
	void setup_local() {
		board = new Board();
	}

	@Test
	void boardSanity () {
		// Grouped into assertAll's because if you fail one bounds check test... you don't really have bounds checking
		assertAll("Board pieces sanity",
				() -> assertThrows(StrategyException.class, () -> board.getPieceAt(-1, -1)),
				() -> assertThrows(StrategyException.class, () -> board.getPieceAt(7, 7)),
				() -> assertThrows(StrategyException.class, () -> board.getPieceAt(-1, 0)),
				() -> assertThrows(StrategyException.class, () -> board.getPieceAt(0, -1))
		);

		assertAll("Board squares sanity",
				() -> assertThrows(StrategyException.class, () -> board.getSquareTypeAt(-1, -1)),
				() -> assertThrows(StrategyException.class, () -> board.getSquareTypeAt(7, 7)),
				() -> assertThrows(StrategyException.class, () -> board.getSquareTypeAt(-1, 0)),
				() -> assertThrows(StrategyException.class, () -> board.getSquareTypeAt(0, -1))
		);

		assertAll("Board put sanity",
				() -> assertThrows(StrategyException.class, () -> board.put(m1, -1, -1)),
				() -> assertThrows(StrategyException.class, () -> board.put(m1, 7, 7)),
				() -> assertThrows(StrategyException.class, () -> board.put(m1, -1, 0)),
				() -> assertThrows(StrategyException.class, () -> board.put(m1, 0, -1)),
				() -> assertThrows(StrategyException.class, () -> board.put(null, 0, 0))
		);

		board.put(m1, 0, 0);
		assertThat(board.getPieceAt(0, 0), is(equalTo(m1)));

		// Can't put a piece on a spot where there's already another piece
		assertThrows(StrategyException.class, () -> board.put(m2, 0, 0));
	}

	@Test
	void boardEquals() {
		board.put(m1, 0, 0);
		board.put(m2, 1, 0);
		board.put(m3, 2, 0);

		Board board2 = new Board();
		board2.put(m1, 0, 0);
		board2.put(m2, 1, 0);
		board2.put(m3, 2, 0);

		// Boards constructed identically, they should be identical.
		assertThat(board, is(equalTo(board2)));

		// Modify board 2 to make it have one more piece; boards no longer equal.
		board2.put(m3, 0, 1);
		assertThat(board, not(equalTo(board2)));

		// Ensure that if we put the wrong piece on the the right slot, it still won't be right
		board.put(m2, 0, 1);
		assertThat(board, not(equalTo(board2)));

		// Idiot checks to catch other branches in "equals" code
		assertThat(board, equalTo(board));
		assertThat(board, not(equalTo(null)));
		assertThat(board, not(equalTo("Why would you do this")));
	}

	@Test
	void boardSetup() {
		// Board should start with no choke points and should be completely empty.
		for (int i = 0; i < Board.ROWS; i++) {
			for (int j = 0; j < Board.COLS; j++) {
				assertThat(board.getPieceAt(i, j), is(nullValue()));
				assertThat(board.getSquareTypeAt(i, j), is(equalTo(strategy.Board.SquareType.NORMAL)));
			}
		}
	}
}
