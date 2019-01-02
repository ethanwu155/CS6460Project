import GUI.Table;
import chess.engine.board.Board;

public class JChess {
	
	public static void main(String[] args) {
		
		Board board = Board.createStartingBoard();
		
		System.out.println(board);
		
		Table table = new Table();
	}
}
