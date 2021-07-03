package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {

	private static final int CHECK_BONUS=50;
	private static final int CHECKMATE_BONUS=10000;
	private static final int DEPTH_BONUS=100;
	private static final int CASTLE_BONUS = 50;
	@Override
	public int evalute(Board board, int depth) {

		return scorePlayer(board,board.getWhitePlayer(),depth)
				- scorePlayer(board,board.getBlackPlayer(),depth);
	}

	private int scorePlayer(final Board board ,final  Player player,final int depth) {
		
		
		return getTotalPieceValue(player)+mobility(player)+check(player) + checkmate(player , depth)+
		castle(player);
		// TODO add check , chackmate, castlingmove , mobility.....
	}
	
	private int getTotalPieceValue(final Player player)
	{
		 int totalPieceValue=0;
		for(final Piece piece : player.getAllActivePieces())
			totalPieceValue +=piece.getPieceValue();
		return totalPieceValue;
	}
	
	private int mobility(final Player player)
	{
		return player.getLegalMoves().size();
	}

	
	private int check(final Player player )
	{
		return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
	}
	
	private int checkmate(final Player player , final int depth)
	{
		return player.getOpponent().isInCheckMate()? CHECKMATE_BONUS*depthBonus(depth) : 0;
	}
	
	private int depthBonus(final int depth)
	{
		return depth==0? 1 : depth*DEPTH_BONUS;
	}
	
	private int castle(final Player player)
	{
		return player.isInCastle() ? CASTLE_BONUS : 0;
	}
}
