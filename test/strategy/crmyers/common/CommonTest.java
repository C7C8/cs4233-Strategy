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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import strategy.Board;
import strategy.Piece;
import strategy.StrategyException;
import strategy.crmyers.common.pieces.Bomb;
import strategy.crmyers.common.pieces.Captain;
import strategy.crmyers.common.pieces.Marshal;
import strategy.crmyers.common.pieces.Spy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static strategy.Piece.PieceColor.RED;
import static strategy.Piece.PieceType.BOMB;

public class CommonTest {

	public static StrategyBoardImpl board;
	public static PieceDefined marshal;
	private static PieceDefined m1, m2, m3;


	@BeforeAll
	static void setup_global() {
		board = new StrategyBoardImpl(6, 6);
		marshal = new Marshal(RED);

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
				() -> assertThrows(StrategyException.class, () -> board.put(m1, 0, -1))
		);

		board.put(m1, 0, 0);
		assertThat(board.getPieceAt(0, 0), is(equalTo(m1)));
	}

	/**
	 * Can we compare boards?
	 */
	@Test
	void boardEquals() {
		board.put(m1, 0, 0);
		board.put(m2, 1, 0);
		board.put(m3, 2, 0);

		StrategyBoardImpl board2 = new StrategyBoardImpl(6, 6);
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
	 * BetaBoard copy constructor test
	 */
	@Test
	void boardCopyConstructor() {
		// Create a mock board that doesn't rely on the BetaBoard implementation.
		Board mockBoard = mock(strategy.Board.class);
		when(mockBoard.getPieceAt(0, 0)).thenReturn(m1);
		when(mockBoard.getPieceAt(5, 5)).thenReturn(m2);
		when(mockBoard.getPieceAt(3, 3)).thenReturn(m3);
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getCols(); j++) {
				when(mockBoard.getSquareTypeAt(i, j)).thenReturn(Board.SquareType.CHOKE);
			}
		}
		when(mockBoard.getSquareTypeAt(0, 0)).thenReturn(Board.SquareType.NORMAL);
		when(mockBoard.getSquareTypeAt(5, 5)).thenReturn(Board.SquareType.NORMAL);
		when(mockBoard.getSquareTypeAt(3, 3)).thenReturn(Board.SquareType.NORMAL);

		StrategyBoardImpl realBoard = new StrategyBoardImpl(mockBoard, 6, 6);
		assertThat(realBoard.getPieceAt(0, 0).getClass(), is(equalTo(Bomb.class)));
		assertThat(realBoard.getPieceAt(5, 5).getClass(), is(equalTo(Captain.class)));
		assertThat(realBoard.getPieceAt(3, 3).getClass(), is(equalTo(Spy.class)));
		assertThat(realBoard.getSquareTypeAt(0, 0), equalTo(Board.SquareType.NORMAL));
		assertThat(realBoard.getSquareTypeAt(5, 5), equalTo(Board.SquareType.NORMAL));
		assertThat(realBoard.getSquareTypeAt(3, 3), equalTo(Board.SquareType.NORMAL));
		assertThat(realBoard.getSquareTypeAt(1, 1), equalTo(Board.SquareType.CHOKE));
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
		assertThrows(StrategyException.class, () -> marshal.move(board, 0, 0, 1, 1));
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
			public String toString() {
				return "--";
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
}
