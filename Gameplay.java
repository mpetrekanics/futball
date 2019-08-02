package futball;
import java.lang.Math;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
public class Gameplay extends JPanel implements KeyListener, ActionListener{
private Random rand;
private boolean play = true;
private int score = 0;
private Timer timer;
private int delay = 16;
private int timeCounter = 0;
// field
private int width;
private int height;
private int fieldWidth;
private int fieldHeight;
private int fieldUppLeftX;
private int fieldUppLeftY;
private int wallThick;
// units
private Fielders left;
private Fielders right;
private Ball futball;
private Point aPoint;
private Point bPoint;
private Point cPoint;
private boolean playersTurn;
private boolean nearestTeamLeft;
private int nearestPlayer;
private boolean someoneHasTheBall;
private int ballPlayerD = 20;
private int aimingPrecision = 72;
private boolean ballMoving;
// goalkeepers
private Goalkeeper leftgk;
private Goalkeeper rightgk;
// interception
private int[][] interceptors = new int[5][2];
// scores
private int leftScore;
private int rightScore;
private boolean goal;
private double goalTreshold = 0.3;
// tactics
private int[][] leftGoTo;
private int[][] rightGoTo;
private boolean leftChangedTacticWithArrow;
private boolean rightChangedTacticWithArrow;
// empty positions matrix:
// ----------------------x-----y----radius
// emptiest-------------- ----- ----
// second emptiest------- ----- ----

private int[][] emptiestFive;
private double circlePrecis = 3.0;
private boolean debuggingOn = false;


//CONSTRUCTOR-------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

public Gameplay(int width, int height) {
System.out.println("");
// field 
this.width = width;
this.height = height;
wallThick = width / 93;
fieldUppLeftX = width / 56;
fieldUppLeftY = height / 6;
fieldWidth = width - (wallThick * 4 + 3);
fieldHeight = height - (fieldUppLeftY + wallThick * 4);
// necessary
addKeyListener(this);
setFocusable(true);
setFocusTraversalKeysEnabled(false);
timer = new Timer(delay, this);
timer.start();
// miscellaneous
rand = new Random();
aPoint = new Point(0, 0);
bPoint = new Point(0, 0);
cPoint = new Point(0, 0);
// teams
left = new Fielders(5, true, fieldUppLeftX, fieldUppLeftY, fieldWidth, fieldHeight);
left.setPosition(left.getDefensePosition());
right = new Fielders(5, false, fieldUppLeftX, fieldUppLeftY, fieldWidth, fieldHeight);
right.setPosition(right.getDefensePosition());
playersTurn = false;
nearestTeamLeft = true;
nearestPlayer = 0;
someoneHasTheBall = false;
ballMoving = true;
leftgk = new Goalkeeper(true, fieldUppLeftX, fieldUppLeftY, fieldWidth, fieldHeight);
rightgk = new Goalkeeper(false, fieldUppLeftX, fieldUppLeftY, fieldWidth, fieldHeight);
// ball
futball = new Ball(fieldUppLeftX, fieldUppLeftY, fieldWidth, fieldHeight);
// interception
interceptors = leftTeamPosition(true);
// score
leftScore = 0;
rightScore = 0;
goal = false;
// tactics
leftGoTo = new int[5][2];
rightGoTo = new int[5][2];
left.setTactic(0);
right.setTactic(0);
leftChangedTacticWithArrow = false;
rightChangedTacticWithArrow = false;
emptiestFive = new int[5][3];
cleanEmptiestFive();
}

//PAINTING --------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

public void paint(Graphics g) {
//background
g.setColor(Color.black);
g.fillRect(1,1, width - 8, height - 8);
//side
g.setColor(Color.green);
g.fillRect(fieldUppLeftX - wallThick,fieldUppLeftY - wallThick, fieldWidth+wallThick*2, fieldHeight+wallThick*2);
g.setColor(Color.black);
g.fillRect(fieldUppLeftX,fieldUppLeftY,fieldWidth,fieldHeight);
// goal
g.setColor(Color.red);
g.fillRect(fieldUppLeftX - wallThick,fieldUppLeftY + fieldHeight / 20 * 7,wallThick,fieldHeight / 20 * 6);
g.fillRect(fieldUppLeftX + fieldWidth,fieldUppLeftY + fieldHeight / 20 * 7,wallThick,fieldHeight / 20 * 6);
// shot strength
g.setColor(Color.white);
g.fillRect(width / 2 - 60,10,futball.getShotStrength(),10);
//temporary debugging emptiest circle, uncomment to see circles
if (debuggingOn) {
	g.setColor(Color.green);
	for (int i = 0; i < 5; i++){
		g.drawOval(emptiestFive[i][0] - emptiestFive[i][2], emptiestFive[i][1] - emptiestFive[i][2], emptiestFive[i][2]*2, emptiestFive[i][2]*2);
	}
}
//players
g.setColor(Color.orange);
for (int i = 0; i < 5; i++){
	g.fillOval(left.getPosX(i) - left.getRadius(), left.getPosY(i) - left.getRadius(), left.getRadius()*2, left.getRadius()*2);
}
g.fillRect(fieldUppLeftX - wallThick/2,leftgk.getPosY() - leftgk.getBig()/2, wallThick/2, leftgk.getBig());
g.setColor(Color.cyan);
for (int i = 0; i < 5; i++){
	g.fillOval(right.getPosX(i) - right.getRadius(), right.getPosY(i) - right.getRadius(), right.getRadius()*2, right.getRadius()*2);
}
g.fillRect(fieldUppLeftX + fieldWidth,rightgk.getPosY() - rightgk.getBig()/2, wallThick/2, rightgk.getBig());
//ball
g.setColor(Color.red);
g.fillOval(futball.getPosX() - futball.getRadius(), futball.getPosY() - futball.getRadius(), futball.getRadius()*2, futball.getRadius()*2);


//scores
g.setColor(Color.white);
g.setFont(new Font("serif", Font.BOLD, 25));
g.drawString(""+leftScore + " - " + rightScore, width / 2 - 40, 60);
if (goal) {
	g.setColor(Color.white);
	g.setFont(new Font("serif", Font.BOLD, 40));
	g.drawString("GOAL", width / 2 - 60, 100);
}
//tactics - it should show the current tactics above the field that a player has chosen
switch (left.getTactic()) {
	case 1:
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("DEFENSE", 30, 100);
		break;
	case 2:
		g.setColor(Color.red);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("ATTACK", 30, 100);
		break;
	case 3:
		g.setColor(Color.blue);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("MAN-TO-MAN GUARD", 30, 100);		
		break;
	case 4:
		g.setColor(Color.yellow);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("EMPTY SPACES", 30, 100);		
		break;
	default: //case 0
		g.setColor(Color.green);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("HOLD THIS POSITION", 30, 100);	
}
switch (right.getTactic()) {
	case 1:
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("DEFENSE", 800, 100);
		break;
	case 2:
		g.setColor(Color.red);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("ATTACK", 800, 100);
		break;
	case 3:
		g.setColor(Color.blue);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("MAN-TO-MAN GUARD", 800, 100);	
		break;
	case 4:
		g.setColor(Color.yellow);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("EMPTY SPACES", 800, 100);	
		break;
	default: //case 0
		g.setColor(Color.green);
		g.setFont(new Font("serif", Font.BOLD, 40));
		g.drawString("HOLD THIS POSITION", 800, 100);	
}

g.dispose();
}

// GAME PHASES --------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//this part runs multiple times and lines with setting a boolean and others are not necessary to run multiple times. This might slows down the game.

public void actionPerformed(ActionEvent e) {
timer.start();

if(play) {
// these things happen every time, these are 'above' the phases
if (debuggingOn) {
	cleanEmptiestFive();
	findEmptiestFive(20, circlePrecis);
}	

if (left.getTactic() == 3) leftGoTo = attacker(true).guardByNumberGetPosition(attacker(false).getPosition()); // so the player follow the other player, you don't have to press
if (right.getTactic() == 3) rightGoTo = attacker(false).guardByNumberGetPosition(attacker(true).getPosition()); // ... the down button again and again 
// GAME PHASES: ball moving - players are preparing (this is just a moment) - one player is going for the ball, others are following the current tactic - aiming with the ball
// ... , others don't move - ball moving again
// ball is moving
if (ballMoving && !playersTurn && !someoneHasTheBall) {
	futball.bounce(fieldUppLeftX, fieldUppLeftY, fieldWidth, fieldHeight);
	futball.slow(); // decrease shot strength
	futball.move();
	intercept(interceptors);
	teamScored(true);
	teamScored(false);
}
// ball stopped, prepare for players turn
if (futball.isStopped() && !playersTurn && !someoneHasTheBall) {
	System.out.println("The ball has stopped");
	ballMoving = false;
	futball.setShotStrength(30);
	nearestTeamLeft = nearestTeamLeft();
	nearestPlayer = nearestPlayer(nearestTeamLeft);
	playersTurn = true;
	//System.out.println("this is the players turn." + nearestTeamLeft + nearestPlayer);
	// where to go for the ball
	setDirectionToBall(nearestTeamLeft, nearestPlayer);
}
// the players moving, one for the ball, and all others somewhere else
if (playersTurn && !someoneHasTheBall && !ballMoving) {
	if (!playerCatchBall(nearestTeamLeft, nearestPlayer)) {
		setDirectionToBall(nearestTeamLeft, nearestPlayer);
		movePlayer(nearestTeamLeft, nearestPlayer);
		if (left.getTactic() != 0) moveTeam(true); // move the whole team (except the one player who goes for the ball) unless the tactic is HOLD THIS POSITION
		if (right.getTactic() != 0) moveTeam(false);
		leftgk.followBall(futball.getPosY()); // the goalkeeper moves inside the goal so he is closer to the ball. There are only 3 positions and the ball is, respectively
		// ... the upper part of the field - in front of the goal - lower part of the field
		rightgk.followBall(futball.getPosY());
	} else {
		playersTurn = false;
		someoneHasTheBall = true;
		//if (nearestTeamLeft) futball.catchedBy(true, ballPlayerD);
		//if (!nearestTeamLeft) futball.catchedBy(false, ballPlayerD);
		if (nearestTeamLeft) futball.setAim(0); //by default, a player who catches the ball has the ball turned toward the opponent's goal
		if (!nearestTeamLeft) futball.setAim(Math.PI); 
		putBallNearKicker();
	}
}
// aiming with the ball
if (!playersTurn && someoneHasTheBall && !ballMoving) {
	putBallNearKicker();
	interceptors = leftTeamPosition(!nearestTeamLeft); // only the defender team can intercept the ball, not the kicker team
}
}
repaint();
}

// PRESSING A KEY--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

public void keyTyped(KeyEvent e) {}

public void keyPressed(KeyEvent e) {
if(e.getKeyCode() == KeyEvent.VK_I) {
/*for (int j = 0; j < 5; j++) {
	System.out.println("left pos " + j + " is: (" + left.getPosX(j) + " ;" + left.getPosY(j) + ")");
	System.out.println("right pos " + j + " is: (" + right.getPosX(j) + " ;" + right.getPosY(j) + ")");
	
}*/
System.out.println("*******************************************************info***************************************** ");
System.out.println("\nThe nearest team is: " + nearestTeamLeft() + "\nThe nearest LEFT player is: " + nearestPlayer(true) + "\nThe nearest RIGHT player is: " + nearestPlayer(false));
distances();
System.out.println("playersTurn " + playersTurn);
System.out.println("someoneHasTheBall " + someoneHasTheBall);
System.out.println("ballMoving " + ballMoving);
System.out.println("ball's aim: " + futball.getAim());
System.out.println("the attacker player's coordinates are: " + attacker(nearestTeamLeft).getPosX(nearestPlayer) + " " + attacker(nearestTeamLeft).getPosY(nearestPlayer));
System.out.println("shotstrength is: " + futball.getShotStrength());
//System.out.println("The sum of distances between 0;0 and every player on the field is: " + findDistanceFromEveryPlayer(0, 0));
System.out.println("There is a wall or an other player in 50 radius of the ball " + isSomebodyOrWallInCircle(futball.getPosX(),futball.getPosY(),50));
//System.out.println("the emptiest space on the field is: " + findTheEmptiestSpace(20, 10.0)[0] + ";" + findTheEmptiestSpace(20, 10.0)[1]); 
//cleanEmptiestFive();
//findEmptiestFive(20, 10.0); // this interferes with the game 
debuggingOn = !debuggingOn;
}
if(e.getKeyCode() == KeyEvent.VK_A) {
	if (someoneHasTheBall && nearestTeamLeft) {
		futball.aimLeft(aimingPrecision);
	} else {
		leftGoTo = attacker(true).getDefensePosition();
		left.setTactic(1);
		leftChangedTacticWithArrow = true;
	}
}
if(e.getKeyCode() == KeyEvent.VK_D) {
	if (someoneHasTheBall && nearestTeamLeft) {
		futball.aimRight(aimingPrecision);
	} else {
		leftGoTo = attacker(true).getAttackPosition();
		left.setTactic(2);
		leftChangedTacticWithArrow = true;		
	}
}
if(e.getKeyCode() == KeyEvent.VK_W) {
	if (someoneHasTheBall && nearestTeamLeft) {
		futball.shotHarder();
	} else {
		cleanEmptiestFive();
		findEmptiestFive(20, circlePrecis);
		leftGoTo = attacker(true).chooseFiveEmptiest(convertEmptiestFive());
		left.setTactic(4);
		leftChangedTacticWithArrow = true;
	}
}
if(e.getKeyCode() == KeyEvent.VK_S) {
		if (someoneHasTheBall && nearestTeamLeft) {
		futball.shotSlower();
	} else {
		leftGoTo = attacker(true).guardByNumberGetPosition(attacker(false).getPosition());
		left.setTactic(3);
		leftChangedTacticWithArrow = true;
	}
}

if(e.getKeyCode() == KeyEvent.VK_TAB) {
	if (someoneHasTheBall && nearestTeamLeft) {
		someoneHasTheBall = false;
		ballMoving = true;
	}
}

if(e.getKeyCode() == KeyEvent.VK_0) {
	if (leftChangedTacticWithArrow) {
		left.setTactic(0);
		leftChangedTacticWithArrow = false;
	} else {
		left.setTactic(left.getTactic() + 1);
		if (left.getTactic() == 5) left.setTactic(0);
	}
	switch (left.getTactic()) {
	case 1:
		leftGoTo = attacker(true).getDefensePosition();
		break;
	case 2:
		leftGoTo = attacker(true).getAttackPosition();
		break;
	case 3:
		leftGoTo = attacker(true).guardByNumberGetPosition(attacker(false).getPosition());
		break;
	case 4:
		cleanEmptiestFive();
		findEmptiestFive(20, circlePrecis);
		leftGoTo = attacker(true).chooseFiveEmptiest(convertEmptiestFive());
		break;
	default: //case 0
		}	
}

if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	if (goal && !play) {
		goal = false;
		left.setPosition(left.getDefensePosition());
		right.setPosition(right.getDefensePosition());
		left.setTactic(0);
		right.setTactic(0);
		play = true;
		futball = new Ball(fieldUppLeftX, fieldUppLeftY, fieldWidth, fieldHeight);
	}
}
if(e.getKeyCode() == KeyEvent.VK_LEFT) {
	if (someoneHasTheBall && !nearestTeamLeft) {
		futball.aimLeft(aimingPrecision);
	} else {
		rightGoTo = attacker(false).getAttackPosition();
		right.setTactic(2);
		rightChangedTacticWithArrow = true;
	}
}
if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
	if (someoneHasTheBall && !nearestTeamLeft) {
		futball.aimRight(aimingPrecision);
	} else {
		rightGoTo = attacker(false).getDefensePosition();
		right.setTactic(1);
		rightChangedTacticWithArrow = true;
	}
}

