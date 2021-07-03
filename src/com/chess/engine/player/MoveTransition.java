package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
	
	private final Board transitionBoard;// New board after the piece is moved
	private final Move move; // the move
	private final MoveStatus moveStatus; // status of the move whether it is successful or not
	public MoveTransition(final Board transitionBoard,final Move move,final MoveStatus moveStatus) {
 		super();
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}
	public MoveStatus getMoveStatus() {return moveStatus;}
	public Board getTransitionBoard() {
		return transitionBoard;
	}
	public Move getMove() {
		return move;
	}
	

	
}
