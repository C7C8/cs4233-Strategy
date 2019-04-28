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
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static strategy.Piece.PieceColor.RED;
import static strategy.Piece.PieceType.BOMB;

public class CommonTest {

	public static StrategyBoardImpl board;
	public static PieceDefined marshal;
	private static PieceDefined blueBomb, redBomb, redCaptain, blueSpy;


	@BeforeAll
	static void setup_global() {
		board = new StrategyBoardImpl(6, 6);
		marshal = new Marshal(RED);

		// Make some dummy pieces we can use that will respond based on their type without us actually caring about the
		// underlying implementation.
		blueBomb = mock(PieceDefined.class);
		when(blueBomb.getPieceColor()).thenReturn(Piece.PieceColor.BLUE);
		when(blueBomb.getPieceType()).thenReturn(BOMB);
		redBomb = mock(PieceDefined.class);
		when(redBomb.getPieceColor()).thenReturn(Piece.PieceColor.RED);
		when(redBomb.getPieceType()).thenReturn(BOMB);
		System.out.println(redBomb.getPieceColor());

		redCaptain = mock(PieceDefined.class);
		when(redCaptain.getPieceColor()).thenReturn(Piece.PieceColor.RED);
		when(redCaptain.getPieceType()).thenReturn(Piece.PieceType.CAPTAIN);

		blueSpy = mock(PieceDefined.class);
		when(blueSpy.getPieceColor()).thenReturn(Piece.PieceColor.BLUE);
		when(blueSpy.getPieceType()).thenReturn(Piece.PieceType.SPY);
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
				() -> assertThrows(StrategyException.class, () -> board.put(blueBomb, -1, -1)),
				() -> assertThrows(StrategyException.class, () -> board.put(blueBomb, 7, 7)),
				() -> assertThrows(StrategyException.class, () -> board.put(blueBomb, -1, 0)),
				() -> assertThrows(StrategyException.class, () -> board.put(blueBomb, 0, -1))
		);

		board.put(blueBomb, 0, 0);
		assertThat(board.getPieceAt(0, 0), is(equalTo(blueBomb)));
	}

	/**
	 * Can we compare boards?
	 */
	@Test
	void boardEquals() {
		board.put(blueBomb, 0, 0);
		board.put(redCaptain, 1, 0);
		board.put(blueSpy, 2, 0);

		StrategyBoardImpl board2 = new StrategyBoardImpl(6, 6);
		board2.put(blueBomb, 0, 0);
		board2.put(redCaptain, 1, 0);
		board2.put(blueSpy, 2, 0);

		// Boards constructed identically, they should be identical.
		assertThat(board, is(equalTo(board2)));

		// Modify board 2 to make it have one more piece; boards no longer equal.
		board2.put(blueSpy, 0, 1);
		assertThat(board, not(equalTo(board2)));

		// Ensure that if we put the wrong piece on the the right slot, it still won't be right
		board.put(redCaptain, 0, 1);
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
		when(mockBoard.getPieceAt(0, 0)).thenReturn(blueBomb);
		when(mockBoard.getPieceAt(5, 5)).thenReturn(redCaptain);
		when(mockBoard.getPieceAt(3, 3)).thenReturn(blueSpy);
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
	 * Non-scout pieces can't move more than one square at a time
	 */
	@Test
	void noMultipleMotion() {
		Marshal marshal = new Marshal(RED);
		assertThrows(StrategyException.class, () -> marshal.move(board, 0, 0, 0, 2));
	}

	/**
	 * Boards improper boards must be rejected
	 */
	@Test
	void boardValidation() {
		StrategyBoardImpl board = new StrategyBoardImpl(6, 6);
		Integer[] refPiecesCount = new Integer[12];

		// Base case -- expecting one row of units, but we have none, so don't validate.
		assertThat(board.validateBoard(refPiecesCount, 1), is(equalTo(false)));

		// Put a row of red bombs on the bottom -- still doesn't work, blue doesn't have enough bombs yet
		refPiecesCount[BOMB.ordinal()] = 6;
		for (int i = 0; i < board.getCols(); i++) {
			board.put(redBomb, 0, i);
		}
		assertThat(board.validateBoard(refPiecesCount, 1), is(equalTo(false)));

		// Put a row of blue bombs on top, validates now!
		for (int i = 0; i < board.getCols(); i++) {
			board.put(blueBomb, 5, i);
		}
		assertThat(board.validateBoard(refPiecesCount, 1), is(equalTo(true)));

	}
}
