package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;


/*
 * This is Tile class representing each square on the board.
 * Each tile has two parameters.>> TIle coordinate and the piece on it
 * If the tile is empty, it returns NULL as piece on it.
 * This class stores 64 number of empty tiles for future use. and the class is immutable.
 * 
 */


/*********************** Parent Tile class ***************************/

public abstract class Tile {

	// Member variable for Tile Coordinate.
	private final Integer tileCordinate;
	
	//Private Constructor 
	private Tile(final Integer cordinate) {
		this.tileCordinate = cordinate;
	}
	
	/**************** Abstract method declaration **********************/
	// It will return true or false depending upon the tile type.
	public abstract boolean isTileOccupied();
	//Method to get the piece on tile
	public abstract Piece getPiece();

	/***************** End of abstract method declaration *****************/
	//This is a map which holds the all possible combination of empty tiles.
	private static final Map<Integer, EmptyTile> EMPTY_TIlES_CACHE = createAllEmptyTiles();

	//This method creates all the possible combination of empty tiles.
	private static Map<Integer, EmptyTile> createAllEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTilemap = new HashMap<>();
		for (Integer i = 0; i < BoardUtills.NUM_TILE; i++)
			emptyTilemap.put(i, new EmptyTile(i));
		return ImmutableMap.copyOf(emptyTilemap);
	}

	// Creates a Tile depending upon the piece value
	public static Tile createTile(Integer coordinate, Piece piece) {
		return piece != null ?
				new OcupiedTile(coordinate, piece) : //  creates a occupied tile with a piece on it 
				EMPTY_TIlES_CACHE.get(coordinate); //  returns an empty tile from the cache
	}
	

	//Getter
	public Integer getTileCordinate() {return tileCordinate;}

	/*********************** Inner static class for empty tiles ******************/

	public static final class EmptyTile extends Tile {
		EmptyTile(final Integer cordinate) {
			super(cordinate);
		}
		@Override
		public boolean isTileOccupied() {return false;}
		
		@Override
		public Piece getPiece() {return null;}
		
		@Override
		public String toString(){return "-";}

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
