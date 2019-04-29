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

import strategy.Board;
import strategy.crmyers.delta.DeltaBoard;

/**
 * Epsilon board, really just a re-do of DeltaBoard since the board contains no further game logic.
 */
public class EpsilonBoard extends DeltaBoard {

	/**
	 * Construct an Epsilon board. No validation.
	 * @see EpsilonBoard
	 */
	public EpsilonBoard() {
		super();
	}

	/**
	 * Construct an Epsilon board based on a given board. Validates input.
	 * @param board Board to copy.
	 * @see EpsilonBoard
	 */
	public EpsilonBoard(Board board) {
		super(board);
	}
}
