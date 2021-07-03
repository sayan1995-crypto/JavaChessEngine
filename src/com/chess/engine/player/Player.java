package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {

	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private final boolean isInCheck;
	public Player(final Board board,final Collection<Move> legalMoves
			,final Collection<Move> opponentMoves) {
		super();
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateCastleMove(legalMoves, opponentMoves)));
		this.isInCheck=!Player.calculateAttackeOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
	}
	public static Collection<Move> calculateAttackeOnTile(Integer piecePosition, Collection<Move> moves) {
		final List<Move> attackMoves=new ArrayList<>();
		for(final Move move : moves)
			if(piecePosition==move.getDestinationCoordinate())
				attackMoves.add(move);
		
		return ImmutableList.copyOf(attackMoves);
	}
	private King establishKing() {
		for(final Piece piece : getAllActivePieces())
			if(piece.isKing())
				return (King) piece;
		throw new RuntimeException("Not a valid chess board");
		
	}	
	public abstract Collection<Piece> getAllActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	public abstract Collection<Move> calculateCastleMove(Collection<Move> currentPlayerLegalMove, Collection<Move> opponentPlayerLegalMove); 
	
	public boolean isMoveLegal(final Move move){return this.legalMoves.contains(move);}
	public boolean isInCheck(){return this.isInCheck;}
	public boolean isInCheckMate(){return this.isInCheck && !hasEscapedMove();}
	public King getPlayerKing() {return playerKing;}
	public Collection<Move> getLegalMoves() {return legalMoves;}
	private boolean hasEscapedMove()
	{
		for(Move move : legalMoves)
			{
			final MoveTransition transition=makeMove(move);
			if(transition.getMoveStatus().isDone())
				return true;
			}
		return false;
		
	}
	
	public boolean isInstaleMate()
	{
		return !this.isInCheck && !hasEscapedMove();
	}
	
	public boolean isInCastle()
	{
		return false;
	}
	
	public MoveTransition makeMove(final Move move)
	{
		if(!isMoveLegal(move))
			return new MoveTransition(this.board, move, MoveStatus.ILLIGAL);
		final Board transitionBoard=move.execute();
		
		final Collection<Move> kingAttacksMoves=calculateAttackeOnTile(transitionBoard.getCurrPlayer().getOpponent().getPlayerKing().getPiecePosition(),
				transitionBoard.getCurrPlayer().getLegalMoves());
		if(!kingAttacksMoves.isEmpty())
			return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
		
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	
	
}