if(e.getKeyCode() == KeyEvent.VK_UP) {
	if (someoneHasTheBall && !nearestTeamLeft) {
		futball.shotHarder();
	} else {
		cleanEmptiestFive();
		findEmptiestFive(20, circlePrecis);
		rightGoTo = attacker(false).chooseFiveEmptiest(convertEmptiestFive());
		right.setTactic(4);
		rightChangedTacticWithArrow = true;
	}
}

if(e.getKeyCode() == KeyEvent.VK_DOWN) {
	if (someoneHasTheBall && !nearestTeamLeft) {
		futball.shotSlower();
	} else {
		rightGoTo = attacker(false).guardByNumberGetPosition(attacker(true).getPosition());
		right.setTactic(3);
		rightChangedTacticWithArrow = true;
	}
}

if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
	if (someoneHasTheBall && !nearestTeamLeft) {
		someoneHasTheBall = false;
		ballMoving = true;
	}
}

if(e.getKeyCode() == KeyEvent.VK_PERIOD) {
	if (rightChangedTacticWithArrow) {
		right.setTactic(0);
		rightChangedTacticWithArrow = false;
	} else {
		right.setTactic(right.getTactic() + 1);
		if (right.getTactic() == 5) right.setTactic(0);
	}
	switch (right.getTactic()) {
	case 1:
		rightGoTo = attacker(false).getDefensePosition();
		break;
	case 2:
		rightGoTo = attacker(false).getAttackPosition();
		break;
	case 3:
		rightGoTo = attacker(false).guardByNumberGetPosition(attacker(true).getPosition());
		break;
	case 4:
		cleanEmptiestFive();
		findEmptiestFive(20, circlePrecis);
		rightGoTo = attacker(false).chooseFiveEmptiest(convertEmptiestFive());
		break;
	default: //case 0
		}
}
}

