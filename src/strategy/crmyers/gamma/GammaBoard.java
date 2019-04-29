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

import strategy.StrategyException;
import strategy.crmyers.common.StrategyBoardImpl;

import static strategy.Piece.PieceType.*;

public class GammaBoard extends StrategyBoardImpl {

	/**
	 * Initialize the board; in Gamma strategy, there is a 2x2 block of choke points in the center.
	 */
	public GammaBoard() {
		super(6, 6);
		configureBoard();
	}

	/**
	 * Copy constructor; accepts a board, copies it to this implementation.
	 * @param board Board to copy
	 */
	public GammaBoard(strategy.Board board) {
		super(board, 6, 6);

		// Validate pieces, configure board with choke points
		final int[] refPieceCounts = new int[12];
		refPieceCounts[FLAG.ordinal()] = 1;
		refPieceCounts[MARSHAL.ordinal()] = 1;
		refPieceCounts[COLONEL.ordinal()] = 2;
		refPieceCounts[CAPTAIN.ordinal()] = 2;
		refPieceCounts[LIEUTENANT.ordinal()] = 3;
		refPieceCounts[SERGEANT.ordinal()] = 3;
		if (!validateBoard(refPieceCounts, 2))
			throw new StrategyException("Failed to validate board");
		configureBoard();
	}

	/**
	 * Configure the board with choke points
	 */
	protected void configureBoard() {
		squares[2][2] = SquareType.CHOKE;
		squares[2][3] = SquareType.CHOKE;
		squares[3][2] = SquareType.CHOKE;
		squares[3][3] = SquareType.CHOKE;
		pieces[2][2] = null;
		pieces[2][3] = null;
		pieces[3][2] = null;
		pieces[3][3] = null;
	}
}
