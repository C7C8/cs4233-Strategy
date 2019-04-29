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

package strategy.crmyers.common;

import strategy.Piece;

/**
 * Interface for a move result processor, responsible for deciding what happens with the result of a move/strike
 */
public interface MoveResultProcessor {

	/**
	 * Process the result of a move
	 * @param result Result that the piece output
	 * @param board Board to move on
	 * @param move Object containing coordinates for move
	 * @return Processed move result
	 */
	PieceDefined.MoveResult processMove(PieceDefined.MoveResult result, StrategyBoardImpl board, StrategyGameImpl.Move move, Piece.PieceColor colorTurn);
}
