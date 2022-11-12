package aplication;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;
import Chess.Color;

public class UI {
	
	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String s =sc.nextLine();
			char column = s.charAt(0);int row = Integer.parseInt(s.substring(1));
			return new ChessPosition(column, row);
		}
		catch(RuntimeException e) {
			throw new InputMismatchException("erro lendo posição de xadrez, valores válidos são de a1 até h8.");
		}
	}
	
	public static void printMatch(ChessMatch chessMatch) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		System.out.println("Turn : " + chessMatch.getTurn());
		if(!chessMatch.getCheckMate()) {
			System.out.println("Esperando o jogador "+chessMatch.getCurrentPlayer());
			if(chessMatch.getCheck()) {
			System.out.println("check!");
			}
		}
		else {
			System.out.println("Check mate!");
			System.out.println("Winner: " + chessMatch.getCurrentPlayer());
		}
	}
	
	public static void printBoard(ChessPiece[][] pieces) {
		
		for(int i=0;i<pieces.length;i++) {
			System.out.print(8-i+" ");
			for(int j=0;j<pieces.length;j++) {
				printPiece(pieces[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	

	
	private static void printPiece(ChessPiece piece ){
		
		if (piece == null) {
			System.out.print("-");
		}
		else {
			System.out.print(piece);
		}
		System.out.print(" ");
	}
	
	
}
