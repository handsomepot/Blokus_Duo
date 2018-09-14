package blokus;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

class Buttons{
	private int x, y;
    private Image image;
    private int width,height;
	public Buttons(int x,int y,String name){
		ImageIcon ii = new ImageIcon(name);
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        this.x = x;
        this.y = y;
	}
	
	public Image getImage() {
        return image;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public int getWidth(){
    	return width;
    }
    public int getHeight(){
    	return height;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



}