public void keyReleased(KeyEvent e) {}

// WHO WILL CATCH THE BALL --------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

public boolean nearestTeamLeft() {
	double leftDis;
	double rightDis;
	aPoint.setPoint(futball.getPosX(), futball.getPosY());
	cPoint.setPoint(left.getPosX(nearestPlayer(true)), left.getPosY(nearestPlayer(true)));
	leftDis = aPoint.distance(cPoint);
	cPoint.setPoint(right.getPosX(nearestPlayer(false)), right.getPosY(nearestPlayer(false)));
	rightDis = aPoint.distance(cPoint);
	return leftDis < rightDis ? true : false;
	
}

public void distances() {
	// only for team left
	aPoint.setPoint(futball.getPosX(), futball.getPosY());
	for (int i = 0; i < 5; i++) {
			bPoint.setPoint(left.getPosX(i), left.getPosY(i));
			System.out.println("the distance between left " + i + " and the ball is: " + aPoint.distance(bPoint));
		}
	System.out.println();
	System.out.println();
}

public int nearestPlayer(boolean team) {
	aPoint.setPoint(futball.getPosX(), futball.getPosY());
	double min = Integer.MAX_VALUE; 
	int result = 0; //nézzük ezt a részt meg később
	for (int i = 0; i < 5; i++) {
			bPoint.setPoint(attacker(team).getPosX(i), attacker(team).getPosY(i));
			if (aPoint.distance(bPoint) < min) {
				min = aPoint.distance(bPoint);
				result = i;
			}
		}
	return result;
} 

