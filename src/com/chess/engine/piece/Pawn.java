package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtills;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

	private final static Integer[] CANDIDATE_MOVE_COORDINATES_OFFSET = { 8, 16, 7, 9 };

	public Pawn(final Integer piecePosition,final Alliance pieceAlliance,final Boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		List<Move> legalMoves = new ArrayList<>();
		for (Integer currentCandidateOffset : CANDIDATE_MOVE_COORDINATES_OFFSET) {
			final Integer candidateDestinationCoordinate = this.piecePosition
					+ this.getPieceAlliance().getDirection() * currentCandidateOffset;
			if (!BoardUtills.isValidTile(candidateDestinationCoordinate))
				continue;
			// This is for normal forward move.
			if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

				if (this.pieceAlliance.isPawnPromotionTile(candidateDestinationCoordinate))
					legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
				else
					legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
			}

			// This is Jump move for first time
			else if (currentCandidateOffset == 16 && this.isFirstMove()) {
				final Integer behindCandidateDestinationCoordinate = this.piecePosition
						+ this.pieceAlliance.getDirection() * 8;
				if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied()
						&& !board.getTile(candidateDestinationCoordinate).isTileOccupied())

					legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
			}

			// this is for attacking move
			else if (currentCandidateOffset == 7
					&& (!(BoardUtills.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())
							|| (BoardUtills.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))) {

				final Tile candidateTile = board.getTile(candidateDestinationCoordinate);
				if (candidateTile.isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance())

						if (this.pieceAlliance.isPawnPromotionTile(candidateDestinationCoordinate))
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
									candidateDestinationCoordinate, candidateTile.getPiece())));
						else
							legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,
									candidateTile.getPiece()));
				} else if (board.getEnPassantPawn() != null) {

					final Piece enPassantPawn = board.getEnPassantPawn();
					if ((enPassantPawn.getPiecePosition() == this.piecePosition - this.pieceAlliance.getDirection())
							&& (enPassantPawn.getPieceAlliance() != this.pieceAlliance))
						legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate,
								enPassantPawn));

				}

			}

			// this is for attacking move
			else if (currentCandidateOffset == 9
					&& (!(BoardUtills.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())
							|| (BoardUtills.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
				// checks if the tile is occupied with rival piece
				final Tile candidateTile = board.getTile(candidateDestinationCoordinate);
				if (candidateTile.isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance())

						if (this.pieceAlliance.isPawnPromotionTile(candidateDestinationCoordinate))
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
									candidateDestinationCoordinate, candidateTile.getPiece())));
						else
							legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate,
									candidateTile.getPiece()));
				} else if (board.getEnPassantPawn() != null) {

					final Piece enPassantPawn = board.getEnPassantPawn();
					if ((enPassantPawn.getPiecePosition() == this.piecePosition + this.pieceAlliance.getDirection())
							&& (enPassantPawn.getPieceAlliance() != this.pieceAlliance))
						legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate,
								enPassantPawn));

				}
			}

		}

		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public boolean isKing() {
		return false;
	}

	@Override
	public Piece movePiece(final Move move) {
		return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
	}

	public Piece getPromotedPiece() {
		return new Queen(this.piecePosition, this.pieceAlliance, false);
	}
}
