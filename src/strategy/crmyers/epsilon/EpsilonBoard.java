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
import strategy.crmyers.common.PieceDefined;
import strategy.crmyers.common.pieces.Bomb;
import strategy.crmyers.delta.DeltaBoard;

/**
 * Epsilon board, really just a re-do of DeltaBoard since the board contains no further game logic.
 */
public class EpsilonBoard extends DeltaBoard {
	public EpsilonBoard() {
		super();
		configurePieces();
	}

	public EpsilonBoard(Board board) {
		super(board);
		configurePieces();
	}

	/**
	 * Set all bombs to explodey mode (because they weren't already?)
	 */
	public void configurePieces() {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				PieceDefined piece = pieces[i][j];
				if (piece == null)
					continue;
				if (piece.getPieceType() == Piece.PieceType.BOMB)
					((Bomb) piece).setCharges(2);
			}
		}
	}
}
