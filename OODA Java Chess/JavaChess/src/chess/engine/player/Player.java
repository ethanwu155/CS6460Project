package chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import chess.engine.Colors.Colors;
import chess.engine.board.Board;
import chess.engine.board.Movement;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList;

public abstract class Player {

	protected final Board board;
	
	//keep track of special piece: King
	protected final King playerKing;
	
	protected final Collection<Movement> legalMoves;
	
	private final boolean isCheck;
	
	//constructor
	Player(final Board board, final Collection<Movement> legalMoves, final Collection<Movement> opponentMoves) {
		this.board = board;
		this.playerKing = checkKing();
		
		//need to know opponent moves in order to castle movement so need to combine legal moves with king castle movement
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastle(legalMoves,opponentMoves)));
		this.isCheck = !Player.calculateAttacksOnTile(this.playerKing.getPosition(), opponentMoves).isEmpty();
	}
	
	//checks to see if any of the enemies moves overlaps with the kings position. if it does it is check
	protected static Collection<Movement> calculateAttacksOnTile(int position, Collection<Movement> movements) {
		// TODO Auto-generated method stub
		final List<Movement> attackMoves = new ArrayList<>();
		
		for (final Movement movement: movements) {
			if (position == movement.getDestination())
				attackMoves.add(movement);
		}
		return Collections.unmodifiableList(attackMoves);
	}
	
	//getter method for playerKing
	public King getPlayerKing() {
		return this.playerKing;
	}
	
	//getter method for legalMoves
	public Collection<Movement> getLegalMoves() {
		return this.legalMoves;
	}

	//check to see if there is a king on the board
	//goes through all the players active pieces and see if the piece is the king, if we go through look and king cannot be found than throw error
	private King checkKing() {
		for (final Piece piece : getActivePiece()) {
			if (piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("No King! Not valid board.");
	}
	
	public boolean isMovementLegal(final Movement movement) {
		return this.legalMoves.contains(movement);
	}
	
	//check to see if check conditions
	public boolean isCheck() {
		return this.isCheck;
	}
	
	//check to see if the game is over with check mate
	public boolean isCheckMate() {
		return this.isCheck && !hasEscapeMoves();
	}
	
	//check to see if the game is even
	public boolean stalemate() {
		return !this.isCheck && !hasEscapeMoves();
	}
	
	//check to see if castled
	public boolean castle() {
		return false;
	}
	
	//important method (centerpiece of playing chess)
	public Transition makeMove(final Movement movement) {
		//if there is illegal move return current board
		if (!isMovementLegal(movement)) {
			return new Transition(this.board, movement, MoveStatus.ILLEGAL_MOVEMENT);
		}
		
		//legal moves execute move and return new board with movement
		final Board transitionBoard = movement.execute();
		
		//if there are any moves that put the king in check than move cannot be made than return current board
		final Collection<Movement> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPosition(), transitionBoard.currentPlayer().getLegalMoves());
		
		if (!kingAttacks.isEmpty()) {
			return new Transition(this.board, movement, MoveStatus.LEAVES_PLAYER_CHECK);
		}
		
		return new Transition(transitionBoard, movement, MoveStatus.DONE);
	}
	
	public abstract Collection<Piece> getActivePiece();
	public abstract Colors getColor();
	
	public abstract Player getOpponent();
	
	protected boolean hasEscapeMoves() {
		for (final Movement movement : this.legalMoves) {
			final Transition transition = makeMove(movement);
			if (transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}
	
	protected abstract Collection<Movement> calculateKingCastle(Collection<Movement>playerLegals, Collection<Movement> opponenetsLegalMoves);
}
