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

public class WhitePlayer extends Player {

	public WhitePlayer(final Board board, final Collection<Movement> whiteLegalMoves, final Collection<Movement> blackLegalMoves) {
		// TODO Auto-generated constructor stub
		super(board, whiteLegalMoves, blackLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePiece() {
		// TODO Auto-generated method stub
		return this.board.getWhitePiece();
	}

	@Override
	public Colors getColor() {
		// TODO Auto-generated method stub
		return Colors.WHITE;
	}

	//set up opponent (black)
	@Override
	public Player getOpponent() {
		// TODO Auto-generated method stub
		return this.board.blackPlayer();
	}

	@Override
	protected Collection<Movement> calculateKingCastle(final Collection<Movement> playerLegals,
			final Collection<Movement> opponentsLegalMoves) {
		// TODO Auto-generated method stub
		final List<Movement> kingCastles = new ArrayList<>();
		
		//conditions for castling
		if (this.playerKing.firstMove() && !this.isCheck()) {
			
			//condition that the tiles to the right of king (white) are not occupied
			if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(63);
				//check to see if the rook tile is occupied and that its the rooks first move
				if(rookTile.isTileOccupied() && rookTile.getPiece().firstMove()) {
					//check to see if there are any attacks on the tiles to the right of the king and check to see if the end piece is a rook
					if (Player.calculateAttacksOnTile(61, opponentsLegalMoves).isEmpty() && Player.calculateAttacksOnTile(62, opponentsLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Movement.KingCastleMovement(this.board, this.playerKing, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
					}
				}
			}
			//condition that the tiles to the left of king (white) are not occupied
			if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(56);
				if (rookTile.isTileOccupied() && rookTile.getPiece().firstMove() && Player.calculateAttacksOnTile(58, opponentsLegalMoves).isEmpty() && Player.calculateAttacksOnTile(59, opponentsLegalMoves).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
					kingCastles.add(new Movement.RookCastleMovement(this.board, this.playerKing, 58, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
				}
			}
		}		
		return Collections.unmodifiableList(kingCastles);
	}
}
