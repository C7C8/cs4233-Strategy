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

package strategy.crmyers.gamma;


import strategy.StrategyGame;
import strategy.crmyers.common.GameplayTest;
import strategy.required.StrategyGameFactory;

public class GammaTestExternal extends GameplayTest  {

	/**
	 * Set the gameplay test to use a Gamma board
	 */
	@Override
	protected void boardConfig() {
		board = new GammaBoard();
	}

	/**
	 * Set the gameplay test to use a Gamma game
	 */
	@Override
	protected void gameConfig() {
		game = StrategyGameFactory.makeGame(StrategyGame.Version.GAMMA, board);
	}
}
