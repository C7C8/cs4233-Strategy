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

import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;

public class AggressorAdvantageMoveProcessor implements MoveResultProcessor {
	/**
	 * Process the result of a move
	 *
	 * @param result Result that the piece output
	 * @param board  Board to move on
	 * @param move   Object containing coordinates for move
	 * @param colorTurn Player that made the move
	 * @return Processed move result
	 */
	@Override
	public PieceDefined.MoveResult processMove(PieceDefined.MoveResult result, StrategyBoardImpl board, StrategyGameImpl.Move move, Piece.PieceColor colorTurn) {
		if (result == PieceDefined.MoveResult.OK ||
				(result == PieceDefined.MoveResult.STRIKE_BLUE && colorTurn == BLUE) ||
				(result == PieceDefined.MoveResult.STRIKE_RED && colorTurn == RED) ||
				(result == PieceDefined.MoveResult.STRIKE_DRAW)) {

			// Either the move was fine or there was a strike victory -- either way, the piece moves.
			board.put(board.getPieceAt(move.fr, move.fc), move.tr, move.tc);
			board.put(null, move.fr, move.fc);

			// Aggressor advantage -- if two same-ranking pieces do battle, the aggressor wins, so the result needs remapping
			result = (colorTurn == RED ? PieceDefined.MoveResult.STRIKE_RED : PieceDefined.MoveResult.STRIKE_BLUE);

		} else if ((result == PieceDefined.MoveResult.STRIKE_BLUE && colorTurn == RED) ||
				(result == PieceDefined.MoveResult.STRIKE_RED && colorTurn == BLUE)) {

			// Strike defeat, attacked piece moves into original spot
			board.put(board.getPieceAt(move.tr, move.tc), move.fr, move.fc);
			board.put(null, move.tr, move.tc);
		} // Else: some kind of victory condition

		return result;
	}
}
