package blokus;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.swing.Timer;

import java.util.*;
class Board extends JPanel implements MouseListener,MouseMotionListener,ActionListener{

	private static final long serialVersionUID = 1L;
	Layout layout = new Layout();
	Layout layout2 = new Layout();
	private final int boardMarginX = 50;
	private final int boardMarginY = 50;
	private final int boardSize = 30;
	private final int playerMarginX = 20;
	private final int playerMarginY = 550;
	private final int SIZE = 16;
	
	private int nowNumber = 0;
	private int mouseOverNumber = 0;
	private int curX,curY;
	private int playerScore = 0;
	private int computerScore = 0;
	
	private int computerX,computerY,patternNumber,blockNumber;
	private int playerPattern;
	private Color nowColor;
	private int [][]board = new int[14][14];/*shapes been placed on board*/
	private Buttons []button = new Buttons[4];
	Map <String, Integer > m = new HashMap<String,Integer>();
	
	private Player []playerInfo;
	int []coordX = new int[5];
	int []coordY = new int[5];
	int now = 0;
	int moves = 0;
	
	
	private Algo algo;
	private long startTime;
	private boolean computerTurn = false;
	private boolean computerNoMove = false;
	private boolean playerNoMove = false;
	Timer timer;
	
	private Color purple = new Color(150,30,200);
	private Color orange = new Color(225,128,0);
	
	private ArrayList<Action>actionList = new ArrayList<Action>();
	private ArrayList<Action>bestActions = new ArrayList<Action>();
	Action bestAction = new Action();
	
	private int []nowCoordX = new int[5];
	private int []nowCoordY = new int[5];
	private boolean isMirror = false;
	public Board(){
		addMouseListener(this);
		addMouseMotionListener(this);
		playerInfo = new Player[3];
		
		for(int i=0;i<3;i++)
			playerInfo[i] = new Player();
		
		setPlayerInfo();

		nowColor = purple.brighter();
		curX = curY = 0;
		m.put("W", 1); m.put("V5",2); m.put("Z5",3);  m.put("X", 4);  m.put("U", 5);  m.put("I5",6); m.put("P", 7); 
		m.put("T5",8); m.put("Y",9);  m.put("L5",10); m.put("N",11); m.put("Z4",12); m.put("I4",13);m.put("O",14);
		m.put("F",15);  m.put("T4",16); m.put("L4",17); m.put("I3",18); m.put("I2",19); m.put("V3",20);m.put("1",21);
		bestAction.setA(9);
		bestAction.setB(9);
		bestActions.add(bestAction);
		timer = new Timer(150, this);
		actionList.clear();
		Random r = new Random();
    	int rr = Math.abs(r.nextInt())%(2);
    	/*if(rr==0)
    		isMirror = true;
    	else
    		isMirror = false;*/
    	
    	boolean []playerUsed = new boolean[22];
		boolean []computerUsed = new boolean[22];
		for(int i=1;i<=21;i++){
			playerUsed[i] = false;
			computerUsed[i] = false;
			if(layout.computerBlocks[i].isUsed)
				computerUsed[i]  = true;	
			if(layout.playerBlocks[i].isUsed)
				playerUsed[i]  = true;	
		
		}
		/*
		State state = new State();
		state.setBoard(board);
		state.setPlayerUsed(playerUsed);
		state.setComputerUsed(computerUsed);
		state.setMoves(moves++);
		
		algo = new Algo(playerInfo,bestActions);//每次產生一個algo去計算
		state.setActionList(actionList);
		bestAction = algo.bestMove(state);
		actionList.add(bestAction);
		
		blockNumber = bestAction.getX();
		patternNumber = bestAction.getY();
		computerX = bestAction.getA();
		computerY = bestAction.getB();
		computerTurn = true;
		layout.computerBlocks[blockNumber].setUsed(true);
		start();*/
    	 
	}
	private void setPlayerInfo(){
		playerInfo[1].setBlocks(layout.playerBlocks);
		playerInfo[2].setBlocks(layout.computerBlocks);
		
		playerInfo[1].setPlayerNum(1);
		playerInfo[1].setStartX(4);
		playerInfo[1].setStartY(4);
		
		playerInfo[2].setPlayerNum(2);
		playerInfo[2].setStartX(9);
		playerInfo[2].setStartY(9);
		
	}
	private void drawBoardlines(Graphics g){
		g.setColor(Color.ORANGE.darker());
		int x = boardMarginX;
		int y = boardMarginY;
		int size = boardSize;
		for(int i = 0;i <= 14;i++){
			g.drawLine(x + i*size,y,x + i*size,y + 14*size);
			g.drawLine(x, y + i*size, x + 14*size, y + i*size);
		}
		for(int i = 0; i < 14;i++)
			for(int j = 0 ; j < 14;j++)
			{
				if(board[i][j] == 1){
					
					g.setColor(purple.brighter());
					g.drawRect(boardMarginX + i*boardSize, boardMarginY + j*boardSize, boardSize, boardSize);
				}	
				if(board[i][j] == 2){
					
					g.setColor(orange.brighter());
					g.drawRect(boardMarginX + i*boardSize, boardMarginY + j*boardSize, boardSize, boardSize);
					
				}
			}
	
	}
	//draw selected item on board and beside board
	public void drawSelect(Graphics g){
		g.setColor(purple);
		if(nowNumber!=0){
			for(int i=0;i<5;i++){
				g.setColor(purple);
				g.fillRect(coordX[i]*20+550, coordY[i]*20+400, 20, 20);
				g.setColor(purple.darker());
				g.drawRect(coordX[i]*20+550, coordY[i]*20+400, 20, 20);
			}
				
			g.setColor(nowColor);
			int nx = (curX - boardMarginX)/boardSize;
			int ny = (curY - boardMarginY)/boardSize;
			if(nx >= 0&& nx < 14 && ny >= 0 && ny < 14)
				for(int i=0;i<5;i++)
					g.fillRect((nx+coordX[i])*boardSize+boardMarginX, (ny+coordY[i])*boardSize+boardMarginY, boardSize,boardSize);
		}
			
	}
	
