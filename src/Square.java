
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JButton;


public class Square extends JButton implements Runnable{
	private static final long serialVersionUID = -1833041876825853140L;
private int status;
private static final int size = 50;
private final int id;

private static final int EMPTY = 0;
private static final int WHITE = 1;
private static final int BLACK = 2;

public int getID(){
	return id;
}

public Square(int id){
	this.id = id;
	setMinimumSize( new Dimension(size, size) );
	setPreferredSize( new Dimension(size, size) );
	setMaximumSize( new Dimension(size, size) );
}

public void setEmpty(){
	status = EMPTY;
}

public void setBlack(){
	status = BLACK;
}

public void setWhite(){
	status = WHITE;
}
public int getStatus(){
	return status;
}

public synchronized void setStatus(int status){
this.status=status;
}

public String toString(){
	if (status == WHITE)
		return "White";
	else if (status == BLACK)
		return "Black";
	else return "Empty";
}


/** Colour of main part */
protected Color drawColor; 
/** Width of border in pixels */
protected int borderSize; 

protected void paintComponent(Graphics g)
{
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (status==BLACK){
			g.setColor(Color.BLACK);
		}else if (status==WHITE){
			g.setColor(Color.WHITE);
		}
		
		if (status!=EMPTY){
			g.fillOval(0, 0, getWidth(), getHeight());
		}
}

public void redrawSelf() 
{
	EventQueue.invokeLater(this);
}

public void run() 
{ 
	repaint(); 
}

public void setColor(Color newColor) { drawColor = newColor; redrawSelf(); }

}