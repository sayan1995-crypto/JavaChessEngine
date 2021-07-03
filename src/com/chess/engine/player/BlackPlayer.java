package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import com.google.common.collect.ImmutableList;

public class BlackPlayer extends Player {

	public BlackPlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMove) {
		super(board, blackStandardLegalMove, whiteStandardLegalMoves);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Piece> getAllActivePieces() {
	
		return this.board.getActiveBlackPieces();
	}

	@Override
	public Alliance getAlliance() {return Alliance.BLACK;}

	@Override
	public Player getOpponent() {
		return this.board.getWhitePlayer();
	}

	@Override
	public Collection<Move> calculateCastleMove(Collection<Move> currentPlayerLegalMove,
			Collection<Move> opponentPlayerLegalMove) {

		List<Move> castleMoves=new ArrayList<Move>();
		
		/*
		 * First check the king is on check or the first move, then check all the in between tiles are empty
		 * then check in between tiles are not in attack, then check for the rook first move
		 */
		
		//Black king side castle move
		if(this.playerKing.isFirstMove() && !this.isInCheck() && this.board.getTile(0).isTileOccupied() && this.board.getTile(0).getPiece().isFirstMove())
			if(!this.board.getTile(5).isTileOccupied() && Player.calculateAttackeOnTile(5,opponentPlayerLegalMove).isEmpty()
				&&!this.board.getTile(6).isTileOccupied() && Player.calculateAttackeOnTile(6,opponentPlayerLegalMove).isEmpty())
			
				castleMoves.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6, (Rook)this.board.getTile(7).getPiece(),
						7,5));
		
		//Black queen side castle move
		if(this.playerKing.isFirstMove() && !this.isInCheck() && this.board.getTile(7).isTileOccupied() && this.board.getTile(7).getPiece().isFirstMove())
			if(!this.board.getTile(1).isTileOccupied() && Player.calculateAttackeOnTile(1,opponentPlayerLegalMove).isEmpty()
				&&!this.board.getTile(2).isTileOccupied() && Player.calculateAttackeOnTile(2,opponentPlayerLegalMove).isEmpty()
				&&!this.board.getTile(3).isTileOccupied() && Player.calculateAttackeOnTile(3,opponentPlayerLegalMove).isEmpty())
			
				castleMoves.add(new Move.QueenSideCastleMove(this.board, this.playerKing,2,(Rook)this.board.getTile(0).getPiece(), 0, 3));
		
		
		
		return ImmutableList.copyOf(castleMoves);
		

	}

	
}
