package futball;
import java.lang.Math;

public class Point{

private int x;
private int y;

public Point(int x, int y) {
	this.x = x;
	this.y = y;
}

public int getX() {
return x;
}

public int getY() {
return y;
}

public void setX(int x) {
this.x = x;
}

public void setY(int y) {
this.y = y;
}

public void setPoint(int x, int y) {
	this.x = x;
	this.y = y;
}

public double direction(Point target) {
	return Math.atan2(target.y - y, target.x - x);	
}

public double distance(Point target) {
	return Math.sqrt(Math.pow(Math.abs(x - target.x), 2) + Math.pow(Math.abs(y - target.y), 2));
}

}