	public void drawIcons(Graphics g){

		button[0] = new Buttons(750,420,"L.png");
		button[1] = new Buttons(830,420,"R.png");
		button[2] = new Buttons(900,420,"UD.png");
		button[3] = new Buttons(960,420,"LR.png");
		
		if(curX >= 750 && curX < 750 + button[0].getWidth() && curY >= 420 && curY < 420 + button[0].getHeight())
			button[0] = new Buttons(750,420,"L2.png");
		else
			button[0] = new Buttons(750,420,"L.png");
		
		if(curX >= 830 && curX < 830 + button[1].getWidth() && curY >= 420 && curY < 420 + button[1].getHeight())
			button[1] = new Buttons(830,420,"R2.png");
		else
			button[1] = new Buttons(830,420,"R.png");
		
		if(curX >= 900 && curX < 900 + button[2].getWidth() && curY >= 420 && curY < 420 + button[2].getHeight())
			button[2] = new Buttons(900,420,"UD2.png");
		else
			button[2] = new Buttons(900,420,"UD.png");
		
		if(curX >= 960 && curX < 960 + button[3].getWidth() && curY >= 420 && curY < 420 + button[3].getHeight())
			button[3] = new Buttons(960,420,"LR2.png");
		else
			button[3] = new Buttons(960,420,"LR.png");
		
		g.drawImage(button[0].getImage(), button[0].getX(), button[0].getY(), this);	
		g.drawImage(button[1].getImage(), button[1].getX(), button[1].getY(), this);
		g.drawImage(button[2].getImage(), button[2].getX(), button[2].getY(), this);
		g.drawImage(button[3].getImage(), button[3].getX(), button[3].getY(), this);
		
	}
	
