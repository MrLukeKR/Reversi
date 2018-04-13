
public interface IControllerFromModel {
	void updateBoards();
	public void setTurnGUI(int player);
	public void endGame();
	public void doSearches(int position,int playerColor,Boolean placePieces);
	boolean isPlayable(int player);	
}