public void movePlayer(boolean team, int player) {
	attacker(team).move(player);
}

public void setDirectionToBall(boolean team, int player) {
	aPoint.setPoint(futball.getPosX(), futball.getPosY());
	bPoint.setPoint(attacker(team).getPosX(player), attacker(team).getPosY(player));
	attacker(team).setDirection(player, bPoint.direction(aPoint));
}

public boolean playerCatchBall(boolean team, int player) {
	return Math.abs(futball.getPosX() - attacker(team).getPosX(player)) < 5 && Math.abs(futball.getPosY() - attacker(team).getPosY(player)) < 5;	
	}


// put the ball near to the kicker player using the Ball class aim attribute
public void putBallNearKicker() {
	futball.setPosX((int) (attacker(nearestTeamLeft).getPosX(nearestPlayer) + Math.cos(futball.getAim()) * ballPlayerD));
	futball.setPosY((int) (attacker(nearestTeamLeft).getPosY(nearestPlayer) + Math.sin(futball.getAim()) * ballPlayerD));	
}

// simplyfies the code; doesn't have to write left and right over and over again. Just use attacker(true) or attacker(false) 
public Fielders attacker(boolean team) {
	if (team) return left;
	return right;
}

// INTERCEPTION --------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

// more than 5 player will crush this  
// makes a team's position into a matrix
public int[][] leftTeamPosition(boolean team) {
	int[][] result = new int[5][2];
	for (int i = 0; i < 5; i++){
		result[i][0] = attacker(team).getPosX(i); 
		result[i][1] = attacker(team).getPosY(i);
	}
	return result;
}

