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

package strategy.required;

import strategy.Board;
import strategy.NotImplementedException;
import strategy.StrategyGame;
import strategy.StrategyGame.Version;
import strategy.crmyers.alpha.AlphaGame;
import strategy.crmyers.beta.BetaGame;
import strategy.crmyers.gamma.GammaGame;

/**
 * Factory for creating Strategy games.
 *
 * @version Mar 18, 2019
 */
public class StrategyGameFactory {
	public static StrategyGame makeGame(Version version, Board board) {
		switch (version) {
			case ALPHA:                    // No need for the board
				return new AlphaGame();
			case BETA:
				return new BetaGame(board);
			case GAMMA:
				return new GammaGame(board);
			default:
				throw new NotImplementedException(
						"StrategyGameFactory.makeGame for version " + version);
		}
	}
}
