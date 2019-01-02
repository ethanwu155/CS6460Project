package chess.engine.pieces;
import chess.engine.board.Movement;
import chess.engine.board.Board;

import java.util.Collection;
import java.util.List;

import chess.engine.Colors.Colors;

//every piece has a tile coordinate
public abstract class Piece {
	
	//fields
	protected final int piecePosition;
	protected final Colors pieceColor;
	protected boolean firstMove;
	protected final PieceType pieceType;
	private final int cashedHashCode;
	
	//constructor
	Piece(final int piecePosition, final Colors pieceColor, final PieceType pieceType) {
		this.piecePosition = piecePosition;
		this.pieceColor = pieceColor;
		this.firstMove = false;
		this.pieceType = pieceType;
		this.firstMove = false;
		this.cashedHashCode = computeHashCode();
	}
	
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceColor.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (firstMove ? 1: 0 );
		return result;
	}

	public int getPosition() {
		return this.piecePosition;
	}
	
	//method that returns the pieceType
	public PieceType getPieceType() {
		return this.pieceType;
	}
	
	
	//method for calculating legal moves for a chess piece
	//all pieces created for chess game will override this method
	public abstract Collection<Movement> calculateMoves(final Board board);
	
	//getter method for piece color
	public Colors getPieceColor() {
		return this.pieceColor;
	}
	
	public boolean firstMove() {
		return this.firstMove;
	}
	
	public abstract Piece movePiece(Movement movement);
	
	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Piece)) {
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPosition() && pieceType == otherPiece.getPieceType() && pieceColor == otherPiece.getPieceColor() && firstMove == otherPiece.firstMove();
	}
	
	@Override
	public int hashCode() {
		return this.cashedHashCode;
	}
	
	public enum PieceType {
		
		PAWN("P") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KNIGHT("N") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return true;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isRook() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		private String pieceName;
		
		PieceType(final String pieceName) {
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString() {
			return this.pieceName;
		}
		
		public abstract boolean isKing();
		
		public abstract boolean isRook();
	}

}
