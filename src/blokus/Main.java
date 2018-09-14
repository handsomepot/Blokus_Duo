package blokus;

import javax.swing.JFrame;


class Main extends JFrame{

	private static final long serialVersionUID = 1L;

	public Main(){
		Board board = new Board();
        add(board);

        setSize(1080, 700);
        setTitle("Blokus");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String args[]){
		Main m = new Main();
		m.setLocationRelativeTo(null);
        m.setVisible(true);

	}
}