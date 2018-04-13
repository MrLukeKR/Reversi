import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//MAINTAINS THE DISPLAY
public class View implements IView {
IControllerFromView c;
JLabel whiteStatusLabel = new JLabel();
JLabel blackStatusLabel = new JLabel();

Square[] localWhite = new Square[64];
Square[] localBlack = new Square[64];

JPanel whiteBoard = new JPanel();
JPanel blackBoard = new JPanel();

JFrame whiteGUI = new JFrame();
JFrame blackGUI = new JFrame();

	public View( IControllerFromView c ){
		this.c = c;
	}
	
	public void create(){
		//CREATE GUI
		System.out.println("Creating view...");
		
		whiteBoard.setLayout(new GridLayout(8,8));
		blackBoard.setLayout(new GridLayout(8,8));
		
		initBoards();
		
		blackGUI.setTitle("REVERSI - Black Player");
		blackGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		blackGUI.setLayout( new BorderLayout() );
		
		whiteGUI.setTitle("REVERSI - White Player");
		whiteGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		whiteGUI.setLayout( new BorderLayout() );
		
		JButton whiteAIButton = new JButton();
		JButton blackAIButton = new JButton();

		whiteAIButton.setText("Play AI (white)");
		whiteStatusLabel.setText("White Player");
		blackAIButton.setText("Play AI (black)");
		blackStatusLabel.setText("Black Player");
		
		
		whiteAIButton.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e){
				c.playAI(1);
			}
		});
		
		blackAIButton.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e){
				c.playAI(2);
			}
		});
		
		blackGUI.add(blackStatusLabel,BorderLayout.NORTH);
		blackGUI.add(blackAIButton, BorderLayout.SOUTH);
		blackGUI.add(blackBoard);
		
		whiteGUI.add(whiteStatusLabel,BorderLayout.NORTH);
		whiteGUI.add(whiteAIButton, BorderLayout.SOUTH);
		whiteGUI.add(whiteBoard);
	
		blackGUI.pack();
		blackGUI.setVisible(true);
		
		whiteGUI.pack();
		whiteGUI.setVisible(true);
	}

	public void initBoards(){
		for (int i=0;i<64;i++){
			localWhite[i] = new Square(i);
			localBlack[i] = new Square(i);
			
			c.makeButton(localWhite[i],i,1);
			c.makeButton(localBlack[i],63-i,2);	
			
			whiteBoard.add(localWhite[i]);
			blackBoard.add(localBlack[i]);
		}
	}
	
	public synchronized void updateBoards(){
			for (int i=0;i<64;i++){	
				localWhite[i].setStatus(c.getSquare(i).getStatus());
				localBlack[i].setStatus(c.getSquare(63-i).getStatus());	
				localWhite[i].redrawSelf();
				localBlack[i].redrawSelf();
		}
		
	}
	public void setTurnGUI(int player){
		if (player==1){
			whiteStatusLabel.setText("White Player - Your Turn");
			blackStatusLabel.setText("Black Player - Not your Turn");
		}else if (player==2){
			blackStatusLabel.setText("Black Player - Your Turn");
			whiteStatusLabel.setText("White Player - Not your Turn");
		}
	}
}