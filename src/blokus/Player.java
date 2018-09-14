package blokus;

class Player{
	private int playerNum;
	private int startX,startY;
	private Blocks []blocks;
	public Player(){
		blocks = new Blocks[22];
		for(int i=0;i<=21;i++)
			blocks[i] = new Blocks();
	}
	
	public void setBlocks(Blocks[]b){
		for(int i=1;i<=21;i++)
			blocks[i] = b[i];
	}
	public Blocks[] getBlocks(){
		return blocks;
	}
	
	public void setPlayerNum(int n){
		playerNum = n;
	}
	
	public int getPlayerNum(){
		return playerNum;
	}
	
	public void setStartX(int x){
		startX = x;
	}
	
	public void setStartY(int y){
		startY = y;
	}
	
	public int getStartX(){
		return startX;
	}
	
	public int getStartY(){
		return startY;
	}
	
	
}