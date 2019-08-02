package futball;
import java.lang.Math;
import java.util.Random;

public class Ball{

private Random rand;
private int shotStrength;
private double speed;
private int radius;
private double aim;
private int posX;
private int posY;
//private int xFlipper;
//private int yFlipper;

private Point point;
private final int maxShotStrength = 120;
private final int minShotStrength = 30;
private final int shotStrengthPrecis = 8;

public Ball(int uppLeftX, int uppLeftY, int width, int height) {
	//xFlipper = 1;
	//yFlipper = 1;
	rand = new Random();
	shotStrength = 30;
	//shotStrength = rand.nextInt(100);
	speed = Math.sqrt(20);
	radius = 10;
	//aim = 0;
	aim = Math.random() * 2 * Math.PI - Math.PI;
	posX = width / 2 + uppLeftX;
	//posY = 200;
	posY = height / 2 + uppLeftY;
	System.out.println("shotStrength is: " + shotStrength + " and the aim is: " + aim);
}

public int getPosX() {
	return posX;
}

public int getPosY() {
	return posY;
}

public void setPosX(int posX) {
	this.posX = posX;
}

public void setPosY(int posY) {
	this.posY = posY;
}

public int getRadius() {
	return radius;
}

public double getAim() {
	return aim;
}

public void setAim(double aim) {
	this.aim = aim;
}

public void setShotStrength(int shotStrength) {
	this.shotStrength = shotStrength;
}

public int getShotStrength() {
	return shotStrength;
}

public int getMaxShotStrength() {
	return maxShotStrength;
}

public void shotHarder() {
	shotStrength = shotStrength + shotStrengthPrecis;
	if (shotStrength > maxShotStrength) shotStrength = maxShotStrength;
}

public void shotSlower() {
	shotStrength = shotStrength - shotStrengthPrecis;
	if (shotStrength < minShotStrength) shotStrength = minShotStrength;
}

public void aimLeft(int aimingPrecision) {
	aim -= Math.PI * 2 / aimingPrecision;
	if (aim <= -1 * Math.PI) aim += Math.PI * 2;
}

public void aimRight(int aimingPrecision) {
	aim += Math.PI * 2 / aimingPrecision;
	if (aim >= Math.PI) aim -= Math.PI * 2;
}

// when the ball moves it slows down and stops. When the whole frame is repainted it deducts 1 from shotStrength
public void slow() {
	if (shotStrength > 0) shotStrength--;
}

public void move() {
	if (shotStrength > 0) {
	posX = (int) (posX + Math.cos(aim) * speed);
	posY = (int) (posY + Math.sin(aim) * speed);
	}
}

// when the ball hits the wall it's bounce back
public void bounce(int fieldUppLeftX, int fieldUppLeftY, int fieldWidth, int fieldHeight) {
	if(posX < fieldUppLeftX + radius) {
	//xFlipper *= -1;
	aim = Math.signum(aim) * (Math.PI - Math.abs(aim));
	posX += 4; // have to add this otherwise it glitches
}
if(posY < fieldUppLeftY + radius) {
	aim = -1.0 * aim;
	//yFlipper *= -1;
	posY += 4;
}
if(posX > fieldUppLeftX + fieldWidth - radius) {
	//xFlipper *= -1; 
	aim = Math.signum(aim) * (Math.PI - Math.abs(aim));
	posX -= 4;
}
if(posY > fieldUppLeftY + fieldHeight - radius) {
	aim = -1.0 * aim;
	//yFlipper *= -1;
	posY -= 4;
}
}

public boolean isStopped() {
	return shotStrength == 0;
}


}