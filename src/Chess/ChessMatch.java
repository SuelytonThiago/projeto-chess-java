package Chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import BoardLayer.Board;
import BoardLayer.Piece;
import BoardLayer.Position;
import Chess.piece.Bishop;
import Chess.piece.Horse;
import Chess.piece.King;
import Chess.piece.Pawn;
import Chess.piece.Queen;
import Chess.piece.Rook;

public class ChessMatch {
	
	private int turn;
	private Color CurrentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;

	private List<Piece> piecesOnTheBoard = new ArrayList<>();	
	private List<Piece> capturedPieces = new ArrayList<>();	
	
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return CurrentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	public boolean getCheckMate() {
		return checkMate;
	}
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	public ChessPiece getPromoted() {
		return promoted;	
	}

	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		CurrentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i=0; i<board.getRows(); i++) {
			for(int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition ) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		if(testCheck(CurrentPlayer)) {
			undoMove(source,target,capturedPiece);
			throw new ChessException("voc~e não pode se colocar em cheque");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		promoted = null;
		if(movedPiece instanceof Pawn) {
			if(movedPiece.getColor() == Color.WHITE && target.getRow() == 0 ||movedPiece.getColor() == Color.BLACK && target.getRow() == 7) {
				promoted =(ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
				
			}
		}
		
		
		check = (testCheck(opponent(CurrentPlayer))) ? true: false;
		
		if(testCheckMate(opponent(CurrentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn(); 
		}
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow()-2 || target.getRow() == source.getRow()+2)) {
			enPassantVulnerable = movedPiece;		
		}
		else {
			enPassantVulnerable = null;
		}
		return (ChessPiece) capturedPiece;
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if(promoted==null) {
			throw new IllegalStateException("não há peça para ser promovida");
		}
		if(!type.equals("B") && !type.equals("H") && !type.equals("R")&& !type.equals("Q")) {
			return promoted;
		}
		Position pos = promoted.getChessPosition().toPosition();
		Piece p =board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if(type.equals("B")) return new Bishop(board,color);
		if(type.equals("H")) return new Horse(board,color);
		if(type.equals("R")) return new Rook(board,color);
		return new Queen(board,color);
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		if (capturedPiece != null){
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		if(p instanceof King && target.getColum() == source.getColum() + 2) {
			Position sourceT= new Position(source.getRow(), source.getColum() + 3);
			Position targetT= new Position(source.getRow(), source.getColum() + 1);
			ChessPiece rook =(ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		if(p instanceof King && target.getColum() == source.getColum() - 2) {
			Position sourceT= new Position(source.getRow(), source.getColum() - 4);
			Position targetT= new Position(source.getRow(), source.getColum() - 1);
			ChessPiece rook =(ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		if(p instanceof Pawn) {
			if(source.getColum() !=target.getColum() && capturedPiece == null) {
				Position pawnPos;
				if(p.getColor() ==Color.WHITE) {
					pawnPos = new Position(target.getRow()+1, target.getColum());
				}
				else {
					pawnPos = new Position(target.getRow()-1, target.getColum());
				}
				capturedPiece = board.removePiece(pawnPos);	
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece!=null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
			
		}
	
		if(p instanceof King && target.getColum() == source.getColum() + 2) {
			Position sourceT= new Position(source.getRow(), source.getColum() + 3);
			Position targetT= new Position(source.getRow(), source.getColum() + 1);
			ChessPiece rook =(ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		if(p instanceof King && target.getColum() == source.getColum() - 2) {
			Position sourceT= new Position(source.getRow(), source.getColum() - 4);
			Position targetT= new Position(source.getRow(), source.getColum() - 1);
			ChessPiece rook =(ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		if(p instanceof Pawn) {
			if(source.getColum() !=target.getColum() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPos;
				if(p.getColor() ==Color.WHITE) {
					pawnPos = new Position(3, target.getColum());
				}
				else {
					pawnPos = new Position(4, target.getColum());
				}
				board.placePiece(pawn, pawnPos);
			
			
			}
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("não existe peça na posição de origem");
		}
		if (CurrentPlayer !=((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("a peça escolhida não é sua");
		}
		if(!board.piece(position).isThereAnyPossibleMove()){
			throw new ChessException("Não existe movimentos possiveis para a peça escolhida");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
		}
	}
	
	private void nextTurn() {
		turn++;
		CurrentPlayer = (CurrentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece>list = piecesOnTheBoard.stream().filter(x-> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof King) {
				return(ChessPiece)p;
			}
		}	
		throw new IllegalStateException("Não existe o rei de cor" + color + "no tabuleiro");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king (color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x->((ChessPiece)x).getColor()==opponent(color)).collect(Collectors.toList());
		for(Piece p: opponentPieces) {
			boolean[][] mat=p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColum()]) {
				return true;
			}
			
		}
	return false;
	}
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x-> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
		 for( Piece p : list) {
			 boolean[][] mat = p.possibleMoves();
			 for(int i=0;i<board.getRows();i++) {
				 for(int j=0;j<board.getColumns();j++) {
					 if(mat[i][j]) {
						 Position source = ((ChessPiece)p).getChessPosition().toPosition();
						 Position target = new Position (i, j);
						 Piece capturedPiece = makeMove(source,target);
						 boolean testCheck=testCheck(color);
						 undoMove(source, target, capturedPiece);
						 if(!testCheck) {
							 return false;
						 }
					 }
				 }
			 }
		 }
		 return true;
	}
	
	
	private void placeNewPiece(char column,int row, ChessPiece piece) {
		board.placePiece(piece,  new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {
		placeNewPiece('a' ,  1, new Rook(board,Color.WHITE));
		placeNewPiece('h' ,  1, new Rook(board,Color.WHITE));
		placeNewPiece('a' ,  8, new Rook(board,Color.BLACK));
		placeNewPiece('h' ,  8, new Rook(board,Color.BLACK));
		
		placeNewPiece('e' ,  8, new King(board,Color.BLACK,this));
		placeNewPiece('e' ,  1, new King(board,Color.WHITE,this));
		
		placeNewPiece('d' ,  8, new Queen(board,Color.BLACK));
		placeNewPiece('d' ,  1, new Queen(board,Color.WHITE));
		
		placeNewPiece('c' ,  8, new Bishop(board,Color.BLACK));
		placeNewPiece('f' ,  8, new Bishop(board,Color.BLACK));
		placeNewPiece('c' ,  1, new Bishop(board,Color.WHITE));
		placeNewPiece('f' ,  1, new Bishop(board,Color.WHITE));
		
		placeNewPiece('b' ,  1, new Horse(board,Color.WHITE));
		placeNewPiece('g' ,  1, new Horse(board,Color.WHITE));
		placeNewPiece('b' ,  8, new Horse(board,Color.BLACK));
		placeNewPiece('g' ,  8, new Horse(board,Color.BLACK));
		
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK,this  ));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK,this  ));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK,this  ));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK,this  ));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK,this  ));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK,this  ));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK,this  ));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK,this  ));
        
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE,this  ));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE,this  ));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE,this  ));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE,this  ));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE,this  ));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE,this  ));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE,this  ));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE,this  ));
        
		
		
		
	}
}
