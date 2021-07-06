package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtills;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.MajorPieceAttackMove;
import com.chess.engine.board.Move.MajoPieceMove;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.ImmutableList;

public class Bishop extends Piece {

	private final static Integer[] CANDIDATE_MOVE_COORDINATES_VECTOR = { -9, -7, +7, +9 };

	public Bishop(Integer piecePosition, Alliance pieceAlliance, Boolean isFirstMove) {
		super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);

	}


	@Override
	public String toString()
	{
		return PieceType.BISHOP.toString();
	}
	
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (Integer currentCandidateOffset : CANDIDATE_MOVE_COORDINATES_VECTOR) {
			Integer candidateDestinationCoordinate = this.piecePosition;

			while (BoardUtills.isValidTile(candidateDestinationCoordinate)){
				if (isFirstCoulmExclusion(candidateDestinationCoordinate, currentCandidateOffset) 
					|| isEightCoulmExclusion(candidateDestinationCoordinate, currentCandidateOffset))
				break;

				candidateDestinationCoordinate += currentCandidateOffset;
				if(BoardUtills.isValidTile(candidateDestinationCoordinate)) {
				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if (!candidateDestinationTile.isTileOccupied())
					{
					legalMoves.add(new MajoPieceMove(board, this, candidateDestinationCoordinate));
					
					}
				else {
					if (candidateDestinationTile.getPiece().getPieceAlliance() != pieceAlliance)
						legalMoves.add(new MajorPieceAttackMove(board, this,
								candidateDestinationCoordinate,candidateDestinationTile.getPiece()));
											
					break;
				}

			}
		}

	}

		return ImmutableList.copyOf(legalMoves);
	}

	private boolean isFirstCoulmExclusion(Integer currentPosition, Integer destination) {
		return BoardUtills.FIRST_COLUMN[currentPosition] && (destination == -9 || destination == 7);
	}

	private boolean isEightCoulmExclusion(Integer currentPosition, Integer destination) {
		return BoardUtills.EIGHTH_COLUMN[currentPosition] && (destination == +9 || destination == -7);
	}
	@Override
	public boolean isKing() {return false;}


	@Override
	public Piece movePiece(Move move) {
		return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance() , false);
	}
}