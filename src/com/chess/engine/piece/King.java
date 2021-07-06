package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtills;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorPieceAttackMove;
import com.chess.engine.board.Move.MajoPieceMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class King extends Piece{

	private final static Integer[] CANDIDATE_MOVE_COORDINATES_OFFSET = {-9, -8, -7, -1, +1, +7, +8, +9};
	
	public King(final Integer piecePosition,final Alliance pieceAlliance ,final Boolean isFirstMove) {
		super(PieceType.KING , piecePosition, pieceAlliance , isFirstMove);
		
	}

	@Override
	public String toString()
	{
		return PieceType.KING.toString();
	}
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (Integer currentCandidateOffest : CANDIDATE_MOVE_COORDINATES_OFFSET) {
		final Integer candidateDestinationCoordinate = this.piecePosition + currentCandidateOffest;
		if (BoardUtills.isValidTile(candidateDestinationCoordinate)) {
			if(isFirstCoulmExclusion(this.piecePosition, currentCandidateOffest)    ||
				isEightCoulmExclusion(this.piecePosition, currentCandidateOffest))
				continue;
			final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
			if (!candidateDestinationTile.isTileOccupied())
				legalMoves.add(new MajoPieceMove(board, this, candidateDestinationCoordinate));
			else {
				if (candidateDestinationTile.getPiece().getPieceAlliance() != pieceAlliance)
					legalMoves.add(new MajorPieceAttackMove(board, this, candidateDestinationCoordinate, candidateDestinationTile.getPiece()));
			}
		}
	}		
	return ImmutableList.copyOf(legalMoves);
}	

	private boolean isFirstCoulmExclusion(final Integer currentPosition,final Integer destination) {
	return BoardUtills.FIRST_COLUMN[currentPosition] && (destination == -9 || destination == -1 || destination == 7);
	}

	private boolean isEightCoulmExclusion(final Integer currentPosition,final Integer destination) {
	return BoardUtills.EIGHTH_COLUMN[currentPosition] && (destination == 9 || destination == 1 || destination == 7);
	}

	
	
	@Override
	public boolean isKing() {
	return true;
	}

	@Override
	public Piece movePiece(final Move move) {
	return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
	}
}