//temporary; could be much better. A player catches every ball in a square around him.
public void intercept(int[][] interceptors) {
	for (int i = 0; i < 5; i++) { 
		if(Math.abs(futball.getPosX() - interceptors[i][0]) < 40 && Math.abs(futball.getPosY() - interceptors[i][1]) < 40 && futball.getShotStrength() < 90) {
			futball.setShotStrength(0);
			System.out.println("interception!");
			break;
		}
	}
}

// GOAL --------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

public void teamScored(boolean team) {
	if(futball.getPosX() > fieldUppLeftX + fieldWidth - futball.getRadius() && futball.getPosX() < fieldUppLeftX + fieldWidth && 
	futball.getPosY() > fieldUppLeftY + fieldHeight / 20 * 7 && futball.getPosY() < fieldUppLeftY + fieldHeight / 20 * 13 && 
	team) {
		double goalLikelihood = calculateGoalLikelihood(false);		
		if (futball.getPosY() > rightgk.getPosY() - rightgk.getBig() && futball.getPosY() < rightgk.getPosY() + rightgk.getBig() ) {
			//futball.setAim(Math.signum(futball.getAim()) * (Math.PI - Math.abs(futball.getAim())));
			futball.setAim(Math.PI);
			futball.setPosX(futball.getPosX() - 4);
		} else if (goalLikelihood < goalTreshold){
			rightgk.setPosY(futball.getPosY());
			futball.setAim(Math.PI);
			futball.setPosX(futball.getPosX() - 4);
		} else {
			leftScore++;
			play = false;
			goal = true;
		}
	}
	if(futball.getPosX() > fieldUppLeftX && futball.getPosX() < fieldUppLeftX + futball.getRadius() && 
	futball.getPosY() > fieldUppLeftY + fieldHeight / 20 * 7 && futball.getPosY() < fieldUppLeftY + fieldHeight / 20 * 13 && 
	!team) {
		double goalLikelihood = calculateGoalLikelihood(true);			
		if (futball.getPosY() > leftgk.getPosY() - leftgk.getBig() && futball.getPosY() < leftgk.getPosY() + leftgk.getBig()) {
			//futball.setAim(Math.signum(futball.getAim()) * (Math.PI - Math.abs(futball.getAim()))); // goal denied by the goalkeeper when it physically hits him
			futball.setPosX(futball.getPosX() + 4); 
			futball.setAim(0.0); // he saves it and it bounce in front of him
		} else if (goalLikelihood < goalTreshold){ // goalkeeper saves it with his skills and with luck
			leftgk.setPosY(futball.getPosY());
			futball.setPosX(futball.getPosX() + 4);
			futball.setAim(0.0);
		} else { // it's a goal
			rightScore++; 
			play = false;
			goal = true;
		}
	}
}

