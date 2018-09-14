package blokus;
class Action{
	private int x;
	private int y;
	private int a;
	private int b;
	private int playerNum;
	//block[x],pattern[y] can be placed on (a,b)
	public Action(){
		x = y = a = b = 0;
	}
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getA(){
		return a;
	}
	public int getB(){
		return b;
	}
	
	public int getPlayerNum(){
		return playerNum;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setA(int a){
		this.a = a;
	}
	public void setB(int b){
		this.b = b;
	}
	public void setPlayerNum(int n){
		playerNum = n;
	}
	
	
}