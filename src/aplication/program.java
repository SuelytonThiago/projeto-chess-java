package aplication;

import java.util.InputMismatchException;
import java.util.Scanner;

import Chess.ChessException;
import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;

public class program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		ChessMatch chessMatch = new ChessMatch();
		
		while(!chessMatch.getCheckMate()) {
			try {
				
			
				UI.printMatch(chessMatch);
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source =UI.readChessPosition(sc);
				
				
				
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			
			if(chessMatch.getPromoted() !=null) {
				System.out.println("escolha a peça que substituirá o peão promovido (B/N/R/Q): ");
				String type = sc.nextLine().toUpperCase();
				
				while(!type.equals("B") && !type.equals("H") && !type.equals("R")&& !type.equals("Q")) {
					System.out.println("Valor inválido,escolha a peça que substituirá o peão promovido (B/N/R/Q): ");
					 type = sc.nextLine().toUpperCase();
				}
				
				chessMatch.replacePromotedPiece(type);
			}
			
			
			}
			catch(ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.printMatch(chessMatch);
	}
}
