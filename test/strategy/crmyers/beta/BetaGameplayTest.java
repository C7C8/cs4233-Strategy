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

package strategy.crmyers.beta;

import strategy.StrategyGame;
import strategy.required.StrategyGameFactory;

/**
 * Beta-specific tests
 */
public class BetaGameplayTest extends strategy.crmyers.common.GameplayTest {

	/**
	 * Set the gameplay test to use a Beta board
	 */
	@Override
	protected void boardConfig() {
		board = new BetaBoard();
	}

	/**
	 * Set the gameplay test to use a Beta game
	 */
	@Override
	protected void gameConfig() {
		game = StrategyGameFactory.makeGame(StrategyGame.Version.BETA, board);
	}
}
