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
import strategy.crmyers.common.pieces.Scout;

import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.Piece.PieceType.BOMB;
import static strategy.Piece.PieceType.FLAG;

public abstract class PieceDefined implements Piece {

	private final PieceColor color;
	private Move last;
	private Move last2;

	public enum MoveResult {
		OK,
		STRIKE_RED,
		STRIKE_BLUE,
		RED_WINS,
		BLUE_WINS,
		@SuppressWarnings("unused") GAME_OVER,
		STRIKE_DRAW,
	}

	public PieceDefined(PieceColor color) {
		this.color = color;
	}

	/**
	 * Dumb helper function to check that we're only moving in one direction at once
	 *
	 * @param fr From row
	 * @param fc From column
	 * @param tr To row
	 * @param tc To column
	 * @return Whether the move is non-diagonal
	 */
	static boolean isDiagonal(int fr, int fc, int tr, int tc) {
		return Math.abs(tc - fc) != 0 && Math.abs(tr - fr) != 0;
	}

	static boolean isOneSpace(int fr, int fc, int tr, int tc) {
		final int dx = Math.abs(tc - fc);
		final int dy = Math.abs(tr - fr);
		return dx > 1 || dy > 1;
	}

	@Override
	public PieceColor getPieceColor() {
		return color;
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
	public MoveResult move(StrategyBoardImpl board, int fr, int fc, int tr, int tc, boolean noRepeatMoves) throws StrategyException {
		if (isDiagonal(fr, fc, tr, tc))
			throw new StrategyException("Diagonal move made");
		// Have to have special code to handle scouts, since scouts call into this code too
		if (this.getClass() != Scout.class && isOneSpace(fr, fc, tr, tc))
			throw new StrategyException("More than one move made by a non-scout piece");
		if (noRepeatMoves && moveRepetition(fr, fc, tr, tc))
		 	 throw new StrategyException("Move repeated");

		Piece piece = board.getPieceAt(tr, tc);
		if (piece == null)
			return MoveResult.OK;
		if (piece.getPieceColor() == getPieceColor())
			throw new StrategyException("Tried to strike a piece of the same color!");

		return strike(piece);
	}

	/**
	 * Same as previous move() but with default of disabled repeat moves
	 */
	public final MoveResult move(StrategyBoardImpl board, int fr, int fc, int tr, int tc) throws StrategyException {
		return move(board, fr, fc, tr, tc, true);
	}

	/**
	 * Determine the outcome of a particular strike.
	 *
	 * @param target Targeted piece.
	 * @return Result of the strike!
	 */
	public MoveResult strike(Piece target) {
		if (target.getPieceType() == BOMB)
			return pieceLoss();
		else if (target.getPieceType() == FLAG)
			return color == BLUE ? MoveResult.BLUE_WINS : MoveResult.RED_WINS;

		final int ourRank = getPieceType().ordinal();
		final int theirRank = target.getPieceType().ordinal();
		if (ourRank > theirRank)
			return pieceVictory();
		else if (ourRank < theirRank)
			return pieceLoss();
		return MoveResult.STRIKE_DRAW;
	}

	/**
	 * Determine whether the piece would perform a back-and-forth move. Calling this function is equivalent to stating
	 * that the move is being made, UNLESS the result is TRUE.
	 *
	 * @param fr From row
	 * @param fc From column
	 * @param tr To row
	 * @param tc To column
	 * @return Whether the move is a repeat or not
	 */
	@SuppressWarnings("SameParameterValue")
	boolean moveRepetition(int fr, int fc, int tr, int tc) {
		Move move = new Move(fr, fc, tr, tc);
		if (move.equals(last2))
			return true;
		last2 = last;
		last = move;
		return false;
	}

	/**
	 * Tiny, dumb helper function to convert piece color + victory to the right strike return
	 *
	 * @return Strike result
	 */
	protected MoveResult pieceVictory() {
		return color == BLUE ? MoveResult.STRIKE_BLUE : MoveResult.STRIKE_RED;
	}

	/**
	 * Tiny, dumb helper function to convert piece color + victory to the right strike return
	 *
	 * @return Strike result
	 */
	protected MoveResult pieceLoss() {
		return color == BLUE ? MoveResult.STRIKE_RED : MoveResult.STRIKE_BLUE;
	}

	/**
	 * @return Symbol that represents this piece
	 */
	public abstract String toString();

	/**
	 * Helper to to ease construction of getStr stuff, just returns a code for the color
	 *
	 * @return R for red, B for blue
	 */
	protected String getColorStr() {
		return color == RED ? "R" : "B";
	}

	/**
	 * Helper class to store moves
	 */
	private static class Move {
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
