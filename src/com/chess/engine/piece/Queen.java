package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtills;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Queen extends Piece {

	private final static Integer[] CANDIDATE_MOVE_COORDINATES_VECTOR = {-9, -8, -7, -1, +1, +7, +8, +9};

	public Queen(Integer piecePosition, Alliance pieceAlliance , Boolean isFirstMove) {
		super(PieceType.QUEEN ,piecePosition, pieceAlliance , isFirstMove);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString()
	{
		return PieceType.QUEEN.toString();
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (Integer currentCandidateOffset : CANDIDATE_MOVE_COORDINATES_VECTOR) {
			Integer candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

			while (BoardUtills.isValidTile(candidateDestinationCoordinate)) {
				if (isFirstCoulmExclusion(this.piecePosition, currentCandidateOffset)
					|| isEightCoulmExclusion(this.piecePosition, currentCandidateOffset))
					break;

				final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
				if (!candidateDestinationTile.isTileOccupied())
				{
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					if((BoardUtills.FIRST_COLUMN[candidateDestinationCoordinate]
						|| BoardUtills.EIGHTH_COLUMN[candidateDestinationCoordinate])
						&&
						(Math.abs(currentCandidateOffset)==1 || Math.abs(currentCandidateOffset)==7 
						|| Math.abs(currentCandidateOffset)==9))
						break;
				}
				else {
					if (candidateDestinationTile.getPiece().getPieceAlliance() != pieceAlliance)
						legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate,
								candidateDestinationTile.getPiece()));
					break;
				}

				candidateDestinationCoordinate += currentCandidateOffset;
			}
		}

		return ImmutableList.copyOf(legalMoves);
	}

	private boolean isFirstCoulmExclusion(Integer currentPosition, Integer destination) {
		return BoardUtills.FIRST_COLUMN[currentPosition] && (destination == -9 || destination==-1|| destination == 7);
	}

	private boolean isEightCoulmExclusion(Integer currentPosition, Integer destination) {
		return BoardUtills.EIGHTH_COLUMN[currentPosition] && (destination == +9 || destination==+1 || destination == -7);
	}
	@Override
	public boolean isKing() {return false;}
	@Override
	public Piece movePiece(Move move) {
		return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance() , false);
	}
}