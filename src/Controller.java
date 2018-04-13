import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

//MANAGING THE PROGRAM
public class Controller implements IControllerFromModel,IControllerFromView {

	IModel model;
	IView view;
	
	public void setViewAndModel(View v, Model m){
		model = m;
		view = v;
	}
	
	public void updateBoards(){
		view.updateBoards();
	}
	
	public Square getSquare(int location){
			return model.getGameSquare(location);
	}
	
	public void setWhite(int location){
		model.getGameSquare(location).setWhite();
	}
	
	public void setBlack(int location){
		model.getGameSquare(location).setBlack();
	}
	
	public void placePiece(int location,int player){
		if (player == 1){
			setWhite(location);
			System.out.println("  Placed WHITE at ["+location%8+","+location/8+"]");
		}else if (player ==2){
			setBlack(location);
			System.out.println("  Placed BLACK at ["+location%8+","+location/8+"]");
		}
		updateBoards();
	}
	
	public void startGame(){
		placePiece(27,1);
		placePiece(28,2);
		placePiece(36,1);
		placePiece(35,2);
		
		model.setPlayerTurn(1);
	}
	
	public void toggleTurn(){
		Boolean whitePlay=isPlayable(1);
		Boolean blackPlay=isPlayable(2);
		
		updateCounts();
		
		if (blackPlay==false && whitePlay==false){
			endGame();
		}
		if (model.getPlayerTurn()==1){
			if (blackPlay==true)
				model.setPlayerTurn(2);
			else
				System.out.println("No moves for black at this time, forfeiting turn...");
		}else{
			if (whitePlay==true)
				model.setPlayerTurn(1);	
			else
				System.out.println("No moves for white at this time, forfeiting turn...");
		}			
	}
	
