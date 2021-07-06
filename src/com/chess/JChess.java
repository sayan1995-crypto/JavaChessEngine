package com.chess;

import javax.swing.SwingUtilities;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class JChess {

	public static void main(String[] args) {

		Board board=Board.createStandardBoard();
		System.out.println(board.toString());
		
		//Table.get().show();
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				Table.get().show();
		
				
			}
		});

	}
	

}
