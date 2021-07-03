package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

public class TakenPiecesPanel extends JPanel{
	private static final EtchedBorder PANEL_BORDER=new EtchedBorder(EtchedBorder.RAISED);
	
	private final JPanel northPanel; 
	private final JPanel southPanel;
	private static final Color PANEL_COLOR=Color.decode("0xFDFE6");
	private static final Dimension TAKEN_PIECES_DIMENSION=new Dimension(74,1080);
	private static final String pieceIconPath="resource/Art/simple/";
	
	public TakenPiecesPanel()
	{
		super(new BorderLayout());
		this.setBackground(PANEL_COLOR);
		this.setBorder(PANEL_BORDER);
		this.northPanel=new JPanel(new GridLayout(8,2));
		this.southPanel=new JPanel(new GridLayout(8,2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		this.add(this.northPanel,BorderLayout.NORTH);
		this.add(this.southPanel,BorderLayout.SOUTH);
		setPreferredSize(TAKEN_PIECES_DIMENSION);
		
	}
	
	public void redo(final MoveLog movelog)
	{
		this.southPanel.removeAll();
		this.northPanel.removeAll();
		final List<Piece> whitePieces=new ArrayList<>();
		final List<Piece> blackPieces=new ArrayList<>();
		
		for(final Move move : movelog.getMoves())
		{
			if(move.isInAttack())
			{
				final Piece attackedPiece=move.getAttackedPiece();
				if(attackedPiece.getPieceAlliance().isWhite())
				whitePieces.add(attackedPiece);
					else
					blackPieces.add(attackedPiece);
			}
		}
		
		Collections.sort(whitePieces, new Comparator<Piece>() {
			@Override
			public int compare(Piece p1 , Piece p2)
			{
			return Ints.compare(p1.getPieceValue() , p2.getPieceValue());
			}
		}
		);
		
		Collections.sort(blackPieces, new Comparator<Piece>() {
			@Override
			public int compare(Piece p1 , Piece p2)
			{
			return Ints.compare(p1.getPieceValue() , p2.getPieceValue());
			}
		}
		);
		
		
		for (final Piece takenPiece : whitePieces) {
			String fileDirectory = pieceIconPath
					+ takenPiece.getPieceAlliance().toString().substring(0, 1)
					+ takenPiece.toString() + ".gif";
			try {
				final BufferedImage image = ImageIO.read(new File(fileDirectory));
				this.southPanel.add(new JLabel(new ImageIcon(image)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		for (final Piece takenPiece : blackPieces) {
			String fileDirectory = pieceIconPath
					+ takenPiece.getPieceAlliance().toString().substring(0, 1)
					+ takenPiece.toString() + ".gif";
			try {
				final BufferedImage image = ImageIO.read(new File(fileDirectory));
				this.northPanel.add(new JLabel(new ImageIcon(image)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		validate();
		
		
	}
	

}
