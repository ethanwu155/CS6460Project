package chess.engine.board;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import chess.engine.pieces.Piece;

//class for representing 64 tiles on chess board
public abstract class Tile {
	
	//field
	protected final int tileCoordinate;
	
	private static final Map<Integer, emptyTile>EMPTY_TILES = createAllEmptyTiles(); 
	
	//constructor
	private Tile(int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}
	
	
	//every empty tile that can exist created upfront
	private static Map<Integer, emptyTile> createAllEmptyTiles() {
		// TODO Auto-generated method stub
		
		final Map<Integer, emptyTile> emptyTileMap = new HashMap<>();
		
		for (int i = 0 ; i < 64 ; i ++) {
			emptyTileMap.put(i, new emptyTile(i));
		}
		
		return Collections.unmodifiableMap(emptyTileMap);
	}
	
	//creating immutable code--only method for creating new tile
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new occupiedTile(tileCoordinate, piece) : EMPTY_TILES.get(tileCoordinate);
	}
	
	
	//abstract method to indicate whether tile is occupied with Piece
	public abstract boolean isTileOccupied();
	
	//abstract method for getting chess piece on tile
	public abstract Piece getPiece();
	
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	
	//subclass for an emptyTile with overriding isTileOccupied/getPiece methods
	public static final class emptyTile extends Tile {
		
		emptyTile(final int coordinate) {
			super(coordinate);
		}

		//by definition an empty tile means method isTileOccupied returns false
		@Override
		public boolean isTileOccupied() {
			// TODO Auto-generated method stub
			return false;
		}
		
		//no piece on tile to get so method returns null
		@Override
		public Piece getPiece() {
			// TODO Auto-generated method stub
			return null;
		}
		
		//if piece is not occupied print out in console with -
		@Override
		public String toString() {
			return "-";
		}
	}
	
	//subclass for an occupied tile
	private static final class occupiedTile extends Tile {
		
		private final Piece pieceOnTile;
		
		occupiedTile(int tileCoordinate, Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}

		@Override
		public boolean isTileOccupied() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Piece getPiece() {
			// TODO Auto-generated method stub
			return this.pieceOnTile;
		}
		
		//if tile is occupied print out in the console with a piece
		@Override
		public String toString() {
			return getPiece().getPieceColor().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString(); 
		}
	}
}
