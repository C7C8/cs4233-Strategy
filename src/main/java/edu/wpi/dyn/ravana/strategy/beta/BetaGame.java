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

package edu.wpi.dyn.ravana.strategy.beta;

import strategy.Board;
import strategy.Piece;
import strategy.StrategyException;
import strategy.StrategyGame;

import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.StrategyGame.MoveResult.*;

public class BetaGame implements StrategyGame {
	private BetaBoard board;
	private Piece.PieceColor colorTurn;
	private int turns;

	public BetaGame(Board board) {
		if (board.getClass() == BetaBoard.class)
			this.board = (BetaBoard) board;
		else
			this.board = new BetaBoard(board);

		colorTurn = RED;
		turns = 0;
	}

	/**
	 * Make a move!
	 * @param fr from row..
	 * @param fc from column...
	 * @param tr ...to row
	 * @param tc ...to column
	 * @return Result of move
	 */
	@Override
	public MoveResult move(int fr, int fc, int tr, int tc) {
		// Sanity check for game over
		if (turns >= 8)
			return MoveResult.GAME_OVER;

		// Sanity checks to make sure that the player is grabbing a piece and it's the right color
		final PieceDefined fPiece = board.getPieceAt(fr, fc);
		if (fPiece == null || fPiece.getPieceColor() != colorTurn)
			return defaultVictory();

		final PieceDefined.MoveResult result;
		try {
			result = fPiece.move(board, fr, fc, tr, tc);

			// === Move result processing ===
			if (result == PieceDefined.MoveResult.OK ||
					(result == PieceDefined.MoveResult.STRIKE_BLUE && colorTurn == BLUE) ||
					(result == PieceDefined.MoveResult.STRIKE_RED && colorTurn == RED)) {

				// Either the move was fine or there was a strike victory -- either way, the piece moves.
				board.put(fPiece, tr, tc);
				board.put(null, fr, fc);
			} else if ((result == PieceDefined.MoveResult.STRIKE_BLUE && colorTurn == RED) ||
					(result == PieceDefined.MoveResult.STRIKE_RED && colorTurn == BLUE)) {

				// Strike defeat, attacked piece moves into original spot
				board.put(board.getPieceAt(tr, tc), fr, fc);
				board.put(null, tr, tc);
			}
			else if (result == PieceDefined.MoveResult.STRIKE_DRAW) {

				// Draw, both pieces eliminated
				board.put(null, tr, tc);
				board.put(null, fr, fc);
			} else {

				// Some kind of victory condition
				turns = 8;
				return convertMoveResult(result);
			}
		} catch (StrategyException ex) {
			// Moving player screwed up; opponent wins.
			System.err.println(ex.getMessage());

			turns = 8; // End the game
			return defaultVictory();
		}

		// Logic for incrementing turns and determining who goes next
		if (colorTurn == RED)
			colorTurn = BLUE;
		else {
			colorTurn = RED;
			turns++;
		}

		return convertMoveResult(result);
	}

	/**
	 * Dumb helper function for returning victory condition based on when a player messes up.
	 * @return BLUE_WINS if colorTurn is RED, RED_WINS if colorTurn is blue.
	 */
	private MoveResult defaultVictory() {
		return colorTurn == RED ? BLUE_WINS	: RED_WINS;
	}

	/**
	 * This BetaStrategy implementation uses an extended MoveResult enum to make move handling
	 * a little easier; this function serves as a bridge between the two.
	 * @param result Move result in PieceDefined format
	 * @return Move result in Strategy format
	 */
	private MoveResult convertMoveResult(PieceDefined.MoveResult result) {
		if (result == PieceDefined.MoveResult.STRIKE_DRAW)
			return OK;
		else
			return MoveResult.values()[result.ordinal()];
	}
}
