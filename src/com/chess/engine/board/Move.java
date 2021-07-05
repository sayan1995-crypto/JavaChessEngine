package com.chess.engine.board;

import java.util.Objects;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;

public abstract class Move {
	
	protected final Board board;
	protected final Piece movedPiece;
	protected final Integer destinationmCordinate;
	private static final Move NULL_MOVE=new nullMove(); 
	private Move(final Board board,final  Piece movedPiece,final Integer destinationmCordinate) {
		super();
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationmCordinate = destinationmCordinate;
	}

	public Board execute() {
		
		Board.Builder builder = new Builder();
		for(final Piece piece : this.board.getCurrPlayer().getAllActivePieces())
			if(!this.movedPiece.equals(piece))
				builder.setPiece(piece);
		for(final Piece piece : this.board.getCurrPlayer().getOpponent().getAllActivePieces())
			builder.setPiece(piece);
		//setting the moved piece
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setAlliance(this.board.getCurrPlayer().getOpponent().getAlliance());
		return builder.build();
	}

	public Integer getDestinationCoordinate() {return this.destinationmCordinate;}
	public Piece getMovedPiece() {return movedPiece;}
	public Integer getCurrentCoordinate() {return movedPiece.getPiecePosition();}	
	public Board getBoard() {return board;}

	public boolean isInAttack() {return false;}
	public boolean isCastlingMove() {return false;}
	public Piece getAttackedPiece() {return null;}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		result = prime * result + ((destinationmCordinate == null) ? 0 : destinationmCordinate.hashCode());
		result = prime * result + ((movedPiece == null) ? 0 : movedPiece.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		if (destinationmCordinate == null) {
			if (other.destinationmCordinate != null)
				return false;
		} else if (!destinationmCordinate.equals(other.destinationmCordinate))
			return false;
		if (movedPiece == null) {
			if (other.movedPiece != null)
				return false;
		} else if (!movedPiece.equals(other.movedPiece))
			return false;
		return true;
	}






	public static class MajorMove extends Move{		
		public MajorMove(Board board, Piece movedPiece, Integer destinationmCordinate) {
			super(board, movedPiece, destinationmCordinate);			
		}
		
		@Override
		public String toString()
		{
			return movedPiece.getPieceType().toString()+BoardUtills.getPositionAtCoordinate(this.destinationmCordinate);
			//return "foo";
		}
		
	}	
	
	public static class AttackMove extends Move{
		private final Piece attakedPiece;
		public AttackMove(Board board, Piece movedPiece, Integer destinationmCordinate , Piece attackedPiece) {
			super(board, movedPiece, destinationmCordinate);
			this.attakedPiece=attackedPiece;		
		}
		
		@Override
		public boolean isInAttack() {return true;}
		@Override
		public Piece getAttackedPiece() {return attakedPiece;}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((attakedPiece == null) ? 0 : attakedPiece.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			AttackMove other = (AttackMove) obj;
			if (attakedPiece == null) {
				if (other.attakedPiece != null)
					return false;
			} else if (!attakedPiece.equals(other.attakedPiece))
				return false;
			return true;
		}
		
		
	}


	public static class PawnMove extends MajorMove{		
		public PawnMove(Board board, Piece movedPiece, Integer destinationmCordinate) {
			super(board, movedPiece, destinationmCordinate);			
		}
		
		
		
	}
	
	public static class PawnAttackMove extends AttackMove{

		public PawnAttackMove(Board board, Piece movedPiece, Integer destinationmCordinate, Piece attackedPiece) {
			super(board, movedPiece, destinationmCordinate, attackedPiece);
		}
		
