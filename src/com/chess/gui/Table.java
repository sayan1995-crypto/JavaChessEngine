package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtills;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import com.google.common.collect.Lists;

public class Table extends Observable{	
	private final JFrame gameFrame;
	private final GameHistoryPanel gameHistoryPanel;
	private final TakenPiecesPanel takenPiecesPanel;
	private final MoveLog moveLog;
	private  BoardPanel boardPanel;
	private final GameSetup gameSetup;
	private  Board gameBoard;
	private BoardDirection boardDirection;
	private Move compueterMove;
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private boolean highlightLegalMove;
	private static final String pieceIconPath="resource/Art/simple/";
	private static final Dimension OUTER_FRAME_DIAMENTION=new Dimension(600,600);
	public static final Dimension BOARD_PANEL_DIMENSION =new Dimension(400 ,400);
	public static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");
	private Move computerMove;
	
    private static final Table INSTANCE=new Table();
    
	private Table()
	{
		this.gameBoard=Board.createStandardBoard();
		this.gameFrame=new JFrame("JChess");	
		this.gameFrame.setSize(OUTER_FRAME_DIAMENTION);
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenubar=createTableMenubar();
		this.gameFrame.setJMenuBar(tableMenubar);
		this.boardPanel=new BoardPanel();
		boardDirection=BoardDirection.NORMAL;
		highlightLegalMove=true;
		this.addObserver(new TableGameAIWatcher());
		this.gameHistoryPanel=new GameHistoryPanel();
		this.takenPiecesPanel=new TakenPiecesPanel();
		this.moveLog=new MoveLog();
		
		this.gameFrame.add(this.boardPanel , BorderLayout.CENTER);
		this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
		this.gameFrame.add(this.gameHistoryPanel , BorderLayout.EAST);
		this.gameSetup=new GameSetup(this.gameFrame , true);
		this.gameFrame.setVisible(true);
		
	}
	
	
	public static Table get(){return INSTANCE;}
	
	public void show()
	{
		Table.get().moveLog.clear();
        Table.get().gameHistoryPanel.redo(gameBoard, Table.get().moveLog);
        Table.get().takenPiecesPanel.redo(Table.get().moveLog);
        Table.get().boardPanel.drawBoard(Table.get().getGameBoard());
        //Table.get().getDebugPanel().redo();
	}
	
	private GameSetup getGameSetup() {return this.gameSetup;}
	private Board getGameBoard() { return this.gameBoard;}
	
	@SuppressWarnings("deprecation")
	private void setupUpdate (final GameSetup gameSetup)
	{
		setChanged();
		notifyObservers(gameSetup); 
	}
	
	private static class TableGameAIWatcher implements Observer{

		@Override
		public void update(final Observable o,final Object arg) {

			if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrPlayer())
					&& !Table.get().getGameBoard().getCurrPlayer().isInCheckMate()
					&& !Table.get().getGameBoard().getCurrPlayer().isInstaleMate()
					
					)
			
			{
					

				//create an AI thread and execute ai work
				final AIThinkThank thinkThank = new AIThinkThank();
				thinkThank.execute();
				
			
		}
			if(Table.get().getGameBoard().getCurrPlayer().isInCheckMate())
				System.out.println("Game over "+Table.get().getGameBoard().getCurrPlayer()+ " is in check mate");
			if(Table.get().getGameBoard().getCurrPlayer().isInstaleMate())
				
