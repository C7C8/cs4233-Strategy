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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.StrategyException;
import strategy.crmyers.beta.BetaBoard;
import strategy.crmyers.common.pieces.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static strategy.Piece.PieceColor.BLUE;
import static strategy.Piece.PieceColor.RED;
import static strategy.Piece.PieceType.*;

public class PieceTest {
	private static BetaBoard board;
	private static PieceDefined bomb;
	private static PieceDefined captain;
	private static PieceDefined colonel;
	private static PieceDefined flag;
	private static PieceDefined general;
	private static PieceDefined lieutenant;
	private static PieceDefined major;
	private static PieceDefined marshal;
	private static PieceDefined miner;
	private static PieceDefined scout;
	private static PieceDefined sergeant;
	private static PieceDefined spy;

	@BeforeEach
	void setup_local() {
		// These pieces are stateful, so we have to recreate them each time
		board = new BetaBoard();
		marshal = new Marshal(RED);
		general = new General(BLUE);
		colonel = new Colonel(RED);
		major = new Major(BLUE);
		captain = new Captain(RED);
		lieutenant = new Lieutenant(BLUE);
		sergeant = new Sergeant(RED);
		miner = new Miner(BLUE);
		scout = new Scout(RED);
		spy = new Spy(BLUE);
		bomb = new Bomb(RED);
		flag = new Flag(BLUE);
	}

	/**
	 * Do pieces declare they're the right type?
	 * (why is this even a test?)
	 */
	@Test
	void pieceTypes() {
		assertThat(bomb.getPieceType(), is(equalTo(BOMB)));
		assertThat(captain.getPieceType(), is(equalTo(CAPTAIN)));
		assertThat(colonel.getPieceType(), is(equalTo(COLONEL)));
		assertThat(flag.getPieceType(), is(equalTo(FLAG)));
		assertThat(general.getPieceType(), is(equalTo(GENERAL)));
		assertThat(lieutenant.getPieceType(), is(equalTo(LIEUTENANT)));
		assertThat(major.getPieceType(), is(equalTo(MAJOR)));
		assertThat(marshal.getPieceType(), is(equalTo(MARSHAL)));
		assertThat(miner.getPieceType(), is(equalTo(MINER)));
		assertThat(scout.getPieceType(), is(equalTo(SCOUT)));
		assertThat(sergeant.getPieceType(), is(equalTo(SERGEANT)));
		assertThat(spy.getPieceType(), is(equalTo(SPY)));
	}

	/**
	 * Test marshal actions
	 */
	@Test
	void marshalAction() {
		//marshals can defeat 3-11
		assertThat(marshal.strike(general), is(equalTo(marshal.pieceVictory())));
		assertThat(marshal.strike(marshal), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));
	}

	/**
	 * Test general actions
	 */
	@Test
	void generalAction() {
		//generals can defeat 3-10
		assertThat(general.strike(colonel), is(equalTo(general.pieceVictory())));
		assertThat(general.strike(marshal), is(equalTo(general.pieceLoss())));
		assertThat(general.strike(general), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));
	}

	/**
	 * Test colonel actions
	 */
	@Test
	void colonelAction() {
		assertThat(colonel.strike(major), is(equalTo(colonel.pieceVictory())));
		assertThat(colonel.strike(general), is(equalTo(colonel.pieceLoss())));
		assertThat(colonel.strike(colonel), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));
	}

	/**
	 * Test major actions
	 */
	@Test
	void majorAction() {
		assertThat(major.strike(captain), is(equalTo(major.pieceVictory())));
		assertThat(major.strike(colonel), is(equalTo(major.pieceLoss())));
		assertThat(major.strike(major), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));
	}

	/**
	 * Test captain actions
	 */
	@Test
	void captainAction() {
		// captains can defeat 3-7
		assertThat(captain.strike(lieutenant), is(equalTo(captain.pieceVictory())));
		assertThat(captain.strike(major), is(equalTo(captain.pieceLoss())));
		assertThat(captain.strike(captain), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));
	}

	/**
	 * Test lieutenant actions
	 */
	@Test
	void lieutenantAction() {
		assertThat(lieutenant.strike(sergeant), is(equalTo(lieutenant.pieceVictory())));
		assertThat(lieutenant.strike(captain), is(equalTo(lieutenant.pieceLoss())));
		assertThat(lieutenant.strike(lieutenant), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));
	}

	/**
	 * Test sergeant actions
	 */
	@Test
	void sergeantAction() {
		assertThat(sergeant.strike(miner), is(equalTo(sergeant.pieceVictory())));
		assertThat(sergeant.strike(lieutenant), is(equalTo(sergeant.pieceLoss())));
		assertThat(sergeant.strike(sergeant), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));
	}

	/**
	 * Test miner actions
	 */
	@Test
	void minerAction() {
		assertThat(miner.strike(scout), is(equalTo(miner.pieceVictory())));
		assertThat(miner.strike(sergeant), is(equalTo(miner.pieceLoss())));
		assertThat(miner.strike(miner), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));

		// Miners have the unique ability to take out bombs
		assertThat(miner.strike(bomb), is(equalTo(miner.pieceVictory())));
	}

	/**
	 * Test scout actions
	 */
	@Test
	void scoutAction() {
		assertThat(scout.strike(spy), is(equalTo(scout.pieceVictory())));
		assertThat(scout.strike(miner), is(equalTo(scout.pieceLoss())));
		assertThat(scout.strike(scout), is(equalTo(PieceDefined.MoveResult.STRIKE_DRAW)));

		// Scouts have the added bonus of being able to move any amount in any vertical/horizontal direction
		assertThat(scout.move(board, 0, 0, 0, 5), is(equalTo(PieceDefined.MoveResult.OK)));
		assertThat(scout.move(board, 0, 0, 5, 0), is(equalTo(PieceDefined.MoveResult.OK)));

		// ...but not if there's a piece in the way
		board.put(marshal, 0, 3);
		assertThrows(StrategyException.class, () -> scout.move(board, 0, 0, 0, 5));
	}

	/**
	 * Test spy actions
	 */
	@Test
	void spy() {
		// Spies can't succeed at striking anything other than marshals
		assertThat(spy.strike(marshal), is(equalTo(spy.pieceVictory())));
	}

	/**
	 * Test bomb movement (or lack thereof)
	 */
	@Test
	void bombAction() {
		assertThrows(StrategyException.class, () -> bomb.move(board, 0, 0, 1, 1));
		assertThrows(StrategyException.class, () -> bomb.strike(flag));

		// Anything against a bomb fails (except miners, but that's tested elsewhere)
		assertThat(marshal.strike(bomb), is(equalTo(marshal.pieceLoss())));
	}

	/**
	 * Test flag movement (or lack thereof)
	 */
	@Test
	void flagAction() {
		assertThrows(StrategyException.class, () -> flag.move(board, 0, 0, 1, 1));
		assertThrows(StrategyException.class, () -> flag.strike(flag));

		// Anything can take a flag
		assertThat(spy.strike(flag), is(equalTo(PieceDefined.MoveResult.BLUE_WINS)));
	}
}
