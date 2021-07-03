package com.chess.engine.piece;
import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final Integer piecePosition;
	protected final Alliance pieceAlliance;
	protected final Boolean isFirstMove;
	
	public Piece(final PieceType pieceType, 
			final Integer piecePosition ,
			final Alliance pieceAlliance , 
			final Boolean isFirstMove)
	{
		this.pieceType=pieceType;
		this.piecePosition=piecePosition;
		this.pieceAlliance=pieceAlliance;
		this.isFirstMove=isFirstMove;
	}
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	public abstract Piece movePiece(Move move);
	public Integer getPiecePosition() {
		return piecePosition;
	}
	public Alliance getPieceAlliance() {return pieceAlliance;}
	public Boolean isFirstMove(){return this.isFirstMove;}
	public PieceType getPieceType() {return pieceType;}
	public int getPieceValue()
	{
		return this.pieceType.getPieceValue();
	}

	public enum PieceType {
		PAWN("P" , 100), ROOK("R",500), KNIGHT("N",300), BISHOP("B",300), QUEEN("Q",900), KING("K",10000);

		private String pieceType;
		private int pieceValue;
		PieceType(final String pieceType, final int pieceValue) 
		{this.pieceType = pieceType; this.pieceValue=pieceValue;}
		@Override
		public String toString() {return this.pieceType;}
		protected int getPieceValue(){return this.pieceValue;}
	}

	public abstract boolean isKing();
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isFirstMove == null) ? 0 : isFirstMove.hashCode());
		result = prime * result + ((pieceAlliance == null) ? 0 : pieceAlliance.hashCode());
		result = prime * result + ((piecePosition == null) ? 0 : piecePosition.hashCode());
		result = prime * result + ((pieceType == null) ? 0 : pieceType.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (isFirstMove == null) {
			if (other.isFirstMove != null)
				return false;
		} else if (!isFirstMove.equals(other.isFirstMove))
			return false;
		if (pieceAlliance != other.pieceAlliance)
			return false;
		if (piecePosition == null) {
			if (other.piecePosition != null)
				return false;
		} else if (!piecePosition.equals(other.piecePosition))
			return false;
		if (pieceType != other.pieceType)
			return false;
		return true;
	}
	
	
	
}