				System.out.println("Game over "+Table.get().getGameBoard().getCurrPlayer()+ " is in instalemate");
		
	}
	
	}
		private static class AIThinkThank extends SwingWorker<Move, String>
		{
			private AIThinkThank()
			{
				
			}

			@Override
			protected Move doInBackground() throws Exception {
				final MoveStrategy miniMax = new MiniMax(3);
				final Move bestMove = miniMax.execute(Table.get().getGameBoard());
				return bestMove;
			}
			
			@Override
			public void done()
			{
				try {
					final Move bestMove=get();
					
					Table.get().updateComputerMove(bestMove);
					Table.get().updateGameBoard(Table.get().gameBoard.getCurrPlayer().makeMove(bestMove).getTransitionBoard());
					Table.get().moveLog.addMove(bestMove);
					Table.get().gameHistoryPanel.redo(Table.get().getGameBoard(), Table.get().moveLog);
					Table.get().takenPiecesPanel.redo(Table.get().moveLog);
					Table.get().boardPanel.drawBoard(Table.get().getGameBoard());
					Table.get().moveMadeUpdate(PlayerType.COMPUTER);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
		
		
		private void updateGameBoard(final Board board)
		{
			this.gameBoard=board;
		}
		 
		private void updateComputerMove(final Move move)
		{
			this.computerMove=move;
		}
		private void moveMadeUpdate(final PlayerType playerType)
		{
			setChanged();
			notifyObservers(playerType); 
		}
		
		
	private JMenuBar createTableMenubar() {

		JMenuBar tableMenubar = new JMenuBar();
		tableMenubar.add(createFileMenu());
		tableMenubar.add(createPreferenceMenu());
		tableMenubar.add(createOptionsMenu());
		return tableMenubar;
		
		
	}
	private JMenu createFileMenu() {

		final JMenu fileMenu= new JMenu("File");
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open the pgn file");
			}
		});
		
		final JMenuItem exitMenuItem=new JMenuItem("Exit");		
		exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		fileMenu.add(openPGN);
		fileMenu.add(exitMenuItem);
		return fileMenu;	
	}
	
	private JMenu createPreferenceMenu()
	{
		final JMenu preferenceMenu= new JMenu("Preference");
		final JMenuItem flipBoardItem=new JMenuItem("Flip Board");
		final JCheckBoxMenuItem highlightMoveCheckbox=new JCheckBoxMenuItem("Show Highlightes Move" , true);
		flipBoardItem.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				boardDirection=boardDirection.oppoSite();
				boardPanel.drawBoard(gameBoard);
				
			}
		});
		highlightMoveCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {		
				highlightLegalMove =!highlightLegalMove;
			}
		});
		preferenceMenu.add(flipBoardItem);
		preferenceMenu.addSeparator();
		preferenceMenu.add(highlightMoveCheckbox);
		
		return preferenceMenu;
	}
	
	
	
	private JMenu createOptionsMenu()
	{
		final JMenu optionMenu = new JMenu("Options");
		final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
		setupGameMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				Table.get().getGameSetup().promptUser();
				Table.get().setupUpdate(Table.get().getGameSetup());

				
				
			}
		});
		
		optionMenu.add(setupGameMenuItem);
		return optionMenu;
	}
	
	
	
	public enum BoardDirection{
		NORMAL{
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return boardTiles;
			}
			@Override
			BoardDirection oppoSite() {return FLIPPED;}
		},
		FLIPPED{
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				return Lists.reverse(boardTiles);}
			@Override
			BoardDirection oppoSite() {return NORMAL;}
			
		};
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection oppoSite();
		
	}
	
	 
	
	
	
	private class BoardPanel extends JPanel{
		
		final List<TilePanel> boardTiles;
		BoardPanel()
		{
			super(new GridLayout(8,8));
			this.boardTiles=new ArrayList<>();
			for(int i=0;i<BoardUtills.NUM_TILE;i++)
				{
				TilePanel tilePanel=new TilePanel(this ,i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
				}
			
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
			
		}
		public void drawBoard(Board board) {
			removeAll();
			for(TilePanel tilepanel : boardDirection.traverse(boardTiles))
			{
				tilepanel.refreshTileContent(board);
				add(tilepanel);
			}
			validate();
			repaint();
			
		}	
	}
	
	
	
	
	
	public static class MoveLog{
		
		private final List<Move> moves;
		
		MoveLog()
		{
			this.moves=new ArrayList<>();
		}
		
		public List<Move> getMoves(){
			return this.moves;
		}
		public void addMove(final Move move)
		{
			this.moves.add(move);
		}
		public int size()
		{
			return this.moves.size();
		}
		public void clear()
		{
			this.moves.clear();
		}
		public boolean removeMove(final Move move)
		{
			return this.moves.remove(move);
		}
		public Move removeMove(int index)
		{
			return this.moves.remove(index);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private class TilePanel extends JPanel {

		private final int tileId;


		TilePanel(final BoardPanel boardPanel, final int tileId) {
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			drawPieceOnTile(gameBoard);		
			addMouseListener(new MouseListener() {
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {

					if(SwingUtilities.isRightMouseButton(e))
					{
						sourceTile=null;
						destinationTile=null;
						humanMovedPiece=null;						
					} else if (SwingUtilities.isLeftMouseButton(e))
					{
						if(sourceTile==null)
						{
							sourceTile=gameBoard.getTile(tileId);
							humanMovedPiece=sourceTile.getPiece();
							if(humanMovedPiece==null)
								sourceTile=null;
						}
						else
						{
							destinationTile=gameBoard.getTile(tileId);
							final Move move = MoveFactory.createMove(gameBoard , sourceTile.getTileCordinate(), destinationTile.getTileCordinate());
							final MoveTransition moveTransition = gameBoard.getCurrPlayer().makeMove(move);
							if(moveTransition.getMoveStatus().isDone())
							{
							gameBoard=moveTransition.getTransitionBoard();
							moveLog.addMove(move);
							}
							sourceTile=null;humanMovedPiece=null;destinationTile=null;
						}
						
						
					}
					
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
					
							gameHistoryPanel.redo(gameBoard, moveLog);
							takenPiecesPanel.redo(moveLog);
							if(gameSetup.isAIPlayer(gameBoard.getCurrPlayer()))
								Table.get().moveMadeUpdate(PlayerType.HUMAN);
							boardPanel.drawBoard(gameBoard);
							
							
						}
					});
					
				}

				
			});
			
			validate();
		}
	
	public void refreshTileContent(Board board) {
			removeAll();
			assignTileColor();
			drawPieceOnTile(board);
			validate();
			repaint();
			
			
		}

	private void assignTileColor() {
		
		if(BoardUtills.EIGHTH_RANK[tileId] || BoardUtills.SIXTH_RANK[tileId]||
			BoardUtills.FOURTH_RANK[tileId] || BoardUtills.SECOND_RANK[tileId])
			setBackground(tileId %2 ==0 ? lightTileColor : darkTileColor);		
		if(BoardUtills.SEVENTH_RANK[tileId] || BoardUtills.FIFTH_RANK[tileId]||
				BoardUtills.THIRD_RANK[tileId] || BoardUtills.FIRST_RANK[tileId])
				setBackground(tileId %2 !=0 ? lightTileColor : darkTileColor);			
		}
	
	private void drawPieceOnTile(final Board board)
	{
		this.removeAll();

		if (board.getTile(this.tileId).isTileOccupied()) {
			String fileDirectory = pieceIconPath
					+ board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1)
					+ board.getTile(this.tileId).getPiece().toString() + ".gif";
			try {
				final BufferedImage image = ImageIO.read(new File(fileDirectory));
				add(new JLabel(new ImageIcon(image)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		hightLightLegalMoves(gameBoard);
	
	}


	
	private void hightLightLegalMoves(final Board board)
	{
		if(highlightLegalMove)
		{
			
			for(final Move move : pieceLegalMove(board))
			{
				if((move.getDestinationCoordinate() == this.tileId) &&  
				   gameBoard.getCurrPlayer().makeMove(move).getMoveStatus().isDone())
					try {
						add(new JLabel(new ImageIcon(ImageIO.read(new File("resource/Art/misc/green_dot.png")))));
					}
				catch(Exception e){
					e.printStackTrace();
					
				}
			}
		}
	}
	
	private Collection<Move> pieceLegalMove(final Board board)
	{
		
		if(humanMovedPiece !=null && 
			humanMovedPiece.getPieceAlliance() == board.getCurrPlayer().getAlliance())
			{
			List<Move> pieceLegalMoves= new ArrayList<>();
				for(Move move : board.getCurrPlayer().getLegalMoves())
					if(move.getMovedPiece().equals(humanMovedPiece))
						pieceLegalMoves.add(move);
				
				return pieceLegalMoves;						
			}
		return Collections.emptyList();
	}
	
}
	
	
	
	
	
	
	enum PlayerType{
		HUMAN,
		COMPUTER
	}
	
	
}
