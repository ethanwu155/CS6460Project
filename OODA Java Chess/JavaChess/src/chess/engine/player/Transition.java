package chess.engine.player;

import chess.engine.board.Board;
import chess.engine.board.Movement;

public class Transition {
	
	private Board transitionBoard;
	private final Movement movement;
	private final MoveStatus moveStatus;
	
	public Transition(final Board transitionBoard, final Movement movement, final MoveStatus moveStatus) {
		this.transitionBoard = transitionBoard;
		this.movement = movement;
		this.moveStatus = moveStatus;
	}
	
	public MoveStatus getMoveStatus()  {
		return this.moveStatus; 
	}
	
	public Board getBoardTransition() {
		return this.transitionBoard;
	}
	
}
