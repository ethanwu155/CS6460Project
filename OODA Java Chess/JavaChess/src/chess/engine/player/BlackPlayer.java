package chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import chess.engine.Colors.Colors;
import chess.engine.board.Board;
import chess.engine.board.Movement;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

public class BlackPlayer extends Player {

	public BlackPlayer(final Board board, final Collection<Movement> whiteLegalMoves, final Collection<Movement> blackLegalMoves) {
		// TODO Auto-generated constructor stub
		super(board, blackLegalMoves, whiteLegalMoves);
		
	}

	@Override 
	public Collection<Piece> getActivePiece() {
		// TODO Auto-generated method stub
		return this.board.getBlackPiece();
	}

	@Override
	public Colors getColor() {
		// TODO Auto-generated method stub
		return Colors.BLACK;
	}
	
	//set up opponent (white)
	@Override
	public Player getOpponent() {
		return this.board.whitePlayer();
	}

	@Override
	protected Collection<Movement> calculateKingCastle(final Collection<Movement> playerLegals,
			final Collection<Movement> opponentsLegalMoves) {
		// TODO Auto-generated method stub
		final List<Movement> kingCastles = new ArrayList<>();
		
		//conditions for castling
		if (this.playerKing.firstMove() && !this.isCheck()) {
			
			//condition that the tiles to the right of king (black) are not occupied
			if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(7);
				//check to see if the rook tile is occupied and that its the rooks first move
				if(rookTile.isTileOccupied() && rookTile.getPiece().firstMove()) {
					//check to see if there are any attacks on the tiles to the right of the king and check to see if the end piece is a rook
					if (Player.calculateAttacksOnTile(5, opponentsLegalMoves).isEmpty() && Player.calculateAttacksOnTile(6, opponentsLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Movement.KingCastleMovement(this.board, this.playerKing, 6, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
					}
				}
			}
			//condition that the tiles to the left of king (black) are not occupied
			if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(0);
				if (rookTile.isTileOccupied() && rookTile.getPiece().firstMove() && Player.calculateAttacksOnTile(2, opponentsLegalMoves).isEmpty() && Player.calculateAttacksOnTile(3, opponentsLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
					kingCastles.add(new Movement.RookCastleMovement(this.board, this.playerKing, 2, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
				}
			}
		}		
		return Collections.unmodifiableList(kingCastles);
	}
}
