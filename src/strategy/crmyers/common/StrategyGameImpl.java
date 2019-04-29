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

import strategy.Board;
import strategy.Piece;
import strategy.StrategyException;

import java.util.ArrayDeque;

import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.StrategyGame.MoveResult.*;

public class StrategyGameImpl implements strategy.StrategyGame {

	// Game data
	protected StrategyBoardImpl board;
	private Piece.PieceColor colorTurn;
	private int turns;
	private boolean gameOver;
	private final ArrayDeque<Move> redMoves;
	private final ArrayDeque<Move> blueMoves;

	// Game configuration
	private final int maxTurns; // Unlimited if zero
	private final boolean noRepeatMoves;
	private final MoveResultProcessor moveResultProcessor;

	/**
	 * Construct a strategy game using given values
	 * @param maxTurns How many turns the game should last to, 0 for infinite
	 * @param noRepeatMoves Whether repeated moves should be disallowed
	 * @param moveResultProcessor What move result processor to use when acting on move results
	 */
	protected StrategyGameImpl(int maxTurns, boolean noRepeatMoves, MoveResultProcessor moveResultProcessor) {
		this.board = new StrategyBoardImpl(10, 10); // sane default...?
		this.maxTurns = maxTurns;
		this.noRepeatMoves = noRepeatMoves;
		this.moveResultProcessor = moveResultProcessor;
		colorTurn = RED;
		turns = 0;
		gameOver = false;
		redMoves = new ArrayDeque<>();
		blueMoves = new ArrayDeque<>();
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
					|| result == PieceDefined.MoveResult.STRIKE_DRAW
					|| result == PieceDefined.MoveResult.STRIKE_BOMB
					|| result == PieceDefined.MoveResult.DETONATION);
			if (!struck && noRepeatMoves)
				checkRepeatMoves(fr, fc, tr, tc);

			// If a strike happened, clear the move queue so we cancel out move repetition
			if (struck)
				(colorTurn == RED ? redMoves : blueMoves).clear();

			// Delegate to the appropriate move processor (either default or aggressor advantage)
			result = moveResultProcessor.processMove(result, board, new Move(fr, fc, tr, tc), colorTurn);
			if (result == PieceDefined.MoveResult.RED_WINS || result == PieceDefined.MoveResult.BLUE_WINS)
				gameOver = true;

		} catch (StrategyException ex) {
			// Moving player screwed up; opponent wins.
			System.err.println(ex.getMessage());

			gameOver = true;
			result = colorTurn == RED ? PieceDefined.MoveResult.BLUE_WINS : PieceDefined.MoveResult.RED_WINS;
		}

		// Logic for incrementing turns and determining who goes next
		MoveResult ret = convertMoveResult(result);
		if (colorTurn == RED)
			colorTurn = BLUE;
		else {
			colorTurn = RED;
			turns++;

			// If max turns is enabled and the number of turns is in excess, red wins
			if (maxTurns > 0 && turns >= maxTurns)
				return RED_WINS;
		}

		// Make sure the new player can actually play
		if (!canMove())
			ret = (colorTurn == RED ? BLUE_WINS : RED_WINS);

		System.out.println("Turn " + turns + ", color: " + colorTurn.name());
		System.out.println(board.toString());
		return ret;
	}

	/**
	 * Helper to convert a PieceDefined-format move result to a Strategy move result.
	 * @param result Move result in PieceDefined format
	 * @return Move result in Strategy format
	 */
	private MoveResult convertMoveResult(PieceDefined.MoveResult result) {
		if (result == PieceDefined.MoveResult.STRIKE_DRAW || result == PieceDefined.MoveResult.DETONATION)
			return OK;
		else if (result == PieceDefined.MoveResult.STRIKE_BOMB)
			return colorTurn == RED ? STRIKE_BLUE : STRIKE_RED;
		else
			return MoveResult.values()[result.ordinal()];
	}

	/**
	 * Check a move was repeated or not. Throws an exception instead of a return to make move result handling code
	 * a little easier and avoid needless conditionals and cases.
	 * @param fr From row
	 * @param fc From column
	 * @param tr To row
	 * @param tc To column
	 * @throws StrategyException Thrown if a move was repeated.
	 */
	private void checkRepeatMoves(int fr, int fc, int tr, int tc) throws StrategyException {
		Move move = new Move(fr, fc, tr, tc);
		ArrayDeque<Move> curQueue = colorTurn == RED ? redMoves : blueMoves;
		if (curQueue.size() == 2 && move.equals(curQueue.pollLast()))
			throw new StrategyException("Move repeated");
		curQueue.addFirst(move);
	}

	/**
	 * Checks to see if the player whose turn it is can actually move
	 * @return Player mobility status
	 */
	private boolean canMove() throws StrategyException {
		for (int r = 0; r < board.getRows(); r++) {
			for (int c = 0; c < board.getCols(); c++) {
				if (canPieceMove(r, c))
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if a given piece can move or not
	 * @param r Row
	 * @param c Column
	 * @return Piece mobility
	 */
	private boolean canPieceMove(int r, int c) {
		final PieceDefined curPiece = board.getPieceAt(r, c);
		if (curPiece == null || curPiece.getPieceColor() != colorTurn)
			return false;

		// Loop through piece's neighbors to see if it could move to one of those spots
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == j)
					continue;

				// Let the board decide what's out of bounds. The checkBounds function uses exceptions to streamline
				// things elsewhere, so this has to be done using a try/catch
				try { board.checkBounds(r+i, c+j); } catch (StrategyException ex) {
					continue;
				}

				// Can't move into a choke point
				if (board.getSquareTypeAt(r+i, c+j) == Board.SquareType.CHOKE)
					continue;

				// Simulate moving the piece to the new location; if it works, return true.
				final Piece nPiece = board.getPieceAt(r+i, c+j);
				if (nPiece == null || nPiece.getPieceColor() != curPiece.getPieceColor()) {
					if (nPiece != null && nPiece.getPieceType() == Piece.PieceType.BOMB)
						return true; // Bombs are stateful, can't try moving to one
					try {
						curPiece.move(board, r, c, r+i, c+j);
						return true;
					} catch (StrategyException ignored) { }
				}
			}
		}

		// No possible moves for this piece found
		return false;
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
}
