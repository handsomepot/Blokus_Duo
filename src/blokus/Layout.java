package blokus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Layout{
	public String [][]player;
	public String [][]computer;
	public Blocks []playerBlocks;
	public Blocks []computerBlocks;
	
	public Layout(){
		playerBlocks = new Blocks[22];
		computerBlocks = new Blocks[22];
		for(int i = 0;i < 22;i++){
			playerBlocks[i] = new Blocks();
			computerBlocks[i] = new Blocks();
		}
		player = new String[63][5];
		computer = new String[34][6];
		setDisplay();
		setAllPatterns();
		
			
	}
	public void setDisplay(){
		try {
			Scanner scanner = new Scanner(new File("blokus2.txt"));
			for(int j=0;j<5;j++)
				for(int i=0;i<63;i++)
					player[i][j] = scanner.next();
	
			Scanner scanner2 = new Scanner(new File("blokus.txt"));
			for(int j=0;j<6;j++)
				for(int i=0;i<34;i++)
					computer[i][j] = scanner2.next();
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*fill each block with specific patterns*/
	public void setAllPatterns(){
		try {
			int n;
			int corners;
			int squares;
			Scanner scanner = new Scanner(new File("Patterns.txt"));
			for(int i=1;i<=21;i++){
				scanner.nextInt();
				corners = scanner.nextInt();//how many corners
				squares = scanner.nextInt();//how many squares
				n = scanner.nextInt();// how many patterns
				playerBlocks[i].setNumberOfPatterns(n);
				playerBlocks[i].setCorners(corners);
				playerBlocks[i].setSquares(squares);
				computerBlocks[i].setNumberOfPatterns(n);
				computerBlocks[i].setCorners(corners);
				computerBlocks[i].setSquares(squares);
		
				for(int j=0;j<n;j++){
					for(int k=0;k<5;k++){
						int a = scanner.nextInt();
						int b = scanner.nextInt();
						playerBlocks[i].allPatternsX[j][k] = a;
						playerBlocks[i].allPatternsY[j][k] = b;
						
						computerBlocks[i].allPatternsX[j][k] = a;
						computerBlocks[i].allPatternsY[j][k] = b;
						
						
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}