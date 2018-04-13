
public interface IModel {
	public Square getGameSquare(int id);
	public void setPlayerTurn(int player);
	public int getPlayerTurn();
	public int getWhiteCount();
	public int getBlackCount();
	public int getEmptyCount();
	public void clearBoard();
	public void setEmptyCount(int count);
	public void setWhiteCount(int count);
	public void setBlackCount(int count);
	public int getLegalMoves();
	public void setGreedyPosition(int i);
	public void setGreedyTotal(int i);
	public int getGreedyTotal();
	public int getGreedyPosition();
	public void setLegalMoves(int i);
	public void setTotalCaptures(int i);
	public int getTotalCaptures();
	public int getVerticalUpperLimit();
	public int getHorizontalUpperLimit();
	public int getMaximumPosition();
	public int getMinimumPosition();
	public int getHorizontalLowerLimit();
	public int getVerticalLowerLimit();
}
