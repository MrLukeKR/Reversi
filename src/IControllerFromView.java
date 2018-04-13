public interface IControllerFromView {
	public void setTurnGUI(int player);
	public Square getSquare(int i);
	public void playAI(int player);
	public void makeButton(Square tempWhiteSquare, int i,int playerBoard);
	
}
