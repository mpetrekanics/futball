readme.txt for the futball game

--------
ENGLISH-
--------

This is a futball game for 2 players. When you shot the ball and it stops the nearest player will have it. All your other players (for both team) can follow different 
tactics.  

*** CONTROLLS ***
LEFT PLAYER CONTROLLS:

A   : when you have the ball - aiming with the ball ||||||| when you don't have the ball - as a team, defense tactics
S   : when you have the ball - decrease the shot's strength ||||||| when you don't have the ball - as a team, guard a specific opponent
D   : when you have the ball - aiming with the ball ||||||| when you don't have the ball - as a team, attacking tactics
W   : when you have the ball - increase the shot's strength ||||||| when you don't have the ball - as a team, find empty spaces
TAB : when you have the ball - shot
0   : change between tactics

RIGHT PLAYER CONTROLLS:

LEFT_ARROW  : when you have the ball - aiming with the ball ||||||| when you don't have the ball - as a team, attacking tactics
DOWN_ARROW  : when you have the ball - decrease the shot's strength ||||||| when you don't have the ball - as a team, guard a specific opponent
RIGHT_ARROW : when you have the ball - aiming with the ball ||||||| when you don't have the ball - as a team, defense tactics
UP_ARROW    : when you have the ball - increase the shot's strength ||||||| when you don't have the ball - as a team, find empty spaces
CTRL        : when you have the ball - shot
. (PERIOD)  : change between tactics

I : log some information
P : pause the game
F1 : change the color of the left player
F2 : change the color of the right player

// ideas, new features
- randomize the attacking and the defense : not exact positions but squares where to go randomly
- empty spaces tactic: 

- creating options menu: 
	changing the keyboard keys associated with actions
	changing the values like maxShotStrength, etc..

I should rewrite interception like this:
2 kinds of interception:
- the ball directly hits a player - this is working right now
- interception with random factor -> the longer the ball goes near a player the bigger chance there is to intercept the ball by the opponent
- when the ball is intercepted the opponent will go for the ball (his nearest player)
The closest it's is coming to the player + the weaker the shot is -> the higher the chance of interception

- maybe slow down the player when he changes direction (90 degrees or more)


// known bugs
- during the game, when you press the arrow key to move the ball around you it stops moving when the opponent change tactic
- when a player reaches his designated position he keeps moving
- hard to reproduce: sometimes, when choosing empty spaces the players running off the field 
- man-to-man guard tactic for both teams can cause players running off the field







