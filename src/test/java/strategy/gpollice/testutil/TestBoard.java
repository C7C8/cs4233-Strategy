// THIS FILE IS NOT MINE (crmyers) !
// It has been imported into this project to assist with development.
// The only change is to the package name and some of the imports.

package strategy.gpollice.testutil;

import strategy.Board;
import strategy.Piece;
import strategy.Piece.PieceColor;
import strategy.Piece.PieceType;
import strategy.StrategyException;

import java.util.*;

import static strategy.Board.SquareType.NORMAL;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;

/**
 * Master tests for the students' code.
 * @version Apr 1, 2019
 */
public class TestBoard implements Board
{
	private Map<TestCoordinate, Piece> board;
	private int rows, columns;
	
	/**
	 * Board for student tests only
	 */
	public TestBoard(int rows, int columns)
	{
		board = new HashMap<TestCoordinate, Piece>();
		this.rows = rows;
		this.columns = columns;
	}
	
	public void initialize(int rows, int columns, List<Piece> redLineup, List<Piece> blueLineup) {
		placePieces(RED, redLineup, 0, 0);
		placePieces(BLUE, blueLineup, rows-1, columns-1);
	}
	
	private void placePieces(PieceColor color, List<Piece> lineup,
			int startRow, int startColumn)
	{
		int r = startRow;
		int c = startColumn;
		int cInc = startColumn == 0 ? 1 : -1;
		for (Piece p : lineup) {
			if (p.getPieceColor() != color) {
				throw new StrategyException("BoardImpl:placePieces: wrong color piece-" + p.getPieceColor());
			}
			board.put(new TestCoordinate(r, c), p);
			c = c + cInc;
			if (c < 0) { 	// BLUE
				c = columns - 1;
				r--;
			} else if (c > columns - 1) {	// RED
				c = 0;
				r++;
			}
		}
	}
	
	@Override
	public Piece getPieceAt(int row, int column)
	{
		return board.get(new TestCoordinate(row, column));
	}
	
	@Override
	public SquareType getSquareTypeAt(int row, int column)
	{
		return NORMAL;
	}
	
	public List<Piece> makeLineup(PieceColor color, PieceType... types)
	{
		List<Piece> pieces = new LinkedList<Piece>();
		for (PieceType pt : types) {
			pieces.add(new TestPiece(color, pt));
		}
		return pieces;
	}
}

class TestPiece implements Piece
{
	private final PieceType type;
	private final PieceColor color;
	
	public TestPiece(PieceColor color, PieceType type)
	{
		this.type = type;
		this.color = color;
	}

	/**
	 * @return the type
	 */
	@Override
	public PieceType getPieceType()
	{
		return type;
	}

	/**
	 * @return the color
	 */
	@Override
	public PieceColor getPieceColor()
	{
		return color;
	}
}

class TestCoordinate
{
	private final int row, column;
	public TestCoordinate(int row, int col)
	{
		this.row = row;
		this.column = col;
	}
	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(column, row);
	}
	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TestCoordinate other = (TestCoordinate) obj;
		return column == other.column && row == other.row;
	}
}
