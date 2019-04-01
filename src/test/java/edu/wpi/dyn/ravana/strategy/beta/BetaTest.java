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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.Piece;
import strategy.StrategyException;
import strategy.StrategyGame;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.Piece.PieceType.*;

/**
 * For implementation-specific tests of internal components
 */
class BetaTest {

	private static Board board;
	private static PieceDefined m1, m2, m3;

	/**
	 * Setup for tests -- global to all
	 */
	@BeforeAll
	static void setup_global() {
		// Make some dummy pieces we can use that will respond based on their type without us actually caring about the
		// underlying implementation.
		m1 = mock(PieceDefined.class);
		when(m1.getPieceColor()).thenReturn(Piece.PieceColor.BLUE);
		when(m1.getPieceType()).thenReturn(BOMB);

		m2 = mock(PieceDefined.class);
		when(m2.getPieceColor()).thenReturn(Piece.PieceColor.RED);
		when(m2.getPieceType()).thenReturn(Piece.PieceType.CAPTAIN);

		m3 = mock(PieceDefined.class);
		when(m3.getPieceColor()).thenReturn(Piece.PieceColor.BLUE);
		when(m3.getPieceType()).thenReturn(Piece.PieceType.SPY);
	}

	/**
	 * Setup for tests -- run once before each test
	 */
	@BeforeEach
	void setup_local() {
		board = new Board();
	}

	/**
	 * Basic board sanity checking
	 */
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

	/**
	 * Can we compare boards?
	 */
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

	/**
	 * Test board initial state
	 */
	@Test
	void boardInit() {
		// Board should start with no choke points and should be completely empty.
		for (int i = 0; i < Board.ROWS; i++) {
			for (int j = 0; j < Board.COLS; j++) {
				assertThat(board.getPieceAt(i, j), is(nullValue()));
				assertThat(board.getSquareTypeAt(i, j), is(equalTo(strategy.Board.SquareType.NORMAL)));
			}
		}
	}

	/**
	 * Make sure piece direction checking works
	 */
	@Test
	void pieceCheckDirection() {
		assertFalse(PieceDefined.isDiagonal(0, 0, 1, 0));
		assertFalse(PieceDefined.isDiagonal(0, 0, 0, 1));
		assertFalse(PieceDefined.isDiagonal(5, 5, 4, 5));
		assertFalse(PieceDefined.isDiagonal(5, 5, 5, 4));
		assertTrue(PieceDefined.isDiagonal(0, 0, 1, 1));
		assertTrue(PieceDefined.isDiagonal(5, 5, 4, 4));
		assertTrue(PieceDefined.isDiagonal(5, 0, 4, 1));
		assertTrue(PieceDefined.isDiagonal(0, 5, 1, 4));
	}

	/**
	 * Make sure that piece repetition checking works. Totally not needed for this assignment, but it's simple to
	 * implement, so why not?
	 */
	@Test
	void pieceCheckRepetition() {
		// Use an anonymous class to define this piece since we really only care about moveRepetition()
		PieceDefined mobilePiece = new PieceDefined(Piece.PieceColor.BLUE) {
			@Override
			public StrategyGame.MoveResult move(Board board, int fr, int fc, int tr, int tc) throws StrategyException {
				return null;
			}

			@Override
			public StrategyGame.MoveResult strike(Piece target) {
				return null;
			}
		};

		// Make two moves to start the repetition, both are valid
		assertFalse(mobilePiece.moveRepetition(0, 0, 1, 0));
		assertFalse(mobilePiece.moveRepetition(0, 1, 0, 0));

		//Completing the repetition fails...
		assertTrue(mobilePiece.moveRepetition(0, 0, 1, 0));

		//...but moving to a different square succeeds
		assertFalse(mobilePiece.moveRepetition(0, 0, 0, 1));
	}

	/**
	 * Do pieces declare they're the right type?
	 * (why is this even a test?)
	 */
	@Test
	void pieceTypes() {
		Piece bomb = new Bomb(BLUE);
		assertThat(bomb.getPieceType(), is(equalTo(BOMB)));

		Piece captain = new Captain(RED);
		assertThat(captain.getPieceType(), is(equalTo(CAPTAIN)));

		Piece colonel = new Colonel(RED);
		assertThat(colonel.getPieceType(), is(equalTo(COLONEL)));

		Piece flag = new Flag(BLUE);
		assertThat(flag.getPieceType(), is(equalTo(FLAG)));

		Piece general = new General(RED);
		assertThat(general.getPieceType(), is(equalTo(GENERAL)));

		Piece lieutenant = new Lieutenant(BLUE);
		assertThat(lieutenant.getPieceType(), is(equalTo(LIEUTENANT)));

		Piece major = new Major(RED);
		assertThat(major.getPieceType(), is(equalTo(MAJOR)));

		Piece marshal = new Marshall(BLUE);
		assertThat(marshal.getPieceType(), is(equalTo(MARSHALL)));

		Piece miner = new Miner(RED);
		assertThat(miner.getPieceType(), is(equalTo(MINER)));

		Piece scout = new Scout(BLUE);
		assertThat(scout.getPieceType(), is(equalTo(SCOUT)));

		Piece sergeant = new Sergeant(RED);
		assertThat(sergeant.getPieceType(), is(equalTo(SERGEANT)));

		Piece spy = new Spy(BLUE);
		assertThat(spy.getPieceType(), is(equalTo(SPY)));
	}
}
