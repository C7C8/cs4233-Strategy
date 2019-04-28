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

package strategy.crmyers.common.pieces;

import strategy.StrategyException;
import strategy.crmyers.common.PieceDefined;
import strategy.crmyers.common.StrategyBoardImpl;

/**
 * Class to represent a Bomb piece.
 */
public class Bomb extends PieceDefined {


	int charges;

	/**
	 * Create a new bomb
	 * @param color Bomb color
	 * @param charges How many times the bomb can explode; on the last detonation it gets removed from play
	 */
	public Bomb(PieceColor color, int charges) {
		super(color);
		this.charges = charges;
	}

	public Bomb(PieceColor color) {
		super(color);
		charges = -1;
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
	public MoveResult move(StrategyBoardImpl board, int fr, int fc, int tr, int tc) throws StrategyException {
		throw new StrategyException("Bombs cannot move");
	}

	/**
	 * Determine the outcome of a particular strike.
	 *
	 * @param target Targeted piece.
	 * @return Result of the strike!
	 */
	@Override
	public MoveResult strike(PieceDefined target) {
		throw new StrategyException("Bombs cannot strike");
	}

	/**
	 * Return what happened to this bomb; victory
	 * @param prediction Not used
	 * @return What the attacked piece says should happen
	 */
	@Override
	protected MoveResult defend(MoveResult prediction) {
		// Simple logic for bombs with unlimited charges
		if (charges < 0)
			return MoveResult.STRIKE_BOMB;
		else {
			charges--;
			if (charges == 0)
				return MoveResult.DETONATION;
			else
				return MoveResult.STRIKE_BOMB;
		}
	}

	@Override
	public String toString() {
		return getColorStr() + "B";
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.BOMB;
	}

	public void setCharges(int charges) {
		this.charges = charges;
	}
}
