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

package strategy.crmyers.delta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.Board;
import strategy.StrategyException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static strategy.Board.SquareType.CHOKE;

public class DeltaTest {

	DeltaBoard board;

	@BeforeEach
	void setup_local() {
		board = new DeltaBoard();
	}

	/**
	 * Test initial board state
	 */
	@Test
	void boardInit() {
		// EpsilonBoard should start with choke points in two squares in the center and should be completely empty
		assertThat(board.getRows(), is(equalTo(10)));
		assertThat(board.getCols(), is(equalTo(10)));
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getRows(); j++) {
				assertThat(board.getPieceAt(i, j), is(nullValue()));
			}
		}

		// Left choke square
		assertThat(board.getSquareTypeAt(4, 2), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(4, 3), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(5, 2), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(5, 3), is(equalTo(CHOKE)));

		// Right choke square
		assertThat(board.getSquareTypeAt(4, 6), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(4, 7), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(5, 6), is(equalTo(CHOKE)));
		assertThat(board.getSquareTypeAt(5, 7), is(equalTo(CHOKE)));
	}

	/**
	 * Make sure delta rejects bad boards.
	 * Test is simple since we already have a test of the validation function elsewhere.
	 */
	@Test
	void simpleDeltaValidation() {
		Board tBoard = mock(Board.class);
		doReturn(null).when(tBoard).getPieceAt(anyInt(), anyInt());
		assertThrows(StrategyException.class, () -> new DeltaBoard(tBoard));
	}
}
