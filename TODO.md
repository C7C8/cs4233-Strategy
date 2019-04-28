# Epsilon Strategy TODOs

## Bugfixes

* Move repeat detection that isn't unique to pieces(fixes okAfterEightTurns failure)
* Strikes cancel move repetition
* Account for proper cannot-move checking
* Validate board setup for Gamma & Delta boards

## Epsilon

* Aggressor advantage: if two same-ranking pieces battle, the attacker wins instead
* Scout distance attack -- scouts can attack pieces that are three squares away
* Bombs are used up after two detonations
	* *Side note: **Why?** How many times can a real-life bomb explode? If someone can find
a real bomb like that, there are some military people that would be* very *interested in it!*
