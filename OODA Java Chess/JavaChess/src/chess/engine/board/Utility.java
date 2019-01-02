package chess.engine.board;

public class Utility {
	
	public static final int num_tiles = 64;
	
	//array of 64 (number of board tiles) introduced indicating true/false values for column locations
	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1);
	public static final boolean[] SEVEN_COLUMN = initColumn(6);
	public static final boolean[] EIGHT_COLUMN = initColumn(7);
	
	public static final boolean[] SECOND_ROW = initRow(8);
	public static final boolean[] SEVEN_ROW = initRow(48);
	public static final boolean[] FIRST_ROW = initRow(0);
	public static final boolean[] THIRD_ROW = initRow(16);
	public static final boolean[] FOURTH_ROW = initRow(24);
	public static final boolean[] FIFTH_ROW = initRow(32);
	public static final boolean[] SIX_ROW = initRow(40);
	public static final boolean[] EIGHT_ROW = initRow(56);
	
	//method called to help to indicate true/false for column location
	private static boolean[] initColumn(int colNumber) {
		//declare boolean of size 64
		final boolean[] column = new boolean[64];
		
		//while loop
		//colNumber is true, add 8 to colNumber make that true, keep going until colNumber is 64 or greater
		do {
			column[colNumber] = true;
			colNumber += 8;
		}
		while (colNumber < 64);
		
		return column;
	}
	
	private static boolean[] initRow(int rowNumber) {
		// TODO Auto-generated method stub
		final boolean[] row = new boolean[64];
		
		//while loop
		do {
			row[rowNumber] = true;
			rowNumber ++;
		}
		while (rowNumber % 8 != 0);
		
		return row;
	}

	public static boolean isValidCoordinate(final int coordinate) {
		// TODO Auto-generated method stub
		
		//check to see if the tile number is within the bounds of the chess board
		return coordinate >= 0 && coordinate < 64;
	}
	

}
