package de.krillonline.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SudokuField {

	private SudokuCell[][] cells;
	private static int size=0;

	public SudokuField(int size) {
		super();
		SudokuField.size = size;
		this.cells = new SudokuCell[size][size];
		for (int row = 0; row < cells.length; row++) 
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col] = new SudokuCell(0, false);
		}
	}
	
	public SudokuField() {
		if (SudokuField.size == 0 )
			throw new RuntimeException("Field size not set before 1st instanciation of object!");
		this.cells = new SudokuCell[SudokuField.size][SudokuField.size];
		for (int row = 0; row < cells.length; row++) 
			for (int col = 0; col < cells[row].length; col++) {
				cells[row][col] = new SudokuCell(0, false);
		}
		
	}

	public static void setSize(int size) {
		SudokuField.size = size;
	}

	public static int getSize() {
		return size;
	}

}
