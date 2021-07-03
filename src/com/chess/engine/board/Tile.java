package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;

public abstract class Tile {

	private final Integer tileCordinate;

	public abstract boolean isTileOccupied();

	private static final Map<Integer, EmptyTile> EMPTY_TIlES_CACHE = createAllEmptyTiles();

	private static Map<Integer, EmptyTile> createAllEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTilemap = new HashMap<>();
		for (Integer i = 0; i < BoardUtills.NUM_TILE; i++)
			emptyTilemap.put(i, new EmptyTile(i));
		return ImmutableMap.copyOf(emptyTilemap);
	}

	public static Tile createTile(Integer coordinate, Piece piece) {
		return piece != null ? new OcupiedTile(coordinate, piece) : EMPTY_TIlES_CACHE.get(coordinate);
	}

	public abstract Piece getPiece();

	private Tile(final Integer cordinate) {
		this.tileCordinate = cordinate;
	}
	

	public Integer getTileCordinate() {
		return tileCordinate;
	}


	public static final class EmptyTile extends Tile {

		EmptyTile(final Integer cordinate) {
			super(cordinate);
		}

		@Override
		public boolean isTileOccupied() {
			return false;
		}

		@Override
		public Piece getPiece() {
			return null;
		}
		@Override
		public String toString()
		{
		return "-";
		}

	}

	public static final class OcupiedTile extends Tile {

		private final Piece piece;

		private OcupiedTile(final Integer cordinate, final Piece piece) {
			super(cordinate);
			this.piece = piece;
		}

		@Override
		public boolean isTileOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {

			return piece;
		}
		@Override
		public String toString()
		{
			return this.piece.getPieceAlliance().isBlack()? this.piece.toString().toLowerCase() :
				this.piece.toString();
		}

	}

}