// TACTICS --------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

// move the whole team to specific locations (except that player who go for the ball)
public void moveTeam(boolean team) {
	//leftGoTo = attacker(team).getDefensePosition();
	for (int i = 0; i < 5; i++) {
		if (nearestPlayer == i && nearestTeamLeft == team) continue;
		aPoint.setPoint(team ? leftGoTo[i][0] : rightGoTo[i][0], team ? leftGoTo[i][1] : rightGoTo[i][1]);
		bPoint.setPoint(attacker(team).getPosX(i), attacker(team).getPosY(i));
		attacker(team).setDirection(i, bPoint.direction(aPoint));
		attacker(team).move(i);
	}
}

// maybe I should do the empty space tactic with dividing the field 
public void findEmptySpaceUp(boolean team) {
	// find the emptiest space in the oppontets side, in his third, the upper part
}

public void findEmptySpaceDown(boolean team) {
	// find the emptiest space in the oppontets side, in his third, the lower part	
}

public void findEmptySpaceMiddle(boolean team) {
		// find the emptiest space in the middle third
		
}

//trying without divining the field

public double findDistanceFromEveryPlayer(int x, int y) {
	double sum = 0.0;
	aPoint.setPoint(x, y);
	for (int i = 0; i < 5; i++) {
			bPoint.setPoint(left.getPosX(i), left.getPosY(i));
			sum += aPoint.distance(bPoint);
	}
	for (int i = 0; i < 5; i++) {
			bPoint.setPoint(right.getPosX(i), right.getPosY(i));
			sum += aPoint.distance(bPoint);
	}
	return sum;
}

