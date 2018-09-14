package blokus;

import java.util.ArrayList;


class State{
	private int [][]board;
	private int moves = 0;
	private boolean []playerUsed;
	private boolean []computerUsed;
	private ArrayList<Action> actionList;
	public State(){
		board = new int[14][14];
		playerUsed = new boolean[22];
		computerUsed = new boolean[22];
		actionList = new ArrayList<Action>();
	}
	public void setActionList(ArrayList<Action>list){
		actionList = list;
	}
	public ArrayList<Action> getActionList(){
		return actionList;
	}
	public void setBoard(int [][]b){
		for(int i = 0;i < 14;i++)
			for(int j = 0;j < 14;j++)
				board[i][j] = b[i][j];
	}
	

	
	public void setPlayerUsed(boolean []use){
		for(int i = 0; i < 22;i++)
			playerUsed[i] = use[i];
	}
	
	public void setComputerUsed(boolean []use){
		for(int i = 0; i < 22;i++)
			computerUsed[i] = use[i];
	}

	public void setMoves(int m){
		moves = m;
	}
	

	public int[][] getBoard(){
		return board;
	}

	
	public boolean[] getPlayerUsed(){
		return playerUsed;
	}
	public boolean[] getComputerUsed(){
		return computerUsed;
	}

	public int getMoves(){
		return moves;
	}
	
}