package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Alliance;
import com.chess.engine.piece.Bishop;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Knight;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Queen;
import com.chess.engine.piece.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {

	private final List<Tile> gameBoard;
	private final Collection<Piece> activeWhitePieces;
	private final Collection<Piece> activeBlackPieces;
	
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private final Pawn enPassantPawn;
	private Board(final Builder builder)
	{
		this.gameBoard=createGameBoard(builder);
		this.activeBlackPieces=calculateActivePieces(gameBoard,Alliance.BLACK);
		this.activeWhitePieces=calculateActivePieces(gameBoard,Alliance.WHITE);
		this.enPassantPawn=builder.enPassantPawn;
		final Collection<Move> whiteStandardLegalMoves=calculateAllLegalMoves(activeWhitePieces);
		final Collection<Move> blackStandardLegalMoves=calculateAllLegalMoves(activeBlackPieces);
		
		this.whitePlayer=new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer=new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer , this.blackPlayer);
		
	}	
	
	private Collection<Move> calculateAllLegalMoves(Collection<Piece> pieces) {

		long startTime=System.currentTimeMillis();
		List<Move> legalMoves=new ArrayList<>();
		for(final Piece piece : pieces)
			legalMoves.addAll(piece.calculateLegalMoves(this));

		//System.out.println("Time required to calculate legalmoves "+(System.currentTimeMillis()-startTime));
		return ImmutableList.copyOf(legalMoves);
	}


	public WhitePlayer getWhitePlayer() {return whitePlayer;}
	public BlackPlayer getBlackPlayer() {return blackPlayer;}
	public Player getCurrPlayer() {return currentPlayer;}
	public Pawn getEnPassantPawn() {return enPassantPawn;}

	public Collection<Move> getALlLegalMove()
	{
		return ImmutableList.copyOf(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));
	}

	private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliacne) {
		List<Piece> activePieces=new ArrayList<>();
		for(final Tile tile: gameBoard)
			if(tile.isTileOccupied() && tile.getPiece().getPieceAlliance()==alliacne)
				activePieces.add(tile.getPiece());
		return ImmutableList.copyOf(activePieces);
	}




	private static List<Tile> createGameBoard(final Builder builder) {
		Tile[] tiles=new Tile[BoardUtills.NUM_TILE];
		for(Integer i=0;i<BoardUtills.NUM_TILE;i++)
			tiles[i]=Tile.createTile(i, builder.boardConfig.get(i));		
		return ImmutableList.copyOf(tiles);
	}
	public Tile getTile(final Integer coordinate) {
		return gameBoard.get(coordinate);
	}
	
	public Collection<Piece> getActiveWhitePieces() {return activeWhitePieces;}
	public Collection<Piece> getActiveBlackPieces() {return activeBlackPieces;}

	public static Board createStandardBoard()
	{
		Builder builder = new Builder();
		builder.setPiece(new Rook(0, Alliance.BLACK , true));
		builder.setPiece(new Knight(1, Alliance.BLACK , true));
		builder.setPiece(new Bishop(2,Alliance.BLACK , true));
		builder.setPiece(new Queen(3,Alliance.BLACK , true));
		builder.setPiece(new King(4, Alliance.BLACK , true));
		builder.setPiece(new Bishop(5,Alliance.BLACK , true));
		builder.setPiece(new Knight(6, Alliance.BLACK , true));
		builder.setPiece(new Rook(7, Alliance.BLACK , true));

		builder.setPiece(new Pawn(8,Alliance.BLACK , true));
		builder.setPiece(new Pawn(9,Alliance.BLACK , true));
		builder.setPiece(new Pawn(10,Alliance.BLACK , true));
		builder.setPiece(new Pawn(11,Alliance.BLACK , true));
		builder.setPiece(new Pawn(12,Alliance.BLACK , true));
		builder.setPiece(new Pawn(13,Alliance.BLACK , true));
		builder.setPiece(new Pawn(14,Alliance.BLACK , true));
		builder.setPiece(new Pawn(15,Alliance.BLACK , true));
		
		
		builder.setPiece(new Rook(56, Alliance.WHITE, true));
		builder.setPiece(new Knight(57, Alliance.WHITE, true));
		builder.setPiece(new Bishop(58,Alliance.WHITE, true));
		builder.setPiece(new Queen(59,Alliance.WHITE, true));
		builder.setPiece(new King(60, Alliance.WHITE, true));
		builder.setPiece(new Bishop(61,Alliance.WHITE, true));
		builder.setPiece(new Knight(62, Alliance.WHITE, true));
		builder.setPiece(new Rook(63, Alliance.WHITE, true));

		builder.setPiece(new Pawn(48,Alliance.WHITE , true));
		builder.setPiece(new Pawn(49,Alliance.WHITE , true));
		builder.setPiece(new Pawn(50,Alliance.WHITE , true));
		builder.setPiece(new Pawn(51,Alliance.WHITE , true));
		builder.setPiece(new Pawn(52,Alliance.WHITE, true ));
		builder.setPiece(new Pawn(53,Alliance.WHITE , true));
		builder.setPiece(new Pawn(54,Alliance.WHITE, true ));
		builder.setPiece(new Pawn(55,Alliance.WHITE, true ));
		
		builder.setAlliance(Alliance.WHITE);
		
		return builder.build();
		
	}
	
	public static Board createTestBoard()
	{

		Builder builder = new Builder();
		builder.setPiece(new Rook(0, Alliance.BLACK , true));
		//builder.setPiece(new Knight(1, Alliance.BLACK , true));
		builder.setPiece(new Bishop(2,Alliance.BLACK , true));
		builder.setPiece(new Queen(3,Alliance.BLACK , true));
		builder.setPiece(new King(6, Alliance.BLACK , true));
		builder.setPiece(new Bishop(5,Alliance.BLACK , true));
		//builder.setPiece(new Knight(6, Alliance.BLACK , true));
		builder.setPiece(new Rook(7, Alliance.BLACK , true));
		
		//builder.setPiece(new Rook(32, Alliance.WHITE, true));
		//builder.setPiece(new Rook(16, Alliance.WHITE, true));
		builder.setPiece(new Knight(42, Alliance.WHITE, true));
		//builder.setPiece(new Bishop(58,Alliance.WHITE, true));
		builder.setPiece(new Queen(11,Alliance.WHITE, true));
		builder.setPiece(new Queen(35,Alliance.WHITE, true));
		builder.setPiece(new Queen(32,Alliance.WHITE, true));
		builder.setPiece(new King(60, Alliance.WHITE, true));
		builder.setPiece(new Bishop(61,Alliance.WHITE, true));
		builder.setPiece(new Knight(62, Alliance.WHITE, true));
		builder.setPiece(new Rook(63, Alliance.WHITE, true));		
		builder.setAlliance(Alliance.WHITE);
		
		return builder.build();
		
	}
	
	
	
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for(Integer i=0;i<BoardUtills.NUM_TILE;i++)
		{
			final String tileText=gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if((i+1) % BoardUtills.NUM_TILE_PER_COLUMN==0)
				builder.append("\n");
		}
		return builder.toString();		
	}	


	public static class Builder{
		Map<Integer , Piece> boardConfig=new HashMap<>();
		Alliance nextMoveMaker;
		private  Pawn enPassantPawn; 
		public Builder setPiece(final Piece piece)
		{
			boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		public Builder setAlliance(final Alliance nextMoveMaker)
		{
			this.nextMoveMaker=nextMoveMaker;
			return this;
		}
		public Board build()
		{
			return new Board(this);
		}
		public void setEnPassantPawn(Pawn movedPawn) {
			this.enPassantPawn=movedPawn;
		}
		/*
		 * public Pawn getEnPassantPawn() { return this.enPassantPawn; }
		 */
	}

	
}