		@Override
		public String toString()
		{
			return BoardUtills.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1)+"x"+
					BoardUtills.getPositionAtCoordinate(this.destinationmCordinate);
		}
		
	}
	
	public static class PawnEnPassantAttackMove extends PawnAttackMove{

		public PawnEnPassantAttackMove(Board board, Piece movedPiece, Integer destinationmCordinate, Piece attackedPiece) {
			super(board, movedPiece, destinationmCordinate, attackedPiece);
			
		}
		
		@Override
		public Board execute()
		{

			Board.Builder builder = new Builder();
			for(final Piece piece : this.board.getCurrPlayer().getAllActivePieces())
				if(!this.movedPiece.equals(piece))
					builder.setPiece(piece);
			for(final Piece piece : this.board.getCurrPlayer().getOpponent().getAllActivePieces())
				if(!this.getAttackedPiece().equals(piece))
				builder.setPiece(piece);
			//setting the moved piece
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setAlliance(this.board.getCurrPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
		
	}
	public static class PawnJump extends PawnMove{		
		public PawnJump(Board board, Piece movedPiece, Integer destinationmCordinate) {
			super(board, movedPiece, destinationmCordinate);			
		}
		
		@Override
		public Board execute() {
			
			Board.Builder builder = new Builder();
			for(final Piece piece : this.board.getCurrPlayer().getAllActivePieces())
				if(!this.movedPiece.equals(piece))
					builder.setPiece(piece);
			for(final Piece piece : this.board.getCurrPlayer().getOpponent().getAllActivePieces())
				builder.setPiece(piece);
			//setting the moved piece
			final Pawn movedPawn=(Pawn)this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setAlliance(this.board.getCurrPlayer().getOpponent().getAlliance());
			return builder.build();
		}
	}
	
	
	
	
	public static class PawnPromotion extends Move{
		
		final Move decoratedMove;
		final Pawn promotedPawn;
		public PawnPromotion(final Move decoratedMove) {
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(),decoratedMove.getDestinationCoordinate());
			
			this.decoratedMove=decoratedMove;
			this.promotedPawn=(Pawn)decoratedMove.getMovedPiece();
		}
		
		@Override
		public Board execute()
		{
			final Board pawnMovedBoard=decoratedMove.execute();
			final Board.Builder builder = new Builder();
			for(final Piece piece : pawnMovedBoard.getCurrPlayer().getAllActivePieces())
				if(!this.promotedPawn.equals(piece))
					builder.setPiece(piece);
			for(final Piece piece : pawnMovedBoard.getCurrPlayer().getOpponent().getAllActivePieces())
				builder.setPiece(piece);
			//set the promoted piece
			builder.setPiece(this.promotedPawn.getPromotedPiece().movePiece(this));
			builder.setAlliance(pawnMovedBoard.getCurrPlayer().getAlliance());
			return builder.build();
			
			
			
			
		}
		@Override
		public boolean isInAttack()
		{
			return decoratedMove.isInAttack();
		}
		@Override
		public Piece getAttackedPiece()
		{
			return decoratedMove.getAttackedPiece();
		}
		@Override
		public String toString()
		{
			return "";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Objects.hash(decoratedMove, promotedPawn);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof PawnPromotion))
				return false;
			PawnPromotion other = (PawnPromotion) obj;
			return Objects.equals(decoratedMove, other.decoratedMove)
					&& Objects.equals(promotedPawn, other.promotedPawn);
		}
		
		
	
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	static class castleMove extends Move{		
		
		protected final Rook castleRook;
		protected final int rookStartCoordinate;
		protected final int rookDestinationCoordinate;
		public castleMove(final Board board, 
						  final Piece movedPiece,
						  final Integer destinationCordinate,
						  final Rook castleRook,
						  final int rookStartCoordinate,
						  final int rookDestinationCoordinate
						  ) 
		{
			super(board, movedPiece, destinationCordinate);
			this.castleRook=castleRook;
			this.rookStartCoordinate=rookStartCoordinate;
			this.rookDestinationCoordinate=rookDestinationCoordinate;
		}
		

		@Override
		public boolean isCastlingMove() {return true;}
		@Override
		public Board execute() {
			Board.Builder builder = new Builder();
			for(final Piece piece : this.board.getCurrPlayer().getAllActivePieces())
				if((!this.movedPiece.equals(piece)) && (!castleRook.equals(piece)))
					builder.setPiece(piece);
			for(final Piece piece : this.board.getCurrPlayer().getOpponent().getAllActivePieces())
				builder.setPiece(piece);
			//setting the moved piece
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setPiece(new Rook(rookDestinationCoordinate, this.castleRook.getPieceAlliance() , false));
			builder.setAlliance(this.board.getCurrPlayer().getOpponent().getAlliance());
			return builder.build();			
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((castleRook == null) ? 0 : castleRook.hashCode());
			result = prime * result + rookDestinationCoordinate;
			result = prime * result + rookStartCoordinate;
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof castleMove))
				return false;
			castleMove other = (castleMove) obj;
			if (castleRook == null) {
				if (other.castleRook != null)
					return false;
			} else if (!castleRook.equals(other.castleRook))
				return false;
			if (rookDestinationCoordinate != other.rookDestinationCoordinate)
				return false;
			if (rookStartCoordinate != other.rookStartCoordinate)
				return false;
			return true;
		}	
		
		
	}
	
	public static class KingSideCastleMove extends castleMove{

		public KingSideCastleMove(Board board, Piece movedPiece, Integer destinationmCordinate, Rook castleRook,
				int rookStartCoordinate, int rookDestinationCoordinate) {
			super(board, movedPiece, destinationmCordinate, castleRook, rookStartCoordinate, rookDestinationCoordinate);

		}
		@Override
		public String toString() {
			return "0-0";
		}	
		
	}
	
	public static class QueenSideCastleMove extends castleMove{

		public QueenSideCastleMove(Board board, Piece movedPiece, Integer destinationmCordinate, Rook castleRook,
				int rookStartCoordinate, int rookDestinationCoordinate) {
			super(board, movedPiece, destinationmCordinate, castleRook, rookStartCoordinate, rookDestinationCoordinate);
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString()
		{
			return "0-0-0";
		}
		
	}
	
	public static class nullMove extends Move{

		public  nullMove() {
			super(null,null,-1);
		}
		@Override
		public Board execute()
		{
			throw new RuntimeException("Invalid Move");
		}
		
	}
	
	
	public static class MoveFactory{
		
		
		private MoveFactory()
		{
			throw new RuntimeException("Not Initiable");
			
		}
		
		
		public static Move createMove(final Board board, 
				final Integer currentCoordinate, 
				final Integer destinationCoordinate)
		{
			for(final Move move : board.getALlLegalMove())
				if(move.getCurrentCoordinate()==currentCoordinate &&
					move.getDestinationCoordinate()==destinationCoordinate)
					return move;
			
			return NULL_MOVE;
			
		}
		
		
	}

	
	
	}

