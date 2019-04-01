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

import strategy.Piece;
import strategy.StrategyException;

public class Board implements strategy.Board {

	// Constants
	static final int ROWS = 6;
	static final int COLS = 6;

	protected Piece[][] pieces;
	protected SquareType[][] squares;

	/**
	 * Initialize the board; in beta strategy, there are no choke points and the board does not have pieces by default.
	 */
	public Board() {
		pieces = new Piece[ROWS][COLS];
		squares = new SquareType[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				squares[i][j] = SquareType.NORMAL;
			}
		}
	}

	/**
	 * Simple helper to check bounds, since this is used a lot
	 * @param row Row
	 * @param col Column
	 * @throws StrategyException Thrown if bounds are exceeded
	 */
	protected void checkBounds(int row, int col) throws StrategyException {
		if (row < 0 || row > ROWS || col < 0 || col > COLS)
			throw new StrategyException("Row/column index out of bounds");
	}

	/**
	 * Get the piece at a given location.
	 * @param row Row
	 * @param column Column
	 * @return Piece at location; null if nothing there.
	 * @throws StrategyException Thrown if row/column invalid
	 */
	public Piece getPieceAt(int row, int column) throws StrategyException {
		checkBounds(row, column);
		return pieces[row][column];
	}

	/**
	 * Get the square type at a given location
	 * @param row Row
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
	 * @param piece Piece to place
	 * @param row Row
	 * @param column Column
	 * @throws StrategyException Thrown if row/column invalid, piece is null, or square already occupied.
	 */
	public void put(Piece piece, int row, int column) throws StrategyException {
		checkBounds(row, column);
		if (piece == null)
			throw new StrategyException("Piece to place is null");
		if (squares[row][column] != SquareType.NORMAL)
			throw new StrategyException("Place to put at is not a normal square");
		if (pieces[row][column] != null)
			throw new StrategyException("Place to put at is already occupied");
		pieces[row][column] = piece;
	}
}
