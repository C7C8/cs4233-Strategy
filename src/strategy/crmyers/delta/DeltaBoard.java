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

import strategy.StrategyException;
import strategy.crmyers.common.StrategyBoardImpl;

import static strategy.Board.SquareType.CHOKE;
import static strategy.Piece.PieceType.*;

public class DeltaBoard extends StrategyBoardImpl{

	/**
	 * Initialize the board; Delta board is a full size board with a full suite of choke points
	 */
	public DeltaBoard() {
		super(10, 10);
		configureBoard();
	}

	public DeltaBoard(strategy.Board board) {
		super(board, 10, 10);

		//Validate pieces, configure board with choke points
		final Integer[] refPieceCounts = new Integer[12];
		refPieceCounts[MARSHAL.ordinal()]		= 1;
		refPieceCounts[GENERAL.ordinal()] 		= 1;
		refPieceCounts[COLONEL.ordinal()] 		= 2;
		refPieceCounts[MAJOR.ordinal()]			= 3;
		refPieceCounts[CAPTAIN.ordinal()]		= 4;
		refPieceCounts[LIEUTENANT.ordinal()] 	= 4;
		refPieceCounts[SERGEANT.ordinal()] 		= 4;
		refPieceCounts[MINER.ordinal()] 		= 5;
		refPieceCounts[SCOUT.ordinal()] 		= 8;
		refPieceCounts[SPY.ordinal()] 			= 1;
		refPieceCounts[BOMB.ordinal()] 			= 6;
		refPieceCounts[FLAG.ordinal()] 			= 1;
		if (!validateBoard(refPieceCounts, 4))
			throw new StrategyException("Failed to validate board");
		configureBoard();
	}

	protected void configureBoard() {
		squares[4][2] = CHOKE;
		squares[5][2] = CHOKE;
		squares[4][3] = CHOKE;
		squares[5][3] = CHOKE;
		pieces[4][2] = null;
		pieces[5][2] = null;
		pieces[4][3] = null;
		pieces[5][3] = null;

		squares[4][6] = CHOKE;
		squares[5][6] = CHOKE;
		squares[4][7] = CHOKE;
		squares[5][7] = CHOKE;
		pieces[4][6] = null;
		pieces[5][6] = null;
		pieces[4][7] = null;
		pieces[5][7] = null;
	}
}
