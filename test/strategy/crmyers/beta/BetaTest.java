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

package strategy.crmyers.beta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * For implementation-specific tests of internal components
 */
class BetaTest {

	private static BetaBoard board;

	/**
	 * Setup for tests -- run once before each test
	 */
	@BeforeEach
	void setup_local() {
		board = new BetaBoard();
	}

	/**
	 * Test board initial state
	 */
	@Test
	void boardInit() {
		// BetaBoard should start with no choke points and should be completely empty.
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getRows(); j++) {
				assertThat(board.getPieceAt(i, j), is(nullValue()));
				assertThat(board.getSquareTypeAt(i, j), is(equalTo(strategy.Board.SquareType.NORMAL)));
			}
		}
	}






}
