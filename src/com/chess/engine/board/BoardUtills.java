package com.chess.engine.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.board.Move.MajoPieceMove;
import com.chess.engine.board.Move.MajorPieceAttackMove;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;
import com.google.common.collect.ImmutableMap;

public class BoardUtills {

	public static final boolean[] FIRST_COLUMN = initializeColumn(0);
	public static final boolean[] SECOND_COLUMN = initializeColumn(1);
	public static final boolean[] SEVENTH_COLUMN = initializeColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initializeColumn(7);

	public static final boolean[] EIGHTH_RANK = initializeRow(0);
	public static final boolean[] SEVENTH_RANK = initializeRow(8);
	public static final boolean[] SIXTH_RANK = initializeRow(16);
	public static final boolean[] FIFTH_RANK = initializeRow(24);
	public static final boolean[] FOURTH_RANK = initializeRow(32);
	public static final boolean[] THIRD_RANK = initializeRow(40);
	public static final boolean[] SECOND_RANK = initializeRow(48);
	public static final boolean[] FIRST_RANK = initializeRow(56);

	public static final int NUM_TILE = 64;
	public static final int NUM_TILE_PER_COLUMN = 8;
	private static final String[] ALGEBREIC_NOTATION = initializeAlgebreicNotation();
	private static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionCoordinateMap();

	private static boolean[] initializeColumn(Integer columnNumber) {
		final boolean[] columnArray = new boolean[NUM_TILE];
		while (columnNumber < NUM_TILE) {
			columnArray[columnNumber] = true;
			columnNumber += NUM_TILE_PER_COLUMN;
		}
		return columnArray;
	}

	private static String[] initializeAlgebreicNotation() {

		String[] algebreicNotation = new String[NUM_TILE];
		int index = 0;
		for (int j = NUM_TILE_PER_COLUMN; j >= 1; j--)
			for (char i = 'a'; i <= 'h'; i++)

			{
				algebreicNotation[index] = i + "" + j;
				index++;
			}

		return algebreicNotation;

	}

	private static Map<String, Integer> initializePositionCoordinateMap() {
		Map<String, Integer> positionCoordinate = new HashMap<>();
		for (int i = 0; i < NUM_TILE; i++)
			positionCoordinate.put(ALGEBREIC_NOTATION[i], i);
		return ImmutableMap.copyOf(positionCoordinate);
	}

	private static boolean[] initializeRow(Integer rowNumber) {
		final boolean[] rowArray = new boolean[NUM_TILE];
		for (int i = 0; i < NUM_TILE_PER_COLUMN; i++)
			rowArray[rowNumber + i] = true;
		return rowArray;

	}

	public static boolean isValidTile(final Integer coordinate) {
		return coordinate >= 0 && coordinate < NUM_TILE;
	}

	public static int getCoordinateAtPosition(final String position) {
		return POSITION_TO_COORDINATE.get(position);
	}

	public static String getPositionAtCoordinate(final int coordinate) {
		return ALGEBREIC_NOTATION[coordinate];
	}

	public static String generateMoveNotation(final Move move) {
		StringBuilder moveNotation = new StringBuilder();
		if (move.getClass().equals(MajoPieceMove.class)) {
			// Major Piece first letter append with destination coordinate
			// e.g. Ne3
			moveNotation.append(move.getMovedPiece().toString())
			.append(ALGEBREIC_NOTATION[move.getDestinationCoordinate()])
			.append(calChkandChkMateNotation(move));
			

		} else if (move.getClass().equals(MajorPieceAttackMove.class)) {
			// Major Piece first letter append x append captured piece destination
			// coordinate
			moveNotation.append(move.getMovedPiece().toString()).append("x")
			.append(ALGEBREIC_NOTATION[move.getDestinationCoordinate()])
			.append(calChkandChkMateNotation(move));

		} else if (move.getClass().equals(PawnMove.class) || move.getClass().equals(PawnJump.class)) {
			// Only destination coordinate append
			moveNotation.append(ALGEBREIC_NOTATION[move.getDestinationCoordinate()])
			.append(calChkandChkMateNotation(move));

		} else if (move.getClass().equals(PawnAttackMove.class)) {
			moveNotation.append(ALGEBREIC_NOTATION[move.getCurrentCoordinate()].substring(0, 1)).append("x")
			.append(ALGEBREIC_NOTATION[move.getDestinationCoordinate()])
			.append(calChkandChkMateNotation(move));
		}

		else if (move.getClass().equals(PawnPromotion.class)) {
			PawnPromotion promotionMove = (PawnPromotion) move;
			if (promotionMove.isInAttack())
				moveNotation.append(ALGEBREIC_NOTATION[promotionMove.getCurrentCoordinate()]
				.substring(0, 1)).append("x");

			moveNotation.append(ALGEBREIC_NOTATION[move.getDestinationCoordinate()])
			.append("=")
			.append(promotionMove.getPromotedPiece().toString())
			.append(calChkandChkMateNotation(move));
		}

		return moveNotation.toString();
	}
	
	private static String calChkandChkMateNotation(final Move move)
	{
		//StringBuilder builder = new StringBuilder();
		Board transitionBoard=move.getBoard().getCurrPlayer().makeMove(move).getTransitionBoard();
		if(transitionBoard.getCurrPlayer().isInCheckMate())
			return "#";
		if(transitionBoard.getCurrPlayer().isInCheck())
			return "+";
		return "";
	}
}
