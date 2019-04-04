# Board

* Board is sane
	* Checks bounds correctly
	* Stores pieces correctly
	* Board comparisons work
	* Board initializes correctly (all empty, all non-choke)
	* Board can copy construct from input boards

# Pieces
* Pieces can't move diagonally
* Pieces can't perform move repetition (implemented but not used)
* Pieces return correct piece type
* Pieces draw when striking against themselves
* Individual pieces:
	* [one task for normal piece without special movement rules]: Defeated by higher, defeats lower, draws against self
* Miners can defeat bombs
* Scouts can move multiple squares at a time
* Scouts can't jump over other pieces
* Spies can defeat marshals
* Bombs can't move
* Anything but a miner vs. a bomb loses
* Flags can't move
* Anything that can move can defeat a flag

# Full game
* Back-and-forth play
* *Added by BetaStrategyMasterTests* Games can't last more than 8 turns
* Games return GAME_OVER after a victory of any kind
* Players lose if they move out of turn
* Players lose if they try to move a piece that doesn't exist
* Draws result in both pieces getting annihilated
* Moving diagonally results in a victory for the other player
* Friendly fire results in a victory for the other player
* Opponent wins if you can't make a move
