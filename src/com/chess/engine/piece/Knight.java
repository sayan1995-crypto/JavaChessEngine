package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtills;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.piece.Piece.PieceType;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Knight extends Piece {

	private final static Integer[] CANDIDATE_MOVE_COORDINATES_OFFSET = { -17, -15, -10, -6, 6, 10, 15, 17 };

	public Knight(final Integer piecePosition, final Alliance pieceAlliance, final Boolean isFirstMove) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (Integer currentCandidateOffest : CANDIDATE_MOVE_COORDINATES_OFFSET) {
			final Integer candidateDestinationCoordinate = this.piecePosition + currentCandidateOffest;
			if (BoardUtills.isValidTile(candidateDestinationCoordinate)) {
				if (isFirstCoulmExclusion(this.piecePosition, currentCandidateOffest)
						|| isSecondCoulmExclusion(this.piecePosition, currentCandidateOffest)
						|| isSeventhCoulmExclusion(this.piecePosition, currentCandidateOffest)
						|| isEightCoulmExclusion(this.piecePosition, currentCandidateOffest))
					continue;

				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if (!candidateDestinationTile.isTileOccupied())
					legalMoves.add(new MajoPieceMove(board, this, candidateDestinationCoordinate));
				else {
					if (candidateDestinationTile.getPiece().getPieceAlliance() != pieceAlliance)
						legalMoves.add(new MajorPieceAttackMove(board, this, candidateDestinationCoordinate,
								candidateDestinationTile.getPiece()));
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	private boolean isFirstCoulmExclusion(final Integer currentPosition,final Integer destination) {
		return BoardUtills.FIRST_COLUMN[currentPosition]
				&& (destination == -17 || destination == -10 || destination == 6 || destination == 15);
	}

	private boolean isSecondCoulmExclusion(final Integer currentPosition,final Integer destination) {
		return BoardUtills.SECOND_COLUMN[currentPosition] && (destination == -10 || destination == 6);
	}

	private boolean isSeventhCoulmExclusion(final Integer currentPosition,final Integer destination) {
		return BoardUtills.SEVENTH_COLUMN[currentPosition] && (destination == 10 || destination == -6);
	}

	private boolean isEightCoulmExclusion(final Integer currentPosition,final Integer destination) {
		return BoardUtills.EIGHTH_COLUMN[currentPosition]
				&& (destination == 17 || destination == 10 || destination == -6 || destination == -15);
	}

	@Override
	public boolean isKing() {
		return false;
	}

	@Override
	public Piece movePiece(final Move move) {
		return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
	}
}