	private void drawPlaced(Graphics g){
		playerScore = computerScore = 0;
		for(int i = 0; i < 14;i++)
			for(int j = 0 ; j < 14;j++)
			{
				if(board[i][j] == 1){
					playerScore++;
					g.setColor(purple);
					g.fillRect(boardMarginX + i*boardSize, boardMarginY + j*boardSize, boardSize, boardSize);
				}	
				if(board[i][j] == 2){
					computerScore++;
					g.setColor(orange);
					g.fillRect(boardMarginX + i*boardSize, boardMarginY + j*boardSize, boardSize, boardSize);
					
				}
			}
	
	}
	public int getPatternNumber(){
		int []x = new int[5];
		int []y = new int[5];
		for(int i=0;i < layout.playerBlocks[nowNumber].getNumberOfPatterns();i++){
			x = layout.playerBlocks[nowNumber].allPatternsX[i];
			y = layout.playerBlocks[nowNumber].allPatternsY[i];
			for(int k=0;k<5;k++){
				for(int m=0;m<5;m++)
					if(x[k] == coordX[m] && y[k] == coordY[m]){
						x[k] = 100;
						y[k] = 100;
					}
						
			}
			int c = 0;
			for(int k=0;k<5;k++)
				if(x[k]==100 && y[k]==100)
					c++;
			if(c==5)
				return i;
		}
		return 0;
	}
	public void start(){
		timer.start();
		startTime = System.currentTimeMillis();
	}
	public void stop(){
		timer.stop();
	}
	public void move(int which){
		now = which;
		repaint();
	}
	public void test(Graphics g,int which){
		if(blockNumber == 0 && board[4][4]!=0){
			stop();
			return;
		}
			       		
		Color colors[]={Color.red,Color.cyan,Color.GREEN,Color.blue,Color.yellow,Color.pink};
		g.setColor(colors[which]);
		int []x = new int[5];
		int []y = new int[5];
		if(!isMirror){
			x = layout.computerBlocks[blockNumber].allPatternsX[patternNumber];
			y = layout.computerBlocks[blockNumber].allPatternsY[patternNumber];
			for(int i=0;i<5;i++){
				board[computerX + x[i]][computerY + y[i]] = 2;
				g.fillRect(boardMarginX+(computerX + x[i])*boardSize, boardMarginY+(computerY + y[i])*boardSize, boardSize,boardSize);
			}
		}
		else{
			
			for(int i=0;i<5;i++){
				x[i] = 13 - nowCoordX[i];
				y[i] = 13 - nowCoordY[i];
				board[x[i]][y[i]] = 2;
				g.fillRect(boardMarginX+(computerX + x[i])*boardSize, boardMarginY+(computerY + y[i])*boardSize, boardSize,boardSize);
			}
		}
		
		
		repaint();
	}
	
	private void drawStartingPoint(Graphics g){
		
		for(int i = 1;i<8;i++){
			g.setColor(purple.brighter());
			g.drawRect(boardMarginX + 4*boardSize+i,boardMarginY + 4*boardSize+i , boardSize-i*2,boardSize-i*2);
			g.setColor(Color.ORANGE);
			g.drawRect(boardMarginX + 9*boardSize+i,boardMarginY + 9*boardSize+i , boardSize-i*2,boardSize-i*2);
		}
		
	}
	public void paint(Graphics g){
		
		g.setColor(new Color(240,240,240));
		g.fillRect(0, 0, 1080, 800);
		drawStartingPoint(g);
		drawSelect(g);
		drawIcons(g);
		drawPlaced(g);
		
		String str = "";
		g.setColor(orange);
		for(int i=0;i<34;i++)
			for(int j=0;j<6;j++)
				if(!layout.computer[i][j].equals("0")){
					str = layout.computer[i][j];
					if(!layout.computerBlocks[m.get(str)].isUsed){
						g.setColor(orange);
						g.fillRect(500+i*SIZE, 50+j*SIZE, SIZE, SIZE);
						g.setColor(Color.RED.brighter());
						g.drawRect(500+i*SIZE, 50+j*SIZE, SIZE, SIZE);
					}
						
				}
		g.setColor(purple);
		for(int i=0;i<63;i++)
			for(int j=0;j<5;j++)
				if(!layout.player[i][j].equals("0")){
					str = layout.player[i][j];
					if(!layout.playerBlocks[m.get(str)].isUsed){
						if(mouseOverNumber == m.get(str))
							g.setColor(Color.YELLOW);
						else
							g.setColor(purple);
						g.fillRect(playerMarginX+i*SIZE, playerMarginY+j*SIZE, SIZE, SIZE);
						g.setColor(purple.darker());
						g.drawRect(playerMarginX+i*SIZE, playerMarginY+j*SIZE, SIZE, SIZE);
						
					}
						
				}
		drawBoardlines(g);
		
		Font small = new Font("Helvetica", Font.BOLD, 20);
		g.setFont(small);
		g.setColor(new Color(163,73,164));
		g.drawString(Integer.toString(playerScore), 900, 380);
		g.setColor(new Color(255,127,39));
		g.drawString(Integer.toString(computerScore), 20, 40);
		
		
		if(computerNoMove){
			g.setColor(new Color(255,127,39));
			g.drawString("No Move", 70, 40);
		}
			
		if(playerNoMove){
			g.setColor(new Color(163,73,164));
			g.drawString("No Move", 940, 380);
		}
			
		if(computerTurn){
			test(g,now);
		}
			
	}
	
