package BoardLayer;

public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows<1||columns<1) {
			throw new BoardException("É necessário que haja pelo menos uma linha e uma coluna");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row,int colum) {
		if(!positionExists(row, colum)){
			throw new BoardException("Posição não existente no tabuleiro");
		}
		return pieces[row][colum];
	}

	public Piece piece(Position position) {
		if(!positionExists(position)){
			throw new BoardException("Posição não existente no tabuleiro");
		}
		return pieces[position.getRow()][position.getColum()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if(thereIsAPiece(position)) {
			throw new BoardException("já existe uma peça na posição " + position);
		}
		
		pieces[position.getRow()][position.getColum()] = piece;
		piece.position = position;
	}
	
	private boolean positionExists(int row, int colum) {
		return row >=0 && row < rows && colum>=0 && colum < columns;
	}
	
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(),position.getColum());
	}
	
	public boolean thereIsAPiece(Position position) {
		if(!positionExists(position)){
			throw new BoardException("Posição não existente no tabuleiro");
		}
		
		return piece(position) !=null;
	}
}
