readme.txt for futball game

amikor a labdát valaki elrúgja:
- a labda elmegy addig, ameddig
- fokozatosan lassul, végül megáll

kiszámoljuk, hogy ki van a legközelebb mezőnyjátékosok közül
aki a legközelebb van, az elindul a labda felé

a játék közben mindkét játékos  beállíthat mód-okat, ez határozza meg, hogy mit csinál a többi játékos, amíg az egyvalaki elmegy a labdáért

mód0: maradni ott ahol van

mód1:védekezés
mindenki visszafut egy előre meghatározott védekező pozícióba

mód2: támadás
feláll a csapat egy előre meghatározott támadó pozícióba

mód3: embert keresni
minél közelebb menni a legközelebbi ellenfélhez de egyben elosztani is őket: azok felé az ellenfelek felé futni, hogy a futások összege a legkevesebb legyen
az ellenfelet úgy fogjuk, hogy a saját kapunkat fedezzük
ha már kiválasztottad hogy melyik ellenfelet fogod, akkor maradsz rajta. 

mód4: üres területre mozogni
minél messzebb kerülni minden egyéb játékostól
persze nem a sarokba befutni

--------
ENGLISH-
--------

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


// ideas, new features
- randomize the attacking and the defense : not exact positions but squares where to go randomly
- empty spaces tactic: 
each player has a territory where to find the emptiest space
OR
considering the whole field, find the 5 most empty spaces
- interception: not square but circle. The closest it's is coming to the player + the weaker the shot is -> the higher the chance of interception
- creating options menu: 
	changing the keyboard keys associated with actions
	changing the colour of your team
	

// bugs
- during the game, when you press the arrow key to move the ball around you it stops moving when the opponent change tactic
- when a player reaches his designated position he keeps moving