//public double findSquareDistanceFromEveryPlayer(int x, int y) {}

// this is not good enough, it always find a corner. Maybe we should use the squares (power2) of the distances somehow. Until I figure it out I'll try drawing increasing
// ... circles around every point of the field 
/*public int[] findTheEmptiestSpace(int precis) {
	int[] result = new int[2];
	double max = 0.0;
	int i = 1;
	int j = 1;
	double temp = 0;
	System.out.println("fieldWidth: " + fieldWidth + "\nfieldHeight: " + fieldHeight + "\nfieldUppLeftX: " + fieldUppLeftX + "\nfieldUppLeftY: " + fieldUppLeftY);
	while (precis * i < fieldWidth) {
		while (precis * j < fieldHeight) {
			temp = findDistanceFromEveryPlayer(fieldUppLeftX + precis * i, fieldUppLeftY + precis * j);
			//System.out.println("at: " + (fieldUppLeftX + precis * i) + ";" + (fieldUppLeftY + precis * j) + " the current temp is: " + temp);
			if (temp > max) {
				max = temp;
				result[0] = fieldUppLeftX + precis * i;
				result[1] = fieldUppLeftY + precis * j;
			}  
			j++;
		}
		j = 1;
		i++;
	}
	return result;
}*/
/*
public int[][] findFiveEmptiestSpace() {
	int[][] result = new int[5][2];
	return result;
}*/

// so trying it with circles

public boolean isSomebodyOrWallInCircle(int x, int y, double rad) {
	aPoint.setPoint(x, y);
	//every (10) players
	for (int i = 0; i < 5; i++) {
			bPoint.setPoint(left.getPosX(i), left.getPosY(i));
			if (aPoint.distance(bPoint) < rad) return true;
	}
	for (int i = 0; i < 5; i++) {
			bPoint.setPoint(right.getPosX(i), right.getPosY(i));
			if (aPoint.distance(bPoint) < rad) return true;
	}
	// walls
	if (x - rad < fieldUppLeftX) return true;
	if (y - rad < fieldUppLeftY) return true;
	if (x + rad > fieldUppLeftX + fieldWidth) return true;
	if (y + rad > fieldUppLeftY + fieldHeight) return true;
	// and it also has to turn true when it intersects with an already found circle = empty space 
	for (int i = 0; i < 5; i++) {
		if (emptiestFive[i][2] == 0) {
			break;
		} else {
			bPoint.setPoint(emptiestFive[i][0], emptiestFive[i][1]);
			if (aPoint.distance(bPoint) < rad + emptiestFive[i][2]) return true; 
		}
	}
	return false;
}

// it divides the field into squares. Then it calculates the maximum circle in every corner. The side of the square is netPrecis. 
// you can increase the ..precis values if you have a computer with good processor

