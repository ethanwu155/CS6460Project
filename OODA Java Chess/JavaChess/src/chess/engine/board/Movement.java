package chess.engine.board;

import chess.engine.board.Board.Builder;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

public abstract class Movement {
	
	final Board board;
	final Piece movePiece;
	final int destination;
	
	public static final Movement NULL_MOVE = new invalidMovement();
	
	private Movement(final Board board, final Piece movePiece, final int destination) {
		this.board = board;
		this.movePiece = movePiece;
		this.destination = destination;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.destination;
		result = prime * result + this.movePiece.hashCode();
		return result;
	}
	
	@Override
	public boolean equals (final Object other) {
		if(this == other) {
			return true;
		}
		if (!(other instanceof Movement)) {
			return false;
		}
		final Movement otherMove = (Movement) other;
		return getDestination() == otherMove.getDestination() && getMovePiece().equals(otherMove.getMovePiece()); 
	}
	
	public int getCurrentCoordiante() {
		return this.getMovePiece().getPosition();
	}
	
	public Piece getMovePiece() {
		return this.movePiece;
	}
		
	public boolean isAttack() {
		return false;
	}
	
	public boolean isCastle() {
		return false;
	}
	
	public Piece getAttackPiece() {
		return null;
	}
	
	public Board execute() {
		// TODO Auto-generated method stub
		final Builder builder = new Builder();
		for (final Piece piece : this.board.currentPlayer().getActivePiece()) {
			if(!this.movePiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		
		for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePiece()) {
			builder.setPiece(piece);
		}
		builder.setPiece(this.movePiece.movePiece(this));
		builder.setMove(this.board.currentPlayer().getOpponent().getColor());
		return builder.build();
	}
	
	//subclass for a standard chess move (moving piece to empty tile)
	public static final class StandardMovement extends Movement {
		
		public StandardMovement(final Board board, final Piece movePiece, final int destination) {
			super(board, movePiece, destination);
		}
	}
	
	//subclass for attacking chess move (moving piece onto occupied tile of opposite color)
	public static class AttackMovement extends Movement {
		
		//piece of opposite color being attacked
		final Piece pieceAttacked;
		
		public AttackMovement(final Board board, final Piece movePiece, final int destination, final Piece pieceAttacked) {
			super(board, movePiece, destination);
			this.pieceAttacked = pieceAttacked;
		}

		@Override
		public int hashCode() {
			return this.pieceAttacked.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean equals(final Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof AttackMovement)) {
				return false;
			}
			final AttackMovement otherAttackMovement = (AttackMovement) other;
			return super.equals(otherAttackMovement) && getAttackPiece().equals(otherAttackMovement.getAttackPiece());
		}
		
		@Override
		public Board execute() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public boolean isAttack() {
			return true;
		}
		
		@Override
		public Piece getAttackPiece() {
			return this.pieceAttacked;
		}
	}
	
	public int getDestination() {
		return this.destination;
	}
	
	//standard Pawn Movement
	public static final class PawnMovement extends Movement {
		
		public PawnMovement(final Board board, final Piece movePiece, final int destination) {
			super(board, movePiece, destination);
		}
	}
	
	//Pawn Attack implementation
	public static final class PawnAttack extends AttackMovement {
		
		public PawnAttack(final Board board, final Piece movePiece, final int destination, final Piece pieceAttacked) {
			super(board, movePiece, destination, pieceAttacked);
		}
	}
	
	//Pawn Jump Movement implementation
	public static final class PawnJump extends Movement {
		
		public PawnJump(final Board board, final Piece movePiece, final int destination) {
			super(board, movePiece, destination);
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePiece()) {
				if(!this.movePiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePiece()) {
				builder.setPiece(piece);
			}
			final Pawn movedPawn = (Pawn)this.movePiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setMove(this.board.currentPlayer().getOpponent().getColor());
			return builder.build();
		}
	}
	
	//castle
	static abstract class CastleMovement extends Movement {
		
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;
		
		public CastleMovement(final Board board, final Piece movePiece, final int destination, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movePiece, destination);
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
		}
		
		public Rook getCastleRook() {
			return this.castleRook;
		}
		
		@Override
		public boolean isCastle() {
			return true;
		}
		
		@Override
		public Board execute() {
			
			final Builder builder = new Builder();
			for (final Piece piece : this.board.currentPlayer().getActivePiece()) {
				if(!this.movePiece.equals(piece) && !this.castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePiece()) {
				builder.setPiece(piece);
			}
			//set king piece
			builder.setPiece(this.movePiece.movePiece(this));
			//set rook piece
			builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceColor()));
			builder.setMove(this.board.currentPlayer().getOpponent().getColor());
			return builder.build();
		}
	}
	
	public static final class KingCastleMovement extends CastleMovement {
		public KingCastleMovement(final Board board, final Piece movePiece, final int destination, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movePiece, destination, castleRook, castleRookStart, castleRookDestination);
		}
		
		@Override
		public String toString() {
			return "0-0";
		}
	}
	
	public static final class RookCastleMovement extends CastleMovement {
		public RookCastleMovement(final Board board, final Piece movePiece, final int destination, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
			super(board, movePiece, destination, castleRook, castleRookStart, castleRookDestination);
		}
		
		@Override
		public String toString() {
			return "0-0-0";
		}
	}
	
	public static final class invalidMovement extends Movement {
		public invalidMovement() {
			super(null, null, -1);
		}
		
		@Override
		public Board execute() {
			throw new RuntimeException("Cannot execute null move!");
		}
	}
	
	public static class MoveFactory {
		
		private MoveFactory() {
			throw new RuntimeException("no instantiate");
		}
		
		public static Movement createMovement(final Board board, final int currentCoordinate, final int destination) {
			
			for (final Movement movement : board.getLegalMoves()) {
				if (movement.getCurrentCoordiante() == currentCoordinate && movement.getDestination() == destination) {
					return movement;
				}
			}
			return NULL_MOVE;
		}
	}
	


}
