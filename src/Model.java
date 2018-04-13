//MAINTAINS THE DATA
public class Model implements IModel{

	IControllerFromModel c;
	int playerTurn;
	Square[] GameBoard = new Square[64];
	
	private int whiteCount;
	private int blackCount;
	private int emptyCount;
	private int legalMoves;
	private int totalCaptures;
	private int greedyTotal;
	private int greedyPosition;
	private int vulimit=7;
	private int vllimit=0;
	private int hulimit=7;
	private int hllimit=0;
	private int maxPos=63;
	private int minPos=0;
	
	public Model( IControllerFromModel c ){
		this.c = c;
	}
	
	public int getPlayerTurn(){
		return playerTurn;
	}
	
	public synchronized void setPlayerTurn(int player){
		c.setTurnGUI(player);
		playerTurn = player;
	}
	
	public Square getGameSquare(int id){
		return GameBoard[id];
	}
	
	public int getWhiteCount(){
		return whiteCount;
	}
	
	public synchronized void setWhiteCount(int count){
		whiteCount =count;
	}
	
	public int getBlackCount(){
		return blackCount;
	}
	
	public synchronized void setBlackCount(int count){
		blackCount =count;
	}
	
	public int getEmptyCount(){
		return emptyCount;
	}
	
	public synchronized void setEmptyCount(int count){
		emptyCount =count;
	}
	
	
	public void clearBoard(){
		for (int x=0;x<64;x++)
			GameBoard[x].setEmpty();
	}
	
	public void create(){
		//CREATE DATA STORAGE
		System.out.println("Creating model...");
		
		for (int i = 0;i<64;i++){
			GameBoard[i] = new Square(i);
		}
	}
	public int getLegalMoves(){
		return legalMoves;
	}
	public synchronized void setLegalMoves(int legalMoves){
		this.legalMoves=legalMoves;
	}
	
	public int getTotalCaptures(){
		return totalCaptures;
	}
	public synchronized void setTotalCaptures(int totalCaptures){
		this.totalCaptures=totalCaptures;
	}
	
	public int getGreedyTotal(){
		return greedyTotal;
	}
	public synchronized void setGreedyTotal(int greedyTotal){
		this.greedyTotal=greedyTotal;
	}
	
	public int getGreedyPosition(){
		return greedyPosition;
	}
	public synchronized void setGreedyPosition(int greedyPosition){
		this.greedyPosition=greedyPosition;
	}

	public int getVerticalUpperLimit(){
		return vulimit;
	}
	public int getVerticalLowerLimit(){
		return vllimit;
	}
	public int getHorizontalUpperLimit(){
		return hulimit;
	}
	public int getHorizontalLowerLimit(){
		return hllimit;
	}
	
	public int getMaximumPosition(){
		return maxPos;
	}
	public int getMinimumPosition(){
		return minPos;
	}
}