package Chess.piece;

import BoardLayer.Board;
import BoardLayer.Position;
import Chess.ChessPiece;
import Chess.Color;

public class Bishop extends ChessPiece{

	public Bishop(Board board, Color color) {
		super(board, color);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "B";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
				
				Position p=new Position(0, 0);
				// nw
				p.setValues(position.getRow() - 1, position.getColum() - 1);
				while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
					p.setValues(p.getRow() - 1, p.getColum() - 1);
				}
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
				}

				// ne
				p.setValues(position.getRow() - 1, position.getColum() + 1);
				while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
					p.setValues(p.getRow() - 1, p.getColum() + 1);
				}
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
				}

				// se
				p.setValues(position.getRow() + 1, position.getColum() + 1);
				while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
					p.setValues(p.getRow() + 1, p.getColum() + 1);
				}
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
				}

				// sw
				p.setValues(position.getRow() + 1, position.getColum() - 1);
				while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
					p.setValues(p.getRow() + 1, p.getColum() - 1);
				}
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColum()] = true;
				}

		
		
		
		return mat;
	}
	
}
