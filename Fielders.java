package futball;
import java.lang.Math;

public class Fielders{

private boolean left;
private int numberOfPlayers;
private int[] posX;
private int[] posY;
private int radius; // how big the player is
private double[] direction; 
private double speed;
private int[][] goTo;
private int uppLeftX; 
private int uppLeftY;
private int width; 
private int height;
private int tactic;
// 0 for holding the position
// 1 for defense
// 2 for attack
// 3 for man-to-man guarding
// 4 for finding empty spaces
private Point pPoint;
private Point ePoint;

public Fielders(int players, boolean left, int uppLeftX, int uppLeftY, int width, int height) {
	goTo = new int[players][2];
	speed = Math.sqrt(20);
	radius = 10;
	numberOfPlayers = players;
	this.left = left;
	posX = new int[numberOfPlayers];
	posY = new int[numberOfPlayers];
	direction = new double[numberOfPlayers];
	this.uppLeftX = uppLeftX;
	this.uppLeftY = uppLeftY;
	this.width = width;
	this.height = height;
	tactic = 0;
}

public void setTactic(int tactic) {
	this.tactic = tactic;
}

public int getTactic() {
	return tactic;
}

// legacy code, not really used now
public void fivePlayers() {
	int a;
	int b;
	if (left) {
		a = 0;
		b = 0;
	} else {
		a = 3;
		b = 1;
	}
	posX[0] = width / 5 * (1+a) + uppLeftX;
	posY[0] = height / 3 * 1 + uppLeftY;
	
	posX[1] = width / 5 * (1+a) + uppLeftX;
	posY[1] = height / 3 * 2 + uppLeftY;	
	
	posX[2] = width / 5 * (3-b) + uppLeftX;
	posY[2] = height / 4 * 1 + uppLeftY;
	
	posX[3] = width / 5 * (3-b) + uppLeftX;
	posY[3] = height / 4 * 2 + uppLeftY;
	
	posX[4] = width / 5 * (3-b) + uppLeftX;
	posY[4] = height / 4 * 3 + uppLeftY;
}

public void setPosition(int[][] position){
	posX[0] = position[0][0]; 
	posY[0] = position[0][1];
			  	            
	posX[1] = position[1][0];
	posY[1] = position[1][1];
			  	            
    posX[2] = position[2][0];
    posY[2] = position[2][1];
			  	            
    posX[3] = position[3][0];
    posY[3] = position[3][1];
			  	            
    posX[4] = position[4][0];
    posY[4] = position[4][1];
}

public int[][] getPosition(){
	int[][] position = new int[5][2];
	position[0][0] = posX[0]; 
	position[0][1] = posY[0];
		              		
	position[1][0] = posX[1];
	position[1][1] = posY[1];
		              		
    position[2][0] = posX[2];
    position[2][1] = posY[2];
		              		
    position[3][0] = posX[3];
    position[3][1] = posY[3];
		             		
    position[4][0] = posX[4];
    position[4][1] = posY[4];
	return position;
}

// man-to-man guarding
public int[][] guardByNumberGetPosition(int[][] position){
	// player 0 will guard other team player 2
	// player 1 will guard other team player 4
	// player 2 will guard other team player 0
	// player 3 will guard other team player 3
	// player 4 will guard other team player 1
	int[][] result = new int[5][2];
	result[0][0] = position[2][0];  
	result[0][1] = position[2][1]; 
	         	           
	result[1][0] = position[4][0]; 
	result[1][1] = position[4][1]; 
	         	           
	result[2][0] = position[0][0]; 
	result[2][1] = position[0][1]; 
	         	           
	result[3][0] = position[3][0]; 
	result[3][1] = position[3][1]; 
	         	           
	result[4][0] = position[1][0]; 
	result[4][1] = position[1][1];
	for (int i = 0; i < 5; i++) {
		result[i][0] += (left ? radius * -2 : radius * 2);
	}
	return result;
}

//default defense position for left
public int[][] getDefensePosition() {
	int[][] result = new int[5][2];
	result[0][0] = width / 7 * 1 + uppLeftX;
	result[0][1] = height / 3 * 1 + uppLeftY;

	result[1][0] = width / 7 * 1 + uppLeftX;
	result[1][1] = height / 3 * 2 + uppLeftY;	

	result[2][0] = width / 9 * 2 + uppLeftX;
	result[2][1] = height / 4 * 1 + uppLeftY;

	result[3][0] = width / 6 * 1 + uppLeftX;
	result[3][1] = height / 4 * 2 + uppLeftY;

	result[4][0] = width / 9 * 2 + uppLeftX;
	result[4][1] = height / 4 * 3 + uppLeftY;	
	if (!left) flip(result);
	return result;
}

//default ATTACKING position for left
public int[][] getAttackPosition() {
	int[][] result = new int[5][2];
	result[0][0] = width / 20 * 7 + uppLeftX;
	result[0][1] = height / 3 * 1 + uppLeftY;

	result[1][0] = width / 20 * 7 + uppLeftX;
	result[1][1] = height / 3 * 2 + uppLeftY;	

	result[2][0] = width / 20 * 18 + uppLeftX;
	result[2][1] = height / 4 * 1 + uppLeftY;

	result[3][0] = width / 20 * 13 + uppLeftX;
	result[3][1] = height / 4 * 2 + uppLeftY;

	result[4][0] = width / 20 * 18 + uppLeftX;
	result[4][1] = height / 4 * 3 + uppLeftY;	
	if (!left) flip(result);
	return result;
}

// mirror the position to the half line
public int[][] flip(int[][] position) {
	int[][] result = position;
	for (int i = 0; i < 5; i++) {
		result[i][0] = position[i][0] + 2 * ((uppLeftX + width / 2) - position[i][0]);
	}
	return result;
}

// divide 5 positions between players. This is for the empty spaces tactic. If it is player left then the rightest position goes to player4, the second rightest to player3
// ... and so on

public int[][] chooseFiveEmptiest(int[][] position) {
	int[][] result = new int[5][2];

	// +1 : ascending , -1 descending
	int reverse = left ? 1 : -1;

	for (int i = 0; i < 5; i++) {
		System.out.println("original position " + i + " is: " + position[i][0] + ";" + position[i][1]);
	}

	System.out.println("\nAfter sorting:\n");

	int tempX;
	int tempY;
	for (int j = 0; j < 4; j++) {

	for (int i = 0; i < 4; i++) {
		if (reverse * position[i][0] > reverse * position[i + 1][0]) {
			tempX = position[i][0];
			tempY = position[i][1];
			position[i][0] = position[i + 1][0];
			position[i][1] = position[i + 1][1];
			position[i + 1][0] = tempX;
			position[i + 1][1] = tempY;
			result = position;

		}
	}
	}
	 
	for (int i = 0; i < 5; i++) {
		System.out.println("position " + i + " is: " + position[i][0] + ";" + position[i][1]);
	}
	
	// we need to modify this slightly as the two strikers are number 4 and number 2 and they should be at front. So in the result we should exchange 2 and 3
	tempX = result[2][0];
	tempY = result[2][1];
	result[2][0] = result[3][0];
	result[2][1] = result[3][1];
	result[3][0] = tempX;
	result[3][1] = tempY;
	
	
	return result;
}





// given 5 position, and the current positions of the players: everyone get to the nearest position
// return a matrix for leftToGo or rightToGo so the result is: result[n-th player (0th, 1th, 2th player)][x or y] 
// maybe this will be good/better for man-to-man guarding

public int[][] chooseNearest(int[][] position) {
	int[][] result = new int[5][2];
	// starting with the strikers
	
	
	return result;
}


public int getRadius() {
	return radius;
}

public void setGoTo(int[][] goTo){
	this.goTo = goTo;
}

public void setDirection(int player, double direct) {
	direction[player] = direct;
	//System.out.println("team: " + left + " player: " + player + " direction: " + direction[player]); 
}

public int getPosX(int player) {
	return posX[player];
}

public int getPosY(int player) {
	return posY[player];
}

public void setPosX(int player, int pos) {
	posX[player] = pos;
}

public void setPosY(int player, int pos) {
	posY[player] = pos;
}

public void move(int player) {
	
	posX[player] = (int) (posX[player] + Math.cos(direction[player]) * speed);
	posY[player] = (int) (posY[player] + Math.sin(direction[player]) * speed);
	
}



}
