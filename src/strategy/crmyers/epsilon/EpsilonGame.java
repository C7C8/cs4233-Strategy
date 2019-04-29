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

package strategy.crmyers.epsilon;

import strategy.Board;
import strategy.Piece;
import strategy.crmyers.common.AggressorAdvantageMoveProcessor;
import strategy.crmyers.common.PieceDefined;
import strategy.crmyers.common.StrategyBoardImpl;
import strategy.crmyers.common.StrategyGameImpl;
import strategy.crmyers.common.pieces.Bomb;
import strategy.crmyers.common.pieces.Scout;

public class EpsilonGame extends StrategyGameImpl {
	public EpsilonGame(Board board) {
		super(0, true, new AggressorAdvantageMoveProcessor());
		// Used to allow mocked test boards to be passed through, this has no effect on normal runs (seriously)
		if (!board.getClass().toString().contains("crmyers.epsilon.EpsilonBoard"))
			this.board = new EpsilonBoard(board);
		else
			this.board = (StrategyBoardImpl) board;


		// Pieces are configurable by data they each store (with sane defaults); this loop sets up
		// bombs to have charges and scouts to have an attack distance of three.
		for (int i = 0; i < this.board.getRows(); i++) {
			for (int j = 0; j < this.board.getCols(); j++) {
				PieceDefined piece = this.board.getPieceAt(i, j);
				if (piece == null)
					continue;
				if (piece.getPieceType() == Piece.PieceType.BOMB)
					((Bomb) piece).setCharges(2);
				if (piece.getPieceType() == Piece.PieceType.SCOUT)
					((Scout) piece).setAttackDistance(3);
			}
		}
	}
}
