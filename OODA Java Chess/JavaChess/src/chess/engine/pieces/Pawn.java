package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import chess.engine.Colors.Colors;
import chess.engine.board.Board;
import chess.engine.board.Movement;
import chess.engine.board.Utility;
import chess.engine.pieces.Piece.PieceType;

public class Pawn extends Piece {
	
	private final static int[] MOVE_COORDINATES = {7, 8, 9, 16};

	public Pawn(final int piecePosition, final Colors pieceColor) {
		super(piecePosition, pieceColor, PieceType.PAWN);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Movement> calculateMoves(Board board) {
		// TODO Auto-generated method stub		
		int pieceCoordinate;
		
		final List<Movement>legalMoves = new ArrayList<>();
		
		for (final int currentOffset : MOVE_COORDINATES) {
			
			//currentOffset must account for directionality (white pieces moves in the upward direction, black pieces move in the downward direction)
			pieceCoordinate = this.piecePosition + (this.getPieceColor().getDirection() * currentOffset);
			
			//if its not a valid move (tile is occupied) skip
			if (!Utility.isValidCoordinate(pieceCoordinate)) {
				continue;
			}
			
			//condition describing pawn moving one space forward and tile is not occupied
			if (currentOffset == 8 && !board.getTile(pieceCoordinate).isTileOccupied()) {
				//standard one space movement and pawn promotion
				legalMoves.add(new Movement.StandardMovement(board, this, pieceCoordinate));
			}
			
			//pawn jump move (2 spaces) first move for the pawn
			else if (currentOffset == 16 && this.firstMove() && (Utility.SECOND_ROW[this.piecePosition] && this.getPieceColor().isBlack() || Utility.SEVEN_ROW[this.piecePosition] && this.getPieceColor().isWhite())) {
				
				//location of piece behind the pawn jump
				final int behindCoordinate = this.piecePosition + (this.pieceColor.getDirection() *8);
				
				//pawns cannot jump over pieces so check to see if there is a piece in the path of movement and also check to see if there is a piece at the end of pawn jump. If not than apply pawn jump
				if (!board.getTile(behindCoordinate).isTileOccupied() && !board.getTile(pieceCoordinate).isTileOccupied()) {
					legalMoves.add(new Movement.StandardMovement(board, this, pieceCoordinate));
				}
			}
			//attacking movement and check edge cases
			else if (currentOffset == 7 && !(Utility.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isWhite() || Utility.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack())) {
				
				if (board.getTile(pieceCoordinate).isTileOccupied()) {
					final Piece attackPiece = board.getTile(pieceCoordinate).getPiece();
					
					if(this.pieceColor != attackPiece.getPieceColor()) {
						legalMoves.add(new Movement.StandardMovement(board, this, pieceCoordinate));
					}
				}
			}
			//attacking movement and check edge cases
			else if (currentOffset == 9 && !(Utility.FIRST_COLUMN[this.piecePosition] && this.pieceColor.isBlack() || Utility.EIGHT_COLUMN[this.piecePosition] && this.pieceColor.isWhite())) {
				if (board.getTile(pieceCoordinate).isTileOccupied()) {
					final Piece attackPiece = board.getTile(pieceCoordinate).getPiece();
					
					if(this.pieceColor != attackPiece.getPieceColor()) {
						legalMoves.add(new Movement.StandardMovement(board, this, pieceCoordinate));
					}
				}
			}
			
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}
	
	@Override
	public Piece movePiece(final Movement movement) {
		// TODO Auto-generated method stub
		return new Pawn(movement.getDestination(), movement.getMovePiece().getPieceColor());
	}

}