	private void mirrorLR(){
		for(int i=0;i<5;i++)
			coordX[i] = -1*coordX[i];
	}
	
	private void mirrorUD(){
		for(int i=0;i<5;i++)
			coordY[i] = -1*coordY[i];
	}
	
	private void rotateR(){
		int []x = new int[5];
		int []y = new int[5];
		for(int i=0;i<5;i++){
			x[i]= coordX[i];
			y[i]= coordY[i];
			coordX[i] = -1*y[i];
			coordY[i] = x[i];
		}
	}
	
	private void rotateL(){
		int []x = new int[5];
		int []y = new int[5];
		for(int i=0;i<5;i++){
			x[i]= coordX[i];
			y[i]= coordY[i];
			coordX[i] = y[i];
			coordY[i] = -1*x[i];
		}
	}
	private boolean hasLegalMove(){
		int []x = new int[5]; int []y = new int[5];
		for(int i = 1; i <= 21 ;i++){
			if(!layout.playerBlocks[i].isUsed)
			{
				for(int j = 0; j < playerInfo[1].getBlocks()[i].getNumberOfPatterns();j++){
					x = playerInfo[1].getBlocks()[i].allPatternsX[j];
					y = playerInfo[1].getBlocks()[i].allPatternsY[j];
					for(int cx = 0; cx < 14;cx++)
						for(int cy = 0; cy < 14;cy++){
						if(isValid(cx, cy, x, y, 1)){
								
								return true;
							}
						}
				}
			}
		}
		return false;
	}
	
