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
import strategy.StrategyException;

import java.util.ArrayDeque;

import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.StrategyGame.MoveResult.*;

public class StrategyGameImpl implements strategy.StrategyGame {

	// Game data
	protected StrategyBoardImpl board;
	protected Piece.PieceColor colorTurn;
	protected int turns;
	protected boolean gameOver;
	protected ArrayDeque<Move> redMoves;
	protected ArrayDeque<Move> blueMoves;

	// Game configuration
	protected final int maxTurns; // Unlimited if zero
	protected final boolean noRepeatMoves;


	public StrategyGameImpl(int maxTurns, boolean noRepeatMoves) {
		this.board = new StrategyBoardImpl(10, 10); // sane default...?
		this.maxTurns = maxTurns;
		this.noRepeatMoves = noRepeatMoves;
		colorTurn = RED;
		turns = 0;
		gameOver = false;
		redMoves = new ArrayDeque<>();
		blueMoves = new ArrayDeque<>();
	}

	/**
	 * Make a move!
	 *
	 * @param fr from row..
	 * @param fc from column...
	 * @param tr ...to row
	 * @param tc ...to column
	 * @return Result of move
	 */
	@Override
	public MoveResult move(int fr, int fc, int tr, int tc) {
		// Sanity check for game over
		if (gameOver)
			return GAME_OVER;

		// Sanity checks to make sure that the player is grabbing a piece and it's the right color.
		final PieceDefined fPiece;
		try {
			fPiece = board.getPieceAt(fr, fc);
		} catch (StrategyException ex) {
			gameOver = true;
			return colorTurn == RED ? BLUE_WINS : RED_WINS;
		}
		if (fPiece == null) {
			gameOver = true;
			return colorTurn == RED ? BLUE_WINS : RED_WINS;
		}
		if (fPiece.getPieceColor() != colorTurn) {
			gameOver = true;
			return colorTurn == RED ? BLUE_WINS : RED_WINS;
		}


		PieceDefined.MoveResult result;
		try {
			result = fPiece.move(board, fr, fc, tr, tc);
			final boolean struck = (result == PieceDefined.MoveResult.STRIKE_RED
					|| result == PieceDefined.MoveResult.STRIKE_BLUE
					|| result == PieceDefined.MoveResult.STRIKE_DRAW);
			if (!struck && noRepeatMoves)
				checkRepeatMoves(fr, fc, tr, tc);

			// If a strike happened, clear the move queue so we cancel out move repetition
			if (struck)
				(colorTurn == RED ? redMoves : blueMoves).clear();

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
			} else if (result == PieceDefined.MoveResult.STRIKE_DRAW) {

				// Draw, both pieces eliminated
				board.put(null, tr, tc);
				board.put(null, fr, fc);
			} else {
				// Some kind of victory condition
				gameOver = true;
				return convertMoveResult(result);
			}
		} catch (StrategyException ex) {
			// Moving player screwed up; opponent wins.
			System.err.println(ex.getMessage());

			gameOver = true;
			result = colorTurn == RED ? PieceDefined.MoveResult.BLUE_WINS : PieceDefined.MoveResult.RED_WINS;
		}

		// Logic for incrementing turns and determining who goes next
		if (colorTurn == RED)
			colorTurn = BLUE;
		else {
			colorTurn = RED;
			turns++;

			// If max turns is enabled and the number of turns is in excess, red wins
			if (maxTurns > 0 && turns >= maxTurns)
				return RED_WINS;
		}

		System.out.println("Turn " + turns + ", color: " + colorTurn.name());
		System.out.println(board.toString());
		return convertMoveResult(result);
	}

	/**
	 * This BetaStrategy implementation uses an extended MoveResult enum to make move handling
	 * a little easier; this function serves as a bridge between the two.
	 *
	 * @param result Move result in PieceDefined format
	 * @return Move result in Strategy format
	 */
	private MoveResult convertMoveResult(PieceDefined.MoveResult result) {
		if (result == PieceDefined.MoveResult.STRIKE_DRAW)
			return OK;
		else
			return MoveResult.values()[result.ordinal()];
	}

	/**
	 * Helper class to store moves
	 */
	static class Move {
		final int fr;
		final int fc;
		final int tr;
		final int tc;

		Move(int fr, int fc, int tr, int tc) {
			this.fr = fr;
			this.fc = fc;
			this.tr = tr;
			this.tc = tc;
		}

		// Autogenerated
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Move move = (Move) o;
			return fr == move.fr &&
					fc == move.fc &&
					tr == move.tr &&
					tc == move.tc;
		}
	}

	private void checkRepeatMoves(int fr, int fc, int tr, int tc) throws StrategyException {
		Move move = new Move(fr, fc, tr, tc);
		ArrayDeque<Move> curQueue = colorTurn == RED ? redMoves : blueMoves;
		if (curQueue.size() == 2 && move.equals(curQueue.pollLast()))
			throw new StrategyException("Move repeated");
		curQueue.addFirst(move);
	}
}
