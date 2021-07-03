package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy{

	private final BoardEvaluator boardEvaluator;
	private final int searchDepth;
	
	public MiniMax(final int searchDepth) {
	this.boardEvaluator=new StandardBoardEvaluator();
	this.searchDepth=searchDepth;
	}
	
	@Override
	public String toString()
	{return "Minimax";}
	@Override
	public Move execute(Board board) {
		
		final long startTime=System.currentTimeMillis();
		Move bestMove=null;
		
		int highestSeenValue=Integer.MIN_VALUE;
		int lowestSeenValue=Integer.MAX_VALUE;
		int currentValue;
		System.out.println(board.getCurrPlayer().toString()+" Thinking with depth ="+searchDepth);
		
		int numMoves=board.getCurrPlayer().getLegalMoves().size();
		for(final Move move : board.getCurrPlayer().getLegalMoves())
		{
			
			final MoveTransition moveTransition=board.getCurrPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone())
			{
				/*Assumin that the AI will be playing as black player.
				 * SO for black player, it will choose the maximum gain from all possible minimum gain. 
				 */
				
				currentValue=board.getCurrPlayer().getAlliance().isWhite() ?
						min(moveTransition.getTransitionBoard(),searchDepth-1) :
						max(moveTransition.getTransitionBoard() , searchDepth-1);
				if(board.getCurrPlayer().getAlliance().isWhite() 
						&& currentValue>=highestSeenValue)
				{
					highestSeenValue=currentValue;
					bestMove=move;
				}
				else if(board.getCurrPlayer().getAlliance().isBlack() && currentValue<=lowestSeenValue)
				{
					lowestSeenValue=currentValue;
					bestMove=move;
				}
					
				
			}			
		}
		
		
		
		System.out.println("Total evaluation time "+(System.currentTimeMillis()-startTime)+" ms");
		return bestMove;
		
	}
	
	/***** Two corecursive function call each other **********///
	
	
	
	//minimizing player will choose the minimum value from all possible maximum losses......
	private int min(Board board , int depth)
	{
		if(depth==0 || isEndgameScenario(board))
			return this.boardEvaluator.evalute(board,depth);
		int lowestSeenValue=Integer.MAX_VALUE; // this is maximum losses
		for(final Move move : board.getCurrPlayer().getLegalMoves())
		{
			final MoveTransition moveTransition=board.getCurrPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone())
			{
				final int currentValue=max(moveTransition.getTransitionBoard(),depth-1);// this function returns the maximum losses for a particular move
				if(currentValue<=lowestSeenValue)
					lowestSeenValue=currentValue;
			}
		}
		return lowestSeenValue;
	}
	
	// maximizing player will choose the maximum gain from all possible minimum gain
	private int max(final Board board , int depth)
	{
		if(depth==0 || isEndgameScenario(board))
			return this.boardEvaluator.evalute(board,depth);
		int highestSeenValue=Integer.MIN_VALUE; // this is the minimum gain for opponent.
		for(final Move move : board.getCurrPlayer().getLegalMoves())
		{
			final MoveTransition moveTransition=board.getCurrPlayer().makeMove(move);
			if(moveTransition.getMoveStatus().isDone())
			{
				final int currentValue=min(moveTransition.getTransitionBoard(),depth-1); // this function returns the minimum gains for a particular move
				if(currentValue>=highestSeenValue)
					highestSeenValue=currentValue;
			}
		}
		return highestSeenValue;
	}
	
	
	private boolean isEndgameScenario(Board board)
	{
		return board.getCurrPlayer().isInCheckMate() || board.getCurrPlayer().isInstaleMate();
	}

}
