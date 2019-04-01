# Board

* Add 6x6 arrays for choke points, game pieces
* Initialize choke points
* Add "put" method
* `equals()`
* Random deployment of pieces?

# Pieces

* Extend piece interface into abstract class to include move validation function
* Add move repetition detection to Piece
* Add base (non-functional) implementations for pieces
	1. Flag
	2. Bomb
	3. Spy
	4. Scout
	5. Miner
	6. Sergeant
	7. Lieutenant
	8. Captain
	9. Major
	10. Colonel
	11. General
	12. Marshal
* Implement movement and strike checking for all
	* Implement Scout clear-path checking

# Base Game

* Hook everything into game move function