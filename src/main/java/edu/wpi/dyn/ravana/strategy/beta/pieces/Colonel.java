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

import edu.wpi.dyn.ravana.strategy.beta.Board;
import edu.wpi.dyn.ravana.strategy.beta.PieceDefined;
import strategy.Piece;
import strategy.StrategyException;
import strategy.StrategyGame;

/**
 * Class to represent a Colonel piece.
 */
public class Colonel extends PieceDefined {

	public Colonel(PieceColor color) {
		super(color);
	}

	/**
	 * Move the piece.
	 *
	 * @param board Board to move on
	 * @param fr    From row
	 * @param fc    From column
	 * @param tr    To row
	 * @param tc    To column
	 * @return Result of move
	 * @throws StrategyException Thrown if move is invalid for any reason (e.g. out of bounds)
	 */
	@Override
	public StrategyGame.MoveResult move(Board board, int fr, int fc, int tr, int tc) throws StrategyException {
		return null;
	}

	/**
	 * Determine the outcome of a particular strike.
	 *
	 * @param target Targeted piece.
	 * @return Result of the strike!
	 */
	@Override
	public StrategyGame.MoveResult strike(Piece target) {
		return null;
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.COLONEL;
	}
}
