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

package edu.wpi.dyn.ravana.strategy.beta.pieces;

import edu.wpi.dyn.ravana.strategy.beta.BetaBoard;
import edu.wpi.dyn.ravana.strategy.beta.PieceDefined;
import strategy.StrategyException;

/**
 * Class to represent a Scout piece.
 */
public class Scout extends PieceDefined {

	public Scout(PieceColor color) {
		super(color);
	}

	/**
	 * Move the piece.
	 *
	 * @param board BetaBoard to move on
	 * @param fr    From row
	 * @param fc    From column
	 * @param tr    To row
	 * @param tc    To column
	 * @return Result of move
	 * @throws StrategyException Thrown if move is invalid for any reason (e.g. out of bounds)
	 */
	@Override
	public MoveResult move(BetaBoard board, int fr, int fc, int tr, int tc) throws StrategyException {
		// Disabled for beta strategy
		// if (moveRepetition(fr, fc, tr, tc))
		//	 throw new StrategyException("Move repeated");
		int dx = tc - fc;
		int dy = tr - fr;
		dx /= (dx == 0 ? 1 : Math.abs(dx));
		dy /= (dy == 0 ? 1 : Math.abs(dy));

		for (int r = fr + dy, c = fc + dx; r != tr || c != tc; r += dy, c += dx) {
			if (board.getPieceAt(r, c) != null || board.getSquareTypeAt(r, c) != strategy.Board.SquareType.NORMAL)
				throw new StrategyException("Scout cannot jump over pieces/chokepoints");
		}
		return super.move(board, fr, fc, tr, tc);
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.SCOUT;
	}

	@Override
	public String toString() {
		return getColorStr() + "U";
	}
}