	public boolean isValid(int a,int b,int []coordX,int []coordY,int num){

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
				if(ta == sx && tb == sy)
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
	private boolean canMirror(){
		for(int i=0;i<5;i++){
			if(board[13-nowCoordX[i]][13-nowCoordY[i]]!=0)
				return false;
			
		}
		return true;
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		curX = e.getX();
		curY = e.getY();
		
		/*is inside the board*/
		int a = (curX - boardMarginX)/boardSize;
		int b = (curY - boardMarginY)/boardSize;
		if(!isValid(a, b,coordX,coordY, 1))
			nowColor = new Color(240,120,240).brighter();
		else
			nowColor = purple;
		
		/*mouse over player blocks*/
		int x = (e.getX() - playerMarginX)/SIZE;
		int y = (e.getY() - playerMarginY)/SIZE;
		mouseOverNumber = 0;
		String str = "";
		if(x >= 0&& x < 63 && y >= 0 && y < 5)
			if(!layout.player[x][y].equals("0")){
				str = layout.player[x][y];
				mouseOverNumber = m.get(str);
			}
	
				
		repaint();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(button[0].getBounds().contains(e.getX(),e.getY()))
			rotateL();
		if(button[1].getBounds().contains(e.getX(),e.getY()))
			rotateR();
		if(button[2].getBounds().contains(e.getX(),e.getY()))
			mirrorUD();
		if(button[3].getBounds().contains(e.getX(),e.getY()))
			mirrorLR();
		int x = (e.getX() - playerMarginX)/SIZE;
		int y = (e.getY() - playerMarginY)/SIZE;
		String str = "";
		if(x >= 0&& x < 63 && y >= 0 && y < 5)
		if(!layout.player[x][y].equals("0")){
			str = layout.player[x][y];
			if(!layout.playerBlocks[m.get(str)].isUsed)
				nowNumber = m.get(str);
			else
				nowNumber = 0;
			if(nowNumber!=0){
				for(int i=0;i<5;i++)
				{
					coordX[i] = layout.playerBlocks[nowNumber].getX()[i];
					coordY[i] = layout.playerBlocks[nowNumber].getY()[i];
				}
			}
				
		}
		int a = (curX - boardMarginX)/boardSize;
		int b = (curY - boardMarginY)/boardSize;

		if(isValid(a,b,coordX,coordY,1)&& nowNumber!=0){
			for(int i=0;i < 5;i++)
				if(a + coordX[i]>=0 && a+coordX[i]<14 && b+coordY[i]>=0 && b+coordY[i]<14){
					board[a + coordX[i]][b + coordY[i]] = 1;
					nowCoordX[i] = a + coordX[i];
					nowCoordY[i] = b + coordY[i];
				}
					
			layout.playerBlocks[nowNumber].setUsed(true);
			playerPattern = getPatternNumber();

			boolean []playerUsed = new boolean[22];
			boolean []computerUsed = new boolean[22];
			for(int i=1;i<=21;i++){
				playerUsed[i] = false;
				computerUsed[i] = false;
				if(layout.computerBlocks[i].isUsed)
					computerUsed[i]  = true;	
				if(layout.playerBlocks[i].isUsed)
					playerUsed[i]  = true;	
			
			}

			State state = new State();
			state.setBoard(board);
			state.setPlayerUsed(playerUsed);
			state.setComputerUsed(computerUsed);
			state.setMoves(moves++);
			int temp = nowNumber;
			nowNumber = 0;

		
			Action act = new Action();
			act.setX(temp); act.setY(playerPattern);
			act.setA(a); act.setB(b);
			actionList.add(act);
			System.out.println(playerPattern);
			
			if(moves == 1){
				algo = new Algo(playerInfo,bestActions);//每次產生一個algo去計算
				bestAction = algo.findInBooks(act);
				if(bestAction.getX()==0){
					
					state.setActionList(actionList);
						bestAction = algo.bestMove(state);
						
						
				}
				actionList.add(bestAction);
				blockNumber = bestAction.getX();
				patternNumber = bestAction.getY();
				computerX = bestAction.getA();
				computerY = bestAction.getB();
				computerTurn = true;
				layout.computerBlocks[blockNumber].setUsed(true);
				start();
			}
			else if(moves==2){
				algo = new Algo(playerInfo,bestActions);//每次產生一個algo去計算
				bestAction = algo.findInBooks2(actionList);
				if(bestAction.getX()==0){
					
					state.setActionList(actionList);
						bestAction = algo.bestMove(state);

				}
				actionList.add(bestAction);
				blockNumber = bestAction.getX();
				patternNumber = bestAction.getY();
				computerX = bestAction.getA();
				computerY = bestAction.getB();
				computerTurn = true;
				layout.computerBlocks[blockNumber].setUsed(true);
				start();
			}
			else{
				algo = new Algo(playerInfo,bestActions);//每次產生一個algo去計算
				bestAction = algo.bestMove(state);
				blockNumber = bestAction.getX();
				patternNumber = bestAction.getY();
				computerX = bestAction.getA();
				computerY = bestAction.getB();
				computerTurn = true;
				layout.computerBlocks[blockNumber].setUsed(true);
				start();
			}
				
				
				
			if(blockNumber == 0)
				computerNoMove = true;
				
			if(!hasLegalMove()){
				playerNoMove = true;
				computerTurn = true;
			}
			
			
			
		}
	
		repaint();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		now = (now+1)%4;
		move(now);
		
		if((System.currentTimeMillis()-startTime)>=1250){
			computerTurn = false;
			stop();
			if(!hasLegalMove()){
				playerNoMove = true;
				
				bestActions.add(bestAction);
				algo = new Algo(playerInfo,bestActions);//每次產生一個algo去計算
				boolean []playerUsed = new boolean[22];
				boolean []computerUsed = new boolean[22];
				for(int i=1;i<=21;i++){
					computerUsed[i] = playerUsed[i] = false;
					if(layout.computerBlocks[i].isUsed)
						computerUsed[i] = true;
					if(layout.playerBlocks[i].isUsed)
						playerUsed[i] = true;
					
				}
				State state = new State();
				state.setBoard(board);
				state.setPlayerUsed(playerUsed);
				state.setComputerUsed(computerUsed);
				state.setMoves(moves++);
				
				if(!hasLegalMove()){
					
					Action bestAction = algo.bestMove(state);
					blockNumber = bestAction.getX();
					patternNumber = bestAction.getY();
					computerX = bestAction.getA();
					computerY = bestAction.getB();
					computerTurn = true;
					layout.computerBlocks[blockNumber].setUsed(true);
					start();
					if(blockNumber==0)
						computerNoMove = true;
				}
				else
					return;
				computerTurn = true;
			}

		}
		
	}
	
}