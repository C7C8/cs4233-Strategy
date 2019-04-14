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

import strategy.crmyers.common.StrategyBoard;

public class GammaBoard extends StrategyBoard {

	/**
	 * Initialize the board; in Gamma strategy, there is a 2x2 block of choke points in the center.
	 */
	public GammaBoard() {
		super(6, 6);
		squares[2][2] = SquareType.CHOKE;
		squares[2][3] = SquareType.CHOKE;
		squares[3][2] = SquareType.CHOKE;
		squares[3][3] = SquareType.CHOKE;
	}

	/**
	 * Copy constructor; accepts a board, copies it to this implementation.
	 * @param board Board to copy
	 */
	public GammaBoard(strategy.Board board) {
		super(board, 6, 6);
	}
}