public int[] findTheEmptiestSpace(int netPrecis, double circlePrecis) {
	int[] result = new int[3];
	double max = 0.0;
	int i = 1;
	int j = 1;
	double temp = 0;
	double rad = 20.0;
	System.out.println("fieldWidth: " + fieldWidth + "\nfieldHeight: " + fieldHeight + "\nfieldUppLeftX: " + fieldUppLeftX + "\nfieldUppLeftY: " + fieldUppLeftY);
	while (netPrecis * i < fieldWidth) {
		while (netPrecis * j < fieldHeight) {
			while (!isSomebodyOrWallInCircle(fieldUppLeftX + netPrecis * i, fieldUppLeftY + netPrecis * j, rad)) {
				rad += circlePrecis; // drawing bigger and bigger circles until it finds something
			}
			temp = rad - circlePrecis;
			//System.out.println("at: " + (fieldUppLeftX + precis * i) + ";" + (fieldUppLeftY + precis * j) + " the current temp is: " + temp);
			if (temp > max) {
				max = temp;
				result[0] = fieldUppLeftX + netPrecis * i;
				result[1] = fieldUppLeftY + netPrecis * j;
			}
			rad = 20.0;
			j++;
		}
		j = 1;
		i++;
	}
	result[2] = (int) max;
	return result;
}

// possible feature: if the circle is too big (two players should go there) it should be divided to half
public void findEmptiestFive(int netPrecis, double circlePrecis) {
	emptiestFive[0] = findTheEmptiestSpace(netPrecis, circlePrecis);
	emptiestFive[1] = findTheEmptiestSpace(netPrecis, circlePrecis);
	emptiestFive[2] = findTheEmptiestSpace(netPrecis, circlePrecis);
	emptiestFive[3] = findTheEmptiestSpace(netPrecis, circlePrecis);
	emptiestFive[4] = findTheEmptiestSpace(netPrecis, circlePrecis);
}

public void cleanEmptiestFive() {
	for (int i = 0; i < 5; i++) {
		for (int j = 0; j < 3; j++) {
			emptiestFive[i][j] = 0;
		}
	}
}

//the closest player should get the closest empty space, etc
// this is just the easiest way to set the positions between players

public void emptiestFiveToTeam(boolean team) {
	for (int i = 0; i < 5; i++) {
		if (team) {
			leftGoTo[i][0] = emptiestFive[i][0];
			leftGoTo[i][1] = emptiestFive[i][1];
		} else {
			rightGoTo[i][0] = emptiestFive[i][0];
			rightGoTo[i][1] = emptiestFive[i][1];
		}
	}	
}

public int[][] convertEmptiestFive() {
	int[][] result = new int[5][2];
	for (int i = 0; i < 5; i++) {
		result[i][0] = emptiestFive[i][0];
		result[i][1] = emptiestFive[i][1];
	}
	return result;
}



// GOALKEEPER --------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------------------------

public double calculateGoalLikelihood(boolean team) {
	double factorShot = (double) (futball.getShotStrength()) / (double) (futball.getMaxShotStrength()); //relatively how strong the shot is
	double factorPlace = (double) (Math.abs(futball.getPosY() - (team ? leftgk.getPosY() : rightgk.getPosY()))) / (double) (fieldHeight / 20 * 3); // you should aim the corner of the goal and far 
	double factorRandom = Math.random() / 2 - 0.25;
	// ... from the goalkeeper
	System.out.println("futball.getMaxShotStrength(): " + futball.getMaxShotStrength());
	System.out.println("futball.getShotStrength(): " + futball.getShotStrength());
	System.out.println("fieldHeight / 20 * 3: " + fieldHeight / 20 * 3); // the half of the length of the goalline
	System.out.println("futball.getPosY(): " + futball.getPosY());
	System.out.println("goalkeeper " + (team ? leftgk.getPosY() : rightgk.getPosY()));
	System.out.println("(double) (futball.getShotStrength()) / (double) (futball.getMaxShotStrength()) = factorShot : " + 
	factorShot);
	System.out.println("(double) (Math.abs(futball.getPosY() - *WHATEVERGOALKEEPER*.getPosY())) / (double) (fieldHeight / 20 * 3) = factorPlace: " + factorPlace);
	System.out.println("Math.random() / 3.0 - 0.167 = factorRandom: " + factorRandom);
	System.out.println("factorShot * factorPlace + factorRandom : " 
	+ (factorShot * factorPlace + factorRandom) + " This should be greater than " + goalTreshold + " to be a goal, otherwise the goalkeeper saves.");
	return factorShot * factorPlace + factorRandom;
}

}