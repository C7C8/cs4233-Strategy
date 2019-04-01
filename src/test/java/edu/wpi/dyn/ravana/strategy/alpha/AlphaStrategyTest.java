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
package edu.wpi.dyn.ravana.strategy.alpha;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.StrategyGame;

import static edu.wpi.dyn.ravana.strategy.required.StrategyGameFactory.makeGame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static strategy.StrategyGame.MoveResult.BLUE_WINS;
import static strategy.StrategyGame.MoveResult.RED_WINS;
import static strategy.StrategyGame.Version.ALPHA;

/**
 * Test cases for Alpha Strategy.
 * @version Mar 18, 2019
 */
class AlphaStrategyTest
{
	private StrategyGame game;
	
	@BeforeEach
	private void setup()
	{
		game = makeGame(ALPHA, null);
	}
	
	@Test
	void validGame()
	{
		assertEquals(RED_WINS, game.move(0, 0, 1, 0));
	}

	@Test
	void wrongMove()
	{
		assertEquals(BLUE_WINS, game.move(0, 0, 0, 1));
	}
}
