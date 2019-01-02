package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import chess.engine.board.Board;
import chess.engine.board.Movement;
import chess.engine.board.Tile;
import chess.engine.board.Utility;
import chess.engine.pieces.Piece;
import chess.engine.player.Transition;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;

public class Table {
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMove;
	private boolean highLightLegals;
	private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	private final static Dimension BOARD_DIMENSION = new Dimension(400, 350);
	private final static Dimension TILE_DIMENSION = new Dimension(10, 10);
	
	private static String ImagePiecePath = "art/";
	
	private final Color lightColor = Color.WHITE;
	private final Color darkColor = Color.DARK_GRAY;
	
	public Table() {
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar = new JMenuBar();
		
		MenuBarPopulate(tableMenuBar);
		
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.highLightLegals = false;
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessBoard = Board.createStartingBoard();
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}

	private void MenuBarPopulate(final JMenuBar tableMenuBar) {
		// TODO Auto-generated method stub
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferrenceMenu());

	}
	

	private JMenu createFileMenu() {
		// TODO Auto-generated method stub
		final JMenu fileMenu = new JMenu("File");
		
		final JMenuItem exitMenu = new JMenuItem("Exit");
		exitMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenu);
		return fileMenu;
	}
	
	private JMenu createPreferrenceMenu() {
		final JMenu preferenceMenu = new JMenu("Preferences");
		return preferenceMenu;
		
	}
	
	
	//BoardPanel maps to the chess board (visual component)
	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;
		BoardPanel() {
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			
			for (int i = 0; i < 64; i ++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_DIMENSION);
			validate();
		}
		public void drawBoard(Board board) {
			// TODO Auto-generated method stub
			removeAll();
			for(final TilePanel tilePanel : boardTiles) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
		}
	}
	
	//TilePanel maps to a tile in the chess game (visual component of tile)
	private class TilePanel extends JPanel  {
		
		private final int ID;
		TilePanel(final BoardPanel boardPanel, final int ID) {
			super(new GridBagLayout());
			this.ID = ID;
			setPreferredSize(TILE_DIMENSION);
			
			setTileColor();
			setTilePiece(chessBoard);
			
			//add mouse events to help with chess piece movement
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
					// TODO Auto-generated method stub
					
					//Right Mouse click cancel every out
					if (SwingUtilities.isRightMouseButton(e)) {
						sourceTile = null;
						destinationTile = null;
						humanMove = null;
					}
					//Left Mouse click check to see if source tile is null and has a piece on it assign sourceTile to ID otherwise cancel
					else if (SwingUtilities.isLeftMouseButton(e)) {
						
						if (sourceTile == null) {
							sourceTile = chessBoard.getTile(ID);
							humanMove = sourceTile.getPiece();
							if (humanMove == null) {
								sourceTile = null;
							}
						}
						else {
							destinationTile = chessBoard.getTile(ID);
							final Movement movement = Movement.MoveFactory.createMovement(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							final Transition transition = chessBoard.currentPlayer().makeMove(movement);
							if (transition.getMoveStatus().isDone()) {
								chessBoard = transition.getBoardTransition();
							}
							sourceTile = null;
							destinationTile = null;
							humanMove = null;							
						}
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								boardPanel.drawBoard(chessBoard);
							}
						});
					}
				}

				@Override
				public void mouseEntered(final MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(final MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(final MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			validate();	
		}
		

		public void drawTile(Board board) {
			// TODO Auto-generated method stub
			setTileColor();
			setTilePiece(board);
			validate();
			repaint();
		}

		private void setTileColor() {
			// TODO Auto-generated method stub
			if (Utility.FIRST_ROW[this.ID] || Utility.THIRD_ROW[this.ID] || Utility.FIFTH_ROW[this.ID] || Utility.SEVEN_ROW[this.ID]) {
				
				//check to see if the tile id is even. if it is color it light than dark
				setBackground(this.ID % 2 == 0 ? lightColor : darkColor);
			}
			else if (Utility.SECOND_ROW[this.ID] || Utility.FOURTH_ROW[this.ID] || Utility.SIX_ROW[this.ID] || Utility.EIGHT_ROW[this.ID]) {
				
				//check to see if the tile id is not even. if it is not color it dark than black
				setBackground(this.ID % 2 != 0 ? lightColor : darkColor);
			}
		}
		
		private void setTilePiece(final Board board) {
			//remove all components from panel
			
			this.removeAll();
			
			if(board.getTile(this.ID).isTileOccupied()) {
								
				try {
					final BufferedImage image = ImageIO.read(new File(ImagePiecePath + board.getTile(this.ID).getPiece().getPieceColor().toString().substring(0,1) + board.getTile(this.ID).getPiece().toString() + ".gif"));
					add(new JLabel(new ImageIcon(image)));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void highLightLegal(final Board board) {
			if (true) {
				for (final Movement movement : pieceLegalMoves(board)) {
					if (movement.getDestination() == this.ID) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
						}
						catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		private Collection<Movement> pieceLegalMoves(final Board board) {
			if (humanMove != null && humanMove.getPieceColor() == board.currentPlayer().getColor()) {
				return humanMove.calculateMoves(board);
			}
			return Collections.emptyList();
		}
	}
	
	
	
}
