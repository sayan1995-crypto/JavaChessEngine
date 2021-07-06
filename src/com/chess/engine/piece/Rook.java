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
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Rook extends Piece {

	private final static Integer[] CANDIDATE_MOVE_COORDINATES_VECTOR = { -8, -1, +1, +8 };

	public Rook(final Integer piecePosition,final Alliance pieceAlliance,final Boolean isFirstMove) {
		super(PieceType.ROOK, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public String toString() {
		return PieceType.ROOK.toString();
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (Integer currentCandidateOffset : CANDIDATE_MOVE_COORDINATES_VECTOR) {
			Integer candidateDestinationCoordinate = this.piecePosition;

			while (BoardUtills.isValidTile(candidateDestinationCoordinate)) {
				if (isFirstCoulmExclusion(candidateDestinationCoordinate, currentCandidateOffset)
						|| isEightCoulmExclusion(candidateDestinationCoordinate, currentCandidateOffset))
					break;

				candidateDestinationCoordinate += currentCandidateOffset;
				if (BoardUtills.isValidTile(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					if (!candidateDestinationTile.isTileOccupied()) {
						legalMoves.add(new MajoPieceMove(board, this, candidateDestinationCoordinate));

					} else {
						if (candidateDestinationTile.getPiece().getPieceAlliance() != pieceAlliance)
							legalMoves.add(new MajorPieceAttackMove(board, this, candidateDestinationCoordinate,
									candidateDestinationTile.getPiece()));
						break;
					}

				}
			}
		}

		return ImmutableList.copyOf(legalMoves);
	}

	private boolean isFirstCoulmExclusion(final Integer currentPosition,final Integer destination) {
		return BoardUtills.FIRST_COLUMN[currentPosition] && (destination == -1);
	}

	private boolean isEightCoulmExclusion(final Integer currentPosition,final Integer destination) {
		return BoardUtills.EIGHTH_COLUMN[currentPosition] && (destination == +1);
	}

	@Override
	public boolean isKing() {
		return false;
	}

	@Override
	public Piece movePiece(final Move move) {
		return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
	}
}