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
import strategy.crmyers.common.pieces.*;

import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;

public class StrategyBoardImpl implements strategy.Board {

	// Constants
	private final int ROWS;
	private final int COLS;

	// Game data
	protected PieceDefined[][] pieces;
	protected SquareType[][] squares;

	/**
	 * Initialize the board using predetermined rows and columns count
	 * @param rows Board height
	 * @param cols Board width
	 */
	public StrategyBoardImpl(int rows, int cols) {
		ROWS = rows;
		COLS = cols;
		pieces = new PieceDefined[ROWS][COLS];
		squares = new SquareType[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				squares[i][j] = SquareType.NORMAL;
			}
		}
	}

	/**
	 * Copy a given board of the given size to this one
	 * @param board Board to copy
	 * @param rows Board height
	 * @param cols Board width
	 */
	public StrategyBoardImpl(strategy.Board board, int rows, int cols) {
		ROWS = rows;
		COLS = cols;
		pieces = new PieceDefined[ROWS][COLS];
		squares = new SquareType[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				squares[i][j] = board.getSquareTypeAt(i, j);

				final Piece piece = board.getPieceAt(i, j);
				if (piece == null)
					continue;
				final Piece.PieceColor color = piece.getPieceColor();

				PieceDefined newPiece;
				switch (piece.getPieceType()) {
					case BOMB:
						newPiece = new Bomb(color);
						break;
					case CAPTAIN:
						newPiece = new Captain(color);
						break;
					case COLONEL:
						newPiece = new Colonel(color);
						break;
					case FLAG:
						newPiece = new Flag(color);
						break;
					case GENERAL:
						newPiece = new General(color);
						break;
					case LIEUTENANT:
						newPiece = new Lieutenant(color);
						break;
					case MAJOR:
						newPiece = new Major(color);
						break;
					case MARSHAL:
						newPiece = new Marshal(color);
						break;
					case MINER:
						newPiece = new Miner(color);
						break;
					case SCOUT:
						newPiece = new Scout(color);
						break;
					case SERGEANT:
						newPiece = new Sergeant(color);
						break;
					default: // Spy
						newPiece = new Spy(color);
						break;
				}

				pieces[i][j] = newPiece;
			}
		}
	}

	/**
	 * Simple helper to check bounds, since this is used a lot
	 *
	 * @param row Row
	 * @param col Column
	 * @throws StrategyException Thrown if bounds are exceeded
	 */
	void checkBounds(int row, int col) throws StrategyException {
		if (row < 0 || row >= ROWS || col < 0 || col >= COLS)
			throw new StrategyException("Row/column index out of bounds");
	}

	/**
	 * Get the piece at a given location.
	 *
	 * @param row    Row
	 * @param column Column
	 * @return Piece at location; null if nothing there.
	 * @throws StrategyException Thrown if row/column invalid
	 */
	public PieceDefined getPieceAt(int row, int column) throws StrategyException {
		checkBounds(row, column);
		return pieces[row][column];
	}

	/**
	 * Get the square type at a given location
	 *
	 * @param row    Row
	 * @param column Column
	 * @return Square type at location
	 * @throws StrategyException Thrown if row/column invalid
	 */
	public SquareType getSquareTypeAt(int row, int column) throws StrategyException {
		checkBounds(row, column);
		return squares[row][column];
	}

	/**
	 * Put a piece at a given square
	 *
	 * @param piece  Piece to place
	 * @param row    Row
	 * @param column Column
	 * @throws StrategyException Thrown if row/column invalid, piece is null, or square already occupied.
	 */
	public void put(PieceDefined piece, int row, int column) throws StrategyException {
		checkBounds(row, column);
		if (squares[row][column] != SquareType.NORMAL)
			throw new StrategyException("Place to put at is not a normal square");
		pieces[row][column] = piece;
	}

	/**
	 * Validate the board setup by making sure pieces are in the right places and have
	 * the correct count
	 * @param refPieceCounts Array that dictates what piece counts should be checked for
	 * @param startingPieceRows How deep each side's starting position should be
	 * @return True if valid board, false otherwise
	 * @implNote Params are integers because this function is mocked for testing purposes,
	 * and Java is dumb about primitive types
	 */
	public boolean validateBoard(int[] refPieceCounts, int startingPieceRows) {
		int[][] pieceCounts = new int[12][2];

		// First check to make sure pieces are laid out in the correct spots
		for (int r = 0; r < startingPieceRows; r++) {
			for (int c = 0; c < COLS; c++) {
				final Piece piece = pieces[r][c];
				if (piece == null || piece.getPieceColor() != RED)
					return false;

				pieceCounts[piece.getPieceType().ordinal()][RED.ordinal()]++;
			}
		}
		for (int r = ROWS - startingPieceRows; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				final Piece piece = pieces[r][c];
				if (piece == null || piece.getPieceColor() != BLUE)
					return false;

				pieceCounts[piece.getPieceType().ordinal()][BLUE.ordinal()]++;
			}
		}

		// Now make sure piece counts are correct
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 2; j++) {
				if (pieceCounts[i][j] != refPieceCounts[i])
					return false;
			}
		}

		return true;
	}

	/**
	 * Compare boards, determine if they're equal. Autogenerated.
	 *
	 * @param o Object to compare to.
	 * @return Equals
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		StrategyBoardImpl board = (StrategyBoardImpl) o;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				// Make sure that either both are null or both are non-null
				if (board.pieces[i][j] == null && pieces[i][j] != null ||
						board.pieces[i][j] != null && pieces[i][j] == null)
					return false;

				// If individual pieces && squares don't equal each other, fail
				if (board.pieces[i][j] != null && !board.pieces[i][j].equals(pieces[i][j]))
					return false;

				// Check choke points
				if (board.squares[i][j] != squares[i][j])
					return false;
			}
		}
		return true;
	}

	public String toString() {
		StringBuilder ret = new StringBuilder();
		for (int i = ROWS - 1; i >= 0; i--) {
			for (int j = 0; j < COLS; j++) {
				final Piece p = getPieceAt(i, j);
				ret.append(" ");
				if (p != null)
					ret.append(p.toString());
				else if (squares[i][j] == SquareType.CHOKE)
					ret.append("--");
				else
					ret.append("  ");
			}
			ret.append('\n');
		}

		return ret.toString();
	}

	public int getRows() {
		return ROWS;
	}

	public int getCols() {
		return COLS;
	}

}
