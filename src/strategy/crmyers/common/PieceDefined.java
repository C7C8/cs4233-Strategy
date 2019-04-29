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

public abstract class PieceDefined implements Piece {

	private final PieceColor color;

	/**
	 * Used to help make returns from piece moves and strikes easier to deal with
	 */
	public enum MoveResult {
		OK,
		STRIKE_RED,
		STRIKE_BLUE,
		RED_WINS,
		BLUE_WINS,
		@SuppressWarnings("unused") GAME_OVER,
		STRIKE_DRAW, // If two pieces of the same rank fight
		STRIKE_BOMB, // If a bomb is struck
		DETONATION, // If a bomb is struck and it explodes for good
	}

	/**
	 * Construct piece
	 * @param color Piece color
	 */
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

	/**
	 * Helper to screen for moves that are greater than once space
	 * @param fr From row
	 * @param fc From column
	 * @param tr To row
	 * @param tc To column
	 * @return True if the move is greater than one space, false otherwise
	 */
	private static boolean notOneSpace(int fr, int fc, int tr, int tc) {
		final int dx = Math.abs(tc - fc);
		final int dy = Math.abs(tr - fr);
		return dx > 1 || dy > 1;
	}

	@Override
	public PieceColor getPieceColor() {
		return color;
	}

	/**
	 * Calculate results of moving the piece. Does not act on those results (leaves decision up to game object)
	 * @param board Board to move on
	 * @param fr    From row
	 * @param fc    From column
	 * @param tr    To row
	 * @param tc    To column
	 * @return Result of move
	 * @throws StrategyException Thrown if move is invalid for any reason (e.g. out of bounds)
	 */
	public MoveResult move(StrategyBoardImpl board, int fr, int fc, int tr, int tc) throws StrategyException {
		if (isDiagonal(fr, fc, tr, tc))
			throw new StrategyException("Diagonal move made");

		// Have to have special code to handle scouts, since scouts call into this code too
		if (this.getClass() != Scout.class && notOneSpace(fr, fc, tr, tc))
			throw new StrategyException("More than one move made by a non-scout piece");

		PieceDefined piece = board.getPieceAt(tr, tc);
		if (piece == null)
			return MoveResult.OK;
		if (piece.getPieceColor() == getPieceColor())
			throw new StrategyException("Tried to strike a piece of the same color!");

		return strike(piece);
	}

	/**
	 * Determine the outcome of a particular strike.
	 * @param target Targeted piece.
	 * @return Result of the strike!
	 */
	public MoveResult strike(PieceDefined target) {
		final int ourRank = getPieceType().ordinal();
		final int theirRank = target.getPieceType().ordinal();
		MoveResult result;
		if (ourRank > theirRank)
			result = pieceVictory();
		else if (ourRank < theirRank)
			result = pieceLoss();
		else
			result = MoveResult.STRIKE_DRAW;

		// If the defending piece has special code
		MoveResult defenderResult = target.defend(result);
		if (defenderResult != result)
			return defenderResult;
		return result;
	}

	/**
	 * Any special code the piece should execute or or other special cases (flags, bombs, etc)
	 * @param prediction What the attacker says should happen; simplifies this function greatly
	 * @return What the attacked piece says should happen
	 */
	protected MoveResult defend(MoveResult prediction) {
		return prediction;
	}

	/**
	 * Tiny, dumb helper function to convert piece color + victory to the right strike return
	 * @return Strike result
	 */
	protected MoveResult pieceVictory() {
		return color == BLUE ? MoveResult.STRIKE_BLUE : MoveResult.STRIKE_RED;
	}

	/**
	 * Tiny, dumb helper function to convert piece color + victory to the right strike return
	 * @return Strike result
	 */
	protected MoveResult pieceLoss() {
		return color == BLUE ? MoveResult.STRIKE_RED : MoveResult.STRIKE_BLUE;
	}

	/**
	 * @return Two-character symbol that represents this piece
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
}
