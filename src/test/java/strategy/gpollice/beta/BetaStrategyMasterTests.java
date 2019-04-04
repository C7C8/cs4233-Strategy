// THIS FILE IS NOT MINE (crmyers) !
// It has been imported into this project to assist with development.
// The only change is to the package name and some of the imports.

package strategy.gpollice.beta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.NotImplementedException;
import strategy.Piece;
import strategy.StrategyGame;
import strategy.gpollice.testutil.TestBoard;
import strategy.required.StrategyGameFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.Piece.PieceType.*;
import static strategy.StrategyGame.MoveResult.OK;
import static strategy.StrategyGame.MoveResult.RED_WINS;
import static strategy.StrategyGame.Version.BETA;
import static strategy.StrategyGame.Version.ZETA;

/**
 * Master tests for Beta Strategy
 * @version Mar 29, 2019
 */
class BetaStrategyMasterTests
{
	private int rows = 0;
	private int columns = 0;
	private StrategyGame theGame = null;
	private List<Piece> redLineup = null;
	private List<Piece> blueLineup = null;
	private TestBoard theBoard = null;
	
	@BeforeEach
	void betaSetup() throws Exception
	{
		theBoard = new TestBoard(6, 6);
		redLineup = theBoard.makeLineup(RED,
				SERGEANT, SERGEANT, COLONEL, CAPTAIN, LIEUTENANT, LIEUTENANT,
				FLAG, MARSHAL, COLONEL, CAPTAIN, LIEUTENANT, SERGEANT);
		blueLineup = theBoard.makeLineup(BLUE,
				MARSHAL, COLONEL, CAPTAIN, SERGEANT, FLAG, LIEUTENANT,
				LIEUTENANT, LIEUTENANT, SERGEANT, SERGEANT, COLONEL, CAPTAIN);
		theBoard.initialize(6, 6, redLineup, blueLineup);
		theGame = StrategyGameFactory.makeGame(BETA, theBoard);
	}
	
	@Test 
	void redWinsAfterEightTurns()
	{
		theGame.move(1, 1, 2, 1);	// Move 1
		theGame.move(4, 2, 3, 2);
		theGame.move(2, 1, 1, 1);	// Move 2
		theGame.move(3, 2, 4, 2);
		theGame.move(1, 1, 2, 1);	// Move 3
		theGame.move(4, 2, 3, 2);
		theGame.move(2, 1, 1, 1);	// Move 4
		theGame.move(3, 2, 4, 2);
		theGame.move(1, 1, 2, 1);	// Move 5
		theGame.move(4, 2, 3, 2);
		theGame.move(2, 1, 1, 1);	// Move 6
		theGame.move(3, 2, 4, 2);
		theGame.move(1, 1, 2, 1);	// Move 7
		theGame.move(4, 2, 3, 2);
		assertEquals(OK, theGame.move(2, 1, 1, 1));	// Move 8
		assertEquals(RED_WINS, theGame.move(3, 2, 4, 2));
	}

	@Test
	void versionNotImplemented()
	{
		assertThrows(NotImplementedException.class, () -> StrategyGameFactory.makeGame(ZETA, null));
	}
}
