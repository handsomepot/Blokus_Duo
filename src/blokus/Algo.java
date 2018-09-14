package blokus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

class Algo{
	Layout layout;
	private Player []playerInfo = new Player[3];
	int []step = new int [22];
	int []patternNum = new int[22];
	int DEPTH = 0;
	int [][]nowBoard = new int[14][14];
	int [][]V = new int[14][14];
	ArrayList<Action>lastActions = new ArrayList<Action>();
	long time = 0;
	
	public Algo(Player []playerInfo,ArrayList<Action>last){
		layout = new Layout();
		this.playerInfo = playerInfo;
		step[0] = 1;
	    step[1] = 3;
	    step[2] = 4;
	    step[3] = 15;
	    step[4] = 11;
	    step[5] = 8;
	    step[6] = 9;
	    step[7] = 5;
	    step[8] = 4;
	    step[9] = 2;
	    step[10] = 7;
	    lastActions = last;
	    

	}
	/*num means which player*/
	public boolean isValid(int [][]board,int a,int b,int []coordX,int[] coordY,int num){

		int ta,tb;
		int validConers = 0;
		int []row = {0, 0,1,-1,1, 1,-1,-1};
		int []col = {1,-1,0, 0,1,-1, 1,-1};
		int sx = playerInfo[num].getStartX();
		int sy = playerInfo[num].getStartY();

		if(board[sx][sy] == 0)
		{
			for(int i = 0; i < 5;i++){	
				ta = a + coordX[i];
				tb = b + coordY[i];
				if(!(ta >= 0 && ta < 14 && tb >=0 && tb < 14))
					return false;
				if(ta == sx && tb == sy)//contains (4,4) or (9,9)
					return true;
			}
			return false;
		}
		
		for(int i = 0; i < 5;i++){
			
			ta = a + coordX[i];
			tb = b + coordY[i];

			if(!(ta >= 0 && ta < 14 && tb >=0 && tb < 14))//outside the board
				return false;
			if(board[ta][tb] != 0)//has something on the board
				return false;
			
			for(int k = 0; k < 8;k++)
			{
				int a1 = ta + row[k];
				int b1 = tb + col[k];
				if(k < 4){
					
					if(a1 < 0 || a1 >= 14 || b1 < 0 || b1 >=14)
						continue;
					if(board[a1][b1] == num)//touch the adjacent blocks
						return false;
				}
				else{
					if(a1 < 0 || a1 >= 14 || b1 < 0 || b1 >=14)
						continue;
					if(board[a1][b1] == num)//has validCorners
						validConers++;
				}
			}
		}
		if(validConers != 0)
			return true;
		return false;
	}
	