	public synchronized void makeButton(Square squareButton,int id,int playerBoard){
		squareButton.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e){
				System.out.println("Pressed Reversi button ["+id/8+", "+id % 8+"]");
				if (model.getGameSquare(id).getStatus()!=0){
					System.out.println("Cannot place here!");
				}else{
					if (model.getPlayerTurn() == playerBoard){
					doSearches(id,model.getPlayerTurn(),false);
						if (model.getLegalMoves()>0){
							
							doSearches(id,model.getPlayerTurn(),true);
							toggleTurn();
						}else
							System.out.println("No valid moves here!");						
					}else
						System.out.println("Wait your turn!");
				}
			}});
	}
	
	public synchronized void updateCounts(){
		model.setEmptyCount(0);
		model.setBlackCount(0);
		model.setWhiteCount(0);
		int tempEmpty=0;
		int tempWhite=0;
		int tempBlack=0;
		
		for (int i=0;i<64;i++){
			if (model.getGameSquare(i).getStatus()==0){
				tempEmpty=model.getEmptyCount();
				model.setEmptyCount(tempEmpty+1);
			}else if(model.getGameSquare(i).getStatus()==1){
				tempWhite=model.getWhiteCount();
				model.setWhiteCount(tempWhite+1);
			}else if(model.getGameSquare(i).getStatus()==2){
				tempBlack=model.getBlackCount();
				model.setBlackCount(tempBlack+1);
			}
		}
		System.out.println("Coverage - EMPTY: "+model.getEmptyCount()+" WHITE: "+model.getWhiteCount()+ " BLACK: "+model.getBlackCount());
	}
	public boolean isPlayable(int player){
		int totalMoves =0;
		System.out.print("Determinining playability... ");
		for (int i =0;i<64;i++){
			if (model.getGameSquare(i).getStatus()==0){
				doSearches(i,player,false);
				if (model.getLegalMoves()!=0){
					totalMoves+=1;
				}
			}
		}
		if (totalMoves>0){
			if (player==1)
				System.out.println("Board is playable for white");
			else
				System.out.println("Board is playable for black");
			return true;
		}else{
			if (player==1)
				System.out.println("Board is unplayable for white");
			else
				System.out.println("Board is unplayable for black");
			return false;
		}
	}
	
	public void playAI(int player){
		if (model.getPlayerTurn()==player){
			model.setGreedyPosition(0);
			model.setGreedyTotal(0);
			for (int x=0;x<64;x++){
				if (model.getGameSquare(x).getStatus()==0){
					doSearches(x,player,false);
				}
			}
			if(model.getGreedyTotal()>0){
				if (model.getGameSquare(model.getGreedyPosition()).getStatus()==0){
					doSearches(model.getGreedyPosition(),player,true);
					System.out.println("AI Played: ["+model.getGreedyPosition()%8+", "+model.getGreedyPosition()/8+"] with "+model.getGreedyTotal()+" captures");
					toggleTurn();
				}else
					System.out.println("AI ATTEMPED ILLEGAL PLACEMENT");
			}else
				System.out.println("AI could not find a suitable position");
		}else
			System.out.println("[AI] Wait your turn!");
		
	}
	
	public void doSearches(int position,int playerColor, Boolean placePieces){
		model.setLegalMoves(0);
		model.setTotalCaptures(0);
		searchNorth(position,playerColor,placePieces);
		searchNorthEast(position,playerColor,placePieces);
		searchEast(position,playerColor,placePieces);
		searchSouthEast(position,playerColor,placePieces);
		searchSouth(position,playerColor,placePieces);
		searchSouthWest(position,playerColor,placePieces);
		searchWest(position,playerColor,placePieces);
		searchNorthWest(position,playerColor,placePieces);
		if (model.getTotalCaptures()>=model.getGreedyTotal()){
			model.setGreedyTotal(model.getTotalCaptures());
			model.setGreedyPosition(position);
		}
	}
	
	public void searchNorth(int position,int playerColor,Boolean placePieces){
		System.out.println("North:");
		int current = position-8;
		int captureCount=0;
		int currV = current/8;
		
		if (currV<model.getVerticalUpperLimit() && current>=model.getMinimumPosition()){
		int currentStatus = model.getGameSquare(current).getStatus();
		
		while ((currV<model.getVerticalUpperLimit() && current>=model.getMinimumPosition()) && currentStatus!=0 && currentStatus!=playerColor){
			current = current - 8;
			currV = current/8;
			captureCount+=1;
			
			if (currV<model.getVerticalUpperLimit() && current>=model.getMinimumPosition())
				currentStatus = model.getGameSquare(current).getStatus();
		}
		if (currentStatus!=playerColor)
			captureCount=0;
		
		if(captureCount>0 && placePieces){
			////System.out.println("Placed while in searchNorth:");
			for (int x = position;x>current;x-=8){
				placePiece(x,playerColor);
			}
		}
	}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
		
	}
	
	public void searchNorthEast(int position,int playerColor,Boolean placePieces){
		System.out.println("North East:");
		int current = position-7;
		int captureCount=0;
		int currH=current%8;
		int currV=current/8;
		
		if (currV<model.getVerticalUpperLimit() && currH>model.getHorizontalLowerLimit() && current>=model.getMinimumPosition()){
		int currentStatus = model.getGameSquare(current).getStatus();
	
		while ((currV<model.getVerticalUpperLimit() && currH>model.getHorizontalLowerLimit() && current>=model.getMinimumPosition()) && currentStatus!=0 && currentStatus!=playerColor){
			current = current - 7;
			currH=current%8;
			currV=current/8;
			captureCount+=1;
			
			if (currV<model.getVerticalUpperLimit() && currH>model.getHorizontalLowerLimit() && current>=model.getMinimumPosition())
				currentStatus = model.getGameSquare(current).getStatus();
			}
			if (currentStatus!=playerColor){
				captureCount=0;
			}else{
				if (placePieces && captureCount>0){
					//System.out.println("Placed while in searchNorthEast:");
					for (int x = position;x>current;x-=7){
						placePiece(x,playerColor);
					}
				}
			}
		}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
	}
	
	public void searchEast(int position,int playerColor,Boolean placePieces){
		System.out.println("East:");
		int current = position+1;
		int captureCount=0;
		int currH=current%8;
		
		if (current<=model.getMaximumPosition() && currH>model.getHorizontalLowerLimit()){
			int currentStatus = model.getGameSquare(current).getStatus();
			while ((current<=model.getMaximumPosition() && currH>model.getHorizontalLowerLimit()) && currentStatus!=0 && currentStatus!=playerColor){
				current = current +1;
				currH=current%8;
				captureCount+=1;	
				
				if (current<=model.getMaximumPosition() && currH>model.getHorizontalLowerLimit())
					currentStatus = model.getGameSquare(current).getStatus();
			}
			if (currentStatus!=playerColor){
				captureCount=0;
			}else{
				if (placePieces && captureCount>0){
					//System.out.println("Placed while in searchEast:");
					for (int x = position;x<current;x+=1){
						placePiece(x,playerColor);
					}
				}
			}		
		}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
	}
	
	public void searchSouthEast(int position,int playerColor,Boolean placePieces){
		System.out.println("South East:");
		int current = position+9;
		int captureCount=0;
		int currH=current%8;
		int currV=current/8;
		
		if (currH>model.getHorizontalLowerLimit() && currV>model.getVerticalLowerLimit() && current<=model.getMaximumPosition()){
			int currentStatus = model.getGameSquare(current).getStatus();
			while ((currH>model.getHorizontalLowerLimit() && currV>model.getVerticalLowerLimit() && current<=model.getMaximumPosition()) && currentStatus!=0 && currentStatus!=playerColor){
				current = current + 9;
				currH=current%8;
				currV=current/8;
				captureCount+=1;
				
				if (currH>model.getHorizontalLowerLimit() && currV>model.getVerticalLowerLimit() && current<=model.getMaximumPosition())
					currentStatus = model.getGameSquare(current).getStatus();
			}
			if (currentStatus!=playerColor){
				captureCount=0;
			}else{
				if (placePieces && captureCount>0){
					//System.out.println("Placed while in searchSouthEast:");
					for (int x = position;x<current;x+=9){
						placePiece(x,playerColor);
					}
				}
			}
		}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
	}
	
	public void searchSouth(int position,int playerColor,Boolean placePieces){
		System.out.println("South:");
		int current = position+8;
		int captureCount=0;
		int currV=current/8;

		if (current<= model.getMaximumPosition() && currV>model.getVerticalLowerLimit()){
		int currentStatus = model.getGameSquare(current).getStatus();
		while ((current<= model.getMaximumPosition() && currV>model.getVerticalLowerLimit()) && currentStatus!=0 && currentStatus!=playerColor){
			current = current +8;
			currV=current/8;
			captureCount+=1;
			
			if (current<= model.getMaximumPosition() && currV>model.getVerticalLowerLimit())
				currentStatus = model.getGameSquare(current).getStatus();
		}
		
		if (currentStatus!=playerColor){
			captureCount=0;
		}else{
			if (placePieces && captureCount>0){
				//System.out.println("Placed while in searchSouth:");
				for (int x = position;x<current;x+=8){
					placePiece(x,playerColor);
				}
			}
		}
		
		}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
	}
	
	public void searchSouthWest(int position,int playerColor,Boolean placePieces){
		System.out.println("South West:");
		int current = position+7;
		int captureCount=0;
		int currH=current%8;
		int currV=current/8;
		
		if (currH<model.getHorizontalUpperLimit() && currV>model.getVerticalLowerLimit() && current<=model.getMaximumPosition()){
		int currentStatus = model.getGameSquare(current).getStatus();
		
	
		while ((currH<model.getHorizontalUpperLimit()&&currV>model.getVerticalLowerLimit() && current<=model.getMaximumPosition()) && currentStatus!=0 && currentStatus!=playerColor){
			current = current + 7;
			currH=current%8;
			currV=current/8;
			captureCount+=1;
			
			if (currH<model.getHorizontalUpperLimit()&&currV>model.getVerticalLowerLimit() && current<=model.getMaximumPosition())
				currentStatus = model.getGameSquare(current).getStatus();
		}
		if (currentStatus!=playerColor){
			captureCount=0;
		}else{
			if (placePieces && captureCount>0){
				//System.out.println("Placed while in searchSouthWest:");
				for (int x = position;x<current;x+=7){
					placePiece(x,playerColor);
				}
			}
		}

}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
	}
	
	public void searchWest(int position,int playerColor,Boolean placePieces){
		System.out.println("West:");
		int current = position-1;
		int captureCount=0;
		int currH=current%8;
		
		if (currH<model.getHorizontalUpperLimit() && current>=model.getMinimumPosition()){
			int currentStatus = model.getGameSquare(current).getStatus();
			
		while ((currH<model.getHorizontalUpperLimit() && current>=model.getMinimumPosition()) && currentStatus!=0 && currentStatus!=playerColor){
			current = current -1;
			currH=current%8;
			
			if (currH<model.getHorizontalUpperLimit() && current>=model.getMinimumPosition())
			currentStatus = model.getGameSquare(current).getStatus();
			captureCount+=1;
		}
		if (currentStatus!=playerColor){
			captureCount=0;
		}else{
			if (placePieces && captureCount>0){
				//System.out.println("Placed while in searchWest:");
				for (int x = position;x>current;x-=1){
					placePiece(x,playerColor);
				}
			}
		}
		}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
	}
	
	public void searchNorthWest(int position,int playerColor,Boolean placePieces){
		System.out.println("North West:");
		int current = position - 9;
		int captureCount=0;
		int currH=current%8;
		int currV=current/8;
		
		if (currV<model.getVerticalUpperLimit() && currH<model.getHorizontalUpperLimit() && current>=model.getMinimumPosition()){
			int currentStatus = model.getGameSquare(current).getStatus();
	
			while ((currV<model.getVerticalUpperLimit() && currH<model.getHorizontalUpperLimit() && current>=model.getMinimumPosition()) && currentStatus!=0 && currentStatus!=playerColor){
				current = current - 9;
				captureCount+=1;
				currH=current%8;
				currV=current/8;
				
				if (currV<model.getVerticalUpperLimit() && currH<model.getHorizontalUpperLimit() && current>=model.getMinimumPosition())
					currentStatus = model.getGameSquare(current).getStatus();
			}
		
			if (currentStatus!=playerColor){
				captureCount=0;
			}else{
				if (placePieces && captureCount>0){
					for (int x = position;x>current;x-=9){
						placePiece(x,playerColor);
						
					}
				}
			}
	}
		if (captureCount>0){
			model.setLegalMoves(model.getLegalMoves()+1);
			model.setTotalCaptures(model.getTotalCaptures()+captureCount);
		}else
			System.out.println("  No Moves");
	}
	
	public void endGame(){
		int white,black;
		white = model.getWhiteCount();
		black = model.getBlackCount();
		System.out.println("Game Over");
		if (white>black){
			JOptionPane.showMessageDialog(null,"White wins! "+model.getWhiteCount()+":"+model.getBlackCount(),"Game Over",JOptionPane.INFORMATION_MESSAGE);
		}else if (white<black){
			JOptionPane.showMessageDialog(null,"Black wins! "+model.getBlackCount()+":"+model.getWhiteCount(),"Game Over",JOptionPane.INFORMATION_MESSAGE);
		}else if (white==black){
			JOptionPane.showMessageDialog(null,"It's a draw!","Game Over",JOptionPane.INFORMATION_MESSAGE);
		}	
		if (JOptionPane.showOptionDialog(null, "Would you like to play again?", "Rematch", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,null,null)==JOptionPane.YES_OPTION){
			restartGame();	
		}else
			System.exit(0);
	}

	public void setTurnGUI(int player) {
		view.setTurnGUI(player);
	}

	public void restartGame(){
		model.clearBoard();
		startGame();
	}

}
