package com.chess.engine;

import com.chess.engine.board.BoardUtills;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {

	BLACK {
		@Override
		public Integer getDirection() {return +1;}
		@Override
		public Boolean isWhite() {return false;}
		@Override
		public Boolean isBlack() {return true;}
		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			return blackPlayer;
		}
		@Override
		public boolean isPawnPromotionTile(int tileId) {
			return BoardUtills.FIRST_RANK[tileId];
		
		}
		
		
	},
	WHITE {
		@Override
		public Integer getDirection() {return -1;}
		@Override
		public Boolean isWhite() {return true;}
		@Override
		public Boolean isBlack() {return false;}
		@Override
		public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			return whitePlayer;
		}
		@Override
		public boolean isPawnPromotionTile(int tileId) {
			return BoardUtills.EIGHTH_RANK[tileId];
		}
		
	};
	public abstract Integer getDirection();
	public abstract Boolean isWhite();
	public abstract Boolean isBlack();
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
	public abstract boolean isPawnPromotionTile(int tileId);
	
	
}
