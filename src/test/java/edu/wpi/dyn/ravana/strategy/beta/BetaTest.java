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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.Piece;
import strategy.StrategyException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * For implementation-specific tests of internal components
 */
class BetaTest {

	static Board board;

	@BeforeEach
	static void setup() {
		board = new Board();
	}

	@Test
	void boardSanity () {
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

		Piece mockPiece = mock(Piece.class);
		assertAll("Board put sanity",
				() -> assertThrows(StrategyException.class, () -> board.put(mockPiece, -1, -1)),
				() -> assertThrows(StrategyException.class, () -> board.put(mockPiece, 7, 7)),
				() -> assertThrows(StrategyException.class, () -> board.put(mockPiece, -1, 0)),
				() -> assertThrows(StrategyException.class, () -> board.put(mockPiece, 0, -1)),
				() -> assertThrows(StrategyException.class, () -> board.put(null, 0, 0))
		);

		board.put(mockPiece, 0, 0);
		assertThat(board.getPieceAt(0, 0), equalTo(mockPiece));
	}
}
