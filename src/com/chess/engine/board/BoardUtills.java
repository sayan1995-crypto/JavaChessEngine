package com.chess.engine.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class BoardUtills {

	public static final boolean[] FIRST_COLUMN=initializeColumn(0);
	public static final boolean[] SECOND_COLUMN=initializeColumn(1);
	public static final boolean[] SEVENTH_COLUMN=initializeColumn(6);
	public static final boolean[] EIGHTH_COLUMN=initializeColumn(7);
	
	public static final boolean[] EIGHTH_RANK=initializeRow(0);
	public static final boolean[] SEVENTH_RANK=initializeRow(8);
	public static final boolean[] SIXTH_RANK=initializeRow(16);
	public static final boolean[] FIFTH_RANK=initializeRow(24);
	public static final boolean[] FOURTH_RANK=initializeRow(32);
	public static final boolean[] THIRD_RANK=initializeRow(40);
	public static final boolean[] SECOND_RANK=initializeRow(48);
	public static final boolean[] FIRST_RANK=initializeRow(56);
	
	public static final int NUM_TILE=64;
	public static final int NUM_TILE_PER_COLUMN=8;
	private static final String[] ALGEBREIC_NOTATION = initializeAlgebreicNotation();
	private static final Map<String , Integer> POSITION_TO_COORDINATE = initializePositionCoordinateMap();	
	
	
	private static boolean[] initializeColumn(Integer columnNumber)
	{
		final boolean[] columnArray=new boolean[NUM_TILE];
		while(columnNumber<NUM_TILE)
		{
			columnArray[columnNumber]=true;
			columnNumber+=NUM_TILE_PER_COLUMN;
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
		Map<String ,Integer> positionCoordinate=new HashMap<>();
		for(int i=0;i<NUM_TILE;i++)
			positionCoordinate.put(ALGEBREIC_NOTATION[i], i);
		return ImmutableMap.copyOf(positionCoordinate);
	}
	
	private static boolean[] initializeRow(Integer rowNumber)
	{
		final boolean[] rowArray=new boolean[NUM_TILE];
		for(int i=0;i<NUM_TILE_PER_COLUMN;i++)
			rowArray[rowNumber+i]=true;
		return rowArray;
				
	}
	                         
	                         
	
	public static boolean isValidTile(final Integer coordinate) {
		return coordinate>=0 && coordinate<NUM_TILE; 
	}

	public static int getCoordinateAtPosition(final String position)
	{
		return POSITION_TO_COORDINATE.get(position);
	}
	public static String getPositionAtCoordinate(final int coordinate)
	{
		return ALGEBREIC_NOTATION[coordinate];
	}
}
