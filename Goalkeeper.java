package futball;
import java.lang.Math;

public class Goalkeeper{

private boolean left;
private int posY;
private int big;
private int speed;
private int uppLeftX; 
private int uppLeftY;
private int width; 
private int height;
// three default positions
private int up;
private int down;
private int middle;


public Goalkeeper(boolean left, int uppLeftX, int uppLeftY, int width, int height) {
	this.uppLeftX = uppLeftX;
	this.uppLeftY = uppLeftY;
	this.width = width;
	this.height = height;
	middle = uppLeftY + height / 2;
	up = middle - height / 40 * 3;
	down = middle + height / 40 * 3;
	speed = 1;
	this.left = left;
	posY = uppLeftY + height / 2;
	big = 20;
}

public int getBig() {
	return big;
}


public int getPosY() {
	return posY;
}

public void setPosY(int posY){
	this.posY = posY;
}

public void moveDown() {
	if (posY < down) posY += speed;	
}

public void moveUp() {
	if (posY > up) posY -= speed;	
}

public void moveMiddle() {
	if (posY < middle) posY += speed;
	if (posY > middle) posY -= speed;
}

public void followBall(int ballY) {
	if (ballY < uppLeftY + height / 20 * 7) {
		moveUp();
	} else if (ballY > uppLeftY + height / 20 * 13) {
		moveDown();
	} else {
		moveMiddle();
	}
}

}