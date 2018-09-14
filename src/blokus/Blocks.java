package blokus;
class Blocks{
	private int blockNumber;
	private String blockID;
	private int numberOfPatterns;
	public int [][]allPatternsX;//all kinds of coordinates
	public int [][]allPatternsY;
	private int []x;
	private int []y;
	private int corners;//number of corners
	private int squares;//number of squares
	
	boolean isUsed;
	
	public Blocks(){
		this.isUsed = false;
		allPatternsX = new int[8][5];
		allPatternsY = new int[8][5];
		x = new int[5];
		y = new int[5];
	}
	
	public int getBlockNumber(){
		return blockNumber;
	}
	
	public String getBlockID(){
		return blockID;
	}
	

	public int getNumberOfPatterns(){
		return numberOfPatterns;
	}

	public boolean getUsed(){
		return isUsed;
	}

	public int[] getX(){
		x = allPatternsX[0];
		return x;
	}
	
	public int[] getY(){
		y = allPatternsY[0];
		return y;
	}
	
	public int getCorners(){
		
		return corners;
	}
	
	public int getSquares(){
		
		return squares;
	}
	
	
	public void setBlockID(String str){
		blockID = str;
	}
	
	public void setBlockNumber(int x){
		blockNumber = x;
	}
	
	public void setNumberOfPatterns(int x){
		numberOfPatterns = x;
	}
	
	
	public void setUsed(boolean used){
		isUsed = used;
	}
	
	public void setCorners(int c){
		
		corners = c;
	}
	public void setSquares(int sq){
		squares = sq;
	}
	
}