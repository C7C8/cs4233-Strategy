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

package strategy.crmyers.beta.pieces;

import strategy.crmyers.beta.BetaBoard;
import strategy.crmyers.beta.PieceDefined;
import strategy.Piece;
import strategy.StrategyException;

/**
 * Class to represent a Flag piece.
 */
public class Flag extends PieceDefined {

	public Flag(PieceColor color) {
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
		throw new StrategyException("Flags cannot move");
	}

	/**
	 * Determine the outcome of a particular strike.
	 *
	 * @param target Targeted piece.
	 * @return Result of the strike!
	 */
	@Override
	public MoveResult strike(Piece target) {
		throw new StrategyException("Flags cannot strike");
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.FLAG;
	}

	public String toString() {
		return getColorStr() + "F";
	}
}
