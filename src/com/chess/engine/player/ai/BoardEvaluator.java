package com.chess.engine.player.ai;

import com.chess.engine.board.Board;

public interface BoardEvaluator {
	
	int evalute(Board board, int depth);

}
