package futball;

public class Statistics{

private int leftScore;
private int rightScore;
private int leftPasses;
private int rightPasses;
private int leftSaves;
private int rightSaves;
private int leftInterceptions;
private int rightInterceptions;
private long leftPoss;
private long rightPoss;


public Statistics() {
	leftScore = 0;
	rightScore = 0;
	leftPasses = 0;
	rightPasses = 0;
	leftSaves = 0;
	rightSaves = 0;
	leftInterceptions = 0;
	rightInterceptions = 0;
	leftPoss = 0L;
	rightPoss = 0L;
}

public void display() {
	int percentLeftPoss = (int) ((double) leftPoss / (double) (leftPoss + rightPoss) * 100.0);
	
	System.out.println("\n\n\n**********************************STATISTICS***************************************");
	System.out.println("Left Team ------------- Right Team");
	System.out.println(leftScore + "            Goals            " + rightScore);
	System.out.println((leftScore + rightSaves) + "       Attempts On Target    " + (rightScore + leftSaves));
	System.out.println(leftSaves + "            Saves            " + rightSaves);
	System.out.println(leftInterceptions + "         Interceptions       " + rightInterceptions);
	System.out.println(leftPasses + "      Passes Completed       " + rightPasses);
	System.out.println(percentLeftPoss + "%     Ball Possession (%)    " + (100 - percentLeftPoss) + "%");
	System.out.println("\n\n");
	
	/*System.out.println("leftScore;                         : " + leftScore                                 );
	System.out.println("rightScore;                         : " + rightScore                                 );
	System.out.println("leftPasses;                         : " + leftPasses                                 );
	System.out.println("rightPasses;                         : " + rightPasses                                 );
	System.out.println("leftSaves;                         : " + leftSaves                                 );
	System.out.println("rightSaves;                         : " + rightSaves                                 );
	System.out.println("leftInterceptions;                         : " + leftInterceptions                                 );
	System.out.println("rightInterceptions;                         : " + rightInterceptions                                 );
	System.out.println("left possession: " + leftPoss);
	System.out.println("right possession: " + rightPoss);*/	
}

public void addLeftScore(){leftScore++;}

public void addRightScore(){rightScore++;}

public void addLeftPasses(){leftPasses++;}

public void addRightPasses(){rightPasses++;}

public void addLeftSaves(){leftSaves++;}

public void addRightSaves(){rightSaves++;}

public void addLeftInterceptions(){leftInterceptions++;}

public void addRightInterceptions(){rightInterceptions++;}

public void addLeftPoss() {leftPoss++;}

public void addRightPoss() {rightPoss++;}


}
