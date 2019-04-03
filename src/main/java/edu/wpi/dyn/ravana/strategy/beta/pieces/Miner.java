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

import edu.wpi.dyn.ravana.strategy.beta.PieceDefined;
import strategy.Piece;

/**
 * Class to represent a Miner piece.
 */
public class Miner extends PieceDefined {

	public Miner(PieceColor color) {
		super(color);
	}

	/**
	 * Determine the outcome of a particular strike.
	 *
	 * @param target Targeted piece.
	 * @return Result of the strike!
	 */
	@Override
	public MoveResult strike(Piece target) {
		// Miners have the ability to attack bombs, so we perform that check before defaulting to regular strike rules
		if (target.getPieceType() == PieceType.BOMB)
			return pieceVictory();
		return super.strike(target);
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.MINER;
	}

	@Override
	public String toString() {
		return getColorStr() + "M";
	}
}
