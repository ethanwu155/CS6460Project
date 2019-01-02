package chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import chess.engine.Colors.Colors;
import chess.engine.board.Board;
import chess.engine.board.Movement;
import chess.engine.board.Tile;
import chess.engine.board.Utility;
import chess.engine.pieces.Piece.PieceType;

public class Knight extends Piece {

	//fields
	//list of legal movement spots for Knight
	private static final int[] MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};
	
	public Knight(final int piecePosition, final Colors pieceColor) {
		super(piecePosition, pieceColor, PieceType.KNIGHT);
	}

	@Override
	public Collection<Movement> calculateMoves(Board board) {
		
		int pieceCoordinate;
		
		final List<Movement>legalMoves = new ArrayList<>();
		
		//loop through legal move coordinates
		for (final int currentOffset : MOVE_COORDINATES) {
			pieceCoordinate = this.piecePosition + currentOffset;
			
			//if move is valid, piece will move to that location under the condition....
			if(Utility.isValidCoordinate(pieceCoordinate)) {
				
				
				if (firstColumnExclusion(this.piecePosition, currentOffset) || secondColumnExclusion(this.piecePosition, currentOffset) || sevenColumnExclusion(this.piecePosition, currentOffset) || eightColumnExclusion(this.piecePosition, currentOffset)) {
					continue;
				}
				
				final Tile destinationTile = board.getTile(pieceCoordinate);
				//...if the tile is not occupied piece will move to empty tile
				if(!destinationTile.isTileOccupied()) {
					//normal movement
					legalMoves.add(new Movement.StandardMovement(board, this, pieceCoordinate));
				}
				
				//...otherwise we check what piece is on that tile
				//if the piece is the opposite color than we eat the piece
				else {
					final Piece pieceDestination = destinationTile.getPiece();
					final Colors pieceColor = pieceDestination.getPieceColor();
					
					if (this.pieceColor != pieceColor) {
						//attacking movement
						legalMoves.add(new Movement.AttackMovement(board, this, pieceCoordinate, pieceDestination));
					}
				}			
			}	
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}
	
	//check edge cases
	private static boolean firstColumnExclusion(final int currentPosition, final int offset) {
		
		//if knight piece is in the first column and following knight piece moves were being considered 
		//it will not work
		return Utility.FIRST_COLUMN[currentPosition] && (offset == -17) || (offset == -10) ||
				(offset == 6) || (offset == 15);
	}
	
	private static boolean secondColumnExclusion(final int currentPosition, final int offset) {
		
		//if knight piece is in the second column and the following knight piece moves were being considered
		//it will not work
		return Utility.SECOND_COLUMN[currentPosition] && (offset == -10) || (offset == 6);
	}
	
	private static boolean sevenColumnExclusion(final int currentPosition, final int offset) {
		
		//if knight piece is in the seventh column and the following knight piece moves were being considered
		//it will not work
		return Utility.SEVEN_COLUMN[currentPosition] && (offset == -6) ||(offset == 10);
	}
	
	private static boolean eightColumnExclusion(final int currentPosition, final int offset) {
		//if knight piece is in the eighth column and the following knight piece moves were being considered
		//it will not work
		return Utility.EIGHT_COLUMN[currentPosition] && (offset == -15) || (offset == -6) || (offset == 10)
				|| (offset == 17);
	}
	
	@Override
	public Piece movePiece(final Movement movement) {
		// TODO Auto-generated method stub
		return new Knight(movement.getDestination(), movement.getMovePiece().getPieceColor());
	}
}
