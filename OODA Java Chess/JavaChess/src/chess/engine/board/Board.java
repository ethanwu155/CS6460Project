package chess.engine.board;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import chess.engine.Colors.Colors;
import chess.engine.pieces.Bishop;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Queen;
import chess.engine.pieces.Rook;
import chess.engine.player.BlackPlayer;
import chess.engine.player.Player;
import chess.engine.player.WhitePlayer;
import com.google.common.collect.Iterables;

public class Board {
	
	//fields
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    
	//constructor
	private Board(final Builder builder) {
		this.gameBoard = createBoard(builder);
		this.whitePieces = calculateActive(this.gameBoard, Colors.WHITE);
		this.blackPieces = calculateActive(this.gameBoard, Colors.BLACK);
		
		final Collection<Movement>whiteLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Movement>blackLegalMoves = calculateLegalMoves(this.blackPieces);
		
		this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
		this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);
		this.currentPlayer = null;
	}
	
	//method for printing board in the console
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 64; i ++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if ((i + 1) % 8 == 0) {
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	
	public Iterable<Movement> getLegalMoves() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
	}
	
	
	public Collection<Piece>getBlackPiece() {
		return this.blackPieces;
	}
	
	public Collection<Piece>getWhitePiece() {
		return this.whitePieces;
	}
	
	//setting white player
	public Player whitePlayer() {
		return this.whitePlayer;
	}
	
	//setting black player
	public Player blackPlayer() {
		return this.blackPlayer;
	}
	
	public Player currentPlayer() {
		return this.currentPlayer;
	}
	
	private Collection<Movement> calculateLegalMoves (final Collection<Piece>pieces) {
		final List<Movement> legalMoves = new ArrayList<>();
		
		for (final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateMoves(this));
		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	private Collection<Piece> calculateActive(final List<Tile> gameboard, final Colors color) {
		
		final List<Piece> activePiece = new ArrayList<>();
		for (final Tile tile : gameBoard) {
			if (tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				if (piece.getPieceColor() == color) {
					activePiece.add(piece);
				}
			}
		}
		return Collections.unmodifiableList(activePiece);
	}
	
	public Tile getTile(final int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}
	
	//method for populating List from 0 to 63
	private static List<Tile> createBoard(final Builder builder) {
		final Tile[] tiles = new Tile[Utility.num_tiles];
		
		final List<Tile>tilesList = Arrays.asList(tiles);
		
		for (int i = 0; i < Utility.num_tiles; i ++) {
			tiles[i] = Tile.createTile(i, builder.configuration.get(i));
		}
		return Collections.unmodifiableList(tilesList);
	}
	
	//method for setting up starting board
	public static Board createStartingBoard() {
		final Builder builder = new Builder();
		
		//white layout
		builder.setPiece(new Pawn(48, Colors.WHITE));
		builder.setPiece(new Pawn(49, Colors.WHITE));
		builder.setPiece(new Pawn(50, Colors.WHITE));
		builder.setPiece(new Pawn(51, Colors.WHITE));
		builder.setPiece(new Pawn(52, Colors.WHITE));
		builder.setPiece(new Pawn(53, Colors.WHITE));
		builder.setPiece(new Pawn(54, Colors.WHITE));
		builder.setPiece(new Pawn(55, Colors.WHITE));
		builder.setPiece(new Rook(56, Colors.WHITE));
		builder.setPiece(new Knight(57, Colors.WHITE));
		builder.setPiece(new Bishop(58, Colors.WHITE));
		builder.setPiece(new Queen(59, Colors.WHITE));
		builder.setPiece(new King(60, Colors.WHITE));
		builder.setPiece(new Bishop(61, Colors.WHITE));
		builder.setPiece(new Knight(62, Colors.WHITE));
		builder.setPiece(new Rook(63, Colors.WHITE));
		
		//black layout
		builder.setPiece(new Pawn(15, Colors.BLACK));
		builder.setPiece(new Pawn(14, Colors.BLACK));
		builder.setPiece(new Pawn(13, Colors.BLACK));
		builder.setPiece(new Pawn(12, Colors.BLACK));
		builder.setPiece(new Pawn(11, Colors.BLACK));
		builder.setPiece(new Pawn(10, Colors.BLACK));
		builder.setPiece(new Pawn(9, Colors.BLACK));
		builder.setPiece(new Pawn(8, Colors.BLACK));
		builder.setPiece(new Rook(7, Colors.BLACK));
		builder.setPiece(new Knight(6, Colors.BLACK));
		builder.setPiece(new Bishop(5, Colors.BLACK));
		builder.setPiece(new Queen(3, Colors.BLACK));
		builder.setPiece(new King(4, Colors.BLACK));
		builder.setPiece(new Bishop(2, Colors.BLACK));
		builder.setPiece(new Knight(1, Colors.BLACK));
		builder.setPiece(new Rook(0, Colors.BLACK));
		
		//white moves first (set white to first move)
		builder.setMove(Colors.WHITE);
		
		return builder.build();
	}

	
	//introduce builder class to create an instance of the board
	public static class Builder {
		
		//Map Tile Coordinate to a Chess Piece
		Map<Integer, Piece> configuration;
		
		//Keep track of player moves
		Colors nextMove;
		
		//constructor
		public Builder() {
			this.configuration = new HashMap<>();
		}
		
		//setting piece on board
		public Builder setPiece(final Piece piece) {
			this.configuration.put(piece.getPosition(), piece);
			return this;
		}
		
		//setting movement
		public Builder setMove(final Colors nextMove) {
			this.nextMove = nextMove;
			return this;
		}
		
		public Board build() {
			return new Board(this);
		}
		
	}
}