	//number of legal moves
	private int calLegalMoves(State s,int player){
		int c = 0;
		int []legal = new int[22];
		int []x = new int[5]; int []y = new int[5];
		int [][]b = new int[14][14];
		boolean []used = new boolean[22];
		b = s.getBoard();
		if(player==1)
			used = s.getPlayerUsed();
		else
			used = s.getComputerUsed();
		for(int cx = 0; cx < 14;cx++)
			for(int cy = 0; cy < 14;cy++)
				nowBoard[cx][cy] = s.getBoard()[cx][cy];
		for(int i = 1; i <= 21 ;i++){
			if(!used[i])
			{
				for(int j = 0; j < playerInfo[player].getBlocks()[i].getNumberOfPatterns();j++){
					x = playerInfo[player].getBlocks()[i].allPatternsX[j];
					y = playerInfo[player].getBlocks()[i].allPatternsY[j];
					for(int cx = 0; cx < 14;cx++)
						for(int cy = 0; cy < 14;cy++){
							
							if(isValid(b, cx, cy, x, y, player)){

								c++;
								legal[i]=1;
								
								for(int k=0;k<5;k++){
									nowBoard[cx+x[k]][cy+y[k]] = player;
								}

							}
						}
					}
			}
		}
		/*for(int i = 1; i <= 21 ;i++){
			if(legal[i]==1)
				c+=playerInfo[player].getBlocks()[i].getSquares();
		}*/
		return c;
	}
	private ArrayList<Action> monteFindActions(State state,int player){
		ArrayList<Action> validActions = new ArrayList<Action>();
		validActions.clear();
		
		int []x = new int[5]; int []y = new int[5];
		int [][]b = new int[14][14];
		boolean []used = new boolean[22];
		b = state.getBoard();
		if(player==1)
			used = state.getPlayerUsed();
		else
			used = state.getComputerUsed();
		
		for(int i = 1; i <= 18 ;i++){
			if(!used[i])
			{
				int limit = playerInfo[player].getBlocks()[i].getNumberOfPatterns();
				
				for(int j = 0; j < limit;j++){
					x = playerInfo[player].getBlocks()[i].allPatternsX[j];
					y = playerInfo[player].getBlocks()[i].allPatternsY[j];
					for(int cx = 0 ; cx < 14;cx++)
						for(int cy = 0; cy < 14;cy++){
						if(b[cx][cy]!=0)
							continue;
						if(isValid(b, cx, cy, x, y, player)){
									
								Action act = new Action();
								act.setX(i);//block i
								act.setY(j);//pattern j
								act.setA(cx);//can be placed on (cx,cy)
								act.setB(cy);
								act.setPlayerNum(player);
								validActions.add(act);
								
							}
						}
				}
			}
		}
		
		

		return validActions;
		
	}
	private boolean isNow(int cx,int cy, int []x,int []y,int a,int b){
		
		for(int i=0;i<5;i++){

			if(a == cx+x[i] && b == cy+y[i]){
				
				return true;
			}
		}
		return false;
	}
	private boolean upKiss(State s,int cx,int cy, int []x,int []y,int player){
		int [][]b = new int[14][14];
		int []t1 = new int[14];
		
		for(int i=0;i<14;i++)
			for(int j=0;j<14;j++){
				b[i][j] = s.getBoard()[i][j];
			}
		for(int i=0;i<5;i++){
			b[cx+x[i]][cy+y[i]] = player;
		}
		
		for(int i=0;i<14;i++){
			t1[i] = -1;
			for(int j=0;j<14;j++)
			{
				if(b[i][j]!=0){
					t1[i] = j;
					break;
				}
					
			}
		}
		
		//t1 up kiss
		for(int i=1;i+1<14;i++)
		{
			if(t1[i]==0||t1[i]==13)
				continue;
			if(t1[i]!=-1 && t1[i+1]!=-1 && t1[i]==t1[i+1]){
				if(b[i][t1[i]] != b[i+1][t1[i+1]]){
						if(isNow(cx,cy,x,y,i,t1[i])||isNow(cx,cy,x,y,i+1,t1[i+1]))
							return true;
					
					
				}
					
			}
		}
		return false;
	}
	private boolean downKiss(State s,int cx,int cy, int []x,int []y,int player){
//t2 down kiss
		int [][]b = new int[14][14];
		int []t2 = new int[14];
		
		for(int i=0;i<14;i++)
			for(int j=0;j<14;j++){
				b[i][j] = s.getBoard()[i][j];
			}
		for(int i=0;i<5;i++){
			b[cx+x[i]][cy+y[i]] = player;
		}
		for(int i=13;i>=0;i--){
			t2[i] = -1;
			for(int j=13;j>=0;j--)
			{
				if(b[i][j]!=0){
					t2[i] = j;
					break;
				}
					
			}
		}
		for(int i=1;i+1<14;i++)
		{
			if(t2[i]==0||t2[i]==13)
				continue;
			if(t2[i]!=-1 && t2[i+1]!=-1 && t2[i]==t2[i+1]){
				if(b[i][t2[i]] != b[i+1][t2[i+1]]){
					if(isNow(cx,cy,x,y,i,t2[i])||isNow(cx,cy,x,y,i+1,t2[i+1]))
						return true;
				}
					
						
			}
		}
		return false;
	}
	private boolean leftKiss(State s,int cx,int cy, int []x,int []y,int player){
		//left kiss
		int [][]b = new int[14][14];

		int []t3 = new int[14];

		for(int j=0;j<14;j++){
			t3[j] = -1;
			for(int i=0;i<14;i++)
			{
				if(b[i][j]!=0){
					
					t3[j] = i;
					break;
				}
					
			}
		}
		for(int j=1;j+1<14;j++){
			if(t3[j]==0||t3[j]==13)
				continue;
			if(t3[j]!=-1 && t3[j+1]!=-1 && t3[j]==t3[j+1]){
				if(b[t3[j]][j] != b[t3[j+1]][j+1])
					if(isNow(cx,cy,x,y,t3[j],j)||isNow(cx,cy,x,y,t3[j+1],j+1))
						return true;
			}
		}
		
		return false;
	}
	private boolean rightKiss(State s,int cx,int cy, int []x,int []y,int player){
		//left kiss
		int [][]b = new int[14][14];

		int []t4 = new int[14];

		//right kiss
		for(int j=13;j>=0;j--){
			t4[j] = -1;
			for(int i=13;i>=0;i--)
			{
				if(b[i][j]!=0){
					t4[j] = i;
					break;
				}
					
			}
		}
		for(int j=1;j+1<14;j++){
			if(t4[j]==0||t4[j]==13)
				continue;
			if(t4[j]!=-1 && t4[j+1]!=-1 && t4[j]==t4[j+1]){
				if(b[t4[j]][j] != b[t4[j+1]][j+1])
					if(isNow(cx,cy,x,y,t4[j],j)||isNow(cx,cy,x,y,t4[j+1],j+1))
						return true;
			}
		}

		
		return false;
	}
	private boolean isSafe(int [][]board,int cx,int cy, int []x,int []y,int player){
		int [][]b = new int[14][14];
		for(int i=0;i<14;i++)
			for(int j=0;j<14;j++){
				b[i][j] = board[i][j];
			}
		for(int i=0;i<5;i++){
			b[cx+x[i]][cy+y[i]] = player;
			int ta = cx+x[i];
			int tb = cy+y[i];
			if(ta-1 < 0 || ta-2 < 0|| ta+1>=14 || ta+2>=14 || tb-2< 0 || tb-1< 0 || tb+1 >=14 || tb+2 >=14)
				continue;
			if(b[ta-1][tb]==0 && b[ta-2][tb]==1)
				return false;
			if(b[ta][tb-1]==0 && b[ta][tb-2]==1)
				return false;
			
			if(b[ta+1][tb]==0 && b[ta+2][tb]==1)
				return false;
			if(b[ta][tb+1]==0 && b[ta][tb+2]==1)
				return false;
			
		}
		return true;
		
	}
	private boolean isKiss(int [][]board,int cx,int cy, int []x,int []y,int player){
		int [][]b = new int[14][14];
		int []t1 = new int[14];
		int []t2 = new int[14];
		int []t3 = new int[14];
		int []t4 = new int[14];
		
		for(int i=0;i<14;i++)
			for(int j=0;j<14;j++){
				b[i][j] = board[i][j];
			}
		for(int i=0;i<5;i++){
			b[cx+x[i]][cy+y[i]] = player;
			
		}
		//t1 up kiss
		for(int k=2;k<14;k++){
			for(int i=0;i<14;i++){
				t1[i] = -1;
				for(int j=k;j<14;j++)
				{
					if(b[i][j]!=0 && b[i][j-1]==0){
						t1[i] = j;
						break;
					}
						
				}
			}
			for(int i=1;i+1<14;i++)
			{
				if(t1[i]==0||t1[i]==13)
					continue;
				if(t1[i]!=-1 && t1[i+1]!=-1 && t1[i]==t1[i+1]){
					if(b[i][t1[i]] != b[i+1][t1[i+1]]){
							if(isNow(cx,cy,x,y,i,t1[i])||isNow(cx,cy,x,y,i+1,t1[i+1]))
								if(isBoundary(b, i,t1[i], player)&&isBoundary(b, i+1,t1[i+1], 1)||isBoundary(b, i,t1[i], 1)&&isBoundary(b, i+1,t1[i+1], player))
								return true;
						
						
					}
						
				}
			}
			for(int i=0;i<14;i++)
				t1[i] = -1;
		}
		
		
		
		
		
		for(int i=0;i<14;i++)
			for(int j=0;j<14;j++){
				b[i][j] = board[i][j];
			}
		for(int i=0;i<5;i++){
			b[cx+x[i]][cy+y[i]] = player;
		}
		for(int i=13;i>=0;i--){
			t2[i] = -1;
			for(int j=13;j>=0;j--)
			{
				if(b[i][j]!=0){
					t2[i] = j;
					break;
				}
					
			}
		}
		for(int i=1;i+1<14;i++)
		{
			if(t2[i]==0||t2[i]==13)
				continue;
			if(t2[i]!=-1 && t2[i+1]!=-1 && t2[i]==t2[i+1]){
				if(b[i][t2[i]] != b[i+1][t2[i+1]]){
					if(isNow(cx,cy,x,y,i,t2[i])||isNow(cx,cy,x,y,i+1,t2[i+1]))
						return true;
				}
					
						
			}
		}
		for(int k=2;k<14;k++){
			for(int j=0;j<14;j++){
				t3[j] = -1;
				for(int i=k;i<14;i++)
				{
					if(b[i][j]!=0 && b[i-1][j]==0){
						t3[j] = i;
						break;
					}
						
				}
			}
			for(int j=1;j+1<14;j++){
				if(t3[j]==0||t3[j]==13)
					continue;
				if(t3[j]!=-1 && t3[j+1]!=-1 && t3[j]==t3[j+1]){
					if(b[t3[j]][j] != b[t3[j+1]][j+1])
						if(isNow(cx,cy,x,y,t3[j],j)||isNow(cx,cy,x,y,t3[j+1],j+1))
							return true;
				}
			}
			for(int i=0;i<14;i++){
				t3[i] = -1;
			}
		}
		
		
		
		for(int j=13;j>=0;j--){
			t4[j] = -1;
			for(int i=13;i>=0;i--)
			{
				if(b[i][j]!=0){
					t4[j] = i;
					break;
				}
					
			}
		}
		for(int j=1;j+1<14;j++){
			if(t4[j]==0||t4[j]==13)
				continue;
			if(t4[j]!=-1 && t4[j+1]!=-1 && t4[j]==t4[j+1]){
				if(b[t4[j]][j] != b[t4[j+1]][j+1])
					if(isNow(cx,cy,x,y,t4[j],j)||isNow(cx,cy,x,y,t4[j+1],j+1))
						return true;
			}
		}

		return false;
		
	}
	
	
	public ArrayList<Action> findActions(State state,int player){
		ArrayList<Action> validActions = new ArrayList<Action>();
		validActions.clear();
		int dx = 0,dy=0;
		
		int []x = new int[5]; int []y = new int[5];
		int [][]b = new int[14][14];
		boolean []used = new boolean[22];
		b = state.getBoard();
		if(player==1)
			used = state.getPlayerUsed();
		else
			used = state.getComputerUsed();
		
		
			for(int i = 1; i <= 21 ;i++){
				if(!used[i])
				{
					if(state.getMoves()<=3){
						if(playerInfo[2].getBlocks()[i].getSquares()<=3)
							continue;
						if(i==6)
							continue;
					}
					
					int limit = playerInfo[player].getBlocks()[i].getNumberOfPatterns();
					
					for(int j = 0; j < limit;j++){
						x = playerInfo[player].getBlocks()[i].allPatternsX[j];
						y = playerInfo[player].getBlocks()[i].allPatternsY[j];
						for(int cx = 0; cx < 14;cx++)
							for(int cy = 0; cy < 14;cy++){
							if(isValid(b, cx, cy, x, y, player)){
										
									Action act = new Action();
									act.setX(i);//block i
									act.setY(j);//pattern j
									act.setA(cx);//can be placed on (cx,cy)
									act.setB(cy);
									act.setPlayerNum(player);
									validActions.add(act);
									
								}
							}
					}
				}
			}
		

		

		return validActions;
		
	}
	
	
	public Action findInBooks(Action playerAction){
		Action act = new Action();
		try {
			
	    	String name = "book1.txt";
	    	Scanner scanner = new Scanner(new File(name));
			while(scanner.hasNext()){
				int b = scanner.nextInt();
				int p = scanner.nextInt();
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				
				int b2 = scanner.nextInt();
				int p2 = scanner.nextInt();
				int x2 = scanner.nextInt();
				int y2 = scanner.nextInt();
				
				int pb,pp,px,py;
				pb = playerAction.getX();
				pp = playerAction.getY();
				px = playerAction.getA();
				py = playerAction.getB();
				if(pp == p && pb == b && px == x && py == y)
				{	
					act.setX(b2);
					act.setY(p2);
					act.setA(x2);
					act.setB(y2);
					
					return act;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return act;
		
	}
	
	public Action findInBooks2(ArrayList<Action> actionList){
		Action act = new Action();
		try {
			Scanner scanner = new Scanner(new File("book2.txt"));
			while(scanner.hasNext()){
				int b = scanner.nextInt();
				int p = scanner.nextInt();
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				
				int b2 = scanner.nextInt();
				int p2 = scanner.nextInt();
				int x2 = scanner.nextInt();
				int y2 = scanner.nextInt();
				
				int b3 = scanner.nextInt();
				int p3 = scanner.nextInt();
				int x3 = scanner.nextInt();
				int y3 = scanner.nextInt();
				
				int b4 = scanner.nextInt();
				int p4 = scanner.nextInt();
				int x4 = scanner.nextInt();
				int y4 = scanner.nextInt();
				
				
				int pb1,pp1,px1,py1;
				int pb2,pp2,px2,py2;
				int pb3,pp3,px3,py3;
				pb1 = actionList.get(0).getX();
				pp1 = actionList.get(0).getY();
				px1 = actionList.get(0).getA();
				py1 = actionList.get(0).getB();
				
				pb2 = actionList.get(1).getX();
				pp2 = actionList.get(1).getY();
				px2 = actionList.get(1).getA();
				py2 = actionList.get(1).getB();
				
				pb3 = actionList.get(2).getX();
				pp3 = actionList.get(2).getY();
				px3 = actionList.get(2).getA();
				py3 = actionList.get(2).getB();
				
				
				if(pp1 == p && pb1 == b && px1 == x && py1 == y)
				{	
					if(pp2 == p2 && pb2 == b2 && px2 == x2 && py2 == y2)
					{
						if(pp3 == p3 && pb3 == b3 && px3 == x3 && py3 == y3){
							act.setX(b4);
							act.setY(p4);
							act.setA(x4);
							act.setB(y4);
							
							return act;
						}
					}
					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return act;
	}
	
	public boolean noMove(State s,int player){
		int []x = new int[5]; int []y = new int[5];
		int [][]b = new int[14][14];
		boolean []used = new boolean[22];
		b = s.getBoard();
		if(player==1)
			used = s.getPlayerUsed();
		else
			used = s.getComputerUsed();
		for(int i = 1; i <= 21 ;i++){
			if(!used[i])
			{
				for(int j = 0; j < playerInfo[player].getBlocks()[i].getNumberOfPatterns();j++){
					x = playerInfo[player].getBlocks()[i].allPatternsX[j];
					y = playerInfo[player].getBlocks()[i].allPatternsY[j];
					for(int cx = 0; cx < 14;cx++)
						for(int cy = 0; cy < 14;cy++){
						if(isValid(b, cx, cy, x, y, player)){		
								return false;

							}
						}
				}
			}
		}
		return true;
	}
	public boolean isTerminal(int depth,State state,int player){
		int m = state.getMoves();
		if(noMove(state, player))
			return true;
	
		if(m<=2)
			if(depth>=1)
				return true;
		if(depth >= DEPTH)
			return true;
		
		return false;
	}
	private boolean isCorner(State s,int i,int j,int n){
		int []row = {1, 1,-1,-1};
		int []col = {1,-1, 1,-1};
		for(int k = 0; k < 4; k++){
			int a = i + row[k];
			int b = j + col[k];
			if(a < 0 || b < 0 || a >=14 || b >= 14)
				continue;
			if(nowBoard[a][b] == n){
				return true;
			}
				
		}
		return false;
	}
	
	private boolean isBoundary(int [][]board,int i,int j,int n){
		int []row = {0, 0,1,-1};
		int []col = {1,-1,0, 0};
		int []used = new int[4];
		int c = 0;
		for(int k = 0; k < 4; k++){
			int a = i + row[k];
			int b = j + col[k];
			if(a < 0 || b < 0 || a >=14 || b >= 14)
				continue;
			if(board[a][b] == n){
				used[k] = 1;
				c++;
			}
				
		}
		if(c <= 1)
			return true;
		if(c==2){
			if(used[0]==1 && used[2]==1)
				return true;
			if(used[0]==1 && used[3]==1)
				return true;
			if(used[1]==1 && used[2]==1)
				return true;
			if(used[1]==1 && used[3]==1)
				return true;
		}
		return false;
		
	}
	public int getScore(State s){
		int sc = 0;
		if(win(s, 1)){
			sc+=10000;
			return sc;
		}
		if(win(s,2)){
			sc-=10000;
			return sc;
		}
		int []x = new int[5];
		int []y = new int[5];
		
		boolean [][]critical = new boolean[14][14];
		boolean [][]critical2 = new boolean[14][14];
		for(int i=0;i<13;i++){
			for(int j=0;j<13;j++){
				if(s.getBoard()[i][j] == 1 && s.getBoard()[i+1][j+1] == 1){
					if(s.getBoard()[i+1][j] !=1 && s.getBoard()[i][j+1] !=1){
						
						if(isNotBlocked(s, i+1, j,1))
							critical[i][j+1] = true;
						if(isNotBlocked(s, i, j+1,1))
							critical[i+1][j] = true;
					}
					
				}
				
				if(s.getBoard()[i][j] == 2 && s.getBoard()[i+1][j+1] == 2){
					if(s.getBoard()[i+1][j] !=1 && s.getBoard()[i][j+1] !=2){
						
						if(isNotBlocked(s, i+1, j,2))
							critical2[i][j+1] = true;
						if(isNotBlocked(s, i, j+1,2))
							critical2[i+1][j] = true;
					}
					
				}
				if(s.getBoard()[i+1][j] == 1 && s.getBoard()[i][j+1] == 1){
					
					if(s.getBoard()[i][j] !=1 && s.getBoard()[i+1][j+1] !=1){
						
						if(isNotBlocked(s, i, j,1))
							critical[i+1][j+1] = true;
						if(isNotBlocked(s, i+1, j+1,1))
							critical[i][j] = true;
					}
				}
				
				if(s.getBoard()[i+1][j] == 2 && s.getBoard()[i][j+1] == 2){
					
					if(s.getBoard()[i][j] !=2 && s.getBoard()[i+1][j+1] !=2){
						
						if(isNotBlocked(s, i, j,2))
							critical2[i+1][j+1] = true;
						if(isNotBlocked(s, i+1, j+1,2))
							critical2[i][j] = true;
					}
				}
				
				
				
			}
			
		}
		
		
		int []area1 = new int[4];
		int []area2 = new int[4];
		
		sc += calLegalMoves(s, 1)*4;
		
		for(int i = 0;i < 14;i++){
			for(int j = 0; j < 14;j++){
				if(s.getBoard()[i][j]==0){
					
						
					
				}
				
				if(s.getBoard()[i][j]==0){
					if(nowBoard[i][j]==1){
						sc+=1;
					}
				}
				if(i<7&&j<7){
					if(s.getBoard()[i][j]==1){
						area1[0]++;
					}
					if(s.getBoard()[i][j]==2){
						area2[0]++;
					}
				}
				
				if(i<7&&j>=7){
					if(s.getBoard()[i][j]==1){
						area1[1]++;
					}
					if(s.getBoard()[i][j]==2){
						area2[1]++;
					}
				}
				
				if(i>=7&&j<7){
					if(s.getBoard()[i][j]==1){
						area1[2]++;
					}
					if(s.getBoard()[i][j]==2){
						area2[2]++;
					}
				}
				
				if(i>=7&&j>=7){
					if(s.getBoard()[i][j]==1){
						area1[3]++;
					}
					if(s.getBoard()[i][j]==2){
						area2[3]++;
					}
				}
			}
		}
		int min1 = 0,ma1 = 100;
		int min2 = 0,ma2 = 100;
		for(int i=0;i<4;i++){
			if(area1[i] < ma1){
				ma1 = area1[i];
				min1 = i;
			}
			if(area2[i] < ma2){
				ma2 = area2[i];
				min2 = i;
			}
		}
		
		for(int i = 0;i < 14;i++)
			for(int j = 0; j < 14;j++){
				if(min1 == 0){
					if(i<7&&j<7){
						if(nowBoard[i][j]==1)
							sc+=10;
					}
				}
				
				if(min1 == 1){
					if(i<7&&j>=7){
						if(nowBoard[i][j]==1)
							sc+=10;
					}
				}
				
				if(min1 == 2){
					if(i>=7&&j<7){
						if(nowBoard[i][j]==1)
							sc+=10;
					}
				}
				
				if(min1 == 3){
					if(i>=7&&j>=7){
						if(nowBoard[i][j]==1)
							sc+=10;
					}
				}
				if(s.getBoard()[i][j]==1){
					if(isBoundary(s.getBoard(), i, j, 1)){//在boundary
						if(isCorner(s, i, j, 1)){
							//有合法的角
							sc+=25;
							
						}
							
					}
				}
			}
		
		sc -= calLegalMoves(s, 2)*4;
		
		for(int i = 0;i < 14;i++){
			for(int j = 0; j < 14;j++){
				if(s.getBoard()[i][j]==0){
					if(nowBoard[i][j]==2){
						sc-=1;
					}
				}
			}
		}
		
		for(int i = 0;i < 14;i++)
			for(int j = 0; j < 14;j++){
				if(min2 == 0){
					if(i<7&&j<7){
						if(nowBoard[i][j]==2)
							sc-=10;
					}
				}
				
				if(min2 == 1){
					if(i<7&&j>=7){
						if(nowBoard[i][j]==2)
							sc-=10;
					}
				}
				
				if(min2 == 2){
					if(i>=7&&j<7){
						if(nowBoard[i][j]==2)
							sc-=10;
					}
				}
				
				if(min2 == 3){
					if(i>=7&&j>=7){
						if(nowBoard[i][j]==2)
							sc-=10;
					}
				}
				
				if(s.getBoard()[i][j]==2){
					if(isBoundary(s.getBoard(), i, j, 2)){//在boundary
						if(isCorner(s, i, j, 2)){
							//有合法的角
							sc-=25;

						}
							
					}
				}
			}
		
		
		for(int i=1;i<=21;i++){
			if(s.getPlayerUsed()[i]){
				//sc += layout.playerBlocks[i].getCorners();
				sc += layout.playerBlocks[i].getSquares()*400;
			}
				
			if(s.getComputerUsed()[i]){
				//sc -= layout.computerBlocks[i].getCorners();
				sc -= layout.computerBlocks[i].getSquares()*400;
			}
				
		}
		
		
		return sc;
		
	}
	public State result(State s,Action action,int player){
		State newState = new State();
		int b = action.getX();
		int p = action.getY();
		int []x = new int[5]; int []y = new int[5];
		int [][]board = new int[14][14];
		boolean []playerUsed = new boolean[22];
		boolean []computerUsed = new boolean[22];
		int aa = action.getA();
		int bb = action.getB();
		ArrayList<Action> list = s.getActionList();
		for(int i=0;i<14;i++)
			for(int j=0;j<14;j++){
				board[i][j] = s.getBoard()[i][j];
			}
		for(int i=0;i<5;i++){
			if(player==1){
				x[i] = layout.playerBlocks[b].allPatternsX[p][i];
				y[i] = layout.playerBlocks[b].allPatternsY[p][i];
			}
			else
			{
				x[i] = layout.computerBlocks[b].allPatternsX[p][i];
				y[i] = layout.computerBlocks[b].allPatternsY[p][i];
			}
			if(aa+x[i]>=0&&aa+x[i]<14&&bb+y[i]>=0&&bb+y[i]<14)
				board[aa+x[i]][bb+y[i]] = player;
			
		}
		for(int i=1;i<=21;i++){
			playerUsed[i] = s.getPlayerUsed()[i];
			computerUsed[i] = s.getComputerUsed()[i];
		}
		list.add(action);
		newState.setActionList(list);
		if(player==1)
			playerUsed[b] = true;
		else
			computerUsed[b] = true;
		
		newState.setBoard(board);
		newState.setComputerUsed(computerUsed);
		newState.setPlayerUsed(playerUsed);
		if(player==1)
			newState.setMoves(s.getMoves()+1);
		else
			newState.setMoves(s.getMoves());
		
	
		return newState;
	}
	public boolean win(State s,int p){
		int c = 0;
		if(p==1)
		if(noMove(s,2)){
			for(int i=1;i<=21;i++){
				if(s.getPlayerUsed()[i]){
					c += layout.playerBlocks[i].getSquares();
				}
					
				if(s.getComputerUsed()[i]){
					
					c -= layout.computerBlocks[i].getSquares();
				}
					
			}
			if(c>0)
				return true;
			

		}
		
		if(p==2)
			if(noMove(s,1)){
				for(int i=1;i<=21;i++){
					if(s.getPlayerUsed()[i]){
						c += layout.playerBlocks[i].getSquares();
					}
						
					if(s.getComputerUsed()[i]){
						
						c -= layout.computerBlocks[i].getSquares();
					}
						
				}
				if(c<0)
					return true;
				

			}
		
		
		
		return false;
				
	}
	
	
	public Action bestMonte(State s){
		Map <Action, Integer > win = new HashMap<Action,Integer>();
		win.clear();
		
		
		for(int k = 0;k < 10;k++){
			ArrayList<Action> winners = monteCarlo(s);
			
			for(int a = 0; a < winners.size();a++){
				
				if(win.containsKey(winners.get(a))){
					
					win.put(winners.get(a), win.get(winners.get(a))+1);
				}
				else{
					
					win.put(winners.get(a), 1);
				}
				
				
			}
			winners.clear();
		}
		int max = -1;
		
		Action best = new Action();
		for (Map.Entry<Action,Integer> entry : win.entrySet()) {
			  Action key = entry.getKey();
			  Integer value = entry.getValue();
			  if(value > max)
			  {
				  max = value;
				  best = key;
				  //System.out.println(max);
			  }
			}
		if(best.getX()==0)
			return bestMove(s);
		
		return best;
	}
	public ArrayList<Action> monteCarlo(State s){
		State ss = s;
		ArrayList<Action> winners = new ArrayList<Action>();
		winners.clear();
		
		ArrayList<Action> actions = findActions(s,2);

		
		for(int a = 0; a < actions.size();a++)
		{
			ss = s;
			
			State s2 = result(ss,actions.get(a),2);//player 2 goes
			if(win(s2,2))
			{
				winners.clear();
				winners.add(actions.get(a));
				return winners;
			}

			for(int k = 0;k < 20;k++){
				ArrayList<Action> action1 = findActions(s2,1);
				State s1 = new State();
				if(action1.size()!=0){
					Random rr1 = new Random();
			    	int r1 = Math.abs(rr1.nextInt())%(action1.size());//player1 goes
			    	//r1 = r1%(action1.size());
			    	
					s1 = result(s2,action1.get(r1),1);

					if(win(s1,1)){
						winners.add(actions.get(a));
						break;
					}
		            	
				}
					
				
				ArrayList<Action> action2 = findActions(s1,2);
				if(action2.size()==0){
					if(win(s2,2)){
						winners.add(actions.get(a));
						break;
					}
					continue;
				}
				Random rr2 = new Random();
		    	int r2 = Math.abs(rr2.nextInt())%action2.size();//player2 goes
				s2 = result(s1,action2.get(r2),2);
				
				if(win(s2,2)){
					winners.add(actions.get(a));
					break;
				}
			}
			
			
		}
		return winners;
		
			
	}
	public int minArea(State state){
		int []area1 = new int[4];
		int []area2 = new int[4];
		for(int i=0;i<14;i++)
			for(int j=0;j<14;j++){
				if(i<7&&j<7){
					if(state.getBoard()[i][j]==1){
						area1[0]++;
					}
					if(state.getBoard()[i][j]==2){
						area2[0]++;
					}
				}
				
				if(i<7&&j>=7){
					if(state.getBoard()[i][j]==1){
						area1[1]++;
					}
					if(state.getBoard()[i][j]==2){
						area2[1]++;
					}
				}
				
				if(i>=7&&j<7){
					if(state.getBoard()[i][j]==1){
						area1[2]++;
					}
					if(state.getBoard()[i][j]==2){
						area2[2]++;
					}
				}
				
				if(i>=7&&j>=7){
					if(state.getBoard()[i][j]==1){
						area1[3]++;
					}
					if(state.getBoard()[i][j]==2){
						area2[3]++;
					}
				}
			}
		
		int min1 = 100;
		int minArea1 = 0;
		int min2 = 100;
		int minArea2 = 0;
		for(int i=0;i<4;i++){
			if(area1[i] < min1){
				min1 = area1[i];
				minArea1 = i;
			}
			if(area2[i] < min2){
				min2 = area2[i];
				minArea2 = i;
			}
				
				
			
		}
		return minArea2;
		
	}
	private boolean isNotBlocked(State s,int i,int j,int n){
		int []row = {0, 0,1,-1};
		int []col = {1,-1,0, 0};
		for(int k = 0; k < 4;k++)
		{
			int a = i + row[k];
			int b = j + col[k];
			if(a<0||b<0||a>=14||b>=14)
				continue;
			
			if(s.getBoard()[a][b]!=n){
				
				return true;
			}
				
		}
		return false;
	}
	private boolean isCritical(boolean [][]critical,int cx,int cy, int []x,int []y){

		for(int i=0;i<5;i++){
			if(critical[cx+x[i]][cy+y[i]])
				return true;
		
		}
		return false;
	}
	
	
	private boolean isInRange(int n,int cx,int cy, int []x,int []y){
		
		for(int i=0;i<5;i++){
			
			int a = cx+x[i];
			int b = cy+y[i];
			if(n==0){
				if(a<7&&b<7)
					return true;
			}
			
			if(n==1){
				if(a<7&&b>=7)
					return true;
			}
			
			if(n==2){
				if(a>=7&&b<7)
					return true;
			}
			if(n==3){
				if(a>=7&&b>=7)
					return true;
			}
		}
		
		return false;
	}
	
	
	private void DFS(int [][]board,int x,int y){
		
		int []row = {0, 0,1,-1};
		int []col = {1,-1,0, 0};
		V[x][y] = 1;
		for(int k = 0; k < 4;k++)
		{
			int a = x + row[k];
			int b = y + col[k];
			if(a<0||b<0||a>=14||b>=14)
				continue;
			
			if(V[a][b]==0 && board[a][b]==0)
				DFS(board,a,b);
		}
		
	}
	public Action bestMove(State s){
		
		Action best = new Action();//best Action
		
		Map map = new HashMap<Action, Integer>();
		ArrayList<Action> list = new ArrayList<Action>();
		map.clear();
		ArrayList<Action> actions = findActions(s,2);
		ArrayList<Action> betterActions = new ArrayList<Action>();
		boolean [][]critical = new boolean[14][14];
		
		
		for(int i=0;i<13;i++){
			for(int j=0;j<13;j++){
				if(s.getBoard()[i][j] == 1 && s.getBoard()[i+1][j+1] == 1){
					if(s.getBoard()[i+1][j] !=1 && s.getBoard()[i][j+1] !=1){
						
						if(isNotBlocked(s, i+1, j,1))
							critical[i][j+1] = true;
						if(isNotBlocked(s, i, j+1,1))
							critical[i+1][j] = true;
					}
					
				}
				if(s.getBoard()[i+1][j] == 1 && s.getBoard()[i][j+1] == 1){
					
					if(s.getBoard()[i][j] !=1 && s.getBoard()[i+1][j+1] !=1){
						
						if(isNotBlocked(s, i, j,1))
							critical[i+1][j+1] = true;
						if(isNotBlocked(s, i+1, j+1,1))
							critical[i][j] = true;
					}
				}
				
				
				
				
			}
			
		}
		int threshold = 0;
		if(s.getMoves()<=7){
			threshold = 15;
		}
		if(s.getMoves()>8){
			threshold = 8;
		}
		if(s.getMoves()>=12){
			threshold = 3;
		}
		for(int i=1;i<13;i++){
			for(int j=1;j<13;j++)
			{
				int c = 0;
				for(int m=0;m<14;m++)
					for(int n=0;n<14;n++)
						V[m][n] = 0;
				if(s.getBoard()[i][j] == 0 && critical[i][j]){
					DFS(s.getBoard(),i,j);
					for(int m=0;m<14;m++)
						for(int n=0;n<14;n++)
							if(V[m][n]==1)
								c++;
					/*if(c<=threshold){
						critical[i][j] = false;
						
					}*/
						
				}
					
				
			}
		}
				
		for(int i=0;i<14;i++){
			for(int j=0;j<14;j++){
				if(critical[j][i])
					System.out.print("1 ");
				else
					System.out.print("0 ");
				
			}
			System.out.println();
		}
		
		int []value = new int[10000];
		int v = 1000000;
		int t;
		int minArea = minArea(s);
		System.out.println("min = " + minArea);
		for(int i=0;i<actions.size();i++){
			Action a = actions.get(i);
			int []x = new int[5];
			int []y = new int[5];
			
			if(a.getX()==1 || a.getX()==4 || a.getX()==15)
				list.add(a);
			x = playerInfo[2].getBlocks()[a.getX()].allPatternsX[a.getY()];
			y = playerInfo[2].getBlocks()[a.getX()].allPatternsY[a.getY()];
			
			
			
			if(s.getMoves()<=2){
				if(isCritical(critical, a.getA(), a.getB(), x, y)){
					//if(isSafe(s.getBoard(), a.getA(), a.getB(), x, y, 2))
						betterActions.add(a);
					
				}
				if(isKiss(s.getBoard(), a.getA(), a.getB(), x, y, 2)&&isCritical(critical, a.getA(), a.getB(), x, y))
    			{
					
					betterActions.add(a);
					//betterActions.add(a);
    				
    			}
				
			}
			else{
				/*if(isKiss(s.getBoard(), a.getA(), a.getB(), x, y, 2)&&isCritical(critical, a.getA(), a.getB(), x, y))
    			{
					betterActions.add(a);
    				
    			}*/
				if(minArea==0){
					if(isInRange(0,a.getA(), a.getB(), x, y)){
						if(isCritical(critical, a.getA(), a.getB(), x, y)){
							if(playerInfo[2].getBlocks()[a.getX()].getSquares()>=4)
								betterActions.add(a);
							betterActions.add(a);
						}
					}
						
				}
				
				if(minArea==1){
					if(isInRange(1,a.getA(), a.getB(), x, y)){
						if(isCritical(critical, a.getA(), a.getB(), x, y)){
							if(playerInfo[2].getBlocks()[a.getX()].getSquares()>=4)
								betterActions.add(a);
							betterActions.add(a);
						}
					}
						
				}
				
				if(minArea==2){
					if(isInRange(2,a.getA(), a.getB(), x, y)){
						if(isCritical(critical, a.getA(), a.getB(), x, y)){
							if(playerInfo[2].getBlocks()[a.getX()].getSquares()>=4)
								betterActions.add(a);
							betterActions.add(a);
						}
					}
						
				}
				
				if(minArea==3){
					if(isInRange(3,a.getA(), a.getB(), x, y)){
						if(isCritical(critical, a.getA(), a.getB(), x, y)){
							if(playerInfo[2].getBlocks()[a.getX()].getSquares()>=4)
								betterActions.add(a);
							betterActions.add(a);
						}
					}
						
				}
				
				
			}
			
				
			
		}
		
		if(s.getMoves()<=0){
			Random rr1 = new Random();
	    	int r1 = Math.abs(rr1.nextInt())%(list.size());//player1 goes
	    	
    		best = list.get(r1);
    		return best;
    	}
		System.out.println("size = " + betterActions.size());
    	if(betterActions.size()<=0)
    	{
    		for(int i=0;i<actions.size();i++){
    			Action a = actions.get(i);
    			int []x = new int[5];
    			int []y = new int[5];
    			
    			if(a.getX()==1 || a.getX()==4 || a.getX()==15)
    				list.add(a);
    			x = playerInfo[2].getBlocks()[a.getX()].allPatternsX[a.getY()];
    			y = playerInfo[2].getBlocks()[a.getX()].allPatternsY[a.getY()];
    			
    			
    			
    			
    			if(isKiss(s.getBoard(), a.getA(), a.getB(), x, y, 2)&&isCritical(critical, a.getA(), a.getB(), x, y))
        		{
    				betterActions.add(a);
        				
        		}
    			
    			
    		}
    		if(betterActions.size()<=0)
    			betterActions = actions;
    	}
		
		
		time = System.currentTimeMillis();
		for(int k=1;k < 20;k++){
			DEPTH = k;
			
			for(int i=0;i<betterActions.size();i++){
				Action a = betterActions.get(i);
				
				t = alphaBeta(result(s,a,2),1,1,-1000000,1000000);
				value[i] = t;
				if(t < v){
					v = t;
					best = a;
					map.put(best, v);
				}
				
				
				
			}
			if(System.currentTimeMillis()-time >= 8000){
				return best;
				
			}
			
		}
		
		if(best.getX()==21){
			int []x = new int[5];
			int []y = new int[5];
			x = playerInfo[2].getBlocks()[21].allPatternsX[0];
			y = playerInfo[2].getBlocks()[21].allPatternsY[0];
			for(int i=0;i<14;i++)
				for(int j=0;j<14;j++){
					if(isCritical(critical, i, j, x, y)){
						if(isValid(s.getBoard(), 21, 0, x, y, 2)){
							best.setX(21);
							best.setY(0);
							best.setA(i);
							best.setB(j);
						}
						
						
					}
				}
		}
		return best;
		
	}
	public int alphaBeta(State s,int player,int depth,int alpha,int beta){
		if(isTerminal(depth, s,player)){
			return getScore(s);
		}
		if(player==1){
			return maxValue(s,depth,alpha,beta);
		}
		else
			return minValue(s,depth,alpha,beta);
	}
	
	public int maxValue(State s,int depth,int alpha,int beta){
		int v = -1000000;
		ArrayList<Action> actions = findActions(s,2);
		
		
		if(System.currentTimeMillis()-time>=3000)
			return v;
		for(int i=0;i<actions.size();i++){
			Action a = actions.get(i);
			
			
			v = Math.max(v,alphaBeta(result(s,a,1),2,depth+1,alpha,beta));
			
			if(v>=beta)
				return v;
			
			alpha  = Math.max(alpha,v);
			
		}
		return v;
		
	}
	
	public int minValue(State s,int depth,int alpha,int beta){
		int v = 1000000;
		ArrayList<Action> actions = findActions(s,2);
		
		
		if(System.currentTimeMillis()-time>=1000)
			return v;
		for(int i=0;i<actions.size();i++){
			Action a = actions.get(i);
			
			v = Math.min(v,alphaBeta(result(s,a,2),1,depth+1,alpha,beta));
			if(alpha>=v)
				return v;
			
			beta  = Math.min(beta,v);
			
		}
		return v;
	}
}