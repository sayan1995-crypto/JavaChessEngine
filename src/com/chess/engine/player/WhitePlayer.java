package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.nullMove;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import com.google.common.collect.ImmutableList;

public class WhitePlayer extends Player{	

	public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMove) {
		super(board, whiteStandardLegalMoves, blackStandardLegalMove);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Piece> getAllActivePieces() {		
		return this.board.getActiveWhitePieces();
	}

	@Override
	public Alliance getAlliance() {return Alliance.WHITE;}

	@Override
	public Player getOpponent() {
		return this.board.getBlackPlayer();
	}

	@Override
	public Collection<Move> calculateCastleMove(Collection<Move> currentPlayerLegalMove,
			Collection<Move> opponentPlayerLegalMove) {
		
		List<Move> castleMoves=new ArrayList<Move>();
		
		/*
		 * First check the king is on check or the first move, then check all the in between tiles are empty
		 * then check in between tiles are not in attack, then check for the rook first move
		 */
		
		//White king side castle move
		if(this.playerKing.isFirstMove() && !this.isInCheck() && this.board.getTile(63).isTileOccupied() && this.board.getTile(63).getPiece().isFirstMove())
			if(!this.board.getTile(61).isTileOccupied() && Player.calculateAttackeOnTile(61,opponentPlayerLegalMove).isEmpty()
				&&!this.board.getTile(62).isTileOccupied() && Player.calculateAttackeOnTile(62,opponentPlayerLegalMove).isEmpty())
			
		castleMoves.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62, (Rook)this.board.getTile(63).getPiece(),
				63,61));
		
		//White queen side castle move
		if(this.playerKing.isFirstMove() && !this.isInCheck() && this.board.getTile(56).isTileOccupied() && this.board.getTile(56).getPiece().isFirstMove())
			if(!this.board.getTile(59).isTileOccupied() && Player.calculateAttackeOnTile(59,opponentPlayerLegalMove).isEmpty()
				&&!this.board.getTile(58).isTileOccupied() && Player.calculateAttackeOnTile(58,opponentPlayerLegalMove).isEmpty()
				&&!this.board.getTile(57).isTileOccupied() && Player.calculateAttackeOnTile(57,opponentPlayerLegalMove).isEmpty())
			
		castleMoves.add(new Move.QueenSideCastleMove(this.board, this.playerKing,58,(Rook)this.board.getTile(56).getPiece(), 56, 59));
		
		return ImmutableList.copyOf(castleMoves);
		
	}

